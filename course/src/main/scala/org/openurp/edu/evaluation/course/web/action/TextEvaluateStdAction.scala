package org.openurp.edu.evaluation.course.web.action

import scala.collection.mutable.Buffer

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Semester
import org.openurp.edu.base.model.Student
import org.openurp.edu.evaluation.course.model.TeacherRemessage
import org.openurp.edu.evaluation.course.model.TextEvaluateSwitch
import org.openurp.edu.evaluation.course.model.TextEvaluation
import org.openurp.edu.lesson.model.CourseTake
import org.openurp.edu.lesson.model.Lesson
import org.openurp.hr.base.model.Staff

class TextEvaluateStdAction extends RestfulAction[TextEvaluation] {

  def getOtherMap(std: Student, semester: Semester, teachers: Seq[Staff]): collection.Map[Long, Buffer[TeacherRemessage]] = {
    val query = OqlBuilder.from(classOf[TeacherRemessage], "teacherRemessage");
    query.join("left", "teacherRemessage.students", "student");
    query.join("left", "teacherRemessage.textEvaluation", "textEvaluation");
    query.where("student =:std", std);
    query.where("textEvaluation.lesson.semester =:semester", semester);
    query.where("textEvaluation.student !=:std", std);
    query.where("teacherRemessage.visible = true");
    query.orderBy("teacherRemessage.createdAt desc");
    val otherMap = Collections.newMap[Long, Buffer[TeacherRemessage]]
    val results = entityDao.search(query)
    results foreach { teacherRemessage =>
      otherMap.getOrElseUpdate(teacherRemessage.textEvaluation.staff.id, Collections.newBuffer[TeacherRemessage]) += teacherRemessage
    }
    //    for (EvaluateTeacherRemessage evaluateTeacherRemessage : entityDao.search(query)) {
    //      List<EvaluateTeacherRemessage> evaluateTeacherRemessages = otherMap.get(evaluateTeacherRemessage
    //          .getTextEvaluation().getTeacher().getId());
    //      if (null == evaluateTeacherRemessages) {
    //        evaluateTeacherRemessages = CollectUtils.newArrayList();
    //      }
    //      evaluateTeacherRemessages.add(evaluateTeacherRemessage);
    //      otherMap.put(evaluateTeacherRemessage.getTextEvaluation().getTeacher().getId(),
    //          evaluateTeacherRemessages);
    //    }
    for (teacher <- teachers) {
      if (!otherMap.contains(teacher.id)) {
        otherMap.put(teacher.id, null);
      }
    }
    otherMap
  }

  def getMyTextEvaluationMap(std: Student, semester: Semester, teachers: Seq[Staff]): collection.Map[Long, Buffer[TextEvaluation]] = {
    val query = OqlBuilder.from(classOf[TextEvaluation], "textEvaluation");
    query.where("textEvaluation.student =:std", std);
    query.where("textEvaluation.lesson.semester =:semester", semester);
    query.where("textEvaluation.state = true");
    val textEvaluateMap = Collections.newMap[Long, Buffer[TextEvaluation]]
    val results = entityDao.search(query)
    results foreach { textEvaluation =>
      textEvaluateMap.getOrElseUpdate(textEvaluation.staff.id, Collections.newBuffer[TextEvaluation]) += textEvaluation
    }
    for (teacher <- teachers) {
      if (!textEvaluateMap.contains(teacher.id)) {
        textEvaluateMap.put(teacher.id, null)
      }
    }
    textEvaluateMap
  }

