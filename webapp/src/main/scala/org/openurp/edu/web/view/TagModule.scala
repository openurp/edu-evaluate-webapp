package org.openurp.edu.web.view

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.edu.evaluate.questionnaire.web.action.QuestionAction
import org.openurp.edu.evaluate.questionnaire.web.action.QuestionTypeAction
import org.openurp.edu.evaluate.questionnaire.web.action.QuestionnaireAction
import org.openurp.edu.evaluate.questionnaire.web.action.OptionGroupAction
import org.openurp.edu.evaluate.questionnaire.web.action.EvaluationCriteriaAction
import org.openurp.edu.evaluate.questionnaire.web.action.EvaluationConfigAction
import org.openurp.edu.evaluate.questionnaire.service.QuestionTypeService
class TagModule extends AbstractBindModule {

  override def binding() {
    bind("mvc.TagLibrary.eams", classOf[WebTagLibrary])
  }

}