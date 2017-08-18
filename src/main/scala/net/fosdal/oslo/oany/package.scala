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

  }

}
