package org.openurp.edu.evaluate.questionnaire.web

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.edu.evaluate.questionnaire.web.action.QuestionAction
import org.openurp.edu.evaluate.questionnaire.web.action.QuestionTypeAction
import org.openurp.edu.evaluate.questionnaire.web.action.QuestionnaireAction
import org.openurp.edu.evaluate.questionnaire.web.action.OptionGroupAction
import org.openurp.edu.evaluate.questionnaire.web.action.EvaluationCriteriaAction
import org.openurp.edu.evaluate.questionnaire.web.action.EvaluationConfigAction
class DefaultModule extends AbstractBindModule {

  override def binding() {
    bind(classOf[QuestionAction],classOf[QuestionTypeAction])
    bind(classOf[EvaluationCriteriaAction],classOf[OptionGroupAction],classOf[QuestionnaireAction])
    bind(classOf[EvaluationConfigAction])
  }

}