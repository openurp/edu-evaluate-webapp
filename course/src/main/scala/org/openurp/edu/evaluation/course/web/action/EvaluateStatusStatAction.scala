package org.openurp.edu.evaluation.course.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Semester
import java.util.ArrayList
import org.openurp.base.model.Semester
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.base.model.Semester
import org.openurp.base.model.Semester
import org.openurp.base.model.Department
import org.openurp.edu.base.model.Adminclass
import org.openurp.edu.evaluation.course.model.EvaluateSearchDepartment
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.base.code.model.StdType
import org.openurp.base.model.Semester
import org.beangle.commons.collection.Collections
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.course.model.EvaluateSearchManager
import org.openurp.edu.lesson.model.CourseTake
import java.text.DecimalFormat
import org.openurp.edu.evaluation.lesson.model.QuestionnaireLesson

class EvaluateStatusStatAction extends RestfulAction[EvaluateResult] {


  override def  index():String = {
    put("stdTypeList", entityDao.getAll(classOf[StdType]))
    put("departmentList", entityDao.search(OqlBuilder.from(classOf[Department],"dep").where("dep.teaching =:tea",true)));
    put("semester",entityDao.get(classOf[Semester],20141))
    put("departments", entityDao.search(OqlBuilder.from(classOf[Department],"dep").where("dep.teaching =:tea",true)))
    forward()
  }

  override def  search():String= {
    val departmentId = getInt("department.id").get
    val semesterId = getInt("semester.id").get
    if (departmentId != 0) {
      searchDep(semesterId, departmentId);
      return forward("searchDep");
    } else {
      searchSchool(semesterId);
    }
    forward();
  }

  def searchDep(semesterId:Int, departmentId:Int) ={
    val semester = entityDao.get(classOf[Semester], semesterId);
    val department = entityDao.get(classOf[Department], departmentId);
    val date = new java.util.Date();
    // 得到院系下的所有教学班
    val adminClassQuery = OqlBuilder.from(classOf[Adminclass], "adminClass");
    adminClassQuery.where("adminClass.department.id=:depIds", departmentId);
    adminClassQuery.where("adminClass.beginOn<=:efDate", date);
    adminClassQuery.where("adminClass.endOn is null or adminClass.endOn>=:endOn", date);
//    adminClassQuery.where("adminClass.project =:project", 1);
    val adminClassList = entityDao.search(adminClassQuery);
    val evaluateSearchDepartmentList = Collections.newBuffer[EvaluateSearchDepartment]
    adminClassList foreach { adminClass =>
      val query = OqlBuilder.from[Long](classOf[CourseTake].getName, "courseTake");
      query.select("select count(*)");
      query.where("courseTake.lesson.semester =:semester", semester);
      query.where("courseTake.std.state.department.id=:depIds", departmentId);
      query.where("exists( from "+classOf[QuestionnaireLesson].getName +" questionnaireLesson where questionnaireLesson.lesson=courseTake.lesson)");
      query.where("courseTake.std.state.adminclass =:adminClass)", adminClass);
      query.where("courseTake.std.state.adminclass is not null")
//      query.orderBy(Order.parse(get("orderBy")));
      val list = entityDao.search(query);
      // 得到指定学期，院系的学生评教人次总数
      val countAll1 = list(0)
      val countAll = countAll1.intValue();

      val query1 = OqlBuilder.from[Long](classOf[EvaluateResult].getName, "rs");
      query1.select("select count(*)");
      query1.where("rs.lesson.semester =:semester", semester);
      query1.where("rs.student.state.department in(:departments)", department);
      query1.where("rs.student.state.adminclass =:adminClass)", adminClass);
//      query1.orderBy(Order.parse(get("orderBy")));
      val list1 = entityDao.search(query1);
      // 得到指定学期，已经评教的学生人次数
      val haveFinish1 =  list1(0)
      val haveFinish = haveFinish1.intValue();
      var finishRate = "";
      if (countAll != 0) {
        var finishRate1 = (haveFinish * 100 / countAll).toFloat
        val df = new DecimalFormat("0.0");
        finishRate = df.format(finishRate1)+"%"
      }
      val esd = new EvaluateSearchDepartment();
      esd.semester=semester
      esd.adminclass=adminClass
      esd.countAll=countAll
      esd.haveFinish=haveFinish
      esd.finishRate=finishRate
      evaluateSearchDepartmentList +=esd
    
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
      
      val query = OqlBuilder.from[Long](classOf[CourseTake].getName, "courseTake");
      query.select("select count(*)");
      query.where("courseTake.lesson.semester =:semester", semester);
      query.where("courseTake.std.state.department =:manageDepartment", department);
      query.where("exists( from "+classOf[QuestionnaireLesson].getName +" questionnaireLesson where questionnaireLesson.lesson=courseTake.lesson)");
      val list = entityDao.search(query);
      // 得到指定学期，院系的学生评教人次总数
      val countAll1 =  list(0)
      val countAll = countAll1.intValue();

      val query1 = OqlBuilder.from[Long](classOf[EvaluateResult].getName, "rs");
      query1.select("select count(*)");
      query1.where("rs.lesson.semester =:semester", semester);
      query1.where("rs.student.state.department =:manageDepartment", department);
      val list1 = entityDao.search(query1);
      // 得到指定学期，已经评教的学生人次数
      val haveFinish1 = list1(0)
      val haveFinish = haveFinish1.intValue();
      var finishRate = "";
      if (countAll != 0) {
        var finishRate1 =  (haveFinish * 100 / countAll).toFloat
        val df = new DecimalFormat("0.0");
        finishRate = df.format(finishRate1) + "%";
      }
      val esm = new EvaluateSearchManager();
      esm.semester=semester
      esm.department=department
      esm.countAll=countAll
      esm.haveFinish=haveFinish
      esm.finishRate=finishRate
      evaluateSearchManagerList +=esm
    
    }
    put("evaluateSearchManagerList", evaluateSearchManagerList);
  }


}