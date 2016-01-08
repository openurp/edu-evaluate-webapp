[#ftl]
[@b.head/]
[@b.form name="courseEvaluteStatSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=evaluates var="evaluate" sortable="false"]    
        [@b.gridbar title="课程评教结果统计列表"]
        bar.addItem("${b.text('action.export')}","exportData()");
        [/@]
        [@b.row]
            [@b.col property="teacher.code" title="工号" width="8%" /]
            [@b.col property="teacher.name" title="姓名" width="8%" /]
            [@b.col title="性别" property="staff.gender.name" width="5%"]${(evaluate.teacher.staff.gender.name)!}[/@]
            [@b.col title="教师类型" property="teacher.teacherType.name" width="8%"/]
            [@b.col title="部门" property="teacher.department.name" width="13%"/]
            [@b.col title="职称" property="teacher.title.name" width="10%"/]
            [@b.col title="职称等级" property="teacher.title.level.name" width="7%"/]
            [@b.col title="在职状态" property="teacher.state.name" width="7%"/]
            [@b.col title="任课" property="teaching" width="4%"]${evaluate.teacher.teaching?string("是", "否")}[/@]
            [@b.col title="学生评分" property="stdEvaluate" width="7%"/]
            [@b.col title="部门评分" property="depEvaluate" width="7%"/]
            [@b.col property="score" title="总分" width="6%"/]
            [@b.col property="departRank" title="院系排名" width="8%"/]
            [@b.col property="rank" title="全校排名" width="8%" /]
        [/@]
    [/@]
[/@]
<script type="text/javaScript">
    
    function exportData(){
        var form = document.courseEvaluteStatSearchForm;
        bg.form.addHiddens(form,action.page.paramstr);
        bg.form.addParamsInput(form,action.page.paramstr);
        bg.form.addInput(form, "keys", "teacher.code,teacher.name,teacher.staff.gender.name,teacher.teacherType.name,teacher.department.name,teacher.title.name,teacher.title.level.name,teacher.state.name,teaching,stdEvaluate,depEvaluate,score,departRank,rank");
        bg.form.addInput(form, "titles", "教师工号,教师姓名,性别,教师类型,部门,职称,职称等级,在职状态,任课,学生评分,部门评分,总分,院系排名,全校排名");
        bg.form.addInput(form, "fileName", "评教汇总统计");
        form.target = "_News";
        bg.form.submit(form, "courseEvaluateStat!export.action");
    }
            
</script>
[@b.foot/]