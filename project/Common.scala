object CommonBuildConfiguration {

  val preformServiceName: String => String =
    (serviceName: String) => {
      normalizedName("service")(serviceName)
    }

  val preformProjectLibraryImplName: String => String =
    (libraryName: String) => {
      normalizedName("impl")(libraryName)
    }
  val preformProjectLibraryApiName: String => String =
    (libraryName: String) => {
      normalizedName("api")(libraryName)
    }

  private def normalizedName(typeName: String)(name: String) = {
      s"$name${if (!typeName.isEmpty)"-" + typeName}"
    }

}

object ConfigPaths {
  val root = "./"

  def lib: Seq[String] => String = (args: Seq[String]) => {
    root + normalizedPath(List("stack", "playframework") ::: (args toList))
  }

  def api: Seq[String] => String = (args: Seq[String]) => {
    root + normalizedPath(List("stack", "playframework") ::: (args toList)) + "-api"
  }
  
  def service: Seq[String] => String = (args: Seq[String]) => {
    root + normalizedPath(List("stack", "playframework") ::: (args toList)) + "-service"
  }
  
  def impl: Seq[String] => String = (args: Seq[String]) => {
    root + normalizedPath(List("stack", "playframework") ::: (args toList)) + "-impl"
  }

  private def normalizedPath: Seq[String] => String =
    (args: Seq[String]) => {
      s"${args mkString "/"}"
    }
}


object CommonConfiguration {}
