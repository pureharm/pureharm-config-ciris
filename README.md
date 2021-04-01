# pureharm-config-ciris

See [changelog](./CHANGELOG.md).

## modules

The available modules are. Available for Scala `2.13`, only on the JVM platform.

- `"com.busymachines" %% s"pureharm-config-ciris" % "0.1.0"`. Which has these as its main dependencies:
  - [ciris](https://github.com/vlovgr/ciris/releases) `1.2.1`
  - [ip4s](https://github.com/Comcast/ip4s/releases) `2.0.1`
  - [pureharm-core-sprout](https://github.com/busymachines/pureharm-core/releases) `0.2.0`
  - [pureharm-core-anomaly](https://github.com/busymachines/pureharm-core/releases) `0.2.0`

## usage

The encouraged style to use this library is to create your own "config" package in your app, which contains the opiniated way of doing configs in your application. That's why `pureharm-config-ciris` provides a straightforward way of achieving this, while allowing you to add your own stuff on top.

```scala
import cats._
import cats.effect._
import cats.implicits._

import busymachines.pureharm.sprout._
import busymachines.pureharm.config

/** Recommended to do something similar once in your app. To define your prefered
  * "flavor" of reading configs.
  *
  * In this case we basically explicitely say that we only get config values from environment
  * variables.
  */
object myconfig extends config.PureharmConfigAllAliases with config.PureharmConfigAllImplicits {
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
  def env(s: EnvVar): ConfigValue[String] = ciris.env(s.toString)
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

      override def configValue: ConfigValue[DBConfig] =
        (
          env(EnvVar.PH_DB_PORT).as[DBPort].default(DBPort(port"5432")),
          env(EnvVar.PH_DB_HOST).as[DBHost].default(DBHost(host"localhost")),
          env(EnvVar.PH_DB_NAME).as[DBName].default(DBName("example_db_name")),
        ).parMapN(DBConfig.apply)
    }

    private object ServerConfigLoader extends ConfigLoader[ServerConfig] {

      override def configValue: ConfigValue[ServerConfig] = (
        env(EnvVar.PH_DB_PORT).as[ServerPort].default(ServerPort(port"21312")),
        env(EnvVar.PH_DB_HOST).as[ServerHost].default(ServerHost(host"localhost")),
      ).parMapN(ServerConfig.apply)

    }
  }

  /** Instantiate the Config[F] capability once,
    * pass along everywhere where used.
    */
  object MyIOApp extends IOApp {

    override def run(args: List[String]): IO[ExitCode] = runF[IO]

    def runF[F[_]: Async: ContextShift]: F[ExitCode] = {
      implicit val config: Config[F] = Config.async[F]

      for {
        exampleConfig <- ExampleConfig.load[F]
        _             <- Async[F].delay(
          println(s"pass along the $exampleConfig config to the rest of your app in bits and pieces you need")
        )
      } yield ExitCode.Success
    }
  }

}
```

## Copyright and License

All code is available to you under the Apache 2.0 license, available
at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0) and also in
the [LICENSE](./LICENSE) file.
