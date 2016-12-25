package org.openurp.edu.evaluation.student.web

import org.beangle.commons.cdi.bind.AbstractBindModule
import org.openurp.edu.evaluation.app.lesson.service.StdEvaluateSwitchService
import org.openurp.edu.evaluation.student.web.index.IndexAction
import org.openurp.edu.evaluation.student.web.action.LessonAction
import org.openurp.edu.evaluation.student.web.action.TextAction

class DefaultModule extends AbstractBindModule {

  override def binding() {
    bind(classOf[LessonAction])
    bind(classOf[TextAction])
    bind(classOf[IndexAction])
    
  }
}
