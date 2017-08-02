package org.openurp.edu.evaluation.course.model

import org.openurp.base.model.Department
import org.openurp.base.model.Semester

class EvaluateSearchManager {
  
  var  semester:Semester=_

  var  department:Department=_
  /*
   * 院系总评人次
   */
  var  countAll:Long=_
//  总评人数
  var  stdAll:Long=_
  /*
   * 院系实评人次
   */
  var  haveFinish:Long=_
//  实评人数
  var stdFinish:Long=_
  /*
   * 完成率
   */
  var  finishRate:String=_
  var  stdRate:String=_
}
