package org.openurp.edu.evaluation.course.model

import org.beangle.data.model.TemporalAt
import org.openurp.edu.base.model.Project
import org.beangle.data.model.IntId
import org.openurp.base.model.Semester

class TextEvaluateSwitch extends IntId with TemporalAt {

  var semester: Semester = _
  var opened: Boolean = false
  var project: Project =_
  var openedTeacher:Boolean=_
  var textEvaluateOpened:Boolean=_

}