# Oslo
[![Maven Central](https://img.shields.io/maven-central/v/net.fosdal/oslo_2.12.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22net.fosdal%22%20AND%20a%3A%22oslo_2.11%22)
[![Build Status](https://travis-ci.org/sfosdal/oslo.svg?branch=master)](https://travis-ci.org/sfosdal/oslo)
[![Coverage Status](https://coveralls.io/repos/github/sfosdal/oslo/badge.svg?branch=master)](https://coveralls.io/github/sfosdal/oslo?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/4b8e65fb3bc84706bc41e0dd34735b2f)](https://www.codacy.com/app/steve/oslo?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=sfosdal/oslo&amp;utm_campaign=Badge_Grade)

### Overview
Oslo is a tiny Scala library of small but hopefully useful things.

### Getting Started
Using SBT, add the following to your `build.sbt` file (see maven badge for latest version)

```scala
libraryDependencies += "net.fosdal" %% "oslo" % "<version>"
```

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
```scala
  def resourceContents(resourcePath: String): String = {
  def fileContents(filePath: String): String = Source.fromFile(filePath).mkString
  def contents(source: String): String = Try(resourceContents(source)).getOrElse(fileContents(source))
```


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
