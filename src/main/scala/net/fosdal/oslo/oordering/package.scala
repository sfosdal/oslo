package net.fosdal.oslo

package object oordering {

  implicit class OrderedOps[O](val o: Ordered[O]) extends AnyVal {

    def in(min: O, max: O): Boolean = o >= min && o <= max

  }

}
