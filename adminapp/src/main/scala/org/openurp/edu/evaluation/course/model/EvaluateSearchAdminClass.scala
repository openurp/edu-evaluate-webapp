package org.openurp.edu.evaluation.course.model

import org.openurp.edu.base.model.Student
import org.openurp.edu.base.model.Adminclass
import org.openurp.edu.base.model.Semester
import org.openurp.edu.course.model.Clazz

class EvaluateSearchAdminClass {
  var  semester:Semester =_

  var  lesson:Clazz=_

  var  student:Student=_
  /*
   * 学生需要评教的总人次
   */
  var  countAll:Integer=_

  /*
   * 学生已经评教的人次
   */
  var  haveFinish:Integer=_
  /*
   * 完成率
   */
  var  finishRate:String=_
}