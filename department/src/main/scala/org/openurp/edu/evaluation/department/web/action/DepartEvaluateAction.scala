package org.openurp.edu.evaluation.department.web.action

import java.util.Date
import scala.collection.mutable.Buffer
import org.beangle.commons.collection.{ Collections, Order }
import org.beangle.commons.lang.ClassLoaders
import org.beangle.commons.dao.OqlBuilder
import org.beangle.data.transfer.importer.listener.ImporterForeignerListener
import org.beangle.webmvc.api.view.{ Stream, View }
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.{ Department, Semester }
import org.openurp.edu.base.model.Teacher
import org.openurp.edu.evaluation.department.helper.ImportDepartListener
import org.openurp.edu.evaluation.model.{ Question, QuestionType, Questionnaire }
import org.openurp.edu.lesson.model.Lesson
import org.openurp.platform.api.security.Securities
import org.openurp.edu.evaluation.app.department.model.EvaluateSwitch
import org.openurp.edu.evaluation.department.model.DepartEvaluate
import org.openurp.edu.evaluation.department.model.DepartQuestion
import org.beangle.data.transfer.importer.listener.ImporterForeignerListener
import org.openurp.base.model.Department
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.app.department.model.EvaluateSwitch
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.edu.lesson.model.Lesson
import org.beangle.data.transfer.TransferListener

/**
 * @author xinzhou
 */
class DepartEvaluateAction extends RestfulAction[DepartEvaluate] with ImportDataSupport[DepartEvaluate] {

  override def indexSetting(): Unit = {
    put("departments", entityDao.getAll(classOf[Department]))
    put("semesters", entityDao.getAll(classOf[Semester]).sortBy { semester => semester.id })
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)

  }

  def importTeachers(): View = {
    val builder = OqlBuilder.from[Array[Any]](classOf[Lesson].getName, "lesson")
    getInt("departEvaluation.semester.id") foreach { semesterId => builder.where("lesson.semester.id=:id", semesterId) }
    builder.join("lesson.teachers", "teacher")
    builder.select("distinct teacher.id , lesson.teachDepart.id , lesson.semester.id")
    builder.where("lesson.teachDepart.id=:departId", getTeacher.user.department.id)
    builder.where("not exists (from " + classOf[DepartEvaluate].getName + " de where de.semester = lesson.semester and de.teacher = teacher and de.department = lesson.teachDepart)")
    val datas = entityDao.search(builder)
    val departEvaluates = Collections.newBuffer[DepartEvaluate]
    datas foreach { data =>
      val departEvaluate = new DepartEvaluate
      departEvaluate.teacher = new Teacher
      departEvaluate.teacher.id = data(0).asInstanceOf[Long]
      departEvaluate.department = new Department
      departEvaluate.department.id = data(1).asInstanceOf[Int]
      departEvaluate.semester = new Semester
      departEvaluate.semester.id = data(2).asInstanceOf[Int]
      departEvaluate.evaluateAt = new Date()
      departEvaluate.questionnaire = entityDao.get(classOf[Questionnaire], 322L)
      departEvaluates += departEvaluate
    }
    entityDao.saveOrUpdate(departEvaluates)
    val semesterId = get("departEvaluate.semester.id").orNull
    redirect("search", s"orderBy=departEvaluate.teacher.code asc&departEvaluate.semester.id=$semesterId", "导入完成")
  }

  override protected def getQueryBuilder(): OqlBuilder[DepartEvaluate] = {
    val query = OqlBuilder.from(classOf[DepartEvaluate], "departEvaluate")
    getBoolean("passed") match {
      case Some(true) => query.where("departEvaluate.totalScore is not null")
      case Some(false) => query.where("departEvaluate.totalScore is null")
      case None =>
    }
    query.where("departEvaluate.department.id=:id", getTeacher.user.department.id)
    populateConditions(query)
    query.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
  }

  def getTeacher(): Teacher = {
    val teachers = entityDao.findBy(classOf[Teacher], "code", List(Securities.user))
    if (teachers.isEmpty) {
      throw new RuntimeException("Cannot find teacher with code " + Securities.user)
    } else {
      teachers.head
    }
  }

  override def editSetting(departEvaluate: DepartEvaluate): Unit = {
    val semesterId = intId("departEvaluate.semester")
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
    departEvaluate.questionResults foreach { qr =>
      resultMap.put(qr.question, qr.score)
    }
    put("resultMap", resultMap)
    super.editSetting(departEvaluate)
  }

  override def saveAndRedirect(departEvaluate: DepartEvaluate): View = {
    val questionnaire = entityDao.get(classOf[Questionnaire], 322L)
    val resultMap = Collections.newMap[Question, DepartQuestion]
    departEvaluate.questionResults foreach { qr =>
      resultMap.put(qr.question, qr)
    }
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
    departEvaluate.calTotalScore()
    super.saveAndRedirect(departEvaluate)
  }

  def importTemplate: View = {
    Stream(ClassLoaders.getResourceAsStream("departEvaluate.xls"), "application/vnd.ms-excel", "评教结果.xls")
  }

  protected override def importerListeners: List[_ <: TransferListener] = {
    List(new ImporterForeignerListener(entityDao), new ImportDepartListener(entityDao))
  }

}
