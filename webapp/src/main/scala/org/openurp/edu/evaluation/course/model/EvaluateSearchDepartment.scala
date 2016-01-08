package org.openurp.edu.evaluation.course.model

import org.openurp.edu.base.model.Adminclass
import org.openurp.base.model.Semester
import org.beangle.data.model.LongId

class EvaluateSearchDepartment  {
  var  semester:Semester =_

  var  adminclass:Adminclass =_
  /*
   * 院系需要评教的总人次
   */
  var  countAll:Integer =_

  /*
   * 院系已经评教的人次
   */
  var  haveFinish:Integer =_
  /*
   * 完成率
   */
  var  finishRate:String=_
}