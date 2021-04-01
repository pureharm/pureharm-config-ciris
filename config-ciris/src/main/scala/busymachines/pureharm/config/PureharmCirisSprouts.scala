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
import busymachines.pureharm.sprout._

trait PureharmCirisSprouts {

  implicit def pureharmDefaultCirisEnvVarNewTypeConfigDecoder[O, N](implicit
    nt: NewType[O, N],
    ev: ConfigDecoder[String, O],
  ): ConfigDecoder[String, N] = ev.map(nt.newType)

  implicit def pureharmDefaultCirisEnvVarRefinedTypeConfigDecoder[O, N](implicit
    nt: RefinedTypeThrow[O, N],
    ev: ConfigDecoder[String, O],
  ): ConfigDecoder[String, N] = ev.sproutRefined[N]

  implicit class PureharmCirisSproutsOps[O](v: StringConfigDecoder[O]) {
    def sprout[N](implicit s: NewType[O, N]): StringConfigDecoder[N] = v.map(s.newType)

    def sproutRefined[N](implicit s: RefinedTypeThrow[O, N]): StringConfigDecoder[N] =
      v.mapEither { (cfgKey, value) =>
        s.newType[Either[Throwable, *]](value).leftMap(thr => ConfigError(s"$cfgKey --> ${thr.toString}"))
      }
  }
}
