package io.kzonix.cogwheel.config.parser

import com.typesafe.config.ConfigValue

trait ParameterParser[P, C <: ConfigValue] {
  def parse(value: P): C
}
