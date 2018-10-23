package org.openurp.edu.evaluation.app.department.model

import org.beangle.data.model.LongId
import org.openurp.edu.base.model.Semester
import org.openurp.edu.evaluation.model.Questionnaire
import org.beangle.data.model.pojo.DateRange
import java.time.LocalDate

class EvaluateSwitch extends LongId with DateRange {

  var semester: Semester = _
  var opened: Boolean = false
  var questionnaire: Questionnaire = _

  def isOpen(): Boolean = {
    val now = LocalDate.now
    this.opened && now.isBefore(this.endOn) && now.isAfter(this.beginOn)
  }
}

