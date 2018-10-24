/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.edu.evaluation.app.lesson.service

import org.openurp.edu.evaluation.course.stat.model.FinalTeacherScore
import org.openurp.edu.evaluation.course.stat.model.EvalStat

object Ranker {

  def over[B <: EvalStat](stats: scala.Seq[B])(f: (B, Int) => Unit): Unit = {
    if(stats.isEmpty) return
    val sortedStates = stats.sortBy { x => 0 - x.avgScore }
    val ranks = new collection.mutable.HashMap[B, Int]
    var rank = 1;
    var score = sortedStates.head.avgScore;
    var i = 0;
    // 100 100 100 100 100 100 100 99 99 99 98 98 97
    // 1   1   1   1   1   1   1   8  8  8  11 11 12
    sortedStates foreach { x =>
      val rs = java.lang.Float.compare(x.avgScore, score)
      i += 1;
      if (rs == 0) {
        ranks.put(x, rank);
      } else {
        ranks.put(x, i);
        rank = i
        score = x.avgScore;
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
