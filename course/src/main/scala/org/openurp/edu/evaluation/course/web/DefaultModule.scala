package org.openurp.edu.evaluation.course.web

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.edu.evaluation.course.web.action.TextEvaluationSwitchAction
import org.openurp.edu.evaluation.course.web.action.QuestionnaireLessonAction
import org.openurp.edu.evaluation.course.web.action.EvaluateResultAction
import org.openurp.edu.evaluation.course.service.StdEvaluateSwitchService
import org.openurp.edu.evaluation.course.web.action.StdEvaluateSwitchAction

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