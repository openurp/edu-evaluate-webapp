package org.openurp.edu.evaluation.course.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.lesson.stat.model.DepartEvalStat
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.lesson.model.Lesson
import org.beangle.commons.collection.Collections
import org.beangle.commons.collection.Order
import org.openurp.edu.evaluation.model.Option

class DepartEvalSearchAction extends RestfulAction[DepartEvalStat]{
override def  index():String= {
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)
    forward()
  }
    
  override def search(): String = {
    // 页面条件
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val departEvalStat =OqlBuilder.from(classOf[DepartEvalStat],"departEvalStat")
    populateConditions(departEvalStat)
     departEvalStat.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    departEvalStat.where("departEvalStat.lesson.semester=:semester",semester)
    put("departEvalStats", entityDao.search(departEvalStat))
    forward()
  }

   def  info() :String= {
    val questionnaireStat = entityDao.get(classOf[DepartEvalStat],getLong("departEvalStat.id").get)
    put("questionnaireStat", questionnaireStat);
    // zongrenci fix
    val query = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName, "result");
    query.where("result.department =:tea", questionnaireStat.department);
//    query.where("result.lesson.course=:course", questionnaireStat.course)
    query.select("case when result.statType =1 then count(result.id) end,count(result.id)");
    query.groupBy("result.statType");
    entityDao.search(query) foreach { a =>
      put("number1",a(0))
      put("number2",a(1))
    }
    val list = Collections.newBuffer[Option]
    val questions = questionnaireStat.questionnaire.questions
    questions foreach { question =>
      val options = question.optionGroup.options
      options foreach { option =>
        var tt = 0
        list foreach { oldOption =>
          if (oldOption.id==option.id) {
            tt += 1;
          }
        }
        if (tt == 0) {
          list += option
        }
      }
    }
    put("options", list);
    val querys = OqlBuilder.from[Long](classOf[Lesson].getName, "lesson");
    querys.join("lesson.teachers", "teacher");
//    querys.where("teacher=:teach",questionnaireStat.teacher);
    querys.where("lesson.teachDepart=:depart", questionnaireStat.department);
    querys.join("lesson.teachclass.courseTakes", "courseTake");
    querys.select("count(courseTake.std.id)");
    val numbers=entityDao.search(querys)(0)
    put("numbers", entityDao.search(querys)(0));
    val que = OqlBuilder.from(classOf[QuestionResult], "questionR");
//    que.where("questionR.result.teacher=:teaId", questionnaireStat.teacher);
    que.where("questionR.result.department=:depart", questionnaireStat.department);
    que.select("questionR.question.id,questionR.option.id,count(*)");
    que.groupBy("questionR.question.id,questionR.option.id");
    put("questionRs", entityDao.search(que));
    val quer = OqlBuilder.from(classOf[QuestionResult], "questionR");
//    quer.where("questionR.result.teacher=:teaId", questionnaireStat.teacher);
    quer.where("questionR.result.department=:depart", questionnaireStat.department);
    quer.select("questionR.question.id,questionR.question.content,sum(questionR.score)/count(questionR.id)*100");
    quer.groupBy("questionR.question.id,questionR.question.content");
    put("questionResults", entityDao.search(quer));
    forward()
  }
}