/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.edu.evaluation.course.web.action

import java.time.Instant
import java.time.LocalDate

import org.beangle.data.dao.OqlBuilder
import org.beangle.security.Securities
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.model.Student
import org.openurp.edu.base.model.Teacher
import org.openurp.edu.course.model.Clazz
import org.openurp.edu.course.model.CourseTaker
import org.openurp.edu.evaluation.app.course.model.TextEvaluateSwitch
import org.openurp.edu.evaluation.clazz.model.TeacherRemessage
import org.openurp.edu.evaluation.clazz.model.TextEvaluation

class TextEvaluationTeacherAction extends RestfulAction[TextEvaluation] {

  def getTeacher(): Teacher = {
    val teachers = entityDao.search(OqlBuilder.from(classOf[Teacher], "s").where("s.code=:code", Securities.user))
    if (teachers.isEmpty) {
      throw new RuntimeException("Cannot find teacher with code " + Securities.user)
    } else {
      teachers.head
    }
  }
  override protected def indexSetting(): Unit = {
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    put("currentSemester", entityDao.search(semesterQuery).head)
  }

  override def search(): View = {
    val teacher = getTeacher()
    if (teacher == null) { forward("error.teacher.teaNo.needed") }
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    if (teacher == null) {
      addMessage("对不起,您没有权限!");
      return forward("errors");
    }
    val query = OqlBuilder.from(classOf[Clazz], "clazz")
    query.join("left", "clazz.teachers", "teacher")
    query.where("clazz.project.id=:projectId", 1)
    query.where("clazz.semester =:semester", semester)
    //    populateConditions(query);
    query.where("teacher=:teacher", teacher)
    query.orderBy("clazz.semester.id").limit(getPageLimit);
    val results = entityDao.search(query)
    put("clazzs", results)
    put("semester", semester)
    forward()
  }

  def searchTextEvaluation(): View = {
    val teacher = getTeacher()
    if (teacher == null) { forward("error.teacher.teaNo.needed") }
    val clazzId = getLong("clazz.id").get
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val currentSemester = entityDao.search(semesterQuery).head
    // 判断(是否当前学期)
    val clazz = entityDao.get(classOf[Clazz], clazzId)
    val semester = entityDao.get(classOf[Semester], clazz.semester.id)
    var isCurrent = false;
    if (currentSemester.id.longValue() == semester.id.longValue()) {
      isCurrent = true;
    }
    // 获得(文字评教)
    var textEvaluations: Seq[TextEvaluation] = Seq()
    val query = OqlBuilder.from(classOf[TextEvaluation], "textEvaluation");
    query.where("textEvaluation.clazz.id =:clazzId", clazzId);
    query.where("textEvaluation.teacher =:teacher", teacher);

    val switchQuery = OqlBuilder.from(classOf[TextEvaluateSwitch], "switch").where("switch.semester.id=:semesterId", clazz.semester.id)
    val textEvaluationSwitch = entityDao.search(switchQuery)
    //    if (!textEvaluationSwitch.opened) || !textEvaluationSwitchService.isOpenTime()) {
    if (!textEvaluationSwitch.head.opened) {
      addMessage("现在还没有开放文字评教查询!");
      return forward("errors");
    } else if (isCurrent && !textEvaluationSwitch.head.openedTeacher) {
      addMessage("现在不在双向评教时间!");
      return forward("errors");
    } else {
      textEvaluations = entityDao.search(query);
    }
    put("textEvaluations", textEvaluations);

    // 获得(教学班)
    val teachClass = OqlBuilder.from(classOf[Clazz], "clazz")
    teachClass.join("left", "clazz.teachers", "teacher")
    teachClass.where("teacher=:teacher", teacher);
    teachClass.where("clazz.semester =:semester", semester);
    teachClass.select("clazz.teachclass")
    val teachClasses = entityDao.search(teachClass)
    put("isCurrent", isCurrent);
    put("clazz", entityDao.get(classOf[Clazz], clazzId));
    put("teachclasses", teachClasses)
    put("semester", semester)
    forward()
  }

  def saveEvaluateRemessageToStd(): View = {
    val teacher = getTeacher()
    if (teacher == null) { forward("error.teacher.teaNo.needed") }
    // 页面条件
    val clazzId = getLong("clazzId").get
    val clazz = entityDao.get(classOf[Clazz], clazzId)
    val semesterId = clazz.semester.id
    val stdId = getLong("sendObj").get
    val textEvaluationId = getLong("textEvaluationId").get
    val reMessage = get("reMessage").get
    if (teacher == null) {
      redirect("searchTextEvaluation", "&clazz.id=" + clazzId
        + "&semesterId=" + semesterId, "保存失败,你没有权限!")
    }
    // 创建对象
    val std = entityDao.get(classOf[Student], stdId);
    val textEvaluation = entityDao.get(classOf[TextEvaluation], textEvaluationId);
    val date = new java.util.Date();
    val teacherRemessage = new TeacherRemessage();
    teacherRemessage.textEvaluation = textEvaluation
    teacherRemessage.students += std
    teacherRemessage.visible = true
    teacherRemessage.createdAt = Instant.now
    teacherRemessage.updatedAt = Instant.now
    reMessage.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br/>").replaceAll("\n", "<br>").replaceAll("\\'", "&acute;").replaceAll("\"", "&quot;").replaceAll(" ", "&nbsp;")
    teacherRemessage.remessage = reMessage
    try {
      entityDao.saveOrUpdate(teacherRemessage);
      redirect("searchTextEvaluation", "&clazz.id=" + clazzId + "&semesterId=" + semesterId, "info.save.success");
    } catch {
      case e: Exception =>
        e.printStackTrace();
        redirect("searchTextEvaluation", "&clazz.id=" + clazzId + "&semesterId=" + semesterId, "info.save.failure");
    }
  }

