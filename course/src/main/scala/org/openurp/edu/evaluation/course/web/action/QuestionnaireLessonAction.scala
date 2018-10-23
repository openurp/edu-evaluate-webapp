package org.openurp.edu.evaluation.course.web.action

import java.sql.Date
import org.beangle.commons.collection.Order
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.dao.QueryBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.beangle.webmvc.entity.helper.QueryHelper
import org.openurp.base.model.Department
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.code.model.CourseType
import org.openurp.edu.base.code.model.ExamMode
import org.openurp.edu.base.code.model.ExamMode
import org.openurp.edu.base.model.Project
import org.openurp.edu.evaluation.app.lesson.service.StdEvaluateSwitchService
import org.openurp.edu.evaluation.course.model.QuestionnaireClazz
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.edu.course.model.Clazz
import org.openurp.edu.course.model.Clazz
import org.openurp.edu.course.model.Clazz
import org.beangle.commons.lang.Strings
import org.beangle.webmvc.api.annotation.action
import java.time.LocalDate
import org.beangle.commons.collection.Collections

class QuestionnaireClazzAction extends ProjectRestfulAction[QuestionnaireClazz] {

  var evaluateSwitchService: StdEvaluateSwitchService = _

  override def indexSetting(): Unit = {
    put("semesters", getSemesters())
    put("departments", findItemsBySchool(classOf[Department]))
    //    val examModes = entityDao.getAll(classOf[ExamMode])
    //    put("examModes", examModes)
    put("courseTypes", entityDao.getAll(classOf[CourseType]))
    put("questionnaires", entityDao.getAll(classOf[Questionnaire]))
  }

  override def search(): View = {
    val questionnaireId = getLong("questionnaire.id").getOrElse(-1)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)

