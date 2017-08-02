package org.openurp.edu.evaluation.questionnaire.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.beangle.data.dao.EntityDao
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.view.View

class EvaluationConfigAction(entityDao: EntityDao) extends ActionSupport {

  def index(): View = {
    forward()
  }
}