package org.openurp.edu.evaluation.course.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.course.stat.model.ClazzEvalStat
import org.beangle.data.dao.OqlBuilder
import org.openurp.base.model.Department
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.edu.base.code.model.EduSpan
import org.openurp.edu.base.model.Major
import org.beangle.data.model.Entity

class EvaluateResultStatAction  extends RestfulAction[ClazzEvalStat]{
//
//  protected QuestionnairStatService questionnaireStatService;
//
  override protected def indexSetting(): Unit = {
    put("educations", entityDao.getAll(classOf[EduSpan]));
    put("departments", entityDao.search(OqlBuilder.from(classOf[Department],"dep").where("dep.teaching =:tea",true)))
    put("questionnaires", entityDao.search (OqlBuilder.from(classOf[Questionnaire],"qn").where("qn.state=:st", true)))
  }

  
//  override protected def editSetting(entity:Entity )= {
//    put("majors", entityDao.findBy(classOf[Major], "department", entityDao.search(OqlBuilder.from(classOf[Department],"dep").where("dep.teaching =:tea",true))))
//    put("educations", entityDao.getAll(classOf[EduSpan]));
//  }
//
//  
////  @SuppressWarnings({ "unchecked", "rawtypes" })
//  override def  search():String= {
//    put("questionnaireStats", new SinglePage(1, 20, 1, CollectUtils.newArrayList(1)));
//    forward()

//    // FIXME 死方法
//    // put("questionnaireStats", entityDao.search(buildQuery()));
//    // put("educations", baseCodeService.getCodes(EduSpan.class));
//    // put("departments", getDeparts());
//    // put("semesters", entityDao.search(OqlBuilder.from("from Semester")));
//    // put("questionnaires", entityDao.get(Questionnaire.class));
//    // OqlBuilder questionnaireQuery = OqlBuilder.from(Questionnaire.class, "questionnaire");
//    // questionnaireQuery.where("questionnaire.state =:state", true));
//    // put("questionnaireList", entityDao.search(questionnaireQuery));
//    // return forward()
//  }
//
//  // FIXME 死方法
//  // protected OqlBuilder getQueryBuilder() {
//  // String semesterSeqIds = get("semesterSeqIds");
//  // OqlBuilder entityQuery = OqlBuilder.from(EvaluateTeacherStat.class, "questionnaireStat");
//  // String regainLower = get("regainLower");
//  // if (regainLower != null && regainLower.length() > 0) {
//  // int x = new Double((((new Double(regainLower)) * 100))).intValue();
//  // entityQuery
//  // .where(
//  // "questionnaireStat.allTickets!=0 and questionnaireStat.release*(:regainLower)<=questionnaireStat.allTickets*100",
//  // new Integer(x)));
//  // }
//  // if (semesterSeqIds != null && semesterSeqIds.length() > 0) {
//  // entityQuery.where("questionnaireStat.semester.id in (:semesterIds)", Strings
//  // .splitToLong(semesterSeqIds)));
//  // }
//  // String departId = get("questionnaireStat.depart.id");
//  // if (departId != null && departId.length() > 0) {
//  // entityQuery.where("questionnaireStat.depart.id in (:departId)", Strings
//  // .splitToLong(departId)));
//  // } else {
//  // entityQuery.where("questionnaireStat.depart in (:departId)", getDeparts()));
//  // }
//  // populateConditions(entityQuery);
//  // entityQuery.setLimit(getPageLimit());
//  // entityQuery.addOrder(Order.parse(get("orderBy")));
//  // return entityQuery;
//  // }
//
//  @Override
//  public String info() throws Exception {
//    return forward()
//  }
//
//  // public String detailEvaluateResult() {
//  // String questionnaireStatIds = get("questionnaireStatId");
//  // OqlBuilder entityQuery = OqlBuilder.from(EvaluateTeacherStat.class, "questionnaireStat");
//  // entityQuery.where("questionnaireStat.id in (:questionnaireStatIds)", Strings
//  // .splitToLong(questionnaireStatIds)));
//  // entityQuery.addOrder(Order.parse(get("orderBy")));
//  // List list = entityDao.search(entityQuery);
//  // Map questionMap = new HashMap();
//  // Map optionMap = new HashMap();
//  // Map optionGroupMap = new HashMap();
//  // Map optionCode = getOptionCode();
//  // for (Iterator iter = list.iterator(); iter.hasNext();) {
//  // EvaluateTeacherStat questionnaireStat = (EvaluateTeacherStat) iter.next();
//  // List questionlist = new ArrayList();
//  // questionlist.addAll(questionnaireStat.getQuestionStats());
//  // Collections.sort(questionlist);
//  // questionMap.put(questionnaireStat.getId().toString(), questionlist);
//  // for (Iterator iter1 = questionlist.iterator(); iter1.hasNext();) {
//  // TeacherDetailStat questionDetailStat = (TeacherDetailStat) iter1.next();
//  // optionGroupMap.put(questionDetailStat.getQuestion().getOptionGroup().getId().toString(),
//  // questionDetailStat.getQuestion().getOptionGroup());
//  // List optionlist = new ArrayList();
//  // optionlist.addAll(questionDetailStat.getOptionStats());
//  // for (Iterator iter2 = optionlist.iterator(); iter2.hasNext();) {
//  // TeacherOptionStat optionStat = (TeacherOptionStat) iter2.next();
//  // StringBuffer sb = new StringBuffer();
//  // sb.append(questionnaireStat.getId()).append("*").append(questionDetailStat.getId())
//  // .append("*").append(optionCode.get(optionStat.getOption().getId().toString()));
//  // optionMap.put(sb.toString(), optionStat);
//  // }
//  // }
//  // }
//  // put("questionMap", questionMap);
//  // put("optionGroupMap", optionGroupMap);
//  // put("optionMap", optionMap);
//  // put("questionnaires", list);
//  // put("questionnaireStatId", questionnaireStatIds);
//  // return forward()
//  // }
//  //
//  // // public Map getOptionCode() {
//  // // Map codeMap = new HashMap();
//  // // codeMap.put("6", "A");
//  // // codeMap.put("5", "B");
//  // // codeMap.put("4", "C");
//  // // codeMap.put("3", "D");
//  // // codeMap.put("2", "E");
//  // // return codeMap;
//  // // }
//  //
//  // public Map getOptionCode() {
//  // Map resultMap = new HashMap();
//  // String[] code = { "A", "B", "C", "D", "E", "F", "G" };
//  // List optionGroupList = (List) entityDao.get(OptionGroup.class);
//  // for (Iterator iter = optionGroupList.iterator(); iter.hasNext();) {
//  // OptionGroup optionGroup = (OptionGroup) iter.next();
//  // List optionlist = new ArrayList();
//  // optionlist.addAll(optionGroup.getOptions());
//  // Collections.sort(optionlist);
//  // int i = 0;
//  // for (Iterator iter1 = optionlist.iterator(); iter1.hasNext();) {
//  // Option option = (Option) iter1.next();
//  // resultMap.put(option.getId().toString(), code[i]);
//  // i++;
//  // }
//  // }
//  // return resultMap;
//  // }
//  //
//
//  public String stat() {
//    put("stdTypeList", getStdTypes());
//    put("departmentList", getTeachDeparts());
//    getSemester();
//    String departIdSeq = get("departIdSeq");
//    String educationIdSeq = get("educationIdSeq");
//    String semesterIdSeq = get("semester.id");
//    // if (Strings.isBlank(departIdSeq)) {
//    // departIdSeq = getDepartmentIdSeq();
//    // }
//    // if (Strings.isBlank(educationIdSeq)) {
//    // educationIdSeq =getStdTypeIdSeq();
//    // }
//    User user = entityDao.get(User.class, getUserId());
//    questionnaireStatService.saveStatEvaluateResult(educationIdSeq, departIdSeq, semesterIdSeq, user);
//    questionnaireStatService.setDepartmentStat(educationIdSeq, semesterIdSeq);
//    questionnaireStatService.setCollegeStat(educationIdSeq, semesterIdSeq);
//    return redirect("index", "info.stat.success");
//  }
//
//  /**
//   * 设置有效记录
//   **/
//  public String setValid() {
//    String semesterId = get("semester.id");
//    String percent = get("percent");
//    questionnaireStatService.setValidResult(semesterId, new Integer(percent));
//    return redirect("index", "info.stat.success");
//  }
//
//  public void setQuestionnaireStatService(QuestionnairStatService questionnaireStatService) {
//    this.questionnaireStatService = questionnaireStatService;
//  }
//
//
}