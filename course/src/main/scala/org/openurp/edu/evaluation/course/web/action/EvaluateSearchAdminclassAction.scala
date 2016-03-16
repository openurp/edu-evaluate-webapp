package org.openurp.edu.evaluation.course.web.action

import java.text.DecimalFormat
import org.beangle.commons.collection.Collections
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.base.model.Semester
import org.openurp.edu.base.model.Adminclass
import org.openurp.edu.base.model.Student
import org.openurp.edu.evaluation.course.model.EvaluateSearchAdminclass
import org.openurp.edu.evaluation.course.model.EvaluateSearchDepartment
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.edu.lesson.model.CourseTake
import org.openurp.edu.evaluation.lesson.model.QuestionnaireLesson

class EvaluateSearchAdminclassAction extends RestfulAction[EvaluateResult] {

  override def  index():String= {
//    getSemester();
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)
    put("teachDeparts", entityDao.search(OqlBuilder.from(classOf[Department],"dep").where("dep.teaching=:tea",true)))
    forward()
  }

  /**
   * 班级评教情况
   */
  override def  search():String= {
//    val semesterId = getInt("semester.id").get
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val departmentId = getInt("department.id").get
    val className = get("adminclass.name").get
    val semester = entityDao.get(classOf[Semester], semesterId);
    val department = entityDao.get(classOf[Department], departmentId);
    val date = new java.util.Date();
    // 得到院系下的所有教学班
    val adminClassQuery = OqlBuilder.from(classOf[Adminclass], "adminClass");
    adminClassQuery.where("adminClass.department.id=:depIds", departmentId);
    adminClassQuery.where("adminClass.beginOn<=:efDate", date);
    adminClassQuery.where("adminClass.project.id =:project", 1);
    adminClassQuery.where("adminClass.endOn is null or adminClass.endOn>=:endOn", date);
    if (Strings.isNotBlank(className)) {
      adminClassQuery.where("adminClass.name like :className", "%" + className + "%");
    }
    val adminClassList = entityDao.search(adminClassQuery);
    val evaluateSearchDepartmentList = Collections.newBuffer[EvaluateSearchDepartment]
    adminClassList foreach { adminClass =>
      val query = OqlBuilder.from[Long](classOf[CourseTake].getName, "courseTake");
      query.select("select count(*)");
      query.where("courseTake.lesson.semester =:semester", semester);
      query.where("courseTake.std.state.department.id=:depIds", departmentId);
      query.where("exists( from "+classOf[QuestionnaireLesson].getName +" questionnaireLesson where questionnaireLesson.lesson=courseTake.lesson)");
      query.where("courseTake.std.state.adminclass =:adminClass)", adminClass);
//      query.orderBy(Order.parse(get("orderBy")));
      val list = entityDao.search(query);
      // 得到指定学期，院系的学生评教人次总数
      val countAll1 = list(0);
      val countAll = countAll1.intValue();

      val query1 = OqlBuilder.from[Long](classOf[EvaluateResult].getName, "rs");
      query1.select("select count(*)");
      query1.where("rs.lesson.semester =:semester", semester);
      query1.where("rs.student.state.department in(:departments)", department);
      query1.where("rs.student.state.adminclass =:adminClass)", adminClass);
//      query1.orderBy(Order.parse(get("orderBy")));
      val list1 = entityDao.search(query1);
      // 得到指定学期，已经评教的学生人次数
      val haveFinish1 = list1(0);
      val haveFinish = haveFinish1.intValue();
      var finishRate = "";
      if (countAll != 0) {
        val finishRate1 =  (haveFinish * 100 / countAll)
        val df = new DecimalFormat("0.0");
        finishRate = df.format(finishRate1) + "%";
      }
      val esd = new EvaluateSearchDepartment();
      esd.semester=semester
      esd.adminclass=adminClass
      esd.countAll=countAll
      esd.haveFinish=haveFinish
      esd.finishRate=finishRate
      evaluateSearchDepartmentList += esd
    
    }
    // Collections.sort(evaluateSearchDepartmentList, new PropertyComparator("adminClass.code"));
    evaluateSearchDepartmentList.sortWith((x,y)=> x.adminclass.code<y.adminclass.code)
    put("evaluateSearchDepartmentList", evaluateSearchDepartmentList);
    put("semester", semester);
    forward();
  }

  /**
   * 学生评教情况
   */
  def  adminClassSearch():String= {
    val semesterId = getInt("semester.id").get
    val date = new java.util.Date();
    var semester:Semester = null;
    if (semesterId != 0) {
      semester =  entityDao.get(classOf[Semester], semesterId);
    }
    // 根据班级代码得到班级
    val adminClassId = getLong("adminclass.id").get
    var adminClass:Adminclass = null;
    if (adminClassId != 0L) {
      adminClass =  entityDao.get(classOf[Adminclass], adminClassId);
    }
    // 得到班级所有的学生
    val  studentQuery = OqlBuilder.from(classOf[Student], "student");
     studentQuery.where("student.state.adminclass=:adminClass", adminClass);
//    studentQuery.where("student.enrollOn<=:enON", date);
//    studentQuery.where("student.graduateOn>=:geON", date);
//    studentQuery.where("student.project.id =:project", 1);
    // studentQuery.where("student.inSchool is true");
    val studentList = entityDao.search(studentQuery);

    val evaluateSearchAdminClassList = Collections.newBuffer[EvaluateSearchAdminclass]
    if (studentList.size > 0) {
      // 得到指定学期，院系的学生评教人次总数
      val query = OqlBuilder.from[Array[Any]](classOf[CourseTake].getName, "courseTake");
      query.select("select courseTake.std.id,count(*)");
      query.where("courseTake.lesson.semester.id=:semesterId", semesterId);
      if (studentList.size > 0) {
        query.where("courseTake.std in (:students)", studentList);
      }
      query.where("exists ( from "+classOf[QuestionnaireLesson].getName +" questionn where questionn.lesson = courseTake.lesson)");
      query.groupBy("courseTake.std.id");
      val list = entityDao.search(query);
      // 得到指定学期，已经评教的学生人次数
      val query1 = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName, "rs");
      query1.select("select rs.student.id,count(*)");
      query1.where("rs.lesson.semester.id=:semesterId", semesterId);
      if (studentList.size > 0) {
        query1.where("rs.student in (:students)", studentList);
      }
      query1.groupBy("rs.student.id");
      val list1 = entityDao.search(query1);
      studentList foreach {student =>
        // 得到指定学期，院系的学生评教人次总数
        var countAll = 0;
        list foreach { ob =>
          if (ob(0).equals(student.id)) {
            countAll = ob(1).toString().toInt
          }
        }
        // 得到指定学期，已经评教的学生人次数
        var haveFinish = 0;
        list1 foreach { ob2 =>
          if (ob2(0).equals(student.id)) {
            haveFinish = Integer.valueOf(ob2(1).toString());
          }
        }
        var finishRate = 0f;
        if (countAll != 0) {
          finishRate = (haveFinish.toString().toFloat / countAll.toString().toFloat) * 100f;
        }
        val esc = new EvaluateSearchAdminclass();
        esc.semester=semester
        esc.student=student
        esc.countAll=countAll
        esc.haveFinish=haveFinish
        esc.finishRate=finishRate.toString()
        evaluateSearchAdminClassList +=esc
      }
//      Collections.sort(evaluateSearchAdminClassList, new PropertyComparator("finishRate"));
      evaluateSearchAdminClassList.sortWith((x,y)=>x.finishRate<y.finishRate)
    }
    put("semester", semester);
    put("evaluateSearchAdminClassList", evaluateSearchAdminClassList);
    return forward();
  }

  /**
   * 学生评教详情
   */
  def  info():String= {
    val semesterId = getInt("semester.id").get
    val stuId = getLong("stuIds").get
    val semester =entityDao.get(classOf[Semester], semesterId);
    // 得到指定学期，院系的学生评教人次总数
    val query = OqlBuilder.from(classOf[CourseTake], "courseTake");
    query.where("courseTake.lesson.semester =:semester", semester);
    query.where("courseTake.std.id =:stdId", 68285L);
    query.where("exists ( from "+classOf[QuestionnaireLesson].getName +" questionn where questionn.lesson = courseTake.lesson)");
    val list = entityDao.search(query);
    // 得到指定学期，已经评教的学生人次数
    val query1 = OqlBuilder.from(classOf[EvaluateResult], "rs");
    query1.where("rs.lesson.semester =:semester", semester);
    query1.where("rs.student.id =:stuId", stuId);
    val list1 = entityDao.search(query1);
    put("questionnaireTaskList", list);
    put("evaluateResultList", list1);
    return forward();
  }

}