package org.openurp.edu.evaluation.course.model

import org.openurp.base.model.Department
import org.openurp.base.model.Semester

class EvaluateSearchManager {
  
  var  semester:Semester=_

  var  department:Department=_
  /*
   * 院系需要评教的总人次
   */
  var  countAll:Integer=_

  /*
   * 院系已经评教的人次
   */
  var  haveFinish:Integer=_
  /*
   * 完成率
   */
  var  finishRate:String=_
}