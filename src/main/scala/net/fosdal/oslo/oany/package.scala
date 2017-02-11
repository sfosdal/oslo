package net.fosdal.oslo

package object oany {

  implicit class AnyOps[A](val a: A) extends AnyVal {

    def partialTap(pf: PartialFunction[A, _]): A = {
      pf.lift(a)
      a
    }

    def tap(f: Function[A, _]): A = {
      f(a)
      a
    }

    def in(min: A, max: A)(implicit ord: Ordering[A]): Boolean = {
      ord.gteq(a, min) && ord.lteq(a, max)
    }

  }

}
