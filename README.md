# Oslo
[![Maven Central](https://img.shields.io/maven-central/v/net.fosdal/oslo_2.11.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22net.fosdal%22%20AND%20a%3A%22oslo_2.11%22)
[![CircleCI](https://circleci.com/gh/sfosdal/oslo.svg?style=shield)](https://circleci.com/gh/sfosdal/oslo)
[![Coverage Status](https://coveralls.io/repos/github/sfosdal/oslo/badge.svg?branch=master)](https://coveralls.io/github/sfosdal/oslo?branch=master)

### Overview
Oslo is a tiny Scala library of small but hopefully useful things.

### Getting Started
Oslo is currently available for Scala 2.11.

2.12 support is comming soon. (see: [#4 - Cross Build for 2.12](/sfosdal/oslo/issues/4))


To get started with SBT, simply add the following to your `build.sbt`
file:

```scala
libraryDependencies += "net.fosdal" %% "oslo" % "0.2.1"
```

Release notes for Oslo are available in [CHANGES.md](CHANGES.md).

### User Guide
Most of Oslo is organized around package objects. In general, this means you need to import the package object and the methods are then available for use. Here is a list of the package objects and examples on using them:

#### net.fosdal.oslo._
TBD

#### net.fosdal.oslo.ocsv._
TBD

#### net.fosdal.oslo.odatetime._
TBD

#### net.fosdal.oslo.oduration._
TBD

#### net.fosdal.oslo.ofile._
TBD

#### net.fosdal.oslo.onumber._
TBD

#### net.fosdal.oslo.oordering._
TBD

#### net.fosdal.oslo.oseq._
TBD

#### net.fosdal.oslo.ostring._

#### oduration
pretty: converts Duration into pretty string.

```scala
import net.fosdal.oslo.oduration._
import scala.concurrent.duration._

4000.seconds.pretty
380.minutes.pretty
35352352.seconds.pretty
```

```
res0: String = 1.1h
res1: String = 6.3h
res2: String = 409.2d
```

pretty(precision:Int): converts Duration into pretty string with supplied precision
```scala
import net.fosdal.oslo.oduration._
import scala.concurrent.duration._

4000.seconds.pretty()
380.minutes.pretty(0)
35352352.seconds.pretty(3)
```

```
res0: String = 1.1h
res1: String = 6h
res2: String = 409.171d
```
