package org.openurp.edu.evaluation.lesson.stat.model

import org.beangle.data.model.LongId
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
    teacher.person.name.formatedName
  }

  def getName(): String = {
    teacher.person.name.formatedName
  }
}
