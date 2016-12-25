package org.openurp.edu.web.view

import org.beangle.commons.cdi.bind.AbstractBindModule
class TagModule extends AbstractBindModule {

  override def binding() {
    bind("mvc.TagLibrary.eams", classOf[WebTagLibrary])
  }

}