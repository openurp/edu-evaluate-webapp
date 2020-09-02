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
package org.openurp.edu.evaluation.student.web.action

import java.time.{Instant, LocalDate}

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.security.Securities
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.base.model.{Semester, Student, Teacher}
import org.openurp.edu.clazz.model.{Clazz, CourseTaker}
import org.openurp.edu.evaluation.app.course.model.TextEvaluateSwitch
import org.openurp.edu.evaluation.clazz.model.{TeacherRemessage, TextEvaluation}
import org.openurp.edu.web.ProjectSupport

import scala.collection.mutable.Buffer

class TextAction extends RestfulAction[TextEvaluation] with ProjectSupport {

  def getOtherMap(std: Student, semester: Semester, teachers: Seq[Teacher]): collection.Map[Long, Buffer[TeacherRemessage]] = {
    val query = OqlBuilder.from(classOf[TeacherRemessage], "teacherRemessage")
    query.join("left", "teacherRemessage.students", "student")
    query.join("left", "teacherRemessage.textEvaluation", "textEvaluation")
    query.where("student =:std", std)
    query.where("textEvaluation.clazz.semester =:semester", semester)
    query.where("textEvaluation.student !=:std", std)
    query.where("teacherRemessage.visible = true")
    query.orderBy("teacherRemessage.createdAt desc")
    val otherMap = Collections.newMap[Long, Buffer[TeacherRemessage]]
    val results = entityDao.search(query)
    results foreach { teacherRemessage =>
      otherMap.getOrElseUpdate(teacherRemessage.textEvaluation.teacher.id, Collections.newBuffer[TeacherRemessage]) += teacherRemessage
    }
    for (teacher <- teachers) {
      if (!otherMap.contains(teacher.id)) {
        otherMap.put(teacher.id, null)
      }
    }
    otherMap
  }

  def getMyTextEvaluationMap(std: Student, semester: Semester, teachers: Seq[Teacher]): collection.Map[Long, Buffer[TextEvaluation]] = {
    val query = OqlBuilder.from(classOf[TextEvaluation], "textEvaluation")
    query.where("textEvaluation.student =:std", std)
    query.where("textEvaluation.clazz.semester =:semester", semester)
    query.where("textEvaluation.audited = true")
    val textEvaluateMap = Collections.newMap[Long, Buffer[TextEvaluation]]
    val results = entityDao.search(query)
    results foreach { textEvaluation =>
      textEvaluateMap.getOrElseUpdate(textEvaluation.teacher.id, Collections.newBuffer[TextEvaluation]) += textEvaluation
    }
    for (teacher <- teachers) {
      if (!textEvaluateMap.contains(teacher.id)) {
        textEvaluateMap.put(teacher.id, null)
      }
    }
    textEvaluateMap
  }

  def getAnnMap(std: Student, semester: Semester, teachers: Seq[Teacher]): collection.Map[Long, Buffer[TeacherRemessage]] = {
    val query = OqlBuilder.from(classOf[TeacherRemessage], "teacherRemessage")
    query.join("left", "teacherRemessage.students", "student")
    query.join("left", "teacherRemessage.textEvaluation", "textEvaluation")
    query.where("student =:std", std)
    query.where("textEvaluation.clazz.semester = :semester", semester)
    query.where("teacherRemessage.visible = false")
    val annMap = Collections.newMap[Long, Buffer[TeacherRemessage]]
    val results = entityDao.search(query)
    results foreach { teacherRemessage =>
      annMap.getOrElseUpdate(teacherRemessage.textEvaluation.teacher.id, Collections.newBuffer[TeacherRemessage]) +=
        teacherRemessage
    }
    for (teacher <- teachers) {
      if (!annMap.contains(teacher.id)) {
        annMap.put(teacher.id, null)
      }
    }
    annMap
  }

  def getTeachersByClazzIdSeq(clazzIdSeq: List[Long]): Seq[Teacher] = {
    val query = OqlBuilder.from[Teacher](classOf[Clazz].getName + " clazz")
    query.join("clazz.teachers", "teacher")
    query.select("teacher")
    query.where("clazz.id in (:clazzIdSeq)", clazzIdSeq)
    entityDao.search(query)
  }

  def getTeacherClazzByClazzIdSeq(clazzIdSeq: List[Long]): Seq[Array[Any]] = {
    val query = OqlBuilder.from[Array[Any]](classOf[Clazz].getName + " clazz")
    query.join("clazz.teachers", "teacher")
    query.select("teacher,clazz")
    query.where("clazz.id in (:clazzIdSeq)", clazzIdSeq)
    entityDao.search(query)
  }

  def getTextEvaluationList(student: Student, clazz: Clazz, teacher: Teacher): Seq[TextEvaluation] = {
    val query = OqlBuilder.from(classOf[TextEvaluation], "textEvaluation")
    query.where("textEvaluation.student =:student", student)
    query.where("textEvaluation.clazz =:clazz", clazz)
    query.where("textEvaluation.teacher =:teacher", teacher)
    entityDao.search(query)
  }

  def getSwitch(): TextEvaluateSwitch = {
    val iterator: Iterator[TextEvaluateSwitch] = entityDao.getAll(classOf[TextEvaluateSwitch]).iterator
    if (iterator.hasNext)
      iterator.next()
    else new TextEvaluateSwitch()
  }

