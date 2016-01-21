package org.openurp.edu.evaluation.course.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.edu.evaluation.model.EvaluationCriteria
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.base.code.model.Education
import org.openurp.edu.evaluation.model.QuestionType
import org.openurp.edu.evaluation.lesson.stat.model.LessonEvalStat
import org.beangle.data.model.bind.Binder.Collection
import org.beangle.data.model.bind.Binder.Collection
import org.beangle.data.model.bind.Binder.Collection
import org.beangle.commons.collection.Collections

class QuestionnaireStatSearchAction  extends RestfulAction[LessonEvalStat] {


//  protected QuestionnairStatService questionnairStatService;

  
 override protected def indexSetting() {
    put("educations", entityDao.getAll(classOf[Education]));
    put("questionTypes", entityDao.getAll(classOf[QuestionType]));
    put("departments", entityDao.search(OqlBuilder.from(classOf[Department],"de").where("de.teaching=:tea",true)));
    put("evaluationCriterias", entityDao.getAll(classOf[EvaluationCriteria]));
  }

//  @SuppressWarnings({ "unchecked", "rawtypes" })
  override def  search():String= {
    val query = OqlBuilder.from(classOf[LessonEvalStat], "questionnaireStat");
    populateConditions(query);
    query.limit(getPageLimit);
//    query.orderBy(Order.parse(get("orderBy")));
    put("questionnaireStats", entityDao.search(query));
    val criteriaId = getLong("evaluationCriteriaId").get
    var criteria:EvaluationCriteria = null;
    if (criteriaId != 0) {
      criteria = entityDao.get(classOf[EvaluationCriteria], criteriaId);
    }
    put("criteria", criteria);
    val qtypeId = getLong("selectTypeId").get
    var qtypes=Collections.newBuffer[QuestionType]
    if (qtypeId != 0) {
      qtypes += entityDao.get(classOf[QuestionType], qtypeId);
    } else {
      qtypes ++= entityDao.getAll(classOf[QuestionType]);
    }
    put("questionTypes", qtypes);
    forward();
  }

  protected def getOptionMap():collection.Map[String,Float]= {
    val optionNameMap = Collections.newMap[String, Float]
    optionNameMap.put("A", 90.toFloat);
    optionNameMap.put("B", 80.toFloat);
    optionNameMap.put("C", 60.toFloat);
    optionNameMap.put("D", 0.toFloat);
     optionNameMap
  }

//  public void setQuestionnairStatService(QuestionnairStatService questionnairStatService) {
//    this.questionnairStatService = questionnairStatService;
//  }

}