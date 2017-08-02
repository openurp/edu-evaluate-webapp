package org.openurp.edu.evaluation.department.web

import org.beangle.cdi.bind.BindModule
import org.openurp.edu.evaluation.department.web.action.DepartEvaluateSearchAction
import org.openurp.edu.evaluation.department.web.action.DepartEvaluateAction
import org.openurp.edu.evaluation.department.web.action.SupervisiorEvaluateAction
import org.openurp.edu.evaluation.department.web.action.EvaluateSwitchAction

class EvaluateModule extends BindModule {

  override def binding() {
    bind(classOf[EvaluateSwitchAction])
    bind(classOf[DepartEvaluateAction])
    bind(classOf[SupervisiorEvaluateAction])
    bind(classOf[DepartEvaluateSearchAction])
  }
}
