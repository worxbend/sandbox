package io.kzonix.cogwheel.config.parser

import com.typesafe.config._
import io.kzonix.cogwheel.config.parser.CompositeKeyParser.Node
import io.kzonix.cogwheel.config.parser.CompositeKeyParser.TrieNode
import io.kzonix.cogwheel.config.parser.CompositeKeyParser.ParamNamePatters.PathWithSequenceIndex
import io.kzonix.cogwheel.config.parser.CompositeKeyParser.ParamNamePatters.SequenceIndex

import scala.annotation.tailrec
import scala.collection.immutable
import scala.collection.mutable
import scala.jdk.CollectionConverters._
import scala.util.Try

class CompositeKeyParser(nextParser: ParameterParser[Map[String, ConfigValue], ConfigValue])
    extends ParameterParser[Map[String, ConfigValue], ConfigValue] {

  private final def buildTree(node: Node): ConfigValue = {

    // check if the current recursion call reaches the end for some specific child entry
    // if it reach the end of one invocation branch, we need to filter them out from the list of child eligible for further recursion calls.
    //   "/region-1/customer/another_app/1/nested1/1/nested2/3/nested3 | "     -> json, <-- end, should be marked as finished node
    //   "/region-1/customer/another_app/1/nested1/1/nested2/3/nested3 | /last" -> json  <-- should be recursively processed and merged on step back with previously finished nodes
    // [step 1]
    val finishedEntries: Map[String, ConfigValue]       = node.children
      .filter { case (key, _) => key.isEmpty }
    val finishedValues: immutable.Iterable[ConfigValue] = finishedEntries.map { case (_, value) => value }
    // prepare all non-finished entries for next recursion calls
    val interimNodes: Seq[Node]                         = (node.children -- finishedEntries.keySet)
      .groupMap {
        case (key, _) => extractKeyParts(key).head
      } {
        case (key, value) => extractKeyParts(key).tail.mkString("/") -> value
      }
      .map {
        case (key, value) =>
          if (key.matches(SequenceIndex))
            node.markCollection() // dirty hack to mark parent node as the one that holds collection of elements
          Node(
            key,
            value.toMap
          )
      }
      .toSeq

    // if filtered collection of child nodes (for further recursive calls) is empty
    // then it means the the single element is left in the tail

    // e.g "/region-1/customer/another_app/1/nested1/1/nested2/3/nested3"
    // e.g "/region-1/customer/another_app/1/nested1/1/nested2/3/nested3/last" <-- we cannot have this possible coz of uniqueness of param-names in SSM
    //     "/region-1/customer/another_app/1/nested1/1/nested2/3/nested3/last" <--
    if (interimNodes.isEmpty) node.children.values.head
    else {
      // otherwise we have go down in our recursive data structure on each child element
      val builtNodes: Seq[ConfigValue] = interimNodes.map { node =>
         val configValue = buildTree(node)
         // construct config object for nested objects
         // e.g: "/region-1/customer/another_app/1/nested1/1/nested2/3/nested3/aaa" -> value
         //      "/region-1/customer/another_app/1/nested1/1/nested2/3 -> { "nested3": { "last": { ..value } } }
         if (!node.id.matches(SequenceIndex))
           configValue.atPath(node.id).root()
         else
           // otherwise config object should not be wrapped with parent key
           // e.g:
           //      "/region-1/customer/another_app/1/nested1/1/nested2/0 -> { "nested3": { "last": { ..value } } }
           //      "/region-1/customer/another_app/1/nested1/1/nested2/1 -> { "nested3": { "last": { ..value } } }
           //                                                          ^ if parent is collection identifier
           configValue
      }
      // Combine all folded child nodes to 'ConfigList' type
      if (node.isCollection) ConfigValueFactory.fromIterable(builtNodes.asJava)
      // If there is some 'finishedValues', then it means that current node can not be a collection holder
      // Combine finished nodes with those that are built on this step [step 1].
      else if (finishedValues.size == 1)
        finishedValues.head
          .withFallback(
            builtNodes.foldLeft(ConfigFactory.empty().root()) { case (c1, c2) => c1.withFallback(c2) }
          )
      else
        // if current current node is not be collection holder, all 'built' nodes should merged as the object type 'ConfigObject'
        builtNodes.foldLeft(ConfigFactory.empty().root()) { case (c1, c2) => c1.withFallback(c2) }
    }
  }

  private def extractKeyParts(key: String) =
    key.split("/").filter(_.nonEmpty)

  def parse(parameters: Map[String, ConfigValue]): ConfigObject = {
    val (compositeParams, simpleParams) = parameters.partition {
      case (path, _) => PathWithSequenceIndex.r.findAllIn(path).nonEmpty
    }

    val trie = TrieNode()
    compositeParams.foreach {
      case (path, value) =>
        trie.append(
          path,
          value
        )
    }
//    println(trie)
    trie.reverse().foreach(println)
    TrieNode.fold(trie.reverse())
    val cfg  = if (compositeParams.nonEmpty) {
      val config = buildTree(
        Node(
          "root", // dummy value, will be ignored
          compositeParams
        )
      )
      // convert config value to object at some key level
      // by wrapping to some enclosed key and fetching config object by the same key
      config.atKey("root").getConfig("root")
    } else ConfigFactory.empty()

    // combines the result with the result of other parser
    cfg.root().withFallback(nextParser.parse(simpleParams))
  }

}

