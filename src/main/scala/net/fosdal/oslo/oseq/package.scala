package net.fosdal.oslo

package object oseq {

  implicit class SeqOps[A](private val s: Seq[A]) extends AnyVal {

    def minOption(implicit cmp: Ordering[A]): Option[A] = if (s.isEmpty) None else Some(s.min)

    def maxOption(implicit cmp: Ordering[A]): Option[A] = if (s.isEmpty) None else Some(s.max)

    def minOptionBy[B: Ordering](f: A => B): Option[A] = s.reduceOption(Ordering.by(f).min)

    def maxOptionBy[B: Ordering](f: A => B): Option[A] = s.reduceOption(Ordering.by(f).max)

    def minByAndApply[B: Ordering](f: A => B): B = f(s.reduce(Ordering.by(f).min))

    def maxByAndApply[B: Ordering](f: A => B): B = f(s.reduce(Ordering.by(f).max))

    def minOptionByAndApply[B: Ordering](f: A => B): Option[B] = s.reduceOption(Ordering.by(f).min).map(f)

    def maxOptionByAndApply[B: Ordering](f: A => B): Option[B] = s.reduceOption(Ordering.by(f).max).map(f)

    def minByAndApplyOrElse[B: Ordering](f: A => B, default: B): B = s.minOptionByAndApply(f).getOrElse(default)

    def maxByAndApplyOrElse[B: Ordering](f: A => B, default: B): B = s.maxOptionByAndApply(f).getOrElse(default)

  }

}
