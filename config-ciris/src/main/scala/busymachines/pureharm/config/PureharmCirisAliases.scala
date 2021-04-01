/*
 * Copyright 2021 BusyMachines
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package busymachines.pureharm.config

trait PureharmCirisAliases {
  type ConfigDecoder[A, B] = ciris.ConfigDecoder[A, B]
  val ConfigDecoder: ciris.ConfigDecoder.type = ciris.ConfigDecoder

  type Secret[+A] = ciris.Secret[A]
  val Secret: ciris.Secret.type = ciris.Secret

  type ConfigError = ciris.ConfigError
  val ConfigError: ciris.ConfigError.type = ciris.ConfigError

  type ConfigKey = ciris.ConfigKey
  val ConfigKey: ciris.ConfigKey.type = ciris.ConfigKey

  type ConfigValue[A] = ciris.ConfigValue[A]
  val ConfigValue: ciris.ConfigValue.type = ciris.ConfigValue

  def default[A](value: => A): ConfigValue[A] = ciris.default(value)
}
