package org.openurp.edu.evaluation.course.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.edu.evaluation.model.EvaluationCriteria
import org.beangle.commons.dao.OqlBuilder
import org.openurp.edu.base.code.model.Education
import org.openurp.edu.evaluation.model.QuestionType
import org.openurp.edu.evaluation.lesson.stat.model.LessonEvalStat
import org.beangle.commons.model.bind.Binder.Collection
import org.beangle.commons.model.bind.Binder.Collection
import org.beangle.commons.model.bind.Binder.Collection
import org.beangle.commons.collection.Collections
import org.openurp.base.model.Semester
import org.openurp.edu.base.code.model.StdType
import org.openurp.edu.evaluation.model.Questionnaire

class QuestionnaireStatSearchAction  extends RestfulAction[LessonEvalStat] {


//  protected QuestionnairStatService questionnairStatService;

  
 override def  index():String= {
//    put("educations", entityDao.getAll(classOf[Education]));
//    put("questionTypes", entityDao.getAll(classOf[QuestionType]));
//    put("departments", entityDao.search(OqlBuilder.from(classOf[Department],"de").where("de.teaching=:tea",true)));
//    put("evaluationCriterias", entityDao.getAll(classOf[EvaluationCriteria]));
//    val semesters = entityDao.getAll(classOf[Semester])
//    put("semesters", semesters)
//    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
//    put("currentSemester", entityDao.search(semesterQuery).head)
//    forward()
   
   val stdType = entityDao.get(classOf[StdType],5)
    put("stdTypeList", stdType)
    val department = entityDao.get(classOf[Department],20)
    put("departmentList", department)

    var searchFormFlag = get("searchFormFlag").orNull
    if (searchFormFlag==null) {
      searchFormFlag = "beenStat"
    }
    put("searchFormFlag", searchFormFlag)
//    put("educations", getEducations())
    put("departments", entityDao.getAll(classOf[Department]))
    val query= OqlBuilder.from(classOf[Questionnaire], "questionnaire").where("questionnaire.state =:state",true)
    put("questionnaires", entityDao.search(query))
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)
    put("evaluationCriterias",entityDao.getAll(classOf[EvaluationCriteria]))
    put("questionTypes", entityDao.getAll(classOf[QuestionType]))
     forward()
  }

//  @SuppressWarnings({ "unchecked", "rawtypes" })
//  override def  search():String= {
//    val query = OqlBuilder.from(classOf[LessonEvalStat], "questionnaireStat");
//    populateConditions(query);
//    query.limit(getPageLimit);
////    query.orderBy(Order.parse(get("orderBy")));
//    put("questionnaireStats", entityDao.search(query));
//    val criteriaId = getLong("evaluationCriteriaId").getOrElse(0L)
//    var criteria:EvaluationCriteria = null;
//    if (criteriaId != 0L) {
//      criteria = entityDao.get(classOf[EvaluationCriteria], criteriaId);
//    }
//    put("criteria", criteria);
//    val qtypeId = getLong("selectTypeId").getOrElse(0L)
//    var qtypes=Collections.newBuffer[QuestionType]
//    if (qtypeId != 0L) {
//      qtypes += entityDao.get(classOf[QuestionType], qtypeId);
//    } else {
//      qtypes ++= entityDao.getAll(classOf[QuestionType]);
//    }
//    put("questionTypes", qtypes);
//    forward();
//  }

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