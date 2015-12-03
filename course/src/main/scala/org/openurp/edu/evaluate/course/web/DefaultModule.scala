package org.openurp.edu.evaluate.course.web

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.edu.evaluate.course.web.action.EvaluateResultAction
import org.openurp.edu.evaluate.course.web.action.TextEvaluationSwitchAction
import org.openurp.edu.evaluate.course.service.StdEvaluateSwitchService
import org.openurp.edu.evaluate.course.web.action.StdEvaluateSwitchAction
import org.openurp.edu.evaluate.course.web.action.QuestionnaireLessonAction

class DefaultModule extends AbstractBindModule {

  override def binding() {
          // 评教设置
    bind(classOf[QuestionnaireLessonAction])
    bind(classOf[StdEvaluateSwitchAction])
    bind(classOf[TextEvaluationSwitchAction])
    bind(classOf[StdEvaluateSwitchService])
        // 评教结果
    bind(classOf[EvaluateResultAction])
  }
}