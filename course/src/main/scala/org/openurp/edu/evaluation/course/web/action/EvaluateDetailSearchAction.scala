package org.openurp.edu.evaluation.course.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.lesson.model.QuestionnaireLesson
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.openurp.edu.evaluation.model.Option
import org.openurp.edu.evaluation.lesson.stat.model.LessonEvalStat

class EvaluateDetailSearchAction extends RestfulAction[LessonEvalStat] {
//
//  protected TeacherService teachService;
//
//  protected QuestionnairStatService questionnairStatService;
//
//  protected EvaluationCriteriaService evaluationCriteriaService;
//
//  override def  search():String= {
//    val query = OqlBuilder.from(classOf[LessonEvalStat], "evaluateTeacherStat");
//    populateConditions(query);
////    query.orderBy(get(Order.ORDER_STR)).limit(getPageLimit());
//    put("questionnaireStats", entityDao.search(query));
//    forward()
//  }

   def  info() :String= {
    val questionnaireStat = entityDao.get(classOf[LessonEvalStat],getLong("questionnaireStat.id").get)
    put("questionnaireStat", questionnaireStat);
    // zongrenci fix
    val query = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName, "result");
    query.where("result.staff =:tea", questionnaireStat.staff);
    query.where("result.lesson=:less", questionnaireStat.lesson)
    query.select("case when result.statType =1 then count(result.id) end,count(result.id)");
    query.groupBy("result.statType");
    entityDao.search(query) foreach { a =>
      put("number1",a(0))
      put("number2",a(1))
    }
//    var ob = null
//    if (lis.size == 1) {
//      put("number1",lis(0))
//      put("number2",lis(1))
//    }
//    put("number1", null)
//    put("number2", null)
//    val list = new ArrayList<Option>();
    val list = entityDao.getAll(classOf[Option]).toBuffer
    val questions = questionnaireStat.questionnaire.questions
    questions foreach { question =>
      val options = question.optionGroup.options
      options foreach { option =>
        var tt = 0
        list foreach { oldOption =>
//        for (Option oldOption : list) {
          if (oldOption.id.equals(option.id)) {
            tt += 1;
          }
        }
        if (tt == 0) {
          list += option
        }
      }
    }
    put("options", list);
    val querys = OqlBuilder.from(classOf[QuestionnaireLesson], "questionnaireL");
    querys.join("questionnaireL.lesson.teachers", "teacher");
    querys.where("teacher=:teach", questionnaireStat.staff);
    querys.where("questionnaireL.lesson=:lesso", questionnaireStat.lesson);
    querys.join("questionnaireL.lesson.teachclass.courseTakes", "courseTake");
    querys.select("count(courseTake.std.id)");
    put("numbers", entityDao.search(querys)(0));
    val que = OqlBuilder.from(classOf[QuestionResult], "questionR");
    que.where("questionR.result.staff=:teaId", questionnaireStat.staff);
    que.where("questionR.result.lesson=:less", questionnaireStat.lesson);
    que.select("questionR.question.id,questionR.option.id,count(*)");
    que.groupBy("questionR.question.id,questionR.option.id");
    put("questionRs", entityDao.search(que));
    val quer = OqlBuilder.from(classOf[QuestionResult], "questionR");
    quer.where("questionR.result.staff=:teaId", questionnaireStat.staff);
    quer.where("questionR.result.lesson=:less", questionnaireStat.lesson);
    quer.select("questionR.question.id,questionR.question.content,sum(questionR.score)/count(questionR.id)");
    quer.groupBy("questionR.question.id,questionR.question.content");
    put("questionResults", entityDao.search(quer));
    forward()
  }
//
//  public void setTeachService(TeacherService teachService) {
//    this.teachService = teachService;
//  }
//
//  public void setQuestionnairStatService(QuestionnairStatService questionnairStatService) {
//    this.questionnairStatService = questionnairStatService;
//  }
//
//  public void setEvaluationCriteriaService(EvaluationCriteriaService evaluationCriteriaService) {
//    this.evaluationCriteriaService = evaluationCriteriaService;
//  }
//

}