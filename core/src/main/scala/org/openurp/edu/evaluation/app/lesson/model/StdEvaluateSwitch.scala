package org.openurp.edu.evaluation.app.lesson.model

import org.beangle.data.model.IntId
import org.openurp.base.model.Semester
import org.openurp.edu.base.model.Project
import org.beangle.data.model.pojo.TemporalAt

class StdEvaluateSwitch extends IntId with TemporalAt {

  var semester: Semester = _
  var opened: Boolean = false
  var project: Project = _

  def isOpenedAt(d: java.time.Instant): Boolean = {
    if (d.isBefore(this.beginAt)) false;
    if (this.endAt.get.isBefore(d)) false;
    opened
  }
}

