/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.edu.evaluation.course.web.action

import java.time.{Instant, LocalDate}

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.security.Securities
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.base.model.{Semester, Student, Teacher}
import org.openurp.edu.course.model.{Clazz, CourseTaker}
import org.openurp.edu.evaluation.app.course.service.{ClazzFilterStrategyFactory, StdEvaluateSwitchService}
import org.openurp.edu.evaluation.clazz.model.QuestionnaireClazz
import org.openurp.edu.evaluation.clazz.result.model.{EvaluateResult, QuestionResult}
import org.openurp.edu.evaluation.model.{Option, Question}

class EvaluateStdAction extends ProjectRestfulAction[EvaluateResult] {

  var clazzFilterStrategyFactory: ClazzFilterStrategyFactory = _

  var evaluateSwitchService: StdEvaluateSwitchService = _

  def getResultByStdIdAndClazzId(stdId: Long, clazzId: Long, teacherId: Long): EvaluateResult = {
    val query = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult")
    query.where("evaluateResult.student.id =:stdId", stdId)
    query.where("evaluateResult.clazz.id =:clazzId", clazzId)
    if (0 != teacherId) {
      query.where("evaluateResult.teacher.id =:teacherId", teacherId)
    } else {
      query.where("evaluateResult.teacher is null")
    }
    val result = entityDao.search(query)

    if (result.size > 0) result.head else null.asInstanceOf[EvaluateResult]
  }

  def getClazzIdAndTeacherIdOfResult(student: Student, semester: Semester): collection.Map[String, String] = {
    val query = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult")
    //    query.select("evaluateResult.clazz.id,evaluateResult.teacher.id")
    query.where("evaluateResult.student = :student ", student)
    query.where("evaluateResult.clazz.semester = :semester", semester)
    val a = entityDao.search(query)
    a.map(obj => (obj.clazz.id + "_" + (if (null == obj.teacher) "0" else obj.teacher.id), "1")).toMap
  }

  def getStdClazzs(student: Student, semester: Semester): Seq[Clazz] = {

    val query = OqlBuilder.from(classOf[CourseTaker], "courseTaker")
    query.select("distinct courseTaker.clazz.id ")
    query.where("courseTaker.std=:std", student)
    query.where("courseTaker.semester =:semester", semester)
    val clazzIds = entityDao.search(query)
    var stdClazzs: Seq[Clazz] = Seq()
    if (!clazzIds.isEmpty) {
      val entityquery = OqlBuilder.from(classOf[Clazz], "clazz").where("clazz.id in (:clazzIds)", clazzIds)
      stdClazzs = entityDao.search(entityquery)
    }
    stdClazzs
  }

  override protected def indexSetting(): Unit = {
    val std = getStudent
    put("currentSemester", this.getCurrentSemester)
  }

  override def search(): View = {
    val std = getStudent
    val semesterId = getInt("semester.id").get
    val semester = entityDao.get(classOf[Semester], semesterId)
    val clazzList = getStdClazzs(std, semester)
    // 获得(课程问卷,根据学生,根据教学任务)
    var myClazzs: Seq[QuestionnaireClazz] = Seq()
    if (!clazzList.isEmpty) {
      val query = OqlBuilder.from(classOf[QuestionnaireClazz], "questionnaireClazz")
      query.where("questionnaireClazz.clazz in (:clazzList)", clazzList)
      val myquestionnaires = entityDao.search(query)
      myClazzs = myquestionnaires
    }
    // 获得(评教结果,根据学生)
    val evaluateMap = getClazzIdAndTeacherIdOfResult(std, semester)
    put("evaluateMap", evaluateMap)
    put("questionnaireClazzs", myClazzs)
    forward()
  }

