package org.openurp.edu.evaluation.questionnaire.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.beangle.commons.dao.EntityDao
import org.beangle.webmvc.api.action.ActionSupport

class EvaluationConfigAction(entityDao: EntityDao) extends ActionSupport {

  def index(): String = {
    forward()
  }
}