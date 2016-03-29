package org.openurp.edu.evaluation.lesson.stat.model

import org.beangle.data.model.LongId
import org.openurp.base.model.Semester
import org.openurp.hr.base.model.Staff

class FinalTeacherScore extends LongId with Rank {
  var staff:Staff=_
  var semester:Semester=_
  var stdScore:Float=_
  var supviScore:Float=_
  var departScore:Float=_
  var finalScore:Double=_

}