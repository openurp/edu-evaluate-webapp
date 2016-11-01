package org.openurp.edu.evaluation.department.action

import java.util.Date
import scala.collection.mutable.Buffer
import org.beangle.commons.collection.{ Collections, Order }
import org.beangle.commons.lang.ClassLoaders
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.transfer.importer.listener.ImporterForeignerListener
import org.beangle.webmvc.api.view.{ Stream, View }
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.{ Department, Semester }
import org.openurp.edu.base.model.Teacher
import org.openurp.edu.evaluation.app.department.model.EvaluateSwitch
import org.openurp.edu.evaluation.department.helper.ImportSupervisiorListener
import org.openurp.edu.evaluation.department.model.{ SupervisiorEvaluate, SupervisiorQuestion }
import org.openurp.edu.evaluation.model.{ Question, QuestionType, Questionnaire }
import org.openurp.edu.lesson.model.Lesson
import org.beangle.data.transfer.TransferListener

/**
 * @author xinzhou
 */
class SupervisiorEvaluateAction extends RestfulAction[SupervisiorEvaluate] with ImportDataSupport[SupervisiorEvaluate] {

  override def indexSetting(): Unit = {
    put("departments", entityDao.getAll(classOf[Department]))
    put("semesters", entityDao.getAll(classOf[Semester]))
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)
  }

  def importTeachers(): View = {
    val builder = OqlBuilder.from[Array[Any]](classOf[Lesson].getName, "lesson")
    getInt("supervisiorEvaluate.semester.id") foreach { semesterId => builder.where("lesson.semester.id=:id", semesterId) }
    builder.join("lesson.teachers", "teacher")
    builder.select("distinct teacher.id , lesson.teachDepart.id , lesson.semester.id")
    builder.where("not exists (from " + classOf[SupervisiorEvaluate].getName + " se where se.semester = lesson.semester and se.teacher = teacher and se.department = lesson.teachDepart)")
    val datas = entityDao.search(builder)
    val supervisiorEvaluates = Collections.newBuffer[SupervisiorEvaluate]
    datas foreach { data =>
      val supervisiorEvaluate = new SupervisiorEvaluate
      supervisiorEvaluate.teacher = new Teacher
      supervisiorEvaluate.teacher.id = data(0).asInstanceOf[Long]
      supervisiorEvaluate.department = new Department
      supervisiorEvaluate.department.id = data(1).asInstanceOf[Int]
      supervisiorEvaluate.semester = new Semester
      supervisiorEvaluate.semester.id = data(2).asInstanceOf[Int]
      supervisiorEvaluate.evaluateAt = new Date()
      supervisiorEvaluate.questionnaire = entityDao.get(classOf[Questionnaire], 322L)
      supervisiorEvaluates += supervisiorEvaluate
    }
    entityDao.saveOrUpdate(supervisiorEvaluates)
    val semesterId = get("supervisiorEvaluate.semester.id").orNull
    redirect("search", s"orderBy=supervisiorEvaluate.teacher.code asc&supervisiorEvaluate.semester.id=$semesterId", "导入完成")
  }

  override protected def getQueryBuilder(): OqlBuilder[SupervisiorEvaluate] = {
    val query = OqlBuilder.from(classOf[SupervisiorEvaluate], "supervisiorEvaluate")
    getBoolean("passed") match {
      case Some(true) => query.where("supervisiorEvaluate.totalScore is not null")
      case Some(false) => query.where("supervisiorEvaluate.totalScore is null")
      case None =>
    }
    populateConditions(query)
    query.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
  }
  override def editSetting(supervisiorEvaluate: SupervisiorEvaluate): Unit = {
    val semesterId = intId("supervisiorEvaluate.semester")
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
    val resultMap = Collections.newMap[Question, Float]
    supervisiorEvaluate.questionResults foreach { qr =>
      resultMap.put(qr.question, qr.score)
    }
    put("resultMap", resultMap)
    super.editSetting(supervisiorEvaluate)
  }

  override def saveAndRedirect(supervisiorEvaluate: SupervisiorEvaluate): View = {
    val questionnaire = entityDao.get(classOf[Questionnaire], 322L)
    val resultMap = Collections.newMap[Question, SupervisiorQuestion]
    supervisiorEvaluate.questionResults foreach { qr =>
      resultMap.put(qr.question, qr)
    }
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
    supervisiorEvaluate.calTotalScore()
    super.saveAndRedirect(supervisiorEvaluate)
  }

  def importTemplate: View = {
    Stream(ClassLoaders.getResourceAsStream("supervisiorEvaluate.xls"), "application/vnd.ms-excel", "评教结果.xls")
  }

  protected override def importerListeners: List[_ <: TransferListener] = {
    List(new ImporterForeignerListener(entityDao), new ImportSupervisiorListener(entityDao))
  }

}
