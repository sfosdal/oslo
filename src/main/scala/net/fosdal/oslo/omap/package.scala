package net.fosdal.oslo

import java.util.Properties

import scala.collection.JavaConverters._

package object omap {

  implicit class MapOps(private val m: Map[String, String]) extends AnyVal {

    def toProperties: Properties = new Properties().tap(_.putAll(m.asJava))

  }

}
