package io.kzonix.cogwheel

import io.kzonix.cogwheel.aws.AWSParameterStoreClient
import io.kzonix.cogwheel.config.RemoteConfigFactory
import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite

import java.time.Duration

class RemoteConfigFactorySuite extends AnyFunSuite with MockFactory {

  val parameterStoreClient: AWSParameterStoreClient = mock[AWSParameterStoreClient]
  val configFactory                                 = new RemoteConfigFactory(parameterStoreClient)

  val testJson = "{\"menu\": {\"id\": \"file\",\"menuitem\": [{\"value\": \"New\",\"onclick\": \"CreateNewDoc()\"}]}}"

  test("Should parse simple value under the parameter path to config object") {
    (parameterStoreClient.fetchParameters _)
      .expects("/region-1/customer/test_app")
      .returns(Map("/region-1/customer/test_app/test_key" -> "1m"))

    val config = configFactory.loadConfig(
      "region-1",
      "customer.test_app"
    )
    assert(config.getDuration("region-1.customer.test_app.test_key") == Duration.ofMinutes(1L))
  }

  test("Should parse JSON string value under the parameter path to config object") {
    (parameterStoreClient.fetchParameters _)
      .expects("/region-1/customer/test_app")
      .returns(Map("/region-1/customer/test_app/test_key" -> testJson))

    val config = configFactory.loadConfig(
      "region-1",
      "customer.test_app"
    )
    assert(
      config
        .getConfigList("region-1.customer.test_app.test_key.menu.menuitem")
        .get(0)
        .getString("value") == "New"
    )
    assert(
      config
        .getConfigList("region-1.customer.test_app.test_key.menu.menuitem")
        .get(0)
        .getString("onclick") == "CreateNewDoc()"
    )
    assert(config.getString("region-1.customer.test_app.test_key.menu.id") == "file")
  }

  test("Should parse sequence element represents JSON string value under the parameter path config object") {
    (parameterStoreClient.fetchParameters _)
      .expects("/region-1/customer/test_app")
      .returns(
        Map(
          "/region-1/customer/test_app/test_key/seq_0"          -> testJson,
          "/region-1/customer/test_app/test_key/seq_1"          -> testJson,
          "/region-1/customer/test_app/test_key/seq_2"          -> testJson,
          "/region-1/customer/another_app/seq_1/test_key/seq_1" -> testJson,
          "/region-1/customer/another_app/seq_2/test_key/seq_2" -> testJson,
          "/region-1/customer/another_app/seq_3/test_key/seq_3" -> testJson
        )
      )

    val config = configFactory.loadConfig(
      "region-1",
      "customer.test_app"
    )
    println(config)
    assert(
      config
        .getConfigList("region-1.customer.test_app.test_key.menu.menuitem")
        .get(0)
        .getString("value") == "New"
    )
    assert(
      config
        .getConfigList("region-1.customer.test_app.test_key.menu.menuitem")
        .get(0)
        .getString("onclick") == "CreateNewDoc()"
    )
    assert(config.getString("region-1.customer.test_app.test_key.menu.id") == "file")
  }
}
