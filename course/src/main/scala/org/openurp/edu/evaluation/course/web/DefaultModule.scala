package org.openurp.edu.evaluation.course.web

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.edu.evaluation.course.web.action.QuestionnaireLessonAction
import org.openurp.edu.evaluation.course.web.action.EvaluateResultAction
import org.openurp.edu.evaluation.course.service.StdEvaluateSwitchService
import org.openurp.edu.evaluation.course.web.action.StdEvaluateSwitchAction
import org.openurp.edu.evaluation.course.web.action.EvaluateStdAction
import org.openurp.edu.evaluation.course.web.action.TextEvaluationAction
import org.openurp.edu.evaluation.course.web.action.TextEvaluateStdAction
import org.openurp.edu.evaluation.course.web.action.TextEvaluationSearchAction
import org.openurp.edu.evaluation.course.web.action.TextEvaluationTeacherAction
import org.openurp.edu.evaluation.course.web.action.TextEvaluateSwitchAction
import org.openurp.edu.evaluation.course.web.action.EvaluateStatisticsAction
import org.openurp.edu.evaluation.course.model.EvaluateSearchDepartment
import org.openurp.edu.evaluation.course.model.EvaluateSearchAdminclass
import org.openurp.edu.evaluation.course.web.action.EvaluateSearchAdminclassAction
import org.openurp.edu.evaluation.course.web.action.EvaluateStatusStatAction
import org.openurp.edu.evaluation.course.web.action.QuestionnaireStatAction
import org.openurp.edu.evaluation.course.web.action.QuestionnaireStatTeacherAction
import org.openurp.edu.evaluation.course.web.action.QuestionnaireStatSearchAction
import org.openurp.edu.evaluation.course.web.action.TeacherEvalStatAction
import org.openurp.edu.evaluation.course.web.action.TeacherEvalSearchAction
import org.openurp.edu.evaluation.course.web.action.LessonEvalStatAction
import org.openurp.edu.evaluation.course.web.action.LessonEvalSearchAction
import org.openurp.edu.evaluation.course.web.action.CourseEvalSearchAction
import org.openurp.edu.evaluation.course.web.action.CourseEvalStatAction
import org.openurp.edu.evaluation.course.web.action.DepartEvalSearchAction
import org.openurp.edu.evaluation.course.web.action.DepartEvalStatAction

class DefaultModule extends AbstractBindModule {

  override def binding() {
//-------------------------------问卷评教设置+学生评教
// 问卷评教开关   
    bind(classOf[StdEvaluateSwitchAction])
    bind(classOf[StdEvaluateSwitchService])
// 为课程设置问卷和评教方式（教师/课程）
    bind(classOf[QuestionnaireLessonAction])
// 学生进行问卷评教
    bind(classOf[EvaluateStdAction])
//  查看全部学生评教问卷并设置有效或无效
    bind(classOf[EvaluateResultAction])
    
//    -----------------------------问卷回收率统计查询
//  问卷评教的回收率统计，按院系，按班级
    bind(classOf[EvaluateStatusStatAction])
//  按照班级查看问卷回收率，查看单个学生的问卷评教完成情况，不包括评教内容
    bind(classOf[EvaluateSearchAdminclassAction])
    
//-------------------------------问卷评教统计分析
    
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
    
    

//  问卷评教的各类统计:  -------  在做什么？
    bind(classOf[EvaluateStatisticsAction])
    

    
    
//--没用的页面    
//  查看任务问卷评教各问题类别结果及总分，+更改教师？？？+各院系比较
    bind(classOf[QuestionnaireStatAction])
//  查看任务问卷评教各问题类别结果及总分   
    bind(classOf[QuestionnaireStatSearchAction])
    
    
//  教师个人查看自己的评教情况
    bind(classOf[QuestionnaireStatTeacherAction])

    
// --------------------------------文字评教设置+学生评教   
    
//   文字评教开关
    bind(classOf[TextEvaluateSwitchAction]) 
//   学生进行文字评教并且查看教师回复
    bind(classOf[TextEvaluateStdAction])
//   确认学生文字评教（只有已确认的文字评教才能查看到回复）
    bind(classOf[TextEvaluationAction])
//   查看全部文字评教（确认和未确认）
    bind(classOf[TextEvaluationSearchAction])
//   教师回复学生文字评教、发布公告
    bind(classOf[TextEvaluationTeacherAction])
  }
}