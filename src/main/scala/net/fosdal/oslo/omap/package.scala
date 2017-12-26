package net.fosdal.oslo

import java.util.Properties

import scala.collection.JavaConverters._
import scala.language.implicitConversions

package object omap {

  implicit def asProperties(m: Map[String, String]): Properties = m.toProperties

  implicit class MapOps(private val m: Map[String, String]) extends AnyVal {

    def toProperties: Properties = new Properties().tap(_.putAll(m.asJava))

  }

}
