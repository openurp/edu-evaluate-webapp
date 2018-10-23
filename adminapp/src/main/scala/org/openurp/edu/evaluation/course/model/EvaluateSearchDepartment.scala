package org.openurp.edu.evaluation.course.model

import org.openurp.edu.base.model.Adminclass
import org.openurp.edu.base.model.Semester
import org.beangle.data.model.LongId
import org.openurp.edu.course.model.Clazz

class EvaluateSearchDepartment  {
  var  semester:Semester =_

  var  lesson:Clazz =_
  /*
   * 院系需要评教的总人次
   */
  var  countAll:Long =_

  /*
   * 院系已经评教的人次
   */
  var  haveFinish:Long =_
  
  var stdFinish:Long=_
  /*
   * 完成率
   */
  var  finishRate:String=_
}