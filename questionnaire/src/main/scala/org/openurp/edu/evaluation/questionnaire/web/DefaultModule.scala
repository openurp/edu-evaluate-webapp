package org.openurp.edu.evaluation.questionnaire.web

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.edu.evaluation.questionnaire.service.QuestionTypeService
import org.openurp.edu.evaluation.questionnaire.web.action.QuestionTypeAction
import org.openurp.edu.evaluation.questionnaire.web.action.QuestionnaireAction
import org.openurp.edu.evaluation.questionnaire.web.action.EvaluationCriteriaAction
import org.openurp.edu.evaluation.questionnaire.web.action.EvaluationConfigAction
import org.openurp.edu.evaluation.questionnaire.web.action.OptionGroupAction
import org.openurp.edu.evaluation.questionnaire.web.action.QuestionAction
class DefaultModule extends AbstractBindModule {

  override def binding() {
    bind(classOf[QuestionAction],classOf[QuestionTypeAction])
    bind(classOf[EvaluationCriteriaAction],classOf[OptionGroupAction],classOf[QuestionnaireAction])
    bind(classOf[EvaluationConfigAction])
    bind(classOf[QuestionTypeService])
  }

}