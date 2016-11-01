package org.openurp.edu.evaluation.questionnaire.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.model.Question
import org.beangle.data.dao.OqlBuilder
import org.beangle.commons.collection.Order
import org.beangle.commons.lang.Strings
import org.openurp.edu.evaluation.model.Question
import java.sql.Timestamp
import org.beangle.webmvc.api.view.View
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.evaluation.model.Question
import java.sql.Date
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.evaluation.model.OptionGroup
import org.openurp.base.model.Department
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.edu.evaluation.questionnaire.service.QuestionTypeService

/**
 * 问题维护响应类
 *
 * @author chaostone
 */
class QuestionAction extends RestfulAction[Question] {

  var questionTypeService: QuestionTypeService = _

  override def search(): String = {
    val builder = OqlBuilder.from(classOf[Question], "question")
    populateConditions(builder)
    builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    val questions = entityDao.search(builder)

    put("questions", questions)
    forward()
  }

  protected override def editSetting(entity: Question): Unit = {
    val optionGroups = entityDao.getAll(classOf[OptionGroup])
    put("optionGroups", optionGroups);
    val departmentList = entityDao.getAll(classOf[Department])
    val questionTypes = questionTypeService.getQuestionTypes()
    put("questionTypes", questionTypes);
    put("departmentList", departmentList);
  }

  protected def saveAndForward(entity: Question): View = {
    try {
      val question = entity.asInstanceOf[Question]
      val remark = question.remark
      val content = question.content
      question.beginOn = getDate("question.beginOn").get
      val invalidat = getDate("question.endOn").get
      if (!"".equals(invalidat) && invalidat != null) {
        question.endOn = invalidat
      }
      if (remark != null) {
        question.remark = remark.replaceAll("<", "&#60;").replaceAll(">", "&#62;")
      }
      question.content = content.replaceAll("<", "&#60;").replaceAll(">", "&#62;")
      if (!question.persisted) {
        if (question.state) {
          question.beginOn = new Date(System.currentTimeMillis())
        }
      } else {
        question.updatedAt = new java.util.Date
        val questionOld = entityDao.get(classOf[Question], question.id);
        if (questionOld.state != question.state) {
          if (question.state) {
            question.beginOn = new Date(System.currentTimeMillis())
          } else {
            question.endOn = new Date(System.currentTimeMillis())
          }
        }

      }
      entityDao.saveOrUpdate(question);
      return redirect("search", "info.save.success");
    } catch {
      case e: Exception =>
        logger.info("saveAndForwad failure", e);
        return redirect("search", "info.save.failure");
    }
  }
  override def remove(): View = {
    val questionIds = longId("question");
    val questions = entityDao.get(classOf[Question], questionIds);

    val query = OqlBuilder.from(classOf[Questionnaire], "questionnaire");
    query.join("questionnaire.questions", "question");
    query.where("question in (:questions)", questions);
    val questionnaires = entityDao.search(query);
    if (!questionnaires.isEmpty) { redirect("search", "删除失败,选择的数据中已有被评教问卷引用"); }
    entityDao.remove(questions);
    redirect("search", "删除成功");
  }

}
