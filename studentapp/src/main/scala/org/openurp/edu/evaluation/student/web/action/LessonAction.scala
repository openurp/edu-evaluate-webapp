package org.openurp.edu.evaluation.student.web.action

import java.util.Date

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.model.Student
import org.openurp.edu.base.model.Teacher
import org.openurp.edu.evaluation.app.lesson.service.StdEvaluateSwitchService
import org.openurp.edu.evaluation.course.model.QuestionnaireClazz
import org.openurp.edu.evaluation.course.result.model.EvaluateResult
import org.openurp.edu.evaluation.course.result.model.QuestionResult
import org.openurp.edu.evaluation.model.Option
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.course.model.CourseTaker
import org.openurp.edu.course.model.Clazz
import org.openurp.platform.api.security.Securities
import java.time.LocalDate
import java.time.Instant

class ClazzAction extends RestfulAction[EvaluateResult] {

  var evaluateSwitchService: StdEvaluateSwitchService = _

  def getResultByStdIdAndClazzId(stdId: Long, lessonId: Long, teacherId: Long): EvaluateResult = {
    val query = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult")
    query.where("evaluateResult.student.id =:stdId", stdId);
    query.where("evaluateResult.lesson.id =:lessonId", lessonId);
    if (0 != teacherId) {
      query.where("evaluateResult.teacher.id =:teacherId", teacherId);
    } else {
      query.where("evaluateResult.teacher is null");
    }
    val result = entityDao.search(query);

    if (result.size > 0) result.head else null.asInstanceOf[EvaluateResult];
  }

  def getClazzIdAndTeacherIdOfResult(student: Student, semester: Semester): collection.Map[String, String] = {
    val query = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult")
    //    query.select("evaluateResult.lesson.id,evaluateResult.teacher.id")
    query.where("evaluateResult.student = :student ", student)
    query.where("evaluateResult.lesson.semester = :semester", semester)
    val a = entityDao.search(query)
    a.map(obj => (obj.lesson.id + "_" + (if (null == obj.teacher) "0" else obj.teacher.id), "1")).toMap
  }

  def getStdClazzs(student: Student, semester: Semester): Seq[Clazz] = {
    val query = OqlBuilder.from[Clazz](classOf[CourseTaker].getName, "courseTaker")
    query.select("courseTaker.lesson")
    query.where("courseTaker.std=:std", student)
    query.where("courseTaker.semester =:semester", semester)
    entityDao.search(query)
  }

  override protected def indexSetting(): Unit = {
    val std = getStudent()
    if (std == null) { forward("error.std.stdNo.needed") }
    val semesters = evaluateSwitchService.getOpenedSemesters(std.project)
    put("semesters", semesters)
    if (!semesters.isEmpty) put("currentSemester", semesters.head)
  }

  override def search(): View = {
    val std = getStudent()
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val lessonList = getStdClazzs(std, semester)
    // 获得(课程问卷,根据学生,根据教学任务)
    var myClazzs: Seq[QuestionnaireClazz] = Seq();
    if (!lessonList.isEmpty) {
      val query = OqlBuilder.from(classOf[QuestionnaireClazz], "questionnaireClazz")
      query.where("questionnaireClazz.lesson in (:lessonList)", lessonList)
      val myquestionnaires = entityDao.search(query)
      myClazzs = myquestionnaires
    }
    // 获得(评教结果,根据学生)
    val evaluateMap = getClazzIdAndTeacherIdOfResult(std, semester);
    put("evaluateMap", evaluateMap);
    put("questionnaireClazzs", myClazzs);
    forward()
  }

