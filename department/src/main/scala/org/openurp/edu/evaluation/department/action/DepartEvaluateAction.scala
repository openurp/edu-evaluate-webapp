package org.openurp.edu.evaluation.department.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.department.model.DepartEvaluate
import org.openurp.base.model.Department
import org.beangle.data.dao.OqlBuilder
import org.openurp.hr.base.model.Staff
import org.beangle.commons.collection.Order
import org.beangle.commons.collection.page.PageLimit
import org.beangle.data.model.LongId
import org.beangle.commons.lang.Numbers
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.edu.evaluation.model.QuestionType
import scala.collection.mutable.Buffer
import org.beangle.commons.collection.Collections
import org.openurp.edu.evaluation.model.Question
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.department.model.DepartEvaluate
import org.openurp.edu.evaluation.department.model.DepartQuestion
import org.openurp.edu.evaluation.department.model.DepartQuestion
import org.openurp.edu.evaluation.department.model.DepartEvaluate
import java.util.Date
import org.beangle.webmvc.api.view.View
import org.openurp.edu.lesson.model.Lesson
import org.openurp.edu.evaluation.department.model.EvaluateSwitch
import org.openurp.platform.api.security.Securities
import org.openurp.edu.base.model.Course

/**
 * @author xinzhou
 */
class DepartEvaluateAction extends RestfulAction[DepartEvaluate] {

  override def indexSetting(): Unit = {
    put("departments", entityDao.getAll(classOf[Department]))
    put("semesters", entityDao.getAll(classOf[Semester]))
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)

  }

  override def search(): String = {
    val code = get("departEvaluate.staff.person.code").orNull
    val name = get("departEvaluate.staff.person.name").orNull
    val departId = get("departEvaluate.staff.department.id").orNull
    val semesterId = get("departEvaluate.semester.id").orNull

    val query = OqlBuilder.from(classOf[Staff], "staff")
    query.where("staff.state.status.id=:t", 1)
    if (code != null && !"".equals(code)) {
      query.where("staff.code like '%" + code + "%'")
    }
    if (name != null && !"".equals(name)) {
      query.where("staff.code like '%" + name + "%'")
    }
    if (departId != null && !"".equals(departId)) {
      query.where("staff.state.department.id=" + departId)
    }

    val courseDepartId = getStaff.state.department.id
    get("departEvaluate.semester.id") foreach { semesterId =>
      query.where("exists ( from " + classOf[Lesson].getName + s" lesson where staff in elements(lesson.teachers) and lesson.semester.id = $semesterId and lesson.teachDepart.id = $courseDepartId)")
    }
    getBoolean("passed") match {
      case Some(true) => query.where("exists ( from " + classOf[DepartEvaluate].getName + " departE where departE.staff.id = staff.id)")
      case Some(false) => query.where("not exists ( from " + classOf[DepartEvaluate].getName + " departE where departE.staff.id = staff.id)")
      case None =>
    }

    query.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    val staffs = entityDao.search(query)
    put("staffs", staffs)

    val departEvaluate = if (staffs.isEmpty) {
      List.empty[DepartEvaluate]
    } else {
      val builder = OqlBuilder.from(classOf[DepartEvaluate], "departE")
      builder.where("departE.staff in (:staffs)", staffs).where(s"departE.semester.id=$semesterId")
      entityDao.search(builder)
    }

    val evaluateMap =
      if (staffs.isEmpty) Map.empty else {
        departEvaluate.map(e => (e.staff, e)).toMap
      }
    put("evaluateMap", evaluateMap)

    val totalScoreMap = Collections.newMap[Long, Float]
    departEvaluate foreach { de =>
      var totalScore = 0F
      de.questionResults foreach { qr =>
        totalScore += qr.score
        totalScoreMap.put(de.staff.id, totalScore)
      }
    }
    put("totalScoreMap", totalScoreMap)

    forward()
  }

  def getStaff(): Staff = {
    val staffs = entityDao.findBy(classOf[Staff], "code", List(Securities.user))
    if (staffs.isEmpty) {
      throw new RuntimeException("Cannot find staff with code " + Securities.user)
    } else {
      staffs.head
    }
  }

  def addTeaEvaluate(): String = {
    val staffId = longId("staff")
    put("staff", entityDao.get(classOf[Staff], staffId))

    val semesterId = intId("semester")
    put("semester", entityDao.get(classOf[Semester], semesterId))

    val esbuilder = OqlBuilder.from(classOf[EvaluateSwitch], "es")
    esbuilder.where("es.questionnaire.id =:quId", 322L)
    esbuilder.where("es.opened = :opened", true)
    val evaluateSwitches = entityDao.search(esbuilder)
    put("evaluateSwitches", evaluateSwitches)

    if (!evaluateSwitches.isEmpty) {
      val questionnaire = evaluateSwitches.head.questionnaire
      put("questionnaire", questionnaire)

      val questionTree = Collections.newMap[QuestionType, Buffer[Question]]
      questionnaire.questions foreach { question =>
        val key = question.questionType
        var questions: Buffer[Question] = questionTree.get(key).orNull
        if (null == questions) {
          questions = Collections.newBuffer
        }
        questions += question
        questions.sortWith((x, y) => x.priority < y.priority)
        questionTree.put(key, questions)
      }
      put("questionTree", questionTree)

    }
    val query = OqlBuilder.from(classOf[DepartEvaluate], "departE")
    query.where("departE.staff.id=:id", staffId).where("departE.semester.id=:semesterId", semesterId)
    val departEvaluate = entityDao.search(query)

    val resultMap = Collections.newMap[Question, Float]
    departEvaluate foreach { de =>
      de.questionResults foreach { qr =>
        resultMap.put(qr.question, qr.score)
      }
    }
    put("resultMap", resultMap)

    val totalScoreMap = Collections.newMap[Long, Float]
    departEvaluate foreach { de =>
      var totalScore = 0F
      de.questionResults foreach { qr =>
        totalScore += qr.score
        totalScoreMap.put(de.staff.id, totalScore)
      }
    }
    put("totalScoreMap", totalScoreMap)

    forward()
  }

  def saveTeaEvaluate(): View = {
    val staffId = longId("staff")
    val semesterId = intId("semester")
    val questionnaire = entityDao.get(classOf[Questionnaire], 322L)
    val staff = entityDao.get(classOf[Staff], staffId)

    val query = OqlBuilder.from(classOf[DepartEvaluate], "departE")
    query.where("departE.staff.id=:id", staffId).where("departE.semester.id=:semesterId", semesterId)
    var departEvaluate = new DepartEvaluate

    val resultMap = Collections.newMap[Question, DepartQuestion]
    entityDao.search(query) foreach { de =>
      departEvaluate = de
      de.questionResults foreach { qr =>
        resultMap.put(qr.question, qr)
      }
    }

    departEvaluate.staff = staff
    departEvaluate.department = staff.state.department
    departEvaluate.semester = entityDao.get(classOf[Semester], semesterId)
    departEvaluate.evaluateAt = new Date()
    departEvaluate.questionnaire = questionnaire
    questionnaire.questions foreach { question =>
      resultMap.get(question) match {
        case Some(qr) => qr.score = getFloat(question.id + "_score").get
        case None =>
          val qr = new DepartQuestion
          qr.question = question
          qr.result = departEvaluate
          qr.score = getFloat(question.id + "_score").get
          departEvaluate.questionResults += qr
      }
    }
    entityDao.saveOrUpdate(departEvaluate)
    redirect("search", s"&departEvaluate.semester.id=$semesterId", "info.save.success")
  }

}