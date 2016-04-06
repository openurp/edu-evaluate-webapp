package org.openurp.edu.evaluation.course.web.action

import org.beangle.commons.collection.Order
import org.beangle.commons.lang.ClassLoaders
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.context.ActionContext
import org.beangle.webmvc.api.view.Status
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.base.model.Semester
import org.openurp.edu.base.code.model.Education
import org.openurp.edu.base.code.model.StdType
import org.openurp.edu.evaluation.department.model.DepartEvaluate
import org.openurp.edu.evaluation.department.model.SupervisiorEvaluate
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.edu.evaluation.lesson.stat.model.FinalTeacherScore
import org.openurp.edu.evaluation.lesson.stat.model.TeacherEvalStat
import org.openurp.hr.base.model.Staff
import net.sf.jxls.transformer.XLSTransformer
import org.openurp.edu.evaluation.course.service.Ranker
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult

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
    put("semesterId",semesterId)
    forward()
  }
  /**
   * 导出
   */
  def export(): View = {
    val semesterId = getInt("semester.id").get
    val finalScores =OqlBuilder.from(classOf[FinalTeacherScore],"finalTeacherScore")
    populateConditions(finalScores)
    finalScores.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    finalScores.where("finalTeacherScore.semester.id=:semesterId",semesterId)
    val list = collection.JavaConversions.asJavaCollection(entityDao.search(finalScores))
    //查出信息并放到map中
    val beans = new java.util.HashMap[String, Any]
    beans.put("list", list)
    //获得模板路径
    val path = ClassLoaders.getResourceAsStream("template/finalTeacherScore.xls")
    //准备输出流
    val response = ActionContext.current.response
    response.setContentType("application/x-excel")
    response.setHeader("Content-Disposition", "attachmentfilename=finalTeacherScore.xls")
    val os = response.getOutputStream()
    val transformer = new XLSTransformer()
    try {
      //将beans通过模板输入流写到workbook中
      val workbook = transformer.transformXLS(path, beans)
      //将workbook中的内容用输出流写出去
      workbook.write(os)
    } finally {
      if (os != null) {
        os.close()
      }
    }
    Status.Ok
  }

   def rankStat():View = {
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    //    排名
     val rankQuery = OqlBuilder.from(classOf[FinalTeacherScore], "finalTeacherScore")
     rankQuery.where("finalTeacherScore.semester.id=:semesterId", semesterId)
     val evals = entityDao.search(rankQuery)
     Ranker.rOver(evals){(x,r) => 
       x.rank=r;
     }
     val departEvalMaps = evals.groupBy ( x => x.staff.state.department )
     departEvalMaps.values foreach{ departEvals =>
         Ranker.rOver(departEvals){(x,r) => 
         x.departRank=r;
       }
     }
     entityDao.saveOrUpdate(evals);
     redirect("index", "info.action.success")
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
//    怎么保证TeacherEvalStat/DepartEvaluate/SupervisiorEvaluate中每个教师只有一个成绩
//    一个老师可能有两个不同的问卷？
    
    val quer = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName +" questionR,"
        + classOf[DepartEvaluate].getName + " departEvaluate,"
        + classOf[SupervisiorEvaluate].getName + " supervisiorEvaluate")
        
    quer.where("questionR.result.lesson.semester.id=:semesterId", semesterId)
    quer.where("questionR.result.statType is 1")
    quer.select("questionR.result.staff.id,"
//        + "sum(questionR.score),case when questionR.result.statType =1 then count(distinct questionR.result.id) end," 
//        + "count(distinct questionR.result.id),case when questionR.result.statType =1 then sum(questionR.score) end," 
       + "supervisiorEvaluate.totalScore,departEvaluate.totalScore,"
        + "sum(questionR.score)/count(distinct questionR.result.id)");
    quer.where("questionR.result.lesson.semester.id=departEvaluate.semester.id");
    quer.where("questionR.result.staff.id=departEvaluate.staff.id");
    quer.where("questionR.result.lesson.semester.id=supervisiorEvaluate.semester.id");
    quer.where("questionR.result.staff.id=supervisiorEvaluate.staff.id");
    quer.where("questionR.result.lesson.semester.id =:semesterId", semesterId);
    quer.groupBy("questionR.result.staff.id,supervisiorEvaluate.totalScore,departEvaluate.totalScore")
//    val wjStat = entityDao.search(quer)  
    
    
    
//    val que = OqlBuilder.from[Array[Any]](classOf[TeacherEvalStat].getName + " teacherEvalStat,"
//        + classOf[DepartEvaluate].getName + " departEvaluate,"
//        + classOf[SupervisiorEvaluate].getName + " supervisiorEvaluate");
//    que.select("teacherEvalStat.staff.id,teacherEvalStat.score,supervisiorEvaluate.totalScore,departEvaluate.totalScore");
//    que.where("teacherEvalStat.semester.id=departEvaluate.semester.id");
//    que.where("teacherEvalStat.staff.id=departEvaluate.staff.id");
//    que.where("teacherEvalStat.semester.id=supervisiorEvaluate.semester.id");
//    que.where("teacherEvalStat.staff.id=supervisiorEvaluate.staff.id");
//    que.where("teacherEvalStat.semester.id =:semesterId", semesterId);
    
//    val finalScoreMap = new collection.mutable.HashMap[Long,Buffer[Tuple4[Number,Number,Number,Number]]]
//    entityDao.search(que) foreach { a =>
//      val buffer = finalScoreMap.getOrElseUpdate(a(0).asInstanceOf[Long],new ListBuffer[Tuple4[Number,Number,Number,Number]])
//      buffer += Tuple4(a(1).asInstanceOf[Number],a(2).asInstanceOf[Number],a(3).asInstanceOf[Number],(a(1).toString().toFloat)*0.5+(a(2).toString().toFloat)*0.3+(a(3).toString().toFloat)*0.2)
//    }
    val finalScores=entityDao.search(quer)
     finalScores foreach { ob =>
          val questionS = new FinalTeacherScore
          questionS.staff = new Staff()
          questionS.staff.id=ob(0).asInstanceOf[Long]
          questionS.semester=semester
          questionS.stdScore = ob(3).toString().toFloat*10
          questionS.supviScore= ob(1).toString().toFloat
          questionS.departScore=ob(2).toString().toFloat
          questionS.score=(ob(3).toString().toFloat*10*0.5 + ob(1).toString().toFloat*0.3 + ob(2).toString().toFloat*0.2).toFloat
          entityDao.saveOrUpdate(questionS)
    }
    redirect("index", "info.action.success")
  }
}