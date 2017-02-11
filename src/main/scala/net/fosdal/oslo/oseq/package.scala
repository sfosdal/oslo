package net.fosdal.oslo

package object oseq {

  implicit class SeqOps[A](val s: Seq[A]) extends AnyVal {

    def minOption(implicit cmp: Ordering[A]): Option[A] = if (s.isEmpty) None else Some(s.min)

    def maxOption(implicit cmp: Ordering[A]): Option[A] = if (s.isEmpty) None else Some(s.max)

    def minOptionBy[B: Ordering](f: A => B): Option[A] = s.reduceOption(Ordering.by(f).min)

    def maxOptionBy[B: Ordering](f: A => B): Option[A] = s.reduceOption(Ordering.by(f).max)

  }

}
