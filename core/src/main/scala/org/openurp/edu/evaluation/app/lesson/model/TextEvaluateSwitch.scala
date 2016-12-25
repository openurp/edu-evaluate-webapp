package org.openurp.edu.evaluation.app.lesson.model

import org.beangle.commons.model.TemporalAt
import org.openurp.edu.base.model.Project
import org.beangle.commons.model.IntId
import org.openurp.base.model.Semester

class TextEvaluateSwitch extends IntId with TemporalAt {

  var semester: Semester = _
  var opened: Boolean = false
  var project: Project =_
  /** 是否开放(教师查询)*/
  var openedTeacher:Boolean=_
  /** 是否开放(学生文字评教)*/
  var textEvaluateOpened:Boolean=_

}