package org.openurp.edu.evaluation.web

import org.beangle.cdi.bind.BindModule
import org.openurp.edu.evaluation.web.index.IndexAction

class DefaultModule extends BindModule {

  override def binding() {

    bind(classOf[IndexAction])
  }
}
