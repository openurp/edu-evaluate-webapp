package org.openurp.edu.evaluate.department

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.edu.evaluate.department.action.EvaluateSwitchAction

class EvaluateModule extends AbstractBindModule {

  override def binding() {
    bind(classOf[EvaluateSwitchAction])
  }
}
