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

trait CirisIp4sInstances {
  import com.comcast.ip4s._

  implicit val cirisComcastPortDecoder: ConfigDecoder[String, Port] =
    ConfigDecoder[String, Int].mapOption("Port")(Port.fromInt)

  implicit val cirisComcastHostDecoder: ConfigDecoder[String, Host] =
    ConfigDecoder[String, String].mapOption("Host")(Host.fromString)

  implicit val cirisComcastHostnameDecoder: ConfigDecoder[String, Hostname] =
    ConfigDecoder[String, String].mapOption("Hostname")(Hostname.fromString)

  implicit val cirisComcastIpAddressDecoder: ConfigDecoder[String, IpAddress] =
    ConfigDecoder[String, String].mapOption("IpAddress")(IpAddress.fromString)

  implicit val cirisComcastIpv4AddressDecoder: ConfigDecoder[String, Ipv4Address] =
    ConfigDecoder[String, String].mapOption("Ipv4Address")(Ipv4Address.fromString)

  implicit val cirisComcastIpv6AddressDecoder: ConfigDecoder[String, Ipv6Address] =
    ConfigDecoder[String, String].mapOption("Ipv6Address")(Ipv6Address.fromString)
}
