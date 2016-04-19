package org.openurp.edu.evaluation.department

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.edu.evaluation.department.action.EvaluateSwitchAction
import org.openurp.edu.evaluation.department.action.SupervisiorEvaluateAction
import org.openurp.edu.evaluation.department.action.DepartEvaluateAction

class EvaluateModule extends AbstractBindModule {

  override def binding() {
    bind(classOf[EvaluateSwitchAction])
    bind(classOf[DepartEvaluateAction])
    bind(classOf[SupervisiorEvaluateAction])
  }
}
