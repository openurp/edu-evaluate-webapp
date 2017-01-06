[#ftl]
[@b.head/]
[@b.form name="courseEvaluteStatSearchForm" action="!search" target="contentDiv"]    <table id="bar" width="100%"></table>
    <input type="hidden" name="semester.id" value="${semesterId!}">
    [@b.grid items=lessonEvalStats var="lessonEvalStat" sortable="true"]    
        [@b.gridbar title="教师个人评教记录"]
        bar.addItem("${b.text('action.info')}", action.info());
        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="lesson.no" title="课程序号"/]
            [#--[@b.col title="学年学期"]${(lessonEvalStat.semester.schoolYear)!} ${(lessonEvalStat.semester.name)!}[/@]--]
            [@b.col property="lesson.course.code" title="课程代码"/]
            [@b.col property="lesson.course.name" title="课程名称"/]
            [@b.col property="teacher.user.name" title="教师姓名"/]
            [@b.col property="teacher.user.code" title="教师工号"/]
            [@b.col property="score" title="教师任务得分"]${lessonEvalStat.score}[/@]
            [@b.col property="rank" title="全校排名"/]
            [@b.col property="departRank" title="院系排名"/]
        [/@]
    [/@]
[/@]
[@b.foot/]