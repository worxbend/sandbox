package io.kzonix.quickstart

import akka.Done
import akka.actor.ClassicActorSystemProvider
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.Behaviors
import akka.kafka.ConsumerMessage.CommittableOffsetBatch
import akka.kafka.CommitterSettings
import akka.kafka.ConsumerSettings
import akka.kafka.Subscriptions
import akka.kafka.scaladsl.Committer
import akka.kafka.scaladsl.Consumer
import akka.kafka.scaladsl.Consumer.DrainingControl
import akka.stream.scaladsl.RunnableGraph
import com.typesafe.config.Config
import io.kzonix.quickstart.ConsumerStreamActor.ConsumerCommand
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

import java.util.concurrent.atomic.AtomicReference
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class ConsumerStreamActor()(implicit ac: ClassicActorSystemProvider) {

  val controlContainer = new AtomicReference[Consumer.Control](Consumer.NoopControl)

  def create(context: ActorContext[ConsumerCommand]): Unit = {
    val consumerSettings: ConsumerSettings[String, String] = getConsumerSettings(ac.classicSystem.settings.config)

    val control: RunnableGraph[DrainingControl[Done]] = Consumer
      .committableSource(
        consumerSettings,
        Subscriptions.topics("backblaze_smart")
      )
      .groupedWithin(
        100,
        100.milliseconds
      )
      .mapAsync(10) { msg =>
        Future {
          context.log.info(s"Processing $msg")
          CommittableOffsetBatch(msg.map(_.committableOffset))
        }(context.executionContext)
      }
      .toMat(Committer.sink(CommitterSettings(context.system)))(DrainingControl.apply)

    val value: DrainingControl[Done] = control.run()
    controlContainer.set(value)
  }

  private def getConsumerSettings(config: Config) =
    ConsumerSettings(
      config.getConfig("akka.kafka.consumer"),
      new StringDeserializer,
      new StringDeserializer
    )
      .withBootstrapServers("127.0.0.1:9092")
      .withGroupId("group2")
      .withProperty(
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
        "earliest"
      )

}

object ConsumerStreamActor {

  sealed trait ConsumerCommand
  case class StartConsumer(topic: String) extends ConsumerCommand
  case class StopConsumer(topic: String)  extends ConsumerCommand

  def apply(): Behavior[ConsumerCommand] =
    Behaviors.setup { context =>
      Behaviors.receive[ConsumerCommand]((context, msg) =>
        msg match {
          case StartConsumer(topic) => Behaviors.same
          case StopConsumer(topic)  => Behaviors.same
        }
      )
    }

}
