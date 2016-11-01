package org.openurp.edu.evaluation.teacher.web.action

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.edu.evaluation.teacher.web.action.TextEvaluationTeacherAction
import org.openurp.edu.evaluation.teacher.web.action.QuestionnaireStatTeacherAction
import org.openurp.edu.evaluation.teacher.web.action.EvaluateStatusTeacherAction

class DefaultModule extends AbstractBindModule {

  override def binding() {
    // *****教师菜单  评教查询->文字评教
    //  教师个人查看自己的评教回收率
    bind(classOf[EvaluateStatusTeacherAction])
    //  教师个人查看自己的评教结果
    bind(classOf[QuestionnaireStatTeacherAction])
    //  教师回复学生文字评教、发布公告
    bind(classOf[TextEvaluationTeacherAction])
  }
}