  def getAnnMap(std: Student, semester: Semester, teachers: Seq[Staff]): collection.Map[Long, Buffer[TeacherRemessage]] = {
    val query = OqlBuilder.from(classOf[TeacherRemessage], "teacherRemessage")
    query.join("left", "teacherRemessage.students", "student")
    query.join("left", "teacherRemessage.textEvaluation", "textEvaluation")
    query.where("student =:std", std)
    query.where("textEvaluation.lesson.semester = :semester", semester)
    query.where("teacherRemessage.visible = false")
    val annMap = Collections.newMap[Long, Buffer[TeacherRemessage]]
    val results = entityDao.search(query)
    results foreach { teacherRemessage =>
      //      val evaluateTeacherRemessages =
      annMap.getOrElseUpdate(teacherRemessage.textEvaluation.staff.id, Collections.newBuffer[TeacherRemessage]) +=
        teacherRemessage
      //      var evaluateTeacherRemessages:Buffer[EvaluateTeacherRemessage] = annMap.get(evaluateTeacherRemessage.textEvaluation.staff.id).orNull
      //      if (evaluateTeacherRemessages == null) {
      //       evaluateTeacherRemessages = Collections.newBuffer[EvaluateTeacherRemessage]
      //       annMap.put(evaluateTeacherRemessage.textEvaluation.staff.id, evaluateTeacherRemessages)
      //      }
      //       evaluateTeacherRemessages += evaluateTeacherRemessage

    }
    for (teacher <- teachers) {
      if (!annMap.contains(teacher.id)) {
        annMap.put(teacher.id, null);
      }
    }
    annMap
  }
    def getTeachersByLessonIdSeq(lessonIdSeq: List[Long]):Seq[Staff] = {
    val query = OqlBuilder.from[Staff]( classOf[Lesson].getName + " lesson")
    query.join("lesson.teachers","staff");
    query.select("staff")
    query.where("lesson.id in (:lessonIdSeq)",lessonIdSeq)
    entityDao.search(query)
  }
  
  def getTeacherLessonByLessonIdSeq(lessonIdSeq: List[Long]):Seq[Array[Any]] = {
    val query = OqlBuilder.from[Array[Any]]( classOf[Lesson].getName + " lesson")
    query.join("lesson.teachers","staff");
    query.select("staff,lesson")
    query.where("lesson.id in (:lessonIdSeq)",lessonIdSeq)
    entityDao.search(query)
  }

  def getTextEvaluationList(student: Student, lesson: Lesson, teacher: Staff): Seq[TextEvaluation] = {
    val query = OqlBuilder.from(classOf[TextEvaluation], "textEvaluation")
    query.where("textEvaluation.student =:student", student);
    query.where("textEvaluation.lesson =:lesson", lesson);
    query.where("textEvaluation.staff =:teacher", teacher);
    entityDao.search(query)
  }

  def getSwitch(): TextEvaluateSwitch = {
    val iterator: Iterator[TextEvaluateSwitch] = entityDao.getAll(classOf[TextEvaluateSwitch]).iterator
    if (iterator.hasNext)
      iterator.next()
    else new TextEvaluateSwitch()
  }
  def getLessonIdAndTeacherIdOfResult(student: Student, semester: Semester): collection.Map[String, String] = {
    val query = OqlBuilder.from(classOf[TextEvaluation], "textEvaluation")
    //    query.select("textEvaluation.lesson.id,textEvaluation.staff.id")
    query.where("textEvaluation.student = :student ", student)
    query.where("textEvaluation.lesson.semester = :semester", semester)
    val a = entityDao.search(query)
    a.map(obj => (obj.lesson.id + "_" + (if (null == obj.staff) "0" else obj.staff.id), "1")).toMap
  }

  def getStdLessons(student: Student, semester: Semester): Seq[Lesson] = {

    val query = OqlBuilder.from(classOf[CourseTake], "courseTake")
    query.select("distinct courseTake.lesson.id ")
    query.where("courseTake.std=:std", student)
    query.where("courseTake.semester =:semester", semester)
    val lessonIds = entityDao.search(query)
    var stdLessons: Seq[Lesson] = Seq()
    if (!lessonIds.isEmpty) {
      val entityquery = OqlBuilder.from(classOf[Lesson], "lesson").where("lesson.id in (:lessonIds)", lessonIds)
      stdLessons = entityDao.search(entityquery)
    }
    stdLessons
  }
  