  def getClazzIdAndTeacherIdOfResult(student: Student, semester: Semester): collection.Map[String, String] = {
    val query = OqlBuilder.from(classOf[TextEvaluation], "textEvaluation")
    //    query.select("textEvaluation.clazz.id,textEvaluation.teacher.id")
    query.where("textEvaluation.student = :student ", student)
    query.where("textEvaluation.clazz.semester = :semester", semester)
    val a = entityDao.search(query)
    a.map(obj => (obj.clazz.id.toString + "_" + (if (null == obj.teacher) "0" else obj.teacher.id.toString), "1")).toMap
  }

  def getStdClazzs(student: Student, semester: Semester): Seq[Clazz] = {

    val query = OqlBuilder.from(classOf[CourseTaker], "courseTake")
    query.select("distinct courseTake.clazz.id ")
    query.where("courseTake.std=:std", student)
    query.where("courseTake.semester =:semester", semester)
    val clazzIds = entityDao.search(query)
    var stdClazzs: Seq[Clazz] = Seq()
    if (clazzIds.nonEmpty) {
      val entityquery = OqlBuilder.from(classOf[Clazz], "clazz").where("clazz.id in (:clazzIds)", clazzIds)
      stdClazzs = entityDao.search(entityquery)
    }
    stdClazzs
  }

  override protected def indexSetting(): Unit = {
    val std = getStudent(getProject)
    if (std == null) {
      forward("error.std.stdNo.needed")
    }
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    if (semesters.nonEmpty) {
      val now = LocalDate.now
      semesters.find(x => now.isAfter(x.beginOn) && now.isBefore(x.endOn)) foreach { semester =>
        put("currentSemester", semester)
      }
    }
  }

  override def search(): View = {
    val std = getStudent(getProject)
    if (std == null) {
      forward("error.std.stdNo.needed")
    }
    // 页面条件
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val clazzs = getStdClazzs(std, semester)
    // 获得(我的课程)

    if (clazzs.isEmpty) {
      addMessage("对不起,没有评教课程!")
      forward("errors")
    }
    var myCourses = Collections.newBuffer[Clazz]
    clazzs foreach { clazz =>
      if (clazz.teachers.nonEmpty) {
        myCourses += clazz
      }
    }
    put("clazzs", myCourses)
    // 获得(文字评教-已经评教)
    put("evaluateMap", getClazzIdAndTeacherIdOfResult(std, semester))
    forward()
  }

  def loadTextEvaluate(): View = {
    val evaluateId = get("evaluateId").get
    val evaluateState = get("evaluateState").get
    val ids = get("evaluateId").get.split(",")
    // 获得(教学任务)
    val clazz = entityDao.get(classOf[Clazz], ids(0).toLong)
    if (null == clazz) {
      addMessage("找不到该课程!")
      return forward("errors")
    }
    val textEvaluationSwitch = getSwitch()
    if (null == textEvaluationSwitch) {
      //      || !textEvaluationSwitch.isTextEvaluateOpened()) {
      addMessage("现在还没有开放文字评教!")
      forward("errors")
    }
    // 获得(教师)
    val teacher = entityDao.get(classOf[Teacher], ids(1).toLong)
    if (null == teacher) {
      addMessage("该课程没有指定任课教师!")
      forward("errors")
    }
    // 判断(是否更新)
    if ("update".equals(evaluateState)) {
      val std = getStudent(getProject)
      val textEvaluations = getTextEvaluationList(std, clazz, teacher)
      put("textEvaluations", textEvaluations)
    }
    put("teacher", teacher)
    put("clazz", clazz)
    put("evaluateState", evaluateState)
    forward()
  }

  def saveTextEvaluate(): View = {
    val std = getStudent(getProject)
    val ClazzId = longId("clazz")
    val teacherId = getLong("teacherId")
    val clazz = entityDao.get(classOf[Clazz], ClazzId)
    val teacher = entityDao.get(classOf[Teacher], teacherId.get)
    val textOpinion = get("textOpinion").get
    val evaluateByTeacher = getBoolean("evaluateByTeacher").get
    try {
      if (!textOpinion.isEmpty) {
        val textEvaluation = new TextEvaluation()
        textEvaluation.student = std
        textEvaluation.clazz = clazz
        textEvaluation.teacher = teacher
        textEvaluation.contents = textOpinion
        textEvaluation.evaluateByTeacher = evaluateByTeacher
        textEvaluation.evaluateAt = Instant.now
        entityDao.saveOrUpdate(textEvaluation)
      }
      redirect("search", "&semester.id=" + clazz.semester.id, "info.save.success")
    } catch {
      case e: Exception =>
        redirect("search", "&semester.id=" + clazz.semester.id, "info.save.failure")
    }
  }

  def remsgList(): View = {
    val std = getStudent(getProject)
    val ids = longIds("clazz")
    val teachers = getTeachersByClazzIdSeq(ids)
    val clazzs = getTeacherClazzByClazzIdSeq(ids)
    val clazz = entityDao.get(classOf[Clazz], ids.head)
    val semester = clazz.semester

    put("clazzs", clazzs)
    // 获得(教师公告)
    put("annMap", getAnnMap(std, semester, teachers))
    // 获得(评教回复-本人)
    put("textEvaluationMap", getMyTextEvaluationMap(std, semester, teachers))
    // 获得(评教回复-其他)
    put("otherMap", getOtherMap(std, semester, teachers))
    forward()
  }
}
