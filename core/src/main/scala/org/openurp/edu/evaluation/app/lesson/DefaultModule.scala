package org.openurp.edu.evaluation.app.lesson

import org.beangle.cdi.bind.BindModule
import org.openurp.edu.evaluation.app.lesson.service.StdEvaluateSwitchService

class DefaultModule extends  BindModule {

  override def binding() {
    bind(classOf[StdEvaluateSwitchService])
  }

}
