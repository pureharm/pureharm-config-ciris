# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

# unreleased

# 0.2.0

This release brings cats-effect 3 and Scala 3 support!

- split out `ip4s` support in a seperate module called `pureharm-config-ciris-ip4s`, simply mixin `busymachines.pureharm.config.CirisIp4sInstances` to bring in typeclass instances.

### scala versions
- add build for Scala `3`

### dependency upgrades
- [ciris](https://github.com/vlovgr/ciris/releases) `2.0.1`
- [cats-effect](https://github.com/typelevel/cats-effect/releases) `3.2.1`
- [ip4s](https://github.com/Comcast/ip4s/releases) `3.0.3`
- [pureharm-core-sprout](https://github.com/busymachines/pureharm-core/releases) `0.3.0`
- [pureharm-core-anomaly](https://github.com/busymachines/pureharm-core/releases) `0.3.0`

# 0.1.0

Add type aliases for the [`ciris`](https://github.com/vlovgr/ciris/releases) library, w/ sprout integrations,
and support for reading `ip4s` types.

### scala versions
- add build for Scala `2.13`

### dependency upgrades
- [ciris](https://github.com/vlovgr/ciris/releases) `1.2.1`
- [ip4s](https://github.com/Comcast/ip4s/releases) `2.0.1`
- [pureharm-core-sprout](https://github.com/busymachines/pureharm-core/releases) `0.2.0`
- [pureharm-core-anomaly](https://github.com/busymachines/pureharm-core/releases) `0.2.0`