  def saveEvaluateRemessageToClass(): View = {
    val teacher = getTeacher()
    if (teacher == null) { forward("error.teacher.teaNo.needed") }
    // 页面条件
    val clazzId = getLong("clazzId").get
    val clazz = entityDao.get(classOf[Clazz], clazzId)
    val semesterId = clazz.semester.id
    val classNames = get("classNames").get
    val stdId = getLong("stdId");
    val textEvaluationId = getLong("textEvaluationId").get
    val isAnn = getBoolean("isAnn").get
    val reMessage = get("reMessage").get
    if (teacher == null) {
      redirect("searchTextEvaluation", "&clazz.id=" + clazzId + "&semesterId=" + semesterId, "保存失败,你没有权限!");
    }
    // 查询(班级)
    //    val hql = "select courseTake"+ " from org.openurp.edu.teach.clazz.model.Clazz clazz"+ " join clazz.teachers teacher join clazz.teachclass.courseTakers courseTake"+ " where teacher =:teacher and clazz.semester.id =:semesterId and clazz.teachclass.name in (:classNames)";
    val query = OqlBuilder.from[CourseTaker](classOf[Clazz].getName, "clazz")
    query.join("left", "clazz.teachers", "teacher")
    query.join("left", "clazz.teachclass.courseTakers", "courseTaker")
    query.select("courseTaker")
    query.where("teacher=:teacher", teacher);
    query.where("clazz.semester.id = :semesterId", semesterId);
    query.where("clazz.teachclass.name in (:classNames)", classNames.split(','))
    val courseTakers = entityDao.search(query);
    if (courseTakers.isEmpty) { redirect("searchTextEvaluation", "保存失败,班级为空!", "&clazz.id=" + clazzId + "&semesterId=" + semesterId); }
    // 创建对象
    val textEvaluation = entityDao.get(classOf[TextEvaluation], textEvaluationId);
    val date = new java.util.Date();
    val teacherRemessage = new TeacherRemessage();
    teacherRemessage.textEvaluation = textEvaluation
    courseTakers foreach { courseTaker =>
      teacherRemessage.students += courseTaker.std
    }
    //    for (CourseTaker courseTake : courseTakers) {
    //      evaluateTeacherRemessage.addStudent(courseTaker.getStd());
    //    }
    if (!isAnn) {
      val std = entityDao.get(classOf[Student], stdId.get);
      teacherRemessage.students += std
    }
    teacherRemessage.visible = (!isAnn)
    teacherRemessage.createdAt = Instant.now
    teacherRemessage.updatedAt = Instant.now
    reMessage.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br/>").replaceAll("\n", "<br>").replaceAll("\\'", "&acute;").replaceAll("\"", "&quot;").replaceAll(" ", "&nbsp;")
    teacherRemessage.remessage = reMessage
    try {
      entityDao.saveOrUpdate(teacherRemessage);
      return redirect("searchTextEvaluation", "&clazz.id=" + clazzId + "&semesterId=" + semesterId, "info.save.success");
    } catch {
      case e: Exception =>
        e.printStackTrace();
        return redirect("searchTextEvaluation", "&clazz.id=" + clazzId + "&semesterId=" + semesterId, "info.save.failure");
    }
  }

  def showAnn(): View = {
    var semesterId = getInt("semesterId").getOrElse(0)
    if (0 == semesterId) {
      semesterId = getInt("semester.id").get
    }
    put("clazzId", getLong("clazzId"));
    put("semesterId", semesterId);
    put("semester", entityDao.get(classOf[Semester], semesterId));
    forward()
  }

  def listAnn(): View = {
    val teacher = getTeacher()
    if (teacher == null) { forward("error.teacher.teaNo.needed") }
    val clazzId = getLong("clazzId").get
    val semesterId = entityDao.get(classOf[Clazz], clazzId).semester.id
    val query = OqlBuilder.from(classOf[TeacherRemessage], "teacherRemessage");
    query.where("teacherRemessage.visible = false");
    query.where("teacherRemessage.textEvaluation.teacher =:teacher", teacher);
    query.where("teacherRemessage.textEvaluation.clazz.semester.id =:semesterId", semesterId);
    query.orderBy("teacherRemessage.createdAt desc").limit(getPageLimit)
    put("teacherRemessages", entityDao.search(query));
    forward()
  }
}
