package io.kzonix.cogwheel

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder
import io.kzonix.cogwheel.aws.AWSParameterStoreClient
import io.kzonix.cogwheel.aws.SimpleParameterStoreClient
import io.kzonix.cogwheel.config.RemoteConfigFactory

object Main extends App {

  private val awsSimpleSystemsManagementClient: AWSSimpleSystemsManagement =
    AWSSimpleSystemsManagementClientBuilder
      .standard()
      .withCredentials(new DefaultAWSCredentialsProviderChain)
      .withRegion("eu-central-1")
      .build()

  private val client: AWSParameterStoreClient = new SimpleParameterStoreClient(awsSimpleSystemsManagementClient)

  private val configFactory = new RemoteConfigFactory(client)

  private val value = configFactory.loadConfig(
    "dc22",
    "app"
  )

  println(value.withFallback(value))
}
