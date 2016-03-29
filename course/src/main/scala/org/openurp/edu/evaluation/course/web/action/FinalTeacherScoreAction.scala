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
import org.openurp.edu.evaluation.lesson.stat.model.FinalTeacherScore
import org.beangle.webmvc.api.view.View
import org.openurp.edu.base.code.model.Education
import org.openurp.edu.base.code.model.StdType

class FinalTeacherScoreAction extends RestfulAction[FinalTeacherScore] {
  
  
 override def  index():String = {
    put("departments", entityDao.search(OqlBuilder.from(classOf[Department],"dep").where("dep.teaching =:tea",true)))
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)
    forward();
  }

 override def search(): String = {
    // 页面条件
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val finalScores =OqlBuilder.from(classOf[FinalTeacherScore],"finalTeacherScore")
    populateConditions(finalScores)
    finalScores.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    finalScores.where("finalTeacherScore.semester=:semester",semester)
    put("finalTeacherScores", entityDao.search(finalScores))
    
    forward()
  }
  /**
   * 清除统计数据
   **/
  def remove(semesterId:Int) {
    val query = OqlBuilder.from(classOf[FinalTeacherScore], "finalScore")
    query.where("finalScore.semester.id=:semesterId", semesterId)
    entityDao.remove(entityDao.search(query))
  }
  
  
    /**
   * 跳转(统计首页面)
   */
  def  statHome() :String ={
    
    put("stdTypeList", entityDao.getAll(classOf[StdType]))
    put("departmentList",  entityDao.getAll(classOf[Department]))

    put("educations", entityDao.getAll(classOf[Education]))
    val teachingDeparts = entityDao.search(OqlBuilder.from(classOf[Department],"depart").where("depart.teaching =:tea",true))
    put("departments",teachingDeparts)
    
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)
    forward()
  }
  def  stat():View = {
    
    val semesterId = getInt("semester.id").get
    val semester = entityDao.get(classOf[Semester], semesterId)
    
        // 删除历史统计数据
    remove(semesterId)
    
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
    val finalScores=entityDao.search(que)
     finalScores foreach { ob =>
          val questionS = new FinalTeacherScore
          questionS.staff = new Staff()
          questionS.staff.id=ob(0).asInstanceOf[Long]
          questionS.semester=semester
          questionS.stdScore = ob(1).toString().toFloat
          questionS.supviScore= ob(2).toString().toFloat
          questionS.departScore=ob(3).toString().toFloat
          questionS.finalScore=ob(1).toString().toFloat*0.5 + ob(2).toString().toFloat*0.3 + ob(3).toString().toFloat*0.2
          entityDao.saveOrUpdate(questionS)
    }
    redirect("index", "info.action.success")
  }
}