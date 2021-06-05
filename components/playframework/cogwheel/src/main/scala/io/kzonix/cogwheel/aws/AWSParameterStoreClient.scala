package io.kzonix.cogwheel.aws

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathRequest
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathResult

import scala.annotation.tailrec
import scala.jdk.CollectionConverters._

trait AWSParameterStoreClient {
  def fetchParameters(paramPath: String): Map[String, String]
  def fetchParameter(paramPath: String): String
}

class SimpleParameterStoreClient(ssmClient: AWSSimpleSystemsManagement) extends AWSParameterStoreClient {

  private def toMap(result: GetParametersByPathResult): Map[String, String] =
    result.getParameters.asScala.map(p => p.getName -> p.getValue).toMap

  @tailrec private def fetchParameters(request: GetParametersByPathRequest): Map[String, String] = {
    val result: GetParametersByPathResult = ssmClient.getParametersByPath(request)
    val token                             = result.getNextToken
    Option(token).filter(_.nonEmpty) match {
      case Some(token: String) => fetchParameters(request.withNextToken(token))
      case None                => toMap(result)
    }
  }

  override def fetchParameters(paramPath: String): Map[String, String] = {
    val request = new GetParametersByPathRequest()
      .withPath(paramPath)
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