    override protected def indexSetting(): Unit = {
    val std = entityDao.get(classOf[Student], 68285L)
    if (std == null) { forward("error.std.stdNo.needed") }
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
//    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
//    put("currentSemester", entityDao.search(semesterQuery).head)
  }

  override def search(): String = {
    val std = entityDao.get(classOf[Student], 68285L)
    // 页面条件
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val lessons = getStdLessons(std, semester);
    // 获得(我的课程)

    if (lessons.isEmpty) {
      addMessage("对不起,没有评教课程!");
      forward("errors");
    }
    var myCourses = Collections.newBuffer[Lesson]
    lessons foreach { lesson =>
      if (!lesson.teachers.isEmpty) {
        myCourses += lesson
      }
    }
    put("lessons", myCourses);
    // 获得(文字评教-已经评教)
    put("evaluateMap", getLessonIdAndTeacherIdOfResult(std, semester));
    forward()
  }

  def loadTextEvaluate(): String = {
    val evaluateId = get("evaluateId").get
    val evaluateState = get("evaluateState").get
    val ids = get("evaluateId").get.split(",")
    // 获得(教学任务)
    val lesson = entityDao.get(classOf[Lesson], ids(0).toLong)
    if (null == lesson) {
      addMessage("找不到该课程!");
      return forward("errors");
    }
    val textEvaluationSwitch = getSwitch()
    if (null == textEvaluationSwitch) {
      //      || !textEvaluationSwitch.isTextEvaluateOpened()) {
      addMessage("现在还没有开放文字评教!");
      forward("errors");
    }
    // 获得(教师)
    val teacher = entityDao.get(classOf[Staff], ids(1).toLong)
    if (null == teacher) {
      addMessage("该课程没有指定任课教师!");
      forward("errors");
    }
    // 判断(是否更新)
    if ("update".equals(evaluateState)) {
      val std = entityDao.get(classOf[Student], 68285L)
      val textEvaluations = getTextEvaluationList(std, lesson, teacher)
      put("textEvaluations", textEvaluations)
    }
    put("teacher", teacher);
    put("lesson", lesson);
    put("evaluateState", evaluateState);
    forward();
  }

  def saveTextEvaluate(): View = {
    val std = entityDao.get(classOf[Student], 68285L)
    val LessonId = longId("lesson")
    val teacherId = getLong("teacherId")
    val lesson = entityDao.get(classOf[Lesson], LessonId)
    val teacher = entityDao.get(classOf[Staff], teacherId.get)
    val textOpinion = get("textOpinion").get.toString()
    val evaluateByTeacher = getBoolean("evaluateByTeacher").get
    try {
      if (!textOpinion.isEmpty) {
        val textEvaluation = new TextEvaluation()
        textEvaluation.student = std
        textEvaluation.lesson = lesson
        textEvaluation.staff = teacher
        textEvaluation.content = textOpinion
        textEvaluation.evaluateByTeacher = evaluateByTeacher
        textEvaluation.evaluateAt = new java.util.Date()
        entityDao.saveOrUpdate(textEvaluation)
      }
      redirect("search","&semester.id=" + lesson.semester.id, "info.save.success")
    } catch {
      case e: Exception =>
        redirect("search", "&semester.id=" + lesson.semester.id,"info.save.failure")
    }
  }

  def remsgList(): String = {
    val std = entityDao.get(classOf[Student], 68285L)
    val ids = longIds("lesson")
    val teachers = getTeachersByLessonIdSeq(ids);
    val lessons= getTeacherLessonByLessonIdSeq(ids)
    val lesson = entityDao.get(classOf[Lesson],ids(0))
    val semester = lesson.semester

    put("lessons", lessons);
    // 获得(教师公告)
    put("annMap", getAnnMap(std, semester, teachers));
    // 获得(评教回复-本人)
    put("textEvaluationMap", getMyTextEvaluationMap(std, semester, teachers));
    // 获得(评教回复-其他)
    put("otherMap", getOtherMap(std, semester, teachers));
    forward();
  }
}
