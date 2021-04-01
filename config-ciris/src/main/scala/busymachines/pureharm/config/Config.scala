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

import cats.implicits._
import cats.effect._

/** Capability trait for reading config files.
  *
  * It's preferable to create this _explicitely_
  * once in your top-most app, and then pass it
  * along to anywhere where ou need configs to be read
  * this way, you don't pass _extremely_ powerful
  * constraints like Async, which can theoretically
  * do arbitrary side-effects.
  */
sealed trait Config[F[_]] {
  implicit protected val ctxShift: ContextShift[F]
  implicit protected val async:    Async[F]
  def load[T](value: ConfigValue[T]): F[T] = value.load[F]
}

object Config {

  def apply[F[_]](implicit c: Config[F]): Config[F] = c

  def async[F[_]](implicit F: Async[F], cs: ContextShift[F]): Config[F] = new Config[F] {
    override protected val async:    Async[F]        = F
    override protected val ctxShift: ContextShift[F] = cs
  }

  def resource[F[_]](implicit F: Async[F], cs: ContextShift[F]): Resource[F, Config[F]] =
    this.async[F].pure[Resource[F, *]]

}
