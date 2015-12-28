package org.openurp.edu.evaluation.department.action

import org.beangle.data.dao.OqlBuilder
import org.openurp.base.model.Department
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.department.model.SupervisiorEvaluate
import org.openurp.edu.evaluation.department.model.EvaluateSwitch
import org.openurp.edu.evaluation.model.Question
import org.openurp.hr.base.model.Staff
import org.openurp.edu.evaluation.department.model.SupervisiorQuestion
import org.openurp.edu.evaluation.model.QuestionType
import org.openurp.edu.evaluation.model.Questionnaire
import scala.collection.mutable.Buffer
import org.beangle.webmvc.api.view.View
import org.beangle.commons.collection.Collections
import java.util.Date
import org.openurp.edu.lesson.model.Lesson
import org.beangle.commons.collection.Order

/**
 * @author xinzhou
 */
class SupervisiorEvaluateAction extends RestfulAction[SupervisiorEvaluate] {

  override def indexSetting(): Unit = {
    put("departments", entityDao.getAll(classOf[Department]))
    put("semesters", entityDao.getAll(classOf[Semester]))
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)
  }

  override def search(): String = {
    val code = get("supervisiorEvaluate.staff.person.code").orNull
    val name = get("supervisiorEvaluate.staff.person.name").orNull
    val departId = get("supervisiorEvaluate.staff.department.id").orNull
    val semesterId = get("supervisiorEvaluate.semester.id").orNull

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
    get("supervisiorEvaluate.semester.id") foreach { semesterId =>
      query.where("exists ( from " + classOf[Lesson].getName + s" lesson where staff in elements(lesson.teachers) and lesson.semester.id = $semesterId)")
    }
    getBoolean("passed") match {
      case Some(true) => query.where("exists ( from " + classOf[SupervisiorEvaluate].getName + " departE where departE.staff.id = staff.id)")
      case Some(false) => query.where("not exists ( from " + classOf[SupervisiorEvaluate].getName + " departE where departE.staff.id = staff.id)")
      case None =>
    }
    query.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    val staffs = entityDao.search(query)
    put("staffs", staffs)

    val evaluateMap =
      if (staffs.isEmpty) {
        Map.empty
      } else {
        val builder = OqlBuilder.from(classOf[SupervisiorEvaluate], "departE")
        builder.where("departE.staff in (:staffs)", staffs).where(s"departE.semester.id=$semesterId")
        val supervisiorEvaluate = entityDao.search(builder)
        put("totalScoreMap", supervisiorEvaluate.map(de => (de.staff.id, de.totalScore)).toMap)
        supervisiorEvaluate.map(e => (e.staff, e)).toMap
      }
    put("evaluateMap", evaluateMap)

    forward()
  }

  def addTeaEvaluate(): String = {
    val staffId = longId("staff")
    put("staff", entityDao.get(classOf[Staff], staffId))

    val semesterId = intId("semester")
    put("semester", entityDao.get(classOf[Semester], semesterId))

    val esbuilder = OqlBuilder.from(classOf[EvaluateSwitch], "es")
    esbuilder.where("es.questionnaire.id =:quId", 322L)
    esbuilder.where("es.semester.id = :semesterId", semesterId)
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
    val query = OqlBuilder.from(classOf[SupervisiorEvaluate], "departE")
    query.where("departE.staff.id=:id", staffId).where("departE.semester.id=:semesterId", semesterId)
    val supervisiorEvaluate = entityDao.search(query)

    val resultMap = Collections.newMap[Question, Float]
    supervisiorEvaluate foreach { de =>
      de.questionResults foreach { qr =>
        resultMap.put(qr.question, qr.score)
      }
    }
    put("resultMap", resultMap)

    val totalScoreMap = Collections.newMap[Long, Float]
    supervisiorEvaluate foreach { de =>
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

    val query = OqlBuilder.from(classOf[SupervisiorEvaluate], "departE")
    query.where("departE.staff.id=:id", staffId).where("departE.semester.id=:semesterId", semesterId)
    var supervisiorEvaluate = new SupervisiorEvaluate

    val resultMap = Collections.newMap[Question, SupervisiorQuestion]
    entityDao.search(query) foreach { de =>
      supervisiorEvaluate = de
      de.questionResults foreach { qr =>
        resultMap.put(qr.question, qr)
      }
    }

    supervisiorEvaluate.staff = staff
    supervisiorEvaluate.department = staff.state.department
    supervisiorEvaluate.semester = entityDao.get(classOf[Semester], semesterId)
    supervisiorEvaluate.evaluateAt = new Date()
    supervisiorEvaluate.questionnaire = questionnaire
    questionnaire.questions foreach { question =>
      resultMap.get(question) match {
        case Some(qr) => qr.score = getFloat(question.id + "_score").get
        case None =>
          val qr = new SupervisiorQuestion
          qr.question = question
          qr.result = supervisiorEvaluate
          qr.score = getFloat(question.id + "_score").get
          supervisiorEvaluate.questionResults += qr
      }
    }
    entityDao.saveOrUpdate(supervisiorEvaluate)
    redirect("search", s"orderBy=staff.code asc&supervisiorEvaluate.semester.id=$semesterId", "info.save.success")
  }
}