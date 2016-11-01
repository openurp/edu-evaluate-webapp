package org.openurp.edu.evaluation.app.lesson

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.edu.evaluation.app.lesson.service.StdEvaluateSwitchService

class DefaultModule extends AbstractBindModule {

  override def binding() {

    bind(classOf[StdEvaluateSwitchService])
  }

}