  /**
   * 跳转(问卷页面)
   */
  def loadQuestionnaire(): View = {
    val clazzId = get("clazzId").get
    val evaluateState = get("evaluateState").get
    val semesterId = getInt("semester.id").get
    val ids = get("clazzId").get.split(",")
    // 获得(教学任务)
    val clazz = entityDao.get(classOf[Clazz], ids(0).toLong)
    if (null == clazz) {
      addMessage("找不到该课程!")
      return forward("errors")
    }
    val evaluateSwitch = evaluateSwitchService.getEvaluateSwitch(clazz.semester, clazz.project)
    if (null == evaluateSwitch) {
      addMessage("现在还没有开放课程评教!")
      return forward("errors")
    }
    //    if (!evaluateSwitch.checkOpen(new Date())) {
    //      addMessage("不在课程评教开放时间内,开放时间为：!" + evaluateSwitch.beginAt + "～" + evaluateSwitch.endAt)
    //      return forward("errors")
    //    }
    //    OqlBuilder<NotEvaluateStudentBean> que = OqlBuilder.from(NotEvaluateStudentBean.class, "notevaluate")
    //    que.where("notevaluate.std=:std", this.getLoginStudent())
    //    que.where("notevaluate.semester=:semesterId", clazz.getSemester())
    //    List<NotEvaluateStudentBean> notList = entityDao.search(que)
    //    if (notList.size() > 0) {
    //      addMessage("您并非参评学生，不可评教!")
    //      return forward("errors")
    //    }
    // 获得(课程问卷,根据教学任务)
    val questionnaireClazzs = entityDao.findBy(classOf[QuestionnaireClazz], "clazz.id", List(clazz.id))
    if (questionnaireClazzs.isEmpty) {
      addMessage("缺失评教问卷!")
      return forward("errors")
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
      teachers ++= clazz.teachers
    }

    // 判断(是否更新)
    if ("update".equals(evaluateState)) {
      var teacherId: Long = 0
      if (questionnaireClazz.evaluateByTeacher) {
        teacherId = ids(1).toLong
      } else { teacherId = teachers.head.id }
      val std = getStudent
      val evaluateResult = getResultByStdIdAndClazzId(std.id, clazz.id, teacherId)
      if (null == evaluateResult) {
        addMessage("error.dataRealm.insufficient")
        forward("errors")
      }
      // 组装(问题结果)
      val questionMap = evaluateResult.questionResults.map(q => (q.question.id.toString, q.option.id)).toMap
      put("questionMap", questionMap)
      put("evaluateResult", evaluateResult)
    }

    put("clazz", clazz)
    put("teachers", teachers)
    put("questions", questions)
    //questionnaire = entityDao.get(classOf[Questionnaire], questionnaireClazz.questionnaire.id)
    put("questionnaire", questionnaireClazz.questionnaire)
    put("evaluateState", evaluateState)
    forward()
  }

