package org.openurp.edu.evaluation.student.web.action

import org.beangle.commons.inject.bind.AbstractBindModule

class DefaultModule extends AbstractBindModule {

  override def binding() {
    //*****学生菜单   评教->问卷评教    备注可以拿掉吗？
    bind(classOf[EvaluateStdAction])
    //               评教->文字评教
    bind(classOf[TextEvaluateStdAction])
  }
}
