package org.openurp.edu.evaluation.course.web.action

import org.beangle.commons.collection.Collections
import org.beangle.commons.collection.Order
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.openurp.edu.evaluation.lesson.stat.model.TeacherEvalStat
import org.openurp.edu.evaluation.model.Option
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.lesson.model.CourseTaker
import org.openurp.edu.lesson.model.Lesson
import org.openurp.edu.lesson.model.Lesson

class TeacherEvalSearchAction extends RestfulAction[TeacherEvalStat] {

  override def index(): String = {
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
    val teacherEvalStat = OqlBuilder.from(classOf[TeacherEvalStat], "teacherEvalStat")
    populateConditions(teacherEvalStat)
    teacherEvalStat.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    teacherEvalStat.where("teacherEvalStat.semester=:semester", semester)
    put("teacherEvalStats", entityDao.search(teacherEvalStat))
    forward()
  }

  def info(): String = {
    val questionnaireStat = entityDao.get(classOf[TeacherEvalStat], getLong("teacherEvalStat.id").get)
    put("questionnaireStat", questionnaireStat);
    // zongrenci fix
    val query = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName, "result");
    query.where("result.teacher =:tea", questionnaireStat.teacher);
    query.where("result.questionnaire=:quen", questionnaireStat.questionnaire)
    //    query.where("result.lesson=:less", questionnaireStat.lesson)
    query.select("case when result.statType =1 then count(result.id) end,count(result.id)");
    query.groupBy("result.statType");
    entityDao.search(query) foreach { a =>
      put("number1", a(0))
      put("number2", a(1))
    }
    val list = Collections.newBuffer[Option]
    val questions = questionnaireStat.questionnaire.questions
    questions foreach { question =>
      val options = question.optionGroup.options
      options foreach { option =>
        var tt = 0
        list foreach { oldOption =>
          if (oldOption.id == option.id) {
            tt += 1;
          }
        }
        if (tt == 0) {
          list += option
        }
      }
    }
    put("options", list);
    val lessonQue = OqlBuilder.from[Lesson](classOf[EvaluateResult].getName, "evalResult")
    lessonQue.select("distinct evalResult.lesson")
    lessonQue.where("evalResult.lesson.semester.id =:semesterId", questionnaireStat.semester.id);
    lessonQue.join("evalResult.lesson.teachers", "teacher");
    lessonQue.where("teacher.id=:teacherId", questionnaireStat.teacher.id);
    var numbers = 0L
    val lessons = entityDao.search(lessonQue)
    lessons foreach { lesson =>
      val querys = OqlBuilder.from[Long](classOf[CourseTaker].getName, "courseTake");
      querys.join("courseTake.lesson.teachers", "teacher");
      querys.where("teacher.id=:teacherId", questionnaireStat.teacher.id);
      querys.where("courseTake.semester.id=:semesterId", questionnaireStat.semester.id);
      querys.where("courseTake.lesson = (:lesson)", lesson)
      querys.select("count(courseTake.std.id)");
      numbers += entityDao.search(querys)(0)
    }
    put("numbers", numbers);

    val que = OqlBuilder.from(classOf[QuestionResult], "questionR");
    que.where("questionR.result.teacher=:teaId", questionnaireStat.teacher);
    que.where("questionR.result.questionnaire=:quen", questionnaireStat.questionnaire);
    que.select("questionR.question.id,questionR.option.id,count(*)");
    que.groupBy("questionR.question.id,questionR.option.id");
    put("questionRs", entityDao.search(que));
    val quer = OqlBuilder.from(classOf[QuestionResult], "questionR");
    quer.where("questionR.result.teacher=:teaId", questionnaireStat.teacher);
    quer.where("questionR.result.questionnaire=:quen", questionnaireStat.questionnaire);
    quer.select("questionR.question.id,questionR.question.content,sum(questionR.score)/count(questionR.id)*100");
    quer.groupBy("questionR.question.id,questionR.question.content");
    put("questionResults", entityDao.search(quer));
    forward()
  }
}