  /**
   * 跳转(问卷页面)
   */
  def loadQuestionnaire(): View = {
    val lessonId = get("lessonId").get
    val evaluateState = get("evaluateState").get
    val semesterId = getInt("semester.id").get
    val ids = get("lessonId").get.split(",")
    // 获得(教学任务)
    val lesson = entityDao.get(classOf[Clazz], ids(0).toLong)
    if (null == lesson) {
      addMessage("找不到该课程!");
      return forward("errors");
    }
    val evaluateSwitchs = evaluateSwitchService.getEvaluateSwitch(lesson.semester, lesson.project)
    if (evaluateSwitchs.isEmpty) {
      addMessage("现在还没有开放课程评教!");
      return forward("errors");
    }
    val evaluateSwitch = evaluateSwitchs.head
    if (!evaluateSwitch.isOpenedAt(Instant.now)) {
      addMessage("不在课程评教开放时间内,开放时间为：!" + evaluateSwitch.beginAt + "～" + evaluateSwitch.endAt);
      return forward("errors");
    }
    // 获得(课程问卷,根据教学任务)
    val questionnaireClazzs = entityDao.findBy(classOf[QuestionnaireClazz], "lesson.id", List(lesson.id));
    if (questionnaireClazzs.isEmpty) {
      addMessage("缺失评教问卷!");
      return forward("errors");
    }

    val questionnaireClazz = questionnaireClazzs.head
    var questions = questionnaireClazz.questionnaire.questions
    questions.sortWith((x, y) => x.priority < y.priority)

    // 获得(教师列表,根据学生教学任务)
    val teachers = Collections.newBuffer[Teacher]
    if (questionnaireClazz.evaluateByTeacher) {
      val teacher = entityDao.get(classOf[Teacher], ids(1).toLong)
      teachers += teacher
    } else {
      teachers ++= lesson.teachers
    }

    // 判断(是否更新)
    if ("update".equals(evaluateState)) {
      var teacherId: Long = 0;
      if (questionnaireClazz.evaluateByTeacher) {
        teacherId = ids(1).toLong
      } else { teacherId = teachers.head.id }
      val std = getStudent();
      val evaluateResult = getResultByStdIdAndClazzId(std.id, lesson.id, teacherId);
      if (null == evaluateResult) {
        addMessage("error.dataRealm.insufficient");
        forward("errors");
      }
      // 组装(问题结果)
      val questionMap = evaluateResult.questionResults.map(q => (q.question.id.toString, q.option.id)).toMap
      put("questionMap", questionMap);
      put("evaluateResult", evaluateResult);
    }

    put("lesson", lesson);
    put("teachers", teachers);
    put("questions", questions);
    //questionnaire = entityDao.get(classOf[Questionnaire], questionnaireClazz.questionnaire.id);
    put("questionnaire", questionnaireClazz.questionnaire);
    put("evaluateState", evaluateState);
    forward()
  }

  def getStudent(): Student = {
    val stds = entityDao.search(OqlBuilder.from(classOf[Student], "s")
      .where("s.code=:code", Securities.user)
      .where("s.project.code=:project_code", get("project").get))
    if (stds.isEmpty) {
      throw new RuntimeException("Cannot find student with code " + Securities.user)
    } else {
      stds.head
    }
  }