  override def save(): View = {
    val std = getStudent
    // 页面参数
    val clazzId = getLong("clazz.id").get
    var teacherId = getLong("teacherId").get
    //    val semesterId = getInt("semester.id").get
    val teacherIds = longIds("teacher")
    // 根据教学任务,获得课程问卷
    val query = OqlBuilder.from(classOf[QuestionnaireClazz], "questionnaireClazz")
    query.where("questionnaireClazz.clazz.id =:clazzId", clazzId)
    val questionnaireClazzs = entityDao.search(query)
    if (questionnaireClazzs.isEmpty) {
      addMessage("field.evaluate.questionnaire")
      forward("errors")
    }
    val questionnaireClazz = questionnaireClazzs.head
    // 查询(评教结果)
    var evaluateResults: Seq[EvaluateResult] = Seq()
    val queryResult = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult")
    queryResult.where("evaluateResult.clazz.id =:clazzId", clazzId)
    //    queryResult.where("evaluateResult.clazz.semester.id =:semesterId",semesterId)
    queryResult.where("evaluateResult.student =:std", std)
    // 如果教师为空
    if (teacherIds.size == 0) {
      evaluateResults = entityDao.search(queryResult)
    } else if (teacherIds.size == 1) {
      queryResult.where("evaluateResult.teacher.id =:teacherId", teacherId)
      evaluateResults = entityDao.search(queryResult)
    } //    如果是多个教师且为课程评教
    else if (teacherIds.size > 1) {
      queryResult.where("evaluateResult.teacher.id in(:teacherIds)", teacherIds)
      evaluateResults = entityDao.search(queryResult)
    }
    //        & (!questionnaireClazz.evaluateByTeacher)） {
    //      queryResult.where("evaluateResult.teacher.id in(:teacherIds)", teacherIds)
    //      evaluateResults = entityDao.search(queryResult)
    //    }
    //    如果是多个教师且为教师评教
    //    else {
    //      //      teacherId = getLong("teacherId").get
    //      queryResult.where("evaluateResult.teacher.id in(:teacherIds)", teacherIds)
    //      evaluateResults = entityDao.search(queryResult)
    //    }

    var clazz: Clazz = null
    var teacher: Teacher = null
    var newTeacherIds = Collections.newBuffer[Long]
    try {
      // 更新评教记录
      if (evaluateResults.size > 0) {
        evaluateResults foreach { evaluateResult =>
          clazz = evaluateResult.clazz
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
              val option = entityDao.get(classOf[Option], optionId)
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
              entityDao.remove(questionResult)
            }
            if (!questionResult.option.id.equals(optionId)) {
              val option = entityDao.get(classOf[Option], optionId)
              questionResult.option = option
              questionResult.score = question.score * option.proportion.floatValue()
            }
          }
          entityDao.saveOrUpdate(questionResults)
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
          evaluateResult.clazz = clazz
          evaluateResult.department = clazz.teachDepart
          evaluateResult.student = std
          evaluateResult.teacher = entityDao.get(classOf[Teacher], newId)
          evaluateResult.evaluateAt = Instant.now
          questionnaireClazz.questionnaire.questions foreach { question =>
            val optionId = getLong("select" + question.id).get
            val option = entityDao.get(classOf[Option], optionId)
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
        clazz = entityDao.get(classOf[Clazz], clazzId)
        val teachers = entityDao.find(classOf[Teacher], teacherIds)

        // 获得(问卷)
        val questionnaire = questionnaireClazz.questionnaire
        if (questionnaire == null || questionnaire.questions == null) {
          addMessage("评教问卷有误!")
          forward("errors")
        }
        //  一个教师
        if (teachers.size == 1) {
          teacher = teachers.head
          var evaluateTeacher = teacher
          val evaluateResult = new EvaluateResult()
          evaluateResult.clazz = clazz
          evaluateResult.department = clazz.teachDepart
          evaluateResult.student = std
          evaluateResult.teacher = evaluateTeacher
          evaluateResult.evaluateAt = Instant.now
          questionnaire.questions foreach { question =>
            val optionId = getLong("select" + question.id).get
            val option = entityDao.get(classOf[Option], optionId)
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
          evaluateResult.statType = 1
          entityDao.saveOrUpdate(evaluateResult)
        }
        //        如果是按照课程评教，且是多个教师
        if (teachers.size > 1 & (!questionnaireClazz.evaluateByTeacher)) {
          teachers foreach { teacher =>
            val evaluateResult = new EvaluateResult()
            evaluateResult.clazz = clazz
            evaluateResult.department = clazz.teachDepart
            evaluateResult.student = std
            evaluateResult.teacher = teacher
            evaluateResult.evaluateAt = Instant.now
            evaluateResult.statType = 1
            questionnaire.questions foreach { question =>
              val optionId = getLong("select" + question.id).get
              val option = entityDao.get(classOf[Option], optionId)
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
            entityDao.saveOrUpdate(evaluateResult)
          }
        }

      }
      redirect("search", "&semester.id=" + clazz.semester.id, "info.save.success")
    } catch {
      case e: Exception =>
        e.printStackTrace()
        redirect("search", "&semester.id=" + clazz.semester.id, "info.save.failure")
    }
  }

}
