package org.openurp.edu.evaluation.department.model

import org.beangle.data.model.LongId
import org.beangle.data.model.TemporalOn
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.model.Questionnaire

class EvaluateSwitch extends LongId with TemporalOn {

  var semester: Semester = _
  var opened: Boolean = false
  var questionnaire: Questionnaire = _

  def isOpen(): Boolean = {
    val date = new java.util.Date()
    this.opened && date.before(this.endOn.get) && date.after(this.beginOn)
  }
}

