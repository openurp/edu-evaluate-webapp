package org.openurp.edu.evaluation.course.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.lesson.stat.model.DepartEvalStat

class DepartEvalStatAction  extends RestfulAction[DepartEvalStat] {

//  override  def indexSetting() {
//    put("departments", getColleges());
//  }
//
////  @Override
////  @SuppressWarnings({ "unchecked", "rawtypes" })
//  override def  search():String= {
//    put("departmentStats", new SinglePage(1, 20, 1, CollectUtils.newArrayList(1)));
//    return forward();
//  }

  // protected void editSetting(Entity entity) {
  // put("educations", baseCodeService.getCodes(Education.class));
  // }
  //
  // public String search() {
  // put("departmentStats", entityDao.search(buildQuery()));
  // put("semesters", entityDao.search(OqlBuilder.from("from Semester")));
  // return forward();
  // }
  //
  // protected OqlBuilder getQueryBuilder() {
  // String semesterSeqIds = get("semesterSeqIds");
  // OqlBuilder entityQuery = OqlBuilder.from(EvaluateDepartmentStat.class, "departmentStat");
  // String regainLower = get("regainLower");
  // if (regainLower != null && regainLower.length() > 0) {
  // int x = new Double((((new Double(regainLower)) * 100))).intValue();
  // entityQuery
  // .where(
  // "departmentStat.allTickets!=0 and departmentStat.release*(:regainLower)<=departmentStat.allTickets*100",
  // new Integer(x)));
  // }
  // if (semesterSeqIds != null && semesterSeqIds.length() > 0) {
  // entityQuery.where("departmentStat.semester.id in (:semesterIds)", Strings
  // .splitToLong(semesterSeqIds)));
  // }
  // Long departId = getLong("departmentStat.department.id");
  // if (departId != null) {
  // entityQuery.where("departmentStat.department.id  =:departId", departId));
  // }
  // // populateConditions(entityQuery);
  // entityQuery.setLimit(getPageLimit());
  // entityQuery.addOrder(Order.parse(get("orderBy")));
  // return entityQuery;
  // }


}