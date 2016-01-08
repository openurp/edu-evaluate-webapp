package org.openurp.edu.evaluation.course.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.course.model.EvaluateSearchDepartment
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.beangle.data.dao.OqlBuilder
import org.openurp.base.model.Department
import org.openurp.base.model.Semester

class EvaluateSearchDepartmentAction extends RestfulAction[EvaluateResult] {

  override def  index():String= {
//    getSemester();
    val semester = entityDao.get(classOf[Semester],20141)
    put("semesters",semester)
    put("departments", entityDao.search(OqlBuilder.from(classOf[Department],"dep").where("dep.teaching=:tea",true)))
    forward()
  }
  // public String index() {
  // setSemesterDataRealm(hasStdTypeCollege);
  // put("departments", getTeachDeparts());
  // put("semesters", entityDao.search(OqlBuilder.from("from Semester")));
  // return forward();
  // }
  //
  // public String search() {
  // Integer semesterId = getLong("evaluateSearchDepartment.semester.id");
  // Semester semester = entityDao.get(Semester.class, semesterId);
  // Integer departmentId = getLong("evaluateSearchDepartment.department.id");
  // Department department = entityDao.get(Department.class, departmentId);
  // // 得到院系下的所有教学班
  // OqlBuilder adminClassQuery = OqlBuilder.from(Adminclass.class, "adminClass");
  // adminClassQuery.where("adminClass.department in(:departments)", department));
  // adminClassQuery.where("adminClass.enabled =:enabled", true));
  // List adminClassList = entityDao.search(adminClassQuery);
  // List evaluateSearchDepartmentList = new ArrayList();
  // for (Iterator iter = adminClassList.iterator(); iter.hasNext();) {
  // Adminclass adminClass = (Adminclass) iter.next();
  // OqlBuilder query = OqlBuilder.from(CourseTake.class, "courseTake");
  // query.select("select count(*)");
  // query.where("courseTake.task.semester =:semester", semester));
  // query.where("courseTake.std.department in(:departments)", department));
  // query.where("exists( select 1 from QuestionnaireTask questionnaireTask "
  // + " where questionnaireTask.teachTask=courseTake.task)"));
  // query.join("courseTake.std.adminClasses", "adminClass");
  // query.where("adminClass =:adminClass)", adminClass));
  // query.setLimit(getPageLimit());
  // query.addOrder(Order.parse(get("orderBy")));
  // List list = entityDao.search(query);
  // // 得到指定学期，院系的学生评教人次总数
  // Long countAll1 = (Long) list.get(0);
  // int countAll = countAll1.intValue();
  //
  // OqlBuilder query1 = OqlBuilder.from(EvaluateResult.class, "rs");
  // query1.select("select count(*)");
  // query1.where("rs.semester =:semester", semester));
  // query1.where("rs.student.department in(:departments)", department));
  // query1.join("rs.student.adminClasses", "adminClass");
  // query1.where("adminClass =:adminClass)", adminClass));
  // query1.setLimit(getPageLimit());
  // query1.addOrder(Order.parse(get("orderBy")));
  // List list1 = entityDao.search(query1);
  // // 得到指定学期，已经评教的学生人次数
  // Long haveFinish1 = (Long) list1.get(0);
  // int haveFinish = haveFinish1.intValue();
  // String finishRate = "";
  // if (countAll != 0) {
  // float finishRate1 = ((float) (haveFinish * 100 / countAll));
  // DecimalFormat df = new DecimalFormat("0.0");
  // finishRate = df.format(finishRate1) + "%";
  // }
  // EvaluateSearchDepartment esd = new EvaluateSearchDepartment();
  // esd.setSemester(semester);
  // esd.setAdminclass(adminClass);
  // esd.setCountAll(countAll);
  // esd.setHaveFinish(haveFinish);
  // esd.setFinishRate(finishRate);
  // evaluateSearchDepartmentList.add(esd);
  // }
  // Collections.sort(evaluateSearchDepartmentList, new PropertyComparator("adminClass.code"));
  // put("evaluateSearchDepartmentList", evaluateSearchDepartmentList);
  // return forward();
  // }

}