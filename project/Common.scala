object CommonBuildConfiguration {

  private def _globalName: String = "kzonix"

  private def __ : String = "_"

  private val normalizedName: (String, String) => String =
    (name: String, typeName: String) => {
      s"${_globalName}${__}$name${__}$typeName"
    }

  val preformServiceName: String => String =
    (_serviceName: String) => {
      normalizedName.apply(_serviceName, "service")
    }

  val preformModuleName: String => String =
    (_moduleName: String) => {
      normalizedName.apply(_moduleName, "module")
    }

  val preformImplLibraryName: String => String =
    (_libraryName: String) => {
      normalizedName.apply(_libraryName, "impl")
    }

  val preformApiLibraryName: String => String =
    (_libraryName: String) => {
      normalizedName.apply(_libraryName, "api")
    }

}

object ConfigPaths {
  val root = "./"
  private val normalizedPath: List[String] => String =
    (args: List[String]) => {
      s"${args.mkString("/")}"
    }

  val api: Seq[String] => String = (args: Seq[String]) => {
    root + normalizedPath(List("stack", "playframework") ::: args.toList) + "-api"
  }
  val service: Seq[String] => String = (args: Seq[String]) => {
    root + normalizedPath(List("stack", "playframework") ::: args.toList) + "-service"
  }
  val impl: Seq[String] => String = (args: Seq[String]) => {
    root + normalizedPath(List("stack", "playframework") ::: args.toList) + "-impl"
  }
}

object CommonConfiguration {}
