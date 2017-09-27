package org.openurp.edu.evaluation.course.web.action

import java.time.LocalDate

import org.beangle.commons.collection.{ Collections, Order }
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.lesson.result.model.{ EvaluateResult, QuestionResult }
import org.openurp.edu.evaluation.lesson.stat.model.TeacherEvalStat
import org.openurp.edu.evaluation.model.Option
import org.openurp.edu.lesson.model.{ CourseTaker, Lesson }
import org.beangle.webmvc.api.annotation.mapping
import org.beangle.webmvc.api.annotation.param

class TeacherEvalSearchAction extends RestfulAction[TeacherEvalStat] {

  override def index(): View = {
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    put("currentSemester", entityDao.search(semesterQuery).head)
    forward()
  }

  override def search(): View = {
    // 页面条件
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val teacherEvalStat = OqlBuilder.from(classOf[TeacherEvalStat], "teacherEvalStat")
    populateConditions(teacherEvalStat)
    teacherEvalStat.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    teacherEvalStat.where("teacherEvalStat.semester=:semester", semester)
    put("teacherEvalStats", entityDao.search(teacherEvalStat))
    forward()
  }

  @mapping(value = "{id}")
  override def info(@param("id") id: String): View = {
    val teacherEvalStat = entityDao.get(classOf[TeacherEvalStat], id.toLong)
    put("teacherEvalStat", teacherEvalStat)
    val list = Collections.newBuffer[Option]
    val questions = teacherEvalStat.questionnaire.questions
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
    forward()
  }
}
