package io.kzonix.cogwheel.aws

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathRequest
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathResult
import com.amazonaws.services.simplesystemsmanagement.model.Parameter
import io.kzonix.cogwheel.config.ConfigPathUtils.PathUtils.toParamName

import scala.annotation.tailrec
import scala.collection.immutable
import scala.jdk.CollectionConverters._

trait AWSParameterStoreClient {

  type Parameters = immutable.Map[String, String]

  def fetchParameters(paramPath: String): Map[String, String]
  def fetchParameter(paramPath: String): String
}

class SimpleParameterStoreClient(ssmClient: AWSSimpleSystemsManagement) extends AWSParameterStoreClient {

  @tailrec private final def fetchParameters(request: GetParametersByPathRequest): Parameters = {

    def handleResult(result: GetParametersByPathResult): Parameters =
      result.getParameters.asScala.map((p: Parameter) => p.getName -> p.getValue).toMap

    val paramsByPath: GetParametersByPathResult                     = ssmClient.getParametersByPath(request)
    val nextToken                                                   = paramsByPath.getNextToken
    Option(nextToken).filter(_.nonEmpty) match {
      case Some(token: String) => fetchParameters(request.withNextToken(token))
      case None                => handleResult(paramsByPath)
    }
  }

  override def fetchParameters(paramName: String): Map[String, String] = {
    val request = new GetParametersByPathRequest()
      .withPath(toParamName(paramName))
      .withRecursive(true)
      .withWithDecryption(true)

    fetchParameters(request)
  }

  override def fetchParameter(paramPath: String): String = {
    val request                    = new GetParameterRequest().withName(paramPath)
    val result: GetParameterResult = ssmClient.getParameter(request)
    result.getParameter.getValue
  }

}

class CacheAwareParameterStoreClient(parameterStoreClient: AWSParameterStoreClient) extends AWSParameterStoreClient {

  override def fetchParameters(paramPath: String): Map[String, String] =
    // fetch from cache
    parameterStoreClient.fetchParameters(paramPath)

  override def fetchParameter(paramPath: String): String =
    // fetch from cache
    parameterStoreClient.fetchParameter(paramPath)

}
