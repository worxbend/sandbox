object CommonBuildConfiguration {

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
  private val normalizedName: (String, String) => String =
    (name: String, typeName: String) => {
      s"${this._globalName}${this.--}$name${this.--}$typeName"
    }

  private def _globalName: String = "kzonix"

  private def -- : String = "-"

}

object ConfigPaths {
  val root = "./"
  val api: Seq[String] => String = (args: Seq[String]) => {
    root + normalizedPath(List("stack", "playframework") ::: args.toList) + "-api"
  }
  val service: Seq[String] => String = (args: Seq[String]) => {
    root + normalizedPath(List("stack", "playframework") ::: args.toList) + "-service"
  }
  val impl: Seq[String] => String = (args: Seq[String]) => {
    root + normalizedPath(List("stack", "playframework") ::: args.toList) + "-impl"
  }
  private val normalizedPath: List[String] => String =
    (args: List[String]) => {
      s"${args.mkString("/")}"
    }
}

object CommonConfiguration {}
