package org.openurp.edu.evaluate.course.web.action

import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.beangle.webmvc.entity.action.RestfulAction
import org.springframework.beans.support.PropertyComparator
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.beangle.data.dao.OqlBuilder
import org.beangle.commons.collection.Collections
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult

class EvaluateResultAction extends RestfulAction[EvaluateResult] {
//  override def  index() : String = {
//    put("stdTypeList", getStdTypes());
//    put("departmentList", getTeachDeparts());
//    forward();
//  }
//
//  
//  override def  search(): String = {
//    val query = getQueryBuilder();
//    put("evaluateResults", entityDao.search(query));
//    forward();
//  }
//
//  @Override
// override protected def getQueryBuilder():OqlBuilder[EvaluateResult] ={
//    val query = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult");
//    populateConditions(query);
//    query.where("evaluateResult.lesson.teachDepart in (:teachDeparts)", getTeachDeparts());
//    query.limit(getPageLimit());
//    query;
//  }

  /**
   * 修改(评教结果,是否有效)
   * 
   * @return
   */
//  public String updateState() {
//    Long[] ids = getLongIds(getShortName());
//    Boolean state = getBoolean("isEvaluate");
//    if (null == state) {
//      state = Boolean.FALSE;
//    }
//    List<EvaluateResult> results = entityDao.get(EvaluateResult.class, ids);
//    for (EvaluateResult evaluateResult : results) {
//      evaluateResult.setStatState(state);
//    }
//    try {
//      entityDao.saveOrUpdate(results);
//      return redirect("search", "info.action.success");
//    } catch (Exception e) {
//      return redirect("search", "info.save.failure");
//    }
//  }

//  /**
//   * 更改教师
//   */
//  public String updateTeacher() {
//    put("evaluateR", entityDao.get(EvaluateResult.class, getLong("evaluateResult")));
//    // put("departments",entityDao.get(Department.class, "",));
//    return forward();
//  }
//
//  public String saveEvaluateTea() {
//    try {
//      EvaluateResult evaluateR = entityDao.get(EvaluateResult.class, getLong("evaluateResult"));
//      evaluateR.setStaff(entityDao.get(Staff.class, getLong("teacher.id")));
//      entityDao.saveOrUpdate(evaluateR);
//    } catch (Exception e) {
//      // TODO: handle exception
//      return redirect("search", "info.save.failure");
//    }
//    return redirect("search", "info.action.success");
//  }

  /**
   * 查看(详细信息)
   */
//   override def info()(@param("id") id: String): String = {
//    val entityId = getLongId(getShortName());
//    if (null == entityId) {
//      logger.warn("cannot get paremeter {}Id or {}.id", getShortName(), getShortName());
//    }
//    val result = (EvaluateResult) getModel(getEntityName(), entityId);
//    List<QuestionResult> questionResults = CollectUtils.newArrayList(result.getQuestionResultSet());
//    Collections.sort(questionResults, new PropertyComparator("question"));
//    put("questions", questionResults);
//    put("remark", result.getRemark());
//    return forward();
//  }

  /**
   * 将没有考试资格的学生评教问卷改为无效
   * 
   * @return
   */
//  public String changeToInvalid() {
//    OqlBuilder<EvaluateResult> query = OqlBuilder.from(EvaluateResult.class, "evaluateResult").where(
//        "evaluateResult.lesson.semester=:semester", getSemester());
//    List<EvaluateResult> results = entityDao.search(query);
//    try {
//      for (EvaluateResult result : results) {
//        // TODO kang 怎么确定学生有没有一门课的考试资格
//        OqlBuilder<ExamTake> builder = OqlBuilder.from(ExamTake.class, "examTake");
//        builder.where("examTake.lesson=:lesson", result.getLesson()).where("examTake.std=:std",
//            result.getStudent());
//        List<ExamTake> takes = entityDao.search(builder);
//        if (takes.size() > 0) {
//          if ("违纪".equals(takes.get(0).getExamStatus().getName())) {
//            result.setStatState(false);
//          }
//        }
//      }
//      saveOrUpdate(results);
//    } catch (Exception e) {
//      return redirect("search", "info.action.failure");
//    }
//    return redirect("search", "info.action.success");
//  }
}