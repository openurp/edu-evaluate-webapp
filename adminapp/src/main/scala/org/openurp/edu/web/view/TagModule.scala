package org.openurp.edu.web.view

import org.beangle.cdi.bind.BindModule
class TagModule extends BindModule {

  override def binding() {
    bind("mvc.TagLibrary.eams", classOf[WebTagLibrary])
  }

}