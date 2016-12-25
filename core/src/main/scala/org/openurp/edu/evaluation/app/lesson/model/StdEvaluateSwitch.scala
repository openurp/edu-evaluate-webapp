package org.openurp.edu.evaluation.app.lesson.model

import org.beangle.commons.model.IntId
import org.openurp.base.model.Semester
import org.openurp.edu.base.model.Project
import org.beangle.commons.model.TemporalAt

class StdEvaluateSwitch extends IntId with TemporalAt {

  var semester: Semester = _
  var opened: Boolean = false
  var project: Project = _

  def isOpenedAt(d: java.util.Date): Boolean = {
    if (d.before(this.beginAt)) false;
    if (this.endAt.get.before(d)) false;
    opened
  }
}

