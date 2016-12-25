package org.openurp.edu.evaluation.course.web.action

import java.text.DecimalFormat
import org.beangle.commons.collection.Collections
import org.beangle.commons.dao.OqlBuilder
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.base.model.Semester
import org.openurp.base.model.Semester
import org.openurp.base.model.Semester
import org.openurp.base.model.Semester
import org.openurp.base.model.Semester
import org.openurp.base.model.Semester
import org.openurp.base.model.Semester
import org.openurp.edu.base.code.model.StdType
import org.openurp.edu.base.model.Adminclass
import org.openurp.edu.evaluation.app.lesson.model.EvaluateSearchDepartment
import org.openurp.edu.evaluation.app.lesson.model.EvaluateSearchManager
import org.openurp.edu.evaluation.lesson.model.QuestionnaireLesson
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.edu.lesson.model.CourseTaker
import org.openurp.edu.lesson.model.Lesson
import org.beangle.commons.lang.Strings

class EvaluateStatusStatAction extends RestfulAction[EvaluateResult] {


  override def  index():String = {
    put("stdTypeList", entityDao.getAll(classOf[StdType]))
    put("departmentList", entityDao.search(OqlBuilder.from(classOf[Department],"dep").where("dep.teaching =:tea",true)));
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)
    put("departments", entityDao.search(OqlBuilder.from(classOf[Department],"dep").where("dep.teaching =:tea",true)))
    forward()
  }

  override def  search():String= {
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val departmentId = getInt("department.id").getOrElse(0)
    val lessonNo=get("lesson.no").getOrElse("")
    val courseCode=get("course.code").getOrElse("")
    val courseName=get("course.name").getOrElse("")
    val teacherName=get("teacher.name").getOrElse("")
    if (departmentId == 0 && lessonNo=="" && courseCode=="" && courseName=="" && teacherName=="") {
      searchSchool(semesterId);
    } else {
      searchDep(semesterId);
      return forward("searchDep");
    }
    forward();
  }
//按照开课院系、教学任务查看回收率
  def searchDep(semesterId:Int) ={
    val semester = entityDao.get(classOf[Semester], semesterId);
    val departmentId = getInt("department.id").getOrElse(0)
//      entityDao.get(classOf[Department], departmentId);
    val lessonNo=get("lesson.no").get
    val courseCode=get("course.code").get
    val courseName=get("course.name").get
    val teacherName=get("teacher.name").get
    // 得到院系下的所有教学任务
    val lessonQuery = OqlBuilder.from(classOf[Lesson], "lesson");
    lessonQuery.where("lesson.semester =:semester", semester);
    if (departmentId!=0){
    lessonQuery.where("lesson.teachDepart.id=:depIds", departmentId);
    }
    if (Strings.isNotBlank(lessonNo)) {
      lessonQuery.where("lesson.no =:lessonNo", lessonNo);
    }
    if (Strings.isNotBlank(courseCode)) {
      lessonQuery.where("lesson.course.code =:courseCode", courseCode);
    }
    if (Strings.isNotBlank(courseName)) {
      lessonQuery.where("lesson.course.name like :courseName", "%" + courseName + "%");
    }
   if (Strings.isNotBlank(teacherName)) {
     lessonQuery.join("lesson.teachers","teacher")
     lessonQuery.where("teacher.person.name.formatedName like :teacherName", "%" + teacherName + "%");
    }
    val lessonList = entityDao.search(lessonQuery);
    val evaluateSearchDepartmentList = Collections.newBuffer[EvaluateSearchDepartment]
    lessonList foreach { lesson =>
      
      var countAll:Long=0L
      var haveFinish:Long=0L 
      var stdFinish:Long=0L
      val query = OqlBuilder.from[Long](classOf[CourseTaker].getName, "courseTake");
      query.select("count(*)");
      query.where("courseTake.lesson =:lesson", lesson);
//      query.where("courseTake.lesson.teachDepart.id=:depIds", departmentId);
//      query.where("exists( from "+classOf[QuestionnaireLesson].getName +" questionnaireLesson where questionnaireLesson.lesson=courseTake.lesson)");
//      query.orderBy(Order.parse(get("orderBy")));
      val list = entityDao.search(query);
      // 得到指定学期，院系的学生评教人次总数
      countAll = list(0)

      val query1 = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName, "rs");
      query1.select("count(*),count(distinct student_id)");
      query1.where("rs.lesson =:lesson", lesson);
//      query1.where("rs.student.state.department in(:departments)", department);
//      query1.where("rs.student.state.adminclass =:adminClass)", adminClass);
//      query1.orderBy(Order.parse(get("orderBy")));
      entityDao.search(query1) foreach {list1 =>
      // 得到指定学期，已经评教的学生人次数
      haveFinish =  list1(0).asInstanceOf[Long]
      stdFinish = list1(1).asInstanceOf[Long]
      }
      var finishRate = "";
      if (countAll != 0) {
        val df = new DecimalFormat("0.0");
        finishRate = df.format( (haveFinish * 100 / countAll).toFloat)+"%"
      }
      if (finishRate!=""){
      val esd = new EvaluateSearchDepartment();
      esd.semester=semester
      esd.lesson=lesson
      esd.countAll=countAll
      esd.haveFinish=haveFinish
      esd.stdFinish=stdFinish
      esd.finishRate=finishRate
      evaluateSearchDepartmentList +=esd
      }
    }
