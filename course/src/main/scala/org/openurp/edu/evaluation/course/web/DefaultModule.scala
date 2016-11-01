package org.openurp.edu.evaluation.course.web

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.edu.evaluation.app.lesson.model.{ EvaluateSearchAdminclass, EvaluateSearchDepartment }
import org.openurp.edu.evaluation.app.lesson.service.StdEvaluateSwitchService
import org.openurp.edu.evaluation.course.web.action.{ CourseEvalSearchAction, CourseEvalStatAction, DepartEvalSearchAction, DepartEvalStatAction, EvaluateResultAction, EvaluateStatisticsAction, EvaluateStatusStatAction, EvaluateStatusTeacherAction, FinalTeacherScoreAction, LessonEvalSearchAction, LessonEvalStatAction, QuestionnaireLessonAction, QuestionnaireStatAction, QuestionnaireStatSearchAction, QuestionnaireStatTeacherAction, StdEvaluateSwitchAction, TeacherEvalSearchAction, TeacherEvalStatAction, TextEvaluateSwitchAction, TextEvaluationAction, TextEvaluationSearchAction, TextEvaluationTeacherAction }

class DefaultModule extends AbstractBindModule {

  override def binding() {

    //******教务处菜单
    //               评教设置->课程问卷 --- 为课程设置问卷和评教方式（教师/课程）
    bind(classOf[QuestionnaireLessonAction])
    // 评教设置->问卷评教开关
    bind(classOf[StdEvaluateSwitchAction])
    bind(classOf[StdEvaluateSwitchService])
    // 评教设置->文字评教开关
    bind(classOf[TextEvaluateSwitchAction])

    //  评教管理->问卷有效性--------------查看全部学生评教问卷并设置有效或无效
    bind(classOf[EvaluateResultAction])
    //               评教管理->文字有效性 --------------确认学生文字评教（只有已确认的文字评教才能查看到回复）
    bind(classOf[TextEvaluationAction])
    //               评教管理->?? --------------查看全部文字评教（确认和未确认）
    bind(classOf[TextEvaluationSearchAction])

    // ------------------------问卷回收率即时统计与查询---------------------
    //  问卷评教的回收率统计，按开课院系，按教学任务,具体到课程名称或教师姓名
    bind(classOf[EvaluateStatusStatAction])

    //-------------------------------问卷评教统计分析
    //  教师最终得分排名统计查询导出
    bind(classOf[FinalTeacherScoreAction])

    //  问卷评教统计--按任务得分统计 包括：排名??，院系任务统计，学校任务历史，学校分项统计*****教务处
    bind(classOf[LessonEvalStatAction])
    //  问卷评教统计查询---按任务得分查询********院系管理员？
    bind(classOf[LessonEvalSearchAction])

    //  问卷评教统计--按问卷统计每个教师个人总分  包括：排名??，院系统计，学校历史，学校分项统计 ***教务处
    bind(classOf[TeacherEvalStatAction])
    //  问卷评教统计查询--按问卷统计每个教师个人总分  ***院系管理员
    bind(classOf[TeacherEvalSearchAction])

    //  问卷评教统计--按课程教师统计 包括：排名??，院系任务统计，学校任务历史，学校分项统计*****教务处
    bind(classOf[CourseEvalStatAction])
    //  问卷评教统计查询---按课程教师查询********院系管理员？
    bind(classOf[CourseEvalSearchAction])

    //  问卷评教统计--按开课院系统计 包括：排名??，院系任务统计，学校任务历史，学校分项统计*****教务处
    bind(classOf[DepartEvalStatAction])
    //  问卷评教统计查询---按开课院系查询********院系管理员？
    bind(classOf[DepartEvalSearchAction])

    //--暂不使用的页面
    //  问卷评教的各类统计:  -------  在做什么？
    bind(classOf[EvaluateStatisticsAction])

    //  查看任务问卷评教各问题类别结果及总分，+更改教师？？？+各院系比较
    bind(classOf[QuestionnaireStatAction])
    //  查看任务问卷评教各问题类别结果及总分
    bind(classOf[QuestionnaireStatSearchAction])
  }
}
