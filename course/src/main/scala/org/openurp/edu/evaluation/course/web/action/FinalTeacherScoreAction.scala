package org.openurp.edu.evaluation.course.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.lesson.stat.model.TeacherEvalStat
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.base.model.Department
import org.openurp.base.model.Semester
import org.beangle.commons.collection.Order
import org.openurp.edu.evaluation.department.model.DepartEvaluate
import org.openurp.edu.evaluation.department.model.SupervisiorEvaluate

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
    
    val que = OqlBuilder.from[Double](classOf[TeacherEvalStat].getName + " teacherEvalStat,"+ classOf[DepartEvaluate].getName + " departEvaluate,"+ classOf[SupervisiorEvaluate].getName + " supervisiorEvaluate");
    que.select("sum(questionResult.score)/count(distinct evaluateResult.id)");
    que.where("evaluateResult.id=questionResult.result.id");
    
    val queryBuilder = OqlBuilder.from(classOf[TeacherEvalStat],"teacherEvalStat")
    populateConditions(queryBuilder);
    queryBuilder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    queryBuilder.where("teacherEvalStat.semester =:semester", semester);
//    val builder=queryBuilder.limit(getPageLimit).orderBy("teacherEvalStat.rank asc");
    put("teacherEvalStats", entityDao.search(queryBuilder));
    forward();
  }
}