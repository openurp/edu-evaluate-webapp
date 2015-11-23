package org.openurp.edu.evaluation.department.model

import org.beangle.data.model.TemporalOn
import org.beangle.data.model.IntId
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.model.Questionnaire

class EvaluateSwitch extends IntId with TemporalOn {

  var semester: Semester = _
  var opened: Boolean = false
  var questionnaire: Questionnaire = _

}