  override def save(): View = {
    val std = getStudent()
    // 页面参数
    val lessonId = getLong("lesson.id").get
    var teacherId = getLong("teacherId").get
    //    val semesterId = getInt("semester.id").get
    val teacherIds = longIds("teacher")
    // 根据教学任务,获得课程问卷
    val query = OqlBuilder.from(classOf[QuestionnaireClazz], "questionnaireClazz");
    query.where("questionnaireClazz.lesson.id =:lessonId", lessonId);
    val questionnaireClazzs = entityDao.search(query);
    if (questionnaireClazzs.isEmpty) {
      addMessage("field.evaluate.questionnaire");
      forward("errors");
    }
    val questionnaireClazz = questionnaireClazzs.head
    // 查询(评教结果)
    var evaluateResults: Seq[EvaluateResult] = Seq()
    val queryResult = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult");
    queryResult.where("evaluateResult.lesson.id =:lessonId", lessonId);
    //    queryResult.where("evaluateResult.lesson.semester.id =:semesterId",semesterId);
    queryResult.where("evaluateResult.student =:std", std);
    // 如果教师为空
    if (teacherIds.size == 0) {
      evaluateResults = entityDao.search(queryResult);
    } else if (teacherIds.size == 1) {
      queryResult.where("evaluateResult.teacher.id =:teacherId", teacherId);
      evaluateResults = entityDao.search(queryResult);
    } //    如果是多个教师且为课程评教
    else if (teacherIds.size > 1) {
      queryResult.where("evaluateResult.teacher.id in(:teacherIds)", teacherIds);
      evaluateResults = entityDao.search(queryResult);
    }

    var lesson: Clazz = null;
    var teacher: Teacher = null;
    var newTeacherIds = Collections.newBuffer[Long]
    try {
      // 更新评教记录
      if (evaluateResults.size > 0) {
        evaluateResults foreach { evaluateResult =>
          lesson = evaluateResult.lesson
          teacher = evaluateResult.teacher
          newTeacherIds += teacher.id
          // 修改(问题选项)
          val questionResults = evaluateResult.questionResults
          val questions = questionnaireClazz.questionnaire.questions
          // 判断(是否添加问题)
          val oldQuestions = Collections.newBuffer[Question]
          questionResults foreach { questionResult =>
            oldQuestions += questionResult.question
          }
          questions foreach { question =>
            if (!oldQuestions.contains(question)) {
              val optionId = getLong("select" + question.id).get
              val option = entityDao.get(classOf[Option], optionId);
              val questionResult = new QuestionResult()
              questionResult.questionType = question.questionType
              questionResult.question = question
              questionResult.option = option
              questionResult.result = evaluateResult
              evaluateResult.questionResults += questionResult
            }
          }
          // 重新赋值
          evaluateResult.remark = get("evaluateResult.remark").getOrElse("")
          // 修改
          questionResults foreach { questionResult =>
            val question = questionResult.question
            val optionId = getLong("select" + question.id).get
            if (optionId == 0L) {
              questionResult.result = null
              entityDao.remove(questionResult);
            }
            if (!questionResult.option.id.equals(optionId)) {
              val option = entityDao.get(classOf[Option], optionId);
              questionResult.option = option
              questionResult.score = question.score * option.proportion.floatValue()
            }
          }
          entityDao.saveOrUpdate(questionResults);
        }
        var newId: Long = 0L
        //       一门课有新增教师，要为新增教师新增评教记录
        if (teacherIds.size > newTeacherIds.size) {
          teacherIds foreach { id =>
            if (!newTeacherIds.contains(id)) {
              newId = id
            }
          }
          val evaluateResult = new EvaluateResult()
          evaluateResult.lesson = lesson
          evaluateResult.department = lesson.teachDepart
          evaluateResult.student = std
          evaluateResult.teacher = entityDao.get(classOf[Teacher], newId)
          evaluateResult.evaluateAt = Instant.now
          questionnaireClazz.questionnaire.questions foreach { question =>
            val optionId = getLong("select" + question.id).get
            val option = entityDao.get(classOf[Option], optionId);
            val questionResult = new QuestionResult()
            questionResult.question = question
            questionResult.questionType = question.questionType
            questionResult.result = evaluateResult
            questionResult.option = option
            questionResult.score = question.score * option.proportion.floatValue()
            evaluateResult.questionnaire = questionnaireClazz.questionnaire
            evaluateResult.questionResults += questionResult
          }
          evaluateResult.remark = get("evaluateResult.remark").getOrElse("")
          entityDao.saveOrUpdate(evaluateResult)
        }
      } //      新增评教记录
      else {
        lesson = entityDao.get(classOf[Clazz], lessonId);
        val teachers = entityDao.find(classOf[Teacher], teacherIds);

        // 获得(问卷)
        val questionnaire = questionnaireClazz.questionnaire
        if (questionnaire == null || questionnaire.questions == null) {
          addMessage("评教问卷有误!");
          forward("errors");
        }

        //  一个教师
        if (teachers.size == 1) {
          teacher = teachers.head
          var evaluateTeacher = teacher;
          val evaluateResult = new EvaluateResult()
          evaluateResult.lesson = lesson
          evaluateResult.department = lesson.teachDepart
          evaluateResult.student = std
          evaluateResult.teacher = evaluateTeacher
          evaluateResult.evaluateAt = Instant.now
          questionnaire.questions foreach { question =>
            val optionId = getLong("select" + question.id).get
            val option = entityDao.get(classOf[Option], optionId);
            val questionResult = new QuestionResult()
            questionResult.question = question
            questionResult.questionType = question.questionType
            questionResult.result = evaluateResult
            questionResult.option = option
            questionResult.score = question.score * option.proportion.floatValue()
            evaluateResult.questionnaire = questionnaire
            evaluateResult.questionResults += questionResult
          }
          evaluateResult.remark = get("evaluateResult.remark").getOrElse("")
          evaluateResult.score= evaluateResult.questionResults.foldLeft(0f)(_ + _.score)
          entityDao.saveOrUpdate(evaluateResult)
        }
        //        如果是按照课程评教，且是多个教师
        if (teachers.size > 1 & (!questionnaireClazz.evaluateByTeacher)) {
          teachers foreach { teacher =>
            val evaluateResult = new EvaluateResult()
            evaluateResult.lesson = lesson
            evaluateResult.department = lesson.teachDepart
            evaluateResult.student = std
            evaluateResult.teacher = teacher
            evaluateResult.evaluateAt = Instant.now
            questionnaire.questions foreach { question =>
              val optionId = getLong("select" + question.id).get
              val option = entityDao.get(classOf[Option], optionId);
              val questionResult = new QuestionResult()
              questionResult.question = question
              questionResult.questionType = question.questionType
              questionResult.result = evaluateResult
              questionResult.option = option
              questionResult.score = question.score * option.proportion.floatValue()
              evaluateResult.questionnaire = questionnaire
              evaluateResult.questionResults += questionResult
            }
            evaluateResult.score= evaluateResult.questionResults.foldLeft(0f)(_ + _.score)
            evaluateResult.remark = get("evaluateResult.remark").getOrElse("")
            entityDao.saveOrUpdate(evaluateResult)
          }
        }
      }
      redirect("search", "&semester.id=" + lesson.semester.id, "info.save.success")
    } catch {
      case e: Exception =>
        e.printStackTrace();
        redirect("search", "&semester.id=" + lesson.semester.id, "info.save.failure");
    }
  }

}
