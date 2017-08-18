package net.fosdal.oslo

import scala.math.Ordering.Implicits._

package object oordering {

  /**
    * A functional form of Ops::max. Useful to avoid ambiguities like this:
    *
    * {{{
    *   import scala.math.Ordering.Implicits._
    *
    *   "aaa".max("bbb")
    *   // Error:(17, 13) type mismatch;
    *   // found   : String("bbb")
    *   // required: Ordering[?]
    *   // "aaaa".max("bbb");
    *   //            ^
    * }}}
    *
    * which crop up when you're trying to invoke `max` on an object that itself has a `max` function already.
    *
    * @see [[scala.math.Ordering.max]]
    */
  def max[A: Ordering](x: A, y: A): A = x.max(y)

  /**
    * Like `max`, but for `min`.
    *
    * @see [[net.fosdal.oslo.oordering.max]]
    */
  def min[A: Ordering](x: A, y: A): A = x.min(y)

  implicit class OrderedOps[O](val o: Ordered[O]) extends AnyVal {
    def in(min: O, max: O): Boolean = o >= min && o <= max
  }

  implicit class OrderingOps[O](val o: O) extends AnyVal {
    def in(min: O, max: O)(implicit ord: Ordering[O]): Boolean = {
      ord.gteq(o, min) && ord.lteq(o, max)
    }
  }

}
