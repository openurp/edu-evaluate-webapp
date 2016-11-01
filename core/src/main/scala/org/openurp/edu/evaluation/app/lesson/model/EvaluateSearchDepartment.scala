package org.openurp.edu.evaluation.app.lesson.model

import org.openurp.base.model.Semester
import org.openurp.edu.lesson.model.Lesson

class EvaluateSearchDepartment  {
  var  semester:Semester =_

  var  lesson:Lesson =_
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