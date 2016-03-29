package org.openurp.edu.evaluation.course.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.lesson.stat.model.TeacherEvalStat
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.base.model.Department
import org.openurp.base.model.Semester
import org.beangle.commons.collection.Order
import org.openurp.edu.evaluation.department.model.DepartEvaluate
import scala.collection.mutable.Buffer
import scala.collection.mutable.ListBuffer
import org.openurp.edu.evaluation.department.model.SupervisiorEvaluate
import org.openurp.hr.base.model.Staff

class FinalTeacherScoreAction extends RestfulAction[TeacherEvalStat] {
 override def  index():String = {
    put("departments", entityDao.search(OqlBuilder.from(classOf[Department],"dep").where("dep.teaching =:tea",true)))
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)
    forward();
  }

  

  
  override def search():String= {
    
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val que = OqlBuilder.from[Array[Any]](classOf[TeacherEvalStat].getName + " teacherEvalStat,"+ classOf[DepartEvaluate].getName + " departEvaluate,"+ classOf[SupervisiorEvaluate].getName + " supervisiorEvaluate");
    que.select("teacherEvalStat.staff.id,teacherEvalStat.score,supervisiorEvaluate.totalScore,departEvaluate.totalScore");
    que.where("teacherEvalStat.semester.id=departEvaluate.semester.id");
    que.where("teacherEvalStat.staff.id=departEvaluate.staff.id");
    que.where("teacherEvalStat.semester.id=supervisiorEvaluate.semester.id");
    que.where("teacherEvalStat.staff.id=supervisiorEvaluate.staff.id");
    que.where("teacherEvalStat.semester.id =:semesterId", semesterId);
    
//    val finalScoreMap = new collection.mutable.HashMap[Long,Buffer[Tuple4[Number,Number,Number,Number]]]
//    entityDao.search(que) foreach { a =>
//      val buffer = finalScoreMap.getOrElseUpdate(a(0).asInstanceOf[Long],new ListBuffer[Tuple4[Number,Number,Number,Number]])
//      buffer += Tuple4(a(1).asInstanceOf[Number],a(2).asInstanceOf[Number],a(3).asInstanceOf[Number],(a(1).toString().toFloat)*0.5+(a(2).toString().toFloat)*0.3+(a(3).toString().toFloat)*0.2)
//    }
    val finalScoreMap=entityDao.search(que)
    put("finalScoreMaps", finalScoreMap);
    val staffs =entityDao.getAll(classOf[Staff])
    put("staffs",staffs)
    forward();
  }
}