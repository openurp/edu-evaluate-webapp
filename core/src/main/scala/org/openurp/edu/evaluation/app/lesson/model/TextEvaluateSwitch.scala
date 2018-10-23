package org.openurp.edu.evaluation.app.lesson.model

import org.openurp.edu.base.model.Project
import org.beangle.data.model.IntId
import org.openurp.edu.base.model.Semester
import org.beangle.data.model.pojo.InstantRange

class TextEvaluateSwitch extends IntId with InstantRange {

  var semester: Semester = _
  var opened: Boolean = false
  var project: Project =_
  /** 是否开放(教师查询)*/
  var openedTeacher:Boolean=_
  /** 是否开放(学生文字评教)*/
  var textEvaluateOpened:Boolean=_

}