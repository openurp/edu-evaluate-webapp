package org.openurp.edu.evaluation.lesson.stat.model

import org.beangle.commons.model.LongId
import org.openurp.base.model.Semester
import scala.beans.BeanInfo
import org.openurp.edu.base.model.Teacher

class FinalTeacherScore extends LongId with Rank {
  var teacher: Teacher = _
  var semester: Semester = _
  var stdScore: Float = _

  @BeanInfo
  var supviScore: Float = _
  var departScore: Float = _
  var score: Float = _

  def getTeacherName: String = {
    teacher.user.name
  }

  def getName(): String = {
    teacher.user.name
  }
}
