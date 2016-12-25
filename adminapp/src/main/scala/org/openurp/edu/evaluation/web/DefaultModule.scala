package org.openurp.edu.evaluation.web

import org.beangle.commons.cdi.bind.AbstractBindModule
import org.openurp.edu.evaluation.web.index.IndexAction

class DefaultModule extends AbstractBindModule {

  override def binding() {

    bind(classOf[IndexAction])
  }
}
