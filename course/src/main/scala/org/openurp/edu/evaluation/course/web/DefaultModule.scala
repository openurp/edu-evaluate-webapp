package org.openurp.edu.evaluation.course.web

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.edu.evaluation.course.web.action.QuestionnaireLessonAction
import org.openurp.edu.evaluation.course.web.action.EvaluateResultAction
import org.openurp.edu.evaluation.course.service.StdEvaluateSwitchService
import org.openurp.edu.evaluation.course.web.action.StdEvaluateSwitchAction
import org.openurp.edu.evaluation.course.web.action.EvaluateStdAction
import org.openurp.edu.evaluation.course.web.action.TextEvaluationAction
import org.openurp.edu.evaluation.course.web.action.TextEvaluateStdAction
import org.openurp.edu.evaluation.course.web.action.EvaluateDetailStatAction
import org.openurp.edu.evaluation.course.web.action.EvaluateDetailSearchAction
import org.openurp.edu.evaluation.course.web.action.TextEvaluationSearchAction
import org.openurp.edu.evaluation.course.web.action.TextEvaluationTeacherAction
import org.openurp.edu.evaluation.course.web.action.TextEvaluateSwitchAction
import org.openurp.edu.evaluation.course.web.action.CourseEvalStatAction
import org.openurp.edu.evaluation.course.web.action.EvaluateStatisticsAction
import org.openurp.edu.evaluation.course.model.EvaluateSearchDepartment
import org.openurp.edu.evaluation.course.model.EvaluateSearchAdminclass
import org.openurp.edu.evaluation.course.web.action.EvaluateSearchDepartmentAction
import org.openurp.edu.evaluation.course.web.action.EvaluateSearchAdminclassAction
import org.openurp.edu.evaluation.course.web.action.EvaluateStatusStatAction
import org.openurp.edu.evaluation.course.web.action.QuestionnaireStatAction
import org.openurp.edu.evaluation.course.web.action.QuestionnaireStatTeacherAction
import org.openurp.edu.evaluation.course.web.action.QuestionnaireStatSearchAction

class DefaultModule extends AbstractBindModule {

  override def binding() {

// 问卷评教开关   
    bind(classOf[StdEvaluateSwitchAction])
    bind(classOf[StdEvaluateSwitchService])
// 课程设置问卷和评教对象（教师/课程）
    bind(classOf[QuestionnaireLessonAction])
// 学生进行问卷评教
    bind(classOf[EvaluateStdAction])
// 查看全部学生评教问卷并设置有效或无效
    bind(classOf[EvaluateResultAction])
    
// 评教结果统计
    bind(classOf[EvaluateDetailStatAction])
    bind(classOf[EvaluateDetailSearchAction])
    bind(classOf[CourseEvalStatAction])
    bind(classOf[EvaluateStatisticsAction])
    bind(classOf[EvaluateStatusStatAction])
    bind(classOf[QuestionnaireStatAction])
    bind(classOf[QuestionnaireStatSearchAction])
    bind(classOf[QuestionnaireStatTeacherAction])
    
//  学生评教完成情况
    bind(classOf[EvaluateSearchAdminclassAction])
    bind(classOf[EvaluateSearchDepartmentAction])

    

//    文字评教开关
    bind(classOf[TextEvaluateSwitchAction]) 
//    学生进行文字评教并且查看教师回复
    bind(classOf[TextEvaluateStdAction])
//   确认学生文字评教（只有已确认的文字评教才能查看到回复）
    bind(classOf[TextEvaluationAction])
//    查看全部文字评教（确认和未确认）
    bind(classOf[TextEvaluationSearchAction])
//    教师回复学生文字评教、发布公告
    bind(classOf[TextEvaluationTeacherAction])
  }
}