package org.openurp.edu.evaluate.questionnaire.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.model.Question
import org.beangle.data.dao.OqlBuilder
import org.beangle.commons.collection.Order

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
  
}
