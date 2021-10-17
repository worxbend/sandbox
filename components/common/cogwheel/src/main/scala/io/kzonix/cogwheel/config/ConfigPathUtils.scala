package io.kzonix.cogwheel.config

object ConfigPathUtils {

  object PathUtils {
    private val PathSep      = "/"
    private val ConfigKeySep = "."

    implicit class WithConfigNamespacePrefix(s: String) {
      def withConfigNameSpace(namespace: String): String =
        s"$PathSep${ toParamName(namespace) }$PathSep$s"
    }

    def toParamName(configKey: String): String =
      configKey.replace(
        ConfigKeySep,
        PathSep
      )

    def toConfigKey(paramPath: String): String =
      paramPath
        .stripPrefix(PathSep)
        .replace(
          PathSep,
          ConfigKeySep
        )

  }

}
