package org.openurp.edu.evaluation.department.web

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.edu.evaluation.department.web.action.DepartEvaluateSearchAction
import org.openurp.edu.evaluation.department.web.action.DepartEvaluateAction
import org.openurp.edu.evaluation.department.web.action.SupervisiorEvaluateAction
import org.openurp.edu.evaluation.department.web.action.EvaluateSwitchAction

class EvaluateModule extends AbstractBindModule {

  override def binding() {
    bind(classOf[EvaluateSwitchAction])
    bind(classOf[DepartEvaluateAction])
    bind(classOf[SupervisiorEvaluateAction])
    bind(classOf[DepartEvaluateSearchAction])
  }
}
