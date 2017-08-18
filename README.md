# Oslo
[![Maven Central](https://img.shields.io/maven-central/v/net.fosdal/oslo_2.11.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22net.fosdal%22%20AND%20a%3A%22oslo_2.11%22)
[![CircleCI](https://circleci.com/gh/sfosdal/oslo.svg?style=shield)](https://circleci.com/gh/sfosdal/oslo)
[![Coverage Status](https://coveralls.io/repos/github/sfosdal/oslo/badge.svg?branch=master)](https://coveralls.io/github/sfosdal/oslo?branch=master)

### Overview
Oslo is a tiny Scala library of small but hopefully useful things.

### Getting Started
Oslo is currently available for Scala 2.11. 2.12 support is comming soon.

To get started with SBT, simply add the following to your `build.sbt`
file:

```scala
libraryDependencies += "net.fosdal" %% "oslo" % "0.2.1"
```

Release notes for Oslo are available in [CHANGES.md](CHANGES.md).

### User Guide

#### oslo
TBD

#### oany
TBD

#### ocsv
TBD

#### odatetime
TBD

#### odouble
TBD

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

#### ofile
TBD

#### oint
TBD

#### olong
TBD

#### oordering
TBD

#### oseq
TBD

#### ostring
TBD
