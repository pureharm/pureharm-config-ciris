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

package busymachines.test

import cats._
import cats.effect._
import cats.implicits._

import busymachines.pureharm.sprout._
import busymachines.pureharm.config

/** Recommended to do something similar once in your app. To define your prefered "flavor" of reading configs.
  *
  * In this case we basically explicitely say that we only get config values from environment variables.
  */
object myconfig
  extends config.PureharmConfigAllAliases with config.PureharmConfigAllImplicits with config.CirisIp4sInstances {
  sealed trait EnvVar

  /** Single source of truth for the env vars in the system
    */
  object EnvVar {
    case object PH_DB_PORT     extends EnvVar
    case object PH_DB_HOST     extends EnvVar
    case object PH_DB_NAME     extends EnvVar
    case object PH_SERVER_PORT extends EnvVar
    case object PH_SERVER_HOST extends EnvVar
  }

  /** This alias allows us some bit of typesafety in our app
    */
  def env(s: EnvVar): ConfigValue[CirisEffect, String] = ciris.env(s.toString)
}

/** If it typechecks, ship it!
  */
object CirisConfigExample {
  import com.comcast.ip4s._
  import myconfig._

  type DBPort = DBPort.Type
  object DBPort extends Sprout[Port]

  type DBHost = DBHost.Type
  object DBHost extends Sprout[Host]

  type DBName = DBName.Type
  object DBName extends Sprout[String]

  //in a real world app these might come from a different module that doesn't even depend on config
  case class DBConfig(
    port: DBPort,
    host: DBHost,
    name: DBName,
  )

  type ServerPort = ServerPort.Type
  object ServerPort extends Sprout[Port]

  type ServerHost = ServerHost.Type
  object ServerHost extends Sprout[Host]

  //in a real world app these might come from a different module that doesn't even depend on config
  case class ServerConfig(
    port: ServerPort,
    name: ServerHost,
  )

  //an example of a top-level module config that loads everything at once
  case class ExampleConfig(
    db:     DBConfig,
    server: ServerConfig,
  )

  object ExampleConfig {

    def load[F[_]: Config: Monad]: F[ExampleConfig] = for {
      db     <- DBConfigLoader.load[F]
      server <- ServerConfigLoader.load[F]
    } yield ExampleConfig(
      db,
      server,
    )

    private object DBConfigLoader extends ConfigLoader[DBConfig] {

      override def configValue: ConfigValue[CirisEffect, DBConfig] =
        (
          env(EnvVar.PH_DB_PORT).as[DBPort].default(DBPort(port"5432")),
          env(EnvVar.PH_DB_HOST).as[DBHost].default(DBHost(host"localhost")),
          env(EnvVar.PH_DB_NAME).as[DBName].default(DBName("example_db_name")),
        ).parMapN(DBConfig.apply)
    }

    private object ServerConfigLoader extends ConfigLoader[ServerConfig] {

      override def configValue: ConfigValue[CirisEffect, ServerConfig] = (
        env(EnvVar.PH_DB_PORT).as[ServerPort].default(ServerPort(port"21312")),
        env(EnvVar.PH_DB_HOST).as[ServerHost].default(ServerHost(host"localhost")),
      ).parMapN(ServerConfig.apply)

    }
  }

  /** Instantiate the Config[F] capability once, pass along everywhere where used.
    */
  object MyIOApp extends cats.effect.IOApp.Simple {

    override def run: IO[Unit] = runF[IO]

    def runF[F[_]: Async]: F[Unit] = {
      implicit val config: Config[F] = Config.async[F]

      for {
        exampleConfig <- ExampleConfig.load[F]
        _             <- Async[F].blocking(
          println(s"pass along the $exampleConfig config to the rest of your app in bits and pieces you need")
        )
      } yield ()
    }
  }

}
