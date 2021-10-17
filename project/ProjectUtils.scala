import scala.language.postfixOps

object ProjectUtils {

  object ProjectNames {

    def service(serviceName: String): String = normalizedName(
      "service",
      serviceName
    )

    def app(serviceName: String): String =
      normalizedName(
        "app",
        serviceName
      )

    def lib(libraryName: String): String =
      normalizedName(
        "impl",
        libraryName
      )

    def api(libraryName: String): String =
      normalizedName(
        "api",
        libraryName
      )

    private def normalizedName(typeName: String = "app", name: String): String =
      s"$name-$typeName"
  }

  object ProjectPaths {
    // build.sbt relative root directory
    val root = "./"

    def normalizedPath(args: Seq[String]): String = s"${ args.mkString("/") }"

    trait ProjectType {
      protected val basePath: String

    }

    trait GeneralComponent extends Project {
      override val basePath: String = "components"
    }

    trait Application extends Project {
      override val basePath: String = "applications"
    }

    trait Project extends ProjectType {

      protected val projectMainPath: String

      def api(args: Seq[String]): String =
        root + normalizedPath(
          List(
            basePath,
            projectMainPath
          ) ::: (args toList)
        ) + "-api"

      def impl(args: Seq[String]): String =
        root + normalizedPath(
          List(
            basePath,
            projectMainPath
          ) ::: (args toList)
        ) + "-impl"

      def lib(args: Seq[String]): String =
        root + normalizedPath(
          List(
            basePath,
            projectMainPath
          ) ::: (args toList)
        )

      def service(args: Seq[String]): String =
        root + normalizedPath(
          List(
            basePath,
            projectMainPath
          ) ::: (args toList)
        ) + "-service"

      def app(args: Seq[String]): String =
        root + normalizedPath(
          List(
            basePath,
            projectMainPath
          ) ::: (args toList)
        ) + "-app"

    }

    object Components {

      object Common extends GeneralComponent {
        override val projectMainPath: String = "common"
      }

      object Play extends GeneralComponent {
        override val projectMainPath: String = "playframework"
      }

      object Akka extends GeneralComponent {
        override val projectMainPath: String = "akka"
      }

      object Udash extends GeneralComponent {
        override val projectMainPath: String = "udash"
      }

      object ScalaFX extends GeneralComponent {
        override val projectMainPath: String = "scala-fx"
      }

      object VertX extends GeneralComponent {
        override val projectMainPath: String = "vert-x"
      }

      object Http4s extends GeneralComponent {
        override val projectMainPath: String = "http4s"
      }

      object PicoliCLI extends GeneralComponent {
        override val projectMainPath: String = "picoli-cli"
      }

    }

    object Applications {

      object Common extends Application {
        override val projectMainPath: String = "common"
      }

      object Sandbox extends Application {
        override val projectMainPath: String = "sandbox"
      }

      object Root extends Application {
        override val projectMainPath: String = "./"
      }

    }

  }

}
