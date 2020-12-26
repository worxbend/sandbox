object CommonBuildConfiguration {

  val preformServiceName: String => String =
    (serviceName: String) => {
      normalizedName("service")(serviceName)
    }

  val preformAppName: String => String =
    (serviceName: String) => {
      normalizedName("app")(serviceName)
    }

  val preformProjectLibraryImplName: String => String =
    (libraryName: String) => {
      normalizedName("impl")(libraryName)
    }

  val preformProjectLibraryApiName: String => String =
    (libraryName: String) => {
      normalizedName("api")(libraryName)
    }

  lazy val normalizedName: String ⇒ String ⇒ String = (typeName: String) ⇒
    (name: String) => {
      s"$name${if (typeName != null && typeName.nonEmpty) "-" + typeName}"
    }

}

object ConfigPaths {
  val root = "./"

  lazy val normalizedPath: Seq[String] => String =
    (args: Seq[String]) => {
      s"${args.mkString("/")}"
    }

  trait Project {

    private val basePath = "components"

    lazy val lib: Seq[String] => String = (args: Seq[String]) => {
      root + normalizedPath(
        List(
          basePath,
          stack
        ) ::: (args toList)
      )
    }

    lazy val api: Seq[String] => String = (args: Seq[String]) => {
      root + normalizedPath(
        List(
          basePath,
          stack
        ) ::: (args toList)
      ) + "-api"
    }

    lazy val service: Seq[String] => String = (args: Seq[String]) => {
      root + normalizedPath(
        List(
          basePath,
          stack
        ) ::: (args toList)
      ) + "-service"
    }

    lazy val app: Seq[String] => String = (args: Seq[String]) => {
      root + normalizedPath(
        List(
          basePath,
          stack
        ) ::: (args toList)
      ) + "-app"
    }

    lazy val impl: Seq[String] => String = (args: Seq[String]) => {
      root + normalizedPath(
        List(
          basePath,
          stack
        ) ::: (args toList)
      ) + "-impl"
    }

    val stack: String
  }

  object Play extends Project {
    override val stack: String = "playframework"
  }

  object Udash extends Project {
    override val stack: String = "udash"
  }

  object ScalaFX extends Project {
    override val stack: String = "scala-fx"
  }

  object VertX extends Project {
    override val stack: String = "vert-x"
  }

  object Http4s extends Project {
    override val stack: String = "http4s"
  }

  object PicoliCLI extends Project {
    override val stack: String = "picoli-cli"
  }

}

object CommonConfiguration {}
