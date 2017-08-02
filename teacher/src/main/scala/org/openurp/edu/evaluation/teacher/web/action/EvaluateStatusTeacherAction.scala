package org.openurp.edu.evaluation.teacher.web.action

import java.text.DecimalFormat

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Semester
import org.openurp.edu.base.model.Teacher
import org.openurp.edu.evaluation.app.lesson.model.EvaluateSearchDepartment
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.edu.lesson.model.{ CourseTaker, Lesson }
import org.openurp.platform.api.security.Securities
import java.time.LocalDate
import org.beangle.webmvc.api.view.View

class EvaluateStatusTeacherAction extends RestfulAction[EvaluateResult] {

  def getTeacher(): Teacher = {
    val teachers = entityDao.search(OqlBuilder.from(classOf[Teacher], "s").where("s.code=:code", Securities.user))
    if (teachers.isEmpty) {
      throw new RuntimeException("Cannot find teachers with code " + Securities.user)
    } else {
      teachers.head
    }
  }

  override def index(): View = {
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    put("currentSemester", entityDao.search(semesterQuery).head)
    forward()
  }

  /**
   * 教师评教回收情况
   * //
   */
  override def search(): View = {
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId);
    val teacher = getTeacher()
    if (teacher == null) { forward("error.teacher.teaNo.needed") }
    // 得到院系下的所有级教学任务
    val lessonQuery = OqlBuilder.from(classOf[Lesson], "lesson");
    lessonQuery.where("lesson.semester.id=:semesterId", semesterId);
    if (teacher != null) {
      lessonQuery.join("lesson.teachers", "teacher")
      lessonQuery.where("teacher =:teacher", teacher);
    }
    val lessonList = entityDao.search(lessonQuery);
    val evaluateSearchDepartmentList = Collections.newBuffer[EvaluateSearchDepartment]
    lessonList foreach { lesson =>

      var countAll: Long = 0L
      var haveFinish: Long = 0L
      val query = OqlBuilder.from[Long](classOf[CourseTaker].getName, "courseTake");
      query.select("select count(*)");
      query.where("courseTake.lesson =:lesson", lesson);
      val list = entityDao.search(query);
      // 得到指定学期，院系的学生评教人次总数
      countAll = list(0);

      val query1 = OqlBuilder.from[Long](classOf[EvaluateResult].getName, "rs");
      query1.select("select count(*)");
      query1.where("rs.lesson =:lesson", lesson);
      val list1 = entityDao.search(query1);
      // 得到指定学期，已经评教的学生人次数
      haveFinish = list1(0);
      var finishRate = "";
      if (countAll != 0) {
        val df = new DecimalFormat("0.0");
        finishRate = df.format((haveFinish * 100 / countAll).toFloat) + "%";
      }
      val esd = new EvaluateSearchDepartment();
      esd.semester = semester
      esd.lesson = lesson
      esd.countAll = countAll
      esd.haveFinish = haveFinish
      esd.finishRate = finishRate
      evaluateSearchDepartmentList += esd

    }
    // Collections.sort(evaluateSearchDepartmentList, new PropertyComparator("adminClass.code"));
    evaluateSearchDepartmentList.sortWith((x, y) => x.lesson.course.code < y.lesson.course.code)
    put("evaluateSearchDepartmentList", evaluateSearchDepartmentList);
    put("semester", semester);
    forward()
  }

}
