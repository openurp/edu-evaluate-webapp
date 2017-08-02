
package org.openurp.edu.evaluation.lesson.stat.model

import org.beangle.data.model.LongId
import org.openurp.edu.lesson.model.Lesson
import org.openurp.edu.base.model.Teacher

class LessonEvalStat extends LongId with EvalStat with Rank {

  /** 教学任务 */
  var lesson: Lesson = _
  var teacher: Teacher = _

}
