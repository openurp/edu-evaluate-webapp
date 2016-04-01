package org.openurp.edu.evaluation.lesson.stat.model

import org.beangle.data.model.LongId
import org.openurp.base.model.Semester
import org.openurp.hr.base.model.Staff
import scala.beans.BeanInfo

class FinalTeacherScore extends LongId with Rank {
  var staff:Staff=_
  var semester:Semester=_
  var stdScore:Float=_
  
  @BeanInfo
  var supviScore:Float=_
  var departScore:Float=_
  var score:Float=_

  def getStaffName:String={
    staff.person.name.formatedName
  }
  
  def getName():String={
    staff.person.name.formatedName
  }
}