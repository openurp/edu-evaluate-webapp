package org.openurp.edu.evaluation.app.lesson.model

import org.beangle.data.model.IntId
import org.openurp.base.model.Semester
import org.openurp.edu.base.model.Project
import org.beangle.data.model.TemporalAt

class StdEvaluateSwitch extends IntId with TemporalAt {

  var semester: Semester = _
  var opened: Boolean = false
  var project: Project =_
  

}

