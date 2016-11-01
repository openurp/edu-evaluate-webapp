package org.openurp.edu.evaluation.course.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.lesson.model.TextEvaluation
import org.openurp.base.model.Semester
import org.beangle.commons.collection.Order

class TextEvaluationSearchAction extends RestfulAction[TextEvaluation] {

  //  @Override
  //  protected String getEntityName() {
  //    return TextEvaluation.class.getName();
  //  }

  //  override protected def  getQueryBuilder():OqlBuilder= {
  //    super.getQueryBuilder().where("textEvaluation.lesson.teachDepart in (:departs)", getTeachDeparts());
  //  }
  //
  override protected def indexSetting(): Unit = {
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)
  }

  override def search(): String = {
    // 页面条件
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val state = getBoolean("textEvaluation.state").getOrElse(null)
    val textEvaluation = OqlBuilder.from(classOf[TextEvaluation], "textEvaluation")
    textEvaluation.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    if (state != null)
      textEvaluation.where("textEvaluation.state=:state", state)
    textEvaluation.where("textEvaluation.lesson.semester=:semester", semester)
    put("textEvaluations", entityDao.search(textEvaluation))
    forward()
  }

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