    // 检查时间
    val evaluateSwitch = evaluateSwitchService.getEvaluateSwitch(semester, currentProject)
    if (null != evaluateSwitch) {
      //        && evaluateSwitch.checkOpen(new java.util.Date())) {
      put("isEvaluateSwitch", true)
    }
    val query = OqlBuilder.from(classOf[Questionnaire], "questionnaire")
    query.where("questionnaire.state=true").where(
      "questionnaire.beginOn <= :now and (questionnaire.endOn is null or questionnaire.endOn >= :now)",
      LocalDate.now)
    put("questionnaires", entityDao.search(query))
    // 判断(问卷是否存在)
    questionnaireId match {
      case 0 => //无问卷--教学任务list
        put("lessons", entityDao.search(getQueryBuilderByClazz()))
        forward("lessonList")
      case -1 => //有问卷
        put("questionnaireClazzs", entityDao.search(getQueryBuilder()))
        forward("list")
      case _ => //问卷Id
        put("questionnaireClazzs", entityDao.search(getQueryBuilder()))
        forward("list")
    }
  }

  protected override def getQueryBuilder(): OqlBuilder[QuestionnaireClazz] = {
    val query = OqlBuilder.from(classOf[QuestionnaireClazz], "questionnaireClazz")
    populateConditions(query)
    //    query.where(QueryHelper.extractConditions(classOf[Clazz], "lesson", null))
    //    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("semester.id") //.getOrElse(entityDao.search(semesterQuery).head.id)
    semesterId foreach { semesterId =>
      query.where("questionnaireClazz.lesson.semester.id = :semesterId", semesterId)
    }
    query.where("questionnaireClazz.lesson.project = :project", currentProject)
    // 隐性条件(问卷类别,起始周期,上课人数)
    val questionnaireId = getLong("questionnaire.id").getOrElse(-1)
    val teacherName = get("teacher").orNull
    if (Strings.isNotBlank(teacherName)) {
      query.join("questionnaireClazz.lesson.teachers", "teacher")
      query.where("teacher.person.name.formatedName like :teacherName", "%" + teacherName + "%")
    }
    if (questionnaireId != -1 && questionnaireId != 0) {
      query.where("questionnaireClazz.questionnaire.id =:questionnaireId", questionnaireId)

    }
    query.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    query
  }

  /**
   * 查询语句(教学任务,缺失问卷) 无问卷教学任务list
   *
   * @return
   */
  protected def getQueryBuilderByClazz(): QueryBuilder[Clazz] = {

    val query = OqlBuilder.from(classOf[Clazz], "lesson")
    populateConditions(query)
    query.where("lesson.project=:project", currentProject)
    populateConditions(query)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("semester.id") //.getOrElse(entityDao.search(semesterQuery).head.id)
    semesterId foreach { semesterId =>
      query.where("lesson.semester.id = :semesterId", semesterId)
    }
    val teacherName = get("teacher").getOrElse("")
    if (Strings.isNotBlank(teacherName)) {
      query.join("lesson.teachers", "teacher")
      query.where("teacher.person.name.formatedName like :teacherName", "%" + teacherName + "%")
    }
    // 排除(已有问卷)
    query.where("not exists(from " + classOf[QuestionnaireClazz].getName + " questionnaireClazz"
      + " where questionnaireClazz.lesson = lesson)")
    query.orderBy(get(Order.OrderStr).orNull)
    query.limit(getPageLimit)
    query
  }
  /**
   * 设置(评教类型,定制问卷,条件-问卷缺失)
   *
   * @return
   */
  def saveQuestionnaireClazz(): View = {
    val isAll = get("isAll").get
    var lessons: Seq[Clazz] = Seq()
    // 获取(更新对象)
    if ("all".equals(isAll)) {
      val qurey = getQueryBuilderByClazz()
      lessons = entityDao.search(getQueryBuilderByClazz().limit(null))
    } else {
      val ids = longIds("lesson")
      lessons = entityDao.find(classOf[Clazz], ids)
    }
    println(lessons.size)
    val questionnaireId = longId("questionnaire")
    if (questionnaireId != 0) {
      val isEvaluate = getBoolean("isEvaluate").get
      val questionnaire = entityDao.get(classOf[Questionnaire], questionnaireId)
      //        for (Clazz lesson : lessons) {
      lessons foreach { lesson =>
        val questionnaireClazz = new QuestionnaireClazz()
        questionnaireClazz.questionnaire = questionnaire
        questionnaireClazz.clazz = lesson
        questionnaireClazz.evaluateByTeacher = isEvaluate
        entityDao.saveOrUpdate(questionnaireClazz)
      }
    }
    redirect("search", "info.action.success")

  }

  /**
   * 设置(评教类型,定制问卷,条件-问卷存在)
   *
   * @return
   */
  def updateQuestionnaireClazz(): View = {
    val isAll = get("isAll").get
    var questionnaireClazzs: Seq[QuestionnaireClazz] = Seq()
    // 获取(更新对象)
    if ("all".equals(isAll)) {
      questionnaireClazzs = entityDao.getAll(classOf[QuestionnaireClazz])
    } else {
      val ids = longIds(simpleEntityName)
      questionnaireClazzs = entityDao.find(classOf[QuestionnaireClazz], ids)
    }

    //    try {
    val questionnaireId = this.getLong("questionnaire.id").getOrElse(0L)
    // 判断(是否删除)
    if (questionnaireId == 0L) {
      entityDao.remove(questionnaireClazzs)
    } else {
      val isEvaluate = getBoolean("isEvaluate").get
      val questionnaire = entityDao.get(classOf[Questionnaire], questionnaireId)
      //        for (QuestionnaireClazz questionnaireClazz : questionnaireClazzs) {
      questionnaireClazzs foreach { questionnaireClazz =>
        questionnaireClazz.questionnaire = questionnaire
        questionnaireClazz.evaluateByTeacher = isEvaluate

      }
      entityDao.saveOrUpdate(questionnaireClazzs)
    }
    redirect("search", "--------info.action.success")
    //    } catch {
    //      case e: Exception =>
    //        redirect("search", "----..---info.action.failure")
    //    }
  }
  //
  //  def setEvaluateSwitchService(evaluateSwitchService: StdEvaluateSwitchService) {
  //    this.evaluateSwitchService = evaluateSwitchService
  //  }
}
