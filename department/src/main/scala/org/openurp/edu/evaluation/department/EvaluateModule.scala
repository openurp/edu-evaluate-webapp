package org.openurp.edu.evaluation.department

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.edu.evaluation.department.action.EvaluateSwitchAction
import org.openurp.edu.evaluation.department.action.SupervisiorEvaluateAction
import org.openurp.edu.evaluation.department.action.DepartEvaluateAction
import org.openurp.edu.evaluation.department.action.DepartEvaluateSearchAction

class EvaluateModule extends AbstractBindModule {

  override def binding() {
    bind(classOf[EvaluateSwitchAction])
    bind(classOf[DepartEvaluateAction])
    bind(classOf[SupervisiorEvaluateAction])
    bind(classOf[DepartEvaluateSearchAction])
  }
}
