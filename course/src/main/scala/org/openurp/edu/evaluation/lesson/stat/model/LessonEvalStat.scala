
package org.openurp.edu.evaluation.lesson.stat.model

import org.beangle.data.model.LongId
import org.openurp.edu.course.model.Clazz
import org.openurp.edu.base.model.Teacher
import org.openurp.edu.evaluation.course.stat.model.EvalStat
import org.openurp.edu.evaluation.course.stat.model.Rank

class ClazzEvalStat extends LongId with EvalStat with Rank {

  /** 教学任务 */
  var lesson: Clazz = _
  var teacher: Teacher = _

}
