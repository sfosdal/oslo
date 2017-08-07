# Oslo
[![CircleCI](https://circleci.com/gh/sfosdal/oslo.svg?style=shield)](https://circleci.com/gh/sfosdal/oslo)
[![Coverage Status](https://coveralls.io/repos/github/sfosdal/oslo/badge.svg?branch=master)](https://coveralls.io/github/sfosdal/oslo?branch=master)

### Overview
Oslo is a tiny library of small but hopefully useful things.

### Why would I do this?
* I've had to write these over and over again, I want to stop repeating myself.
* So that others may benefit from the effort of olso
* So that others may lend their efforts to improve olso

## User Guide

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
