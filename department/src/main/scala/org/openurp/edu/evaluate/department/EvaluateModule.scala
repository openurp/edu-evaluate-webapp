package org.openurp.edu.evaluate.department

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.edu.evaluate.department.action.EvaluateSwitchAction
import org.openurp.edu.evaluate.department.action.DepartEvaluateAction

class EvaluateModule extends AbstractBindModule {

  override def binding() {
    bind(classOf[EvaluateSwitchAction])
    bind(classOf[DepartEvaluateAction])
  }
}