object CompositeKeyParser {

  type LeafKey = String

  case class TrieNode(id: String = "root",
                      var fullPath: Seq[String] = Seq("root"),
                      var value: Option[ConfigValue] = None,
                      leaves: mutable.Map[LeafKey, TrieNode] = new mutable.HashMap[LeafKey, TrieNode](),
                      parent: Option[TrieNode] = None
  ) {
    override def toString: String =
      s"Trie(id = $id, fullPath= $fullPath, leaves = ${ leaves.values }, value = $value)"

    def append(path: String, config: ConfigValue) = {

      val pathBlocks: Seq[String] = path.split('/').filter(_.nonEmpty).toSeq

      @tailrec def appendHelper(node: TrieNode, currentIndex: Int): Unit =
        if (currentIndex == pathBlocks.length) {
          node.value = Some(config)
          node.fullPath = pathBlocks
        } else {
          val pathBlock = pathBlocks(currentIndex)
          val result    = node.leaves.getOrElseUpdate(
            pathBlock,
            TrieNode(
              id = pathBlock,
              parent = Some(node),
              fullPath = node.fullPath ++ Seq(pathBlock)
            )
          )

          appendHelper(
            result,
            currentIndex + 1
          )
        }

      appendHelper(
        this,
        0
      )
    }

    def reverse(): Seq[TrieNode] = {

      @tailrec
      def reverseHelper(valueLeaves: Seq[TrieNode], leaves: Seq[TrieNode]): Seq[TrieNode] = {
        val (v, r) = leaves.partition(node => node.value.isDefined)
        r match {
          case Nil => valueLeaves ++ v
          case _   =>
            reverseHelper(
              valueLeaves ++ v,
              r.flatMap(_.leaves.values)
            )
        }

      }

      reverseHelper(
        Seq.empty,
        this.leaves.values.toSeq
      )
    }

  }

  object TrieNode {

    def fold(seq: List[TrieNode]): Seq[TrieNode] = {
      val iterator                  = seq.iterator
      var foldedSeq: List[TrieNode] = List.empty[TrieNode]
      while (iterator.hasNext) {
        val node = iterator.next()
        if (Try(Integer.parseInt(node.id)).isFailure) {
          val value = node.value.get
          foldedSeq = foldedSeq ++ List(
            TrieNode(
              id = node.parent.get.id,
              value = Some(value.atKey(node.id).root())
            )
          )
        }
      }
      foldedSeq
    }

    case class StackFrame(value: ConfigValue, computedChildren: List[ConfigValue], remainingChildren: List[TrieNode])

    def fold(t: TrieNode)(f: String => ConfigValue)(g: (ConfigValue, List[ConfigValue]) => ConfigValue): ConfigValue = {

      def go(stack: List[StackFrame]): ConfigValue = stack match {
        case StackFrame(v, cs, Nil) :: tail                 =>
          val folded = g(
            f(v),
            cs.reverse
          )
          tail match {
            case Nil                                  => folded
            case StackFrame(vUp, csUp, remUp) :: rest =>
              go(
                StackFrame(
                  vUp,
                  folded :: csUp,
                  remUp
                ) :: rest
              )
          }
        case StackFrame(v, cs, nextChild :: others) :: tail =>
          go(
            StackFrame(
              nextChild.value.get,
              Nil,
              nextChild.leaves.values.toList
            ) ::
              StackFrame(
                v,
                cs,
                others
              ) ::
              tail
          )
        case Nil                                            => sys.error("Should not go there")
      }

      go(
        StackFrame(
          t.value.get,
          Nil,
          t.leaves.values.toList
        ) :: Nil
      )
    }

  }

  case class Node(id: String, children: Map[String, ConfigValue]) {
    var isCollection: Boolean  = false
    def markCollection(): Unit = this.isCollection = true
  }

  object ParamNamePatters {
    val SequenceIndex         = "[0-9]+"
    val PathWithSequenceIndex = s"/$SequenceIndex(/)?"

  }

}
