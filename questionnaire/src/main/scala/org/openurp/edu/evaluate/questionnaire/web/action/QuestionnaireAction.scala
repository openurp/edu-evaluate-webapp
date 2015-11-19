package org.openurp.edu.evaluate.questionnaire.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.model.Questionnaire
import org.beangle.data.dao.OqlBuilder
import org.beangle.commons.collection.Order

class QuestionnaireAction extends RestfulAction[Questionnaire] {
  
    
    override def search(): String = {
      val builder = OqlBuilder.from(classOf[Questionnaire], "questionnaire")
      populateConditions(builder)
      builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
      val questionnaires = entityDao.search(builder)
      
      put("questionnaires", questionnaires)
      forward()
    }
}