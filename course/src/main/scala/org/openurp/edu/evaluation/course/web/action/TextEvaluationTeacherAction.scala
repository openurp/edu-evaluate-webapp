package org.openurp.edu.evaluation.course.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Semester
import org.openurp.base.model.Semester
import org.openurp.edu.base.model.Student
import org.openurp.edu.base.model.Student
import org.openurp.edu.evaluation.course.model.TeacherRemessage
import org.openurp.edu.evaluation.course.model.TextEvaluateSwitch
import org.openurp.edu.evaluation.course.model.TextEvaluation
import org.openurp.edu.evaluation.course.model.TextEvaluation
import org.openurp.edu.lesson.model.CourseTake
import org.openurp.edu.lesson.model.CourseTake
import org.openurp.edu.lesson.model.Lesson
import org.openurp.edu.lesson.model.Teachclass
import org.openurp.hr.base.model.Staff
import org.openurp.platform.api.security.Securities

class TextEvaluationTeacherAction extends RestfulAction[TextEvaluation] {
   
  
  def getStaff(): Staff = {
    val staffs = entityDao.search(OqlBuilder.from(classOf[Staff], "s").where("s.code=:code", Securities.user))
    if (staffs.isEmpty) {
      throw new RuntimeException("Cannot find staff with code " + Securities.user)
    } else {
      staffs.head
    }
  }
    override protected def indexSetting(): Unit = {
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)
  }

  override def  search():String= {
    val teacher = getStaff()
    if (teacher == null) { forward("error.teacher.teaNo.needed") }
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    if (teacher == null) {
      addMessage("对不起,您没有权限!");
      return forward("errors");
    }
    val query = OqlBuilder.from(classOf[Lesson], "lesson")
    query.join("left","lesson.teachers", "teacher")
    query.where("lesson.project.id=:projectId", 1)
    query.where("lesson.semester =:semester",semester)
//    populateConditions(query);
    query.where("teacher=:teacher", teacher)
    query.orderBy("lesson.semester.id").limit(getPageLimit);
    val results=entityDao.search(query)
    put("lessons", results)
    put("semester",semester)
    forward();
  }

  def searchTextEvaluation():String= {
    val teacher = getStaff()
    if (teacher == null) { forward("error.teacher.teaNo.needed") }
    val lessonId = getLong("lesson.id").get
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    val currentSemester=entityDao.search(semesterQuery).head
    // 判断(是否当前学期)
    val lesson =entityDao.get(classOf[Lesson],lessonId)
    val semester = entityDao.get(classOf[Semester],lesson.semester.id)
    var isCurrent = false;
    if (currentSemester.id.longValue() == semester.id.longValue()) {
      isCurrent = true;
    }
    // 获得(文字评教)
    var textEvaluations:Seq[TextEvaluation]= Seq()
    val query = OqlBuilder.from(classOf[TextEvaluation], "textEvaluation");
    query.where("textEvaluation.lesson.id =:lessonId", lessonId);
    query.where("textEvaluation.staff =:teacher", teacher);
    
    val switchQuery =OqlBuilder.from(classOf[TextEvaluateSwitch],"switch").where("switch.semester.id=:semesterId",lesson.semester.id)
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
    val teachClass = OqlBuilder.from(classOf[Lesson],"lesson")
    teachClass.join("left","lesson.teachers","teacher")
    teachClass.where("teacher=:teacher",teacher);
    teachClass.where("lesson.semester =:semester", semester);
    teachClass.select("lesson.teachclass")
    val teachClasses =entityDao.search(teachClass)
    put("isCurrent", isCurrent);
    put("lesson", entityDao.get(classOf[Lesson], lessonId));
    put("teachclasses", teachClasses)
    put("semester",semester)
    forward();
  }
  


  def  saveEvaluateRemessageToStd():View= {
    val teacher = getStaff()
   if (teacher == null) { forward("error.teacher.teaNo.needed") }
    // 页面条件
    val lessonId = getLong("lessonId").get
    val lesson = entityDao.get(classOf[Lesson],lessonId)
    val semesterId= lesson.semester.id
    val stdId = getLong("sendObj").get
    val textEvaluationId = getLong("textEvaluationId").get
    val reMessage = get("reMessage").get
    if (teacher == null) { redirect("searchTextEvaluation", "&lesson.id=" + lessonId
        + "&semesterId=" + semesterId , "保存失败,你没有权限!") }
    // 创建对象
    val std = entityDao.get(classOf[Student], stdId);
    val textEvaluation = entityDao.get(classOf[TextEvaluation], textEvaluationId);
    val date = new java.util.Date();
    val teacherRemessage = new TeacherRemessage();
    teacherRemessage.textEvaluation=textEvaluation
    teacherRemessage.students+=std
    teacherRemessage.visible=true
    teacherRemessage.createdAt=date
    teacherRemessage.updatedAt=date
    reMessage.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br/>").replaceAll("\n", "<br>").replaceAll("\\'", "&acute;").replaceAll("\"", "&quot;").replaceAll(" ", "&nbsp;")
    teacherRemessage.remessage=reMessage
    try {
      entityDao.saveOrUpdate(teacherRemessage);
      redirect("searchTextEvaluation",  "&lesson.id=" + lessonId + "&semesterId=" + semesterId,"info.save.success");
    } catch {
      case e: Exception =>
      e.printStackTrace();
      redirect("searchTextEvaluation",  "&lesson.id=" + lessonId + "&semesterId="+ semesterId,"info.save.failure");
    }
  }

  def  saveEvaluateRemessageToClass():View={
    val teacher = getStaff()
   if (teacher == null) { forward("error.teacher.teaNo.needed") }
    // 页面条件
    val lessonId = getLong("lessonId").get
    val lesson = entityDao.get(classOf[Lesson],lessonId)
    val semesterId = lesson.semester.id
    val classNames = get("classNames").get
    val stdId = getLong("stdId");
    val textEvaluationId = getLong("textEvaluationId").get
    val isAnn = getBoolean("isAnn").get
    val reMessage = get("reMessage").get
    if (teacher == null) { 
      redirect("searchTextEvaluation",  "&lesson.id=" + lessonId + "&semesterId=" + semesterId,"保存失败,你没有权限!"); }
    // 查询(班级)
//    val hql = "select courseTake"+ " from org.openurp.edu.teach.lesson.model.Lesson lesson"+ " join lesson.teachers teacher join lesson.teachclass.courseTakes courseTake"+ " where teacher =:teacher and lesson.semester.id =:semesterId and lesson.teachclass.name in (:classNames)";
    val query = OqlBuilder.from[CourseTake](classOf[Lesson].getName,"lesson")
    query.join("left","lesson.teachers", "teacher")
    query.join("left","lesson.teachclass.courseTakes", "courseTake")
    query.select("lesson.teachclass.courseTakes")
    query.where("teacher=:teacher", teacher);
    query.where("lesson.semester.id = :semesterId", semesterId);
    query.where("lesson.teachclass.name in (:classNames)", classNames.split(','))
    val courseTakes = entityDao.search(query);
    if (courseTakes.isEmpty) {  redirect("searchTextEvaluation", "保存失败,班级为空!", "&lesson.id=" + lessonId + "&semesterId=" + semesterId); }
    // 创建对象
    val textEvaluation = entityDao.get(classOf[TextEvaluation], textEvaluationId);
    val date = new java.util.Date();
    val teacherRemessage = new TeacherRemessage();
    teacherRemessage.textEvaluation=textEvaluation
    courseTakes foreach {courseTake =>
       teacherRemessage.students += courseTake.std
    }
//    for (CourseTake courseTake : courseTakes) {
//      evaluateTeacherRemessage.addStudent(courseTake.getStd());
//    }
    if (!isAnn) {
      val std = entityDao.get(classOf[Student], stdId.get);
      teacherRemessage.students+=std
    }
    teacherRemessage.visible=(!isAnn)
    teacherRemessage.createdAt=date
    teacherRemessage.updatedAt=date
    reMessage.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br/>").replaceAll("\n", "<br>").replaceAll("\\'", "&acute;").replaceAll("\"", "&quot;").replaceAll(" ", "&nbsp;")
    teacherRemessage.remessage=reMessage
    try {
      entityDao.saveOrUpdate(teacherRemessage);
      return redirect("searchTextEvaluation",  "&lesson.id=" + lessonId + "&semesterId="+ semesterId,"info.save.success");
    } catch {
      case e: Exception =>
      e.printStackTrace();
      return redirect("searchTextEvaluation",  "&lesson.id=" + lessonId + "&semesterId=" + semesterId,"info.save.failure");
    }
  }

  def  showAnn():String= {
    var semesterId = getInt("semesterId").getOrElse(0)
    if (0 == semesterId) {
      semesterId = getInt("semester.id").get
    }
    put("lessonId", getLong("lessonId"));
    put("semesterId", semesterId);
    put("semester", entityDao.get(classOf[Semester], semesterId));
    forward()
  }

  def  listAnn():String= {
    val teacher = getStaff()
   if (teacher == null) { forward("error.teacher.teaNo.needed") }
    val lessonId=getLong("lessonId").get
    val semesterId = entityDao.get(classOf[Lesson],lessonId).semester.id
    val query = OqlBuilder.from(classOf[TeacherRemessage],"teacherRemessage");
    query.where("teacherRemessage.visible = false");
    query.where("teacherRemessage.textEvaluation.staff =:teacher", teacher);
    query.where("teacherRemessage.textEvaluation.lesson.semester.id =:semesterId", semesterId);
    query.orderBy("teacherRemessage.createdAt desc").limit(getPageLimit)
    put("teacherRemessages", entityDao.search(query));
    forward();
  }
//
////  def setTextEvaluationSwitchService(TextEvaluationSwitchService textEvaluationSwitchService) {
////    this.textEvaluationSwitchService = textEvaluationSwitchService;
////  }
//
//
}