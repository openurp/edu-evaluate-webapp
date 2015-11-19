package org.openurp.edu.evaluate.questionnaire.web.action

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

/**
 * 问题维护响应类
 * 
 * @author chaostone
 */
class QuestionAction extends RestfulAction[Question] {
    
    override def search(): String = {
      val builder = OqlBuilder.from(classOf[Question], "question")
      populateConditions(builder)
      builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
      val questions = entityDao.search(builder)
      
      put("questions", questions)
      forward()
    }
    
  protected override def  saveAndForward(entity: Question): View = {
    try {
      val question =  entity.asInstanceOf[Question]
      val remark = question.remark
      val content = question.content
      question.beginOn = getDate("question.beginOn").get 
      val invalidat = getDate("question.endOn").get
      if (!"".equals(invalidat) && invalidat != null) {
        question.endOn = invalidat
      }
      if (remark!=null) {
        question.remark =remark.replaceAll("<", "&#60;").replaceAll(">", "&#62;")
      }
      question.content=content.replaceAll("<", "&#60;").replaceAll(">", "&#62;")
      if (!question.persisted) {
        if (question.state) {
          question.beginOn = new Date(System.currentTimeMillis())
        }
      } else {
        question.updatedAt = new Date(System.currentTimeMillis())
        val questionOld = entityDao.get(Question.class, question.getId());
        if (questionOld.state != question.state) {
          if (question.getState()) {
            question.setBeginOn(new Timestamp(System.currentTimeMillis()));
          } else {
            question.setEndOn(new Timestamp(System.currentTimeMillis()));
          }
        }

      }
      entityDao.saveOrUpdate(question);
      return redirect("search", "info.save.success");
    } catch (Exception e) {
      logger.info("saveAndForwad failure", e);
      return redirect("search", "info.save.failure");
    }
  }
  
}
