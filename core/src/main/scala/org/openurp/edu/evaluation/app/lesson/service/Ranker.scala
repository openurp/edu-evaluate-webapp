package org.openurp.edu.evaluation.app.lesson.service

import org.openurp.edu.evaluation.lesson.stat.model.EvalStat
import org.openurp.edu.evaluation.lesson.stat.model.FinalTeacherScore

object Ranker {

  def over[B <: EvalStat](stats: scala.Seq[B])(f: (B, Int) => Unit): Unit = {
    val sortedStates = stats.sortBy { x => 0 - x.score }

    val ranks = new collection.mutable.HashMap[B, Int]
    var rank = 1;
    var score = sortedStates.head.score;
    var i = 0;
    // 100 100 100 100 100 100 100 99 99 99 98 98 97
    //      1   1   1   1   1   1   1  8  8  8   11 11 12
    sortedStates foreach { x =>
      val rs = java.lang.Float.compare(x.score, score)
      i += 1;
      if (rs == 0) {
        ranks.put(x, rank);
      } else {
        ranks.put(x, i);
        rank = i
        score = x.score;

      }

    }

    ranks foreach {
      case (x, r) =>
        f(x, r)
    }
  }

  def rOver[B <: FinalTeacherScore](stats: scala.Seq[B])(f: (B, Int) => Unit): Unit = {
    val sortedStates = stats.sortBy { x => 0 - x.score }

    val ranks = new collection.mutable.HashMap[B, Int]
    var rank = 1;
    var score = sortedStates.head.score;
    var i = 0;
    // 100 100 100 100 100 100 100 99 99 99 98 98 97
    //      1   1   1   1   1   1   1  8  8  8   11 11 12
    sortedStates foreach { x =>
      val rs = java.lang.Float.compare(x.score, score)
      i += 1;
      if (rs == 0) {
        ranks.put(x, rank);
      } else {
        ranks.put(x, i);
        rank = i
        score = x.score;

      }

    }

    ranks foreach {
      case (x, r) =>
        f(x, r)
    }
  }
}
