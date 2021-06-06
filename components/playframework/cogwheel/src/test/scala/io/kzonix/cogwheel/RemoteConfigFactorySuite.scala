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

  // TODO: Refactor this test to make it more tinier and readable:
  //  - AAA
  //  - Add more unit test for this particular case of element sequence represents JSON string value on different levels
  test("Should parse sequence element represents JSON string value under the parameter path config object") {
    (parameterStoreClient.fetchParameters _)
      .expects("/region-1/customer")
      .returns(
        Map(
          "/region-1/customer/test_app/test_key/seq_0"                    -> testJson,
          "/region-1/customer/test_app/test_key/seq_1"                    -> testJson,
          "/region-1/customer/test_app/test_key/seq_2"                    -> testJson,
          "/region-1/customer/another_app/seq_0/first/seq_0/second/seq_0" -> testJson,
          "/region-1/customer/another_app/seq_0/first/seq_0/second/seq_1" -> "testJson",
          "/region-1/customer/another_app/seq_1/first/seq_1/second/seq_2" -> testJson,
          "/region-1/customer/another_app/seq_1/first/seq_1/second/seq_3" -> testJson
        )
      )

    val config = configFactory.loadConfig(
      "region-1",
      "customer"
    )
    assert(
      config
        .getConfigList("region-1.customer.another_app")
        .size() == 2
    )
    assert(
      config
        .getConfigList("region-1.customer.another_app")
        .get(0)
        .getConfigList("first")
        .get(0)
        .getList("second")
        .size() == 2
    )
  }

}