//    
    // Collections.sort(evaluateSearchDepartmentList, new PropertyComparator("adminClass.code"));
    put("evaluateSearchDepartmentList", evaluateSearchDepartmentList);
  }

  def searchSchool(semesterId:Int) = {
    val evaluateSearchManagerList =Collections.newBuffer[EvaluateSearchManager]
    val semester = entityDao.get(classOf[Semester], semesterId);
    val departQuery = OqlBuilder.from(classOf[Department], "department");
    departQuery.where("department.teaching =:teaching", true);
    // departQuery.where("department.level =:lever", 1);
    // departQuery.where("department.enabled =:enabled", true);
    val departmentList = entityDao.search(departQuery);
    
    departmentList foreach {department =>
      var countAll:Long =0L
      var stdAll:Long =0L
      var haveFinish:Long =0L
      var stdFinish:Long =0L
      //总评人次
      val query = OqlBuilder.from[Array[Any]](classOf[CourseTaker].getName, "courseTake");
      query.select("count(*),count(distinct std)");
      query.where("courseTake.lesson.semester =:semester", semester);
      query.where("courseTake.lesson.teachDepart =:manageDepartment", department);
//      query.where("exists( from "+classOf[QuestionnaireLesson].getName +" questionnaireLesson where questionnaireLesson.lesson=courseTake.lesson)");
      entityDao.search(query) foreach { list =>
      // 得到指定学期，院系的学生评教人次总数
       countAll =  list(0).asInstanceOf[Long]
       stdAll=list(1).asInstanceOf[Long]
      }
      val query1 = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName, "rs");
      query1.select("count(*),count(distinct student)");
      query1.where("rs.lesson.semester =:semester", semester);
      query1.where("rs.lesson.teachDepart =:manageDepartment", department);
      entityDao.search(query1) foreach {list1=>
      // 得到指定学期，已经评教的学生人次数
       haveFinish = list1(0).asInstanceOf[Long]
       stdFinish=list1(1).asInstanceOf[Long]
      }
      var finishRate = "";
      var stdRate =""
      if (countAll != 0L) {
        val df = new DecimalFormat("0.0");
        finishRate = df.format((haveFinish * 100 / countAll).toFloat) + "%";
        stdRate=df.format((stdFinish*100/stdAll).toFloat)+"%"
      }
      if (finishRate!=""){
      val esm = new EvaluateSearchManager();
      esm.semester=semester
      esm.department=department
      esm.countAll=countAll
      esm.stdAll=stdAll
      esm.haveFinish=haveFinish
      esm.stdFinish=stdFinish
      esm.finishRate=finishRate
      esm.stdRate=stdRate
      evaluateSearchManagerList +=esm
      }
    
    }
    put("evaluateSearchManagerList", evaluateSearchManagerList);
  }


}