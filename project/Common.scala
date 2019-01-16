object CommonBuildConfiguration {

  val preformServiceName: String => String =
    (_serviceName: String) => {
      normalizedName("service")(_serviceName)
    }

  val preformProjectLibraryImplName: String => String =
    (_libraryName: String) => {
      normalizedName("impl")(_libraryName)
    }
  val preformProjectLibraryApiName: String => String =
    (_libraryName: String) => {
      normalizedName("api")(_libraryName)
    }

  private def normalizedName(typeName: String)(name: String) = {
      s"$name${if (!typeName.isEmpty)"-" + typeName}"
    }

}

object ConfigPaths {
  val root = "./"

  def lib: Seq[String] => String = (args: Seq[String]) => {
    root + normalizedPath(List("stack", "playframework") ::: args.toList)
  }

  def api: Seq[String] => String = (args: Seq[String]) => {
    root + normalizedPath(List("stack", "playframework") ::: args.toList) + "-api"
  }
  
  def service: Seq[String] => String = (args: Seq[String]) => {
    root + normalizedPath(List("stack", "playframework") ::: args.toList) + "-service"
  }
  
  def impl: Seq[String] => String = (args: Seq[String]) => {
    root + normalizedPath(List("stack", "playframework") ::: args.toList) + "-impl"
  }

  private def normalizedPath: List[String] => String =
    (args: List[String]) => {
      s"${args.mkString("/")}"
    }
}

object CommonConfiguration {}
