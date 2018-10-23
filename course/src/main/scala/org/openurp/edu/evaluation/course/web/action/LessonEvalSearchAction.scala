package org.openurp.edu.evaluation.course.web.action

import java.time.LocalDate

import org.beangle.commons.collection.Collections
import org.beangle.commons.collection.Order
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.annotation.mapping
import org.beangle.webmvc.api.annotation.param
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.base.model.Semester
import org.openurp.edu.evaluation.course.stat.model.ClazzEvalStat
import org.openurp.edu.evaluation.model.Option

class ClazzEvalSearchAction extends RestfulAction[ClazzEvalStat] {

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
    val lessonEvalStat = OqlBuilder.from(classOf[ClazzEvalStat], "lessonEvalStat")
    populateConditions(lessonEvalStat)
    lessonEvalStat.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    lessonEvalStat.where("lessonEvalStat.lesson.semester=:semester", semester)
    put("lessonEvalStats", entityDao.search(lessonEvalStat))
    forward()
  }

  @mapping(value = "{id}")
  override def info(@param("id") id: String): View = {
    val questionnaireStat = entityDao.get(classOf[ClazzEvalStat], java.lang.Long.parseLong(id));
    put("questionnaireStat", questionnaireStat);

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
    put("questionnaireStat", questionnaireStat);
    forward()
  }

}
