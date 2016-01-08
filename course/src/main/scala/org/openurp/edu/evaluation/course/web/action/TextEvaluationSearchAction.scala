package org.openurp.edu.evaluation.course.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.course.model.TextEvaluation

class TextEvaluationSearchAction extends RestfulAction[TextEvaluation]  {


//  @Override
//  protected String getEntityName() {
//    return TextEvaluation.class.getName();
//  }

//  override protected def  getQueryBuilder():OqlBuilder= {
//    super.getQueryBuilder().where("textEvaluation.lesson.teachDepart in (:departs)", getTeachDeparts());
//  }
//
//  
//  override protected def indexSetting() {
//    put("departments", getColleges());
//  }

//  
//  override protected  def getExportDatas():Collection = {
//    val textEvaluationIds = get("textEvaluationIds");
//    if (!textEvaluationIds.isEmpty) {
//      entityDao.get(classOf[TextEvaluation], Strings.splitToLong(textEvaluationIds));
//    } else {
//      search(getQueryBuilder().limit(null));
//    }
//  }
  // FIXME 导出
  // protected Collection getExportDatas() {
  // OqlBuilder entityQuery = buildQuery();
  // entityQuery.setLimit(null);
  // return entityDao.search(entityQuery);
  // }
  //
  // protected PropertyExtractor getPropertyExtractor() {
  // return new CharacterExtractor(getTextResource());
  // }
  //
  // class CharacterExtractor extends DefaultPropertyExtractor {
  //
  // public CharacterExtractor(TextResource textResource) {
  // this.setTextResource(textResource);
  // }
  //
  // protected Object extract(Object target, String property) throws Exception
  // {
  // Object value = null;
  // try {
  // value = PropertyUtils.getProperty(target, property);
  // } catch (Exception e) {
  // return "";
  // }
  // if ("isCourseEvaluate".equals(property)) {
  // if (Boolean.TRUE.equals(value)) return
  // textResource.getText("attr.evaluate.teacher");
  // else return textResource.getText("attr.evaluate.course");
  // }
  // if ("affirm".equals(property)) {
  // if (Boolean.TRUE.equals(value)) return
  // textResource.getText("common.yes");
  // else return textResource.getText("common.no");
  // }
  // return super.extract(target, property);
  // }
  // }


}