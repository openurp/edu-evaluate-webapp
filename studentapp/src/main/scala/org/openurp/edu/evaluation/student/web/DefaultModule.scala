package org.openurp.edu.evaluation.student.web

import org.beangle.cdi.bind.BindModule
import org.openurp.edu.evaluation.app.lesson.service.StdEvaluateSwitchService
import org.openurp.edu.evaluation.student.web.index.IndexAction
import org.openurp.edu.evaluation.student.web.action.LessonAction
import org.openurp.edu.evaluation.student.web.action.TextAction

class DefaultModule extends BindModule {

  override def binding() {
    bind(classOf[LessonAction])
    bind(classOf[TextAction])
    bind(classOf[IndexAction])
    
  }
}
