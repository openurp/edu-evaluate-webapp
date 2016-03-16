[#ftl]
[@b.head/]
[@b.form name="courseEvaluteStatSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=teacherEvalStats var="teacherEvalStat" sortable="true"]    
        [@b.gridbar title="课程评教结果统计列表"]
        var detailMenu = bar.addMenu("查看详情", "info()");
        detailMenu.addItem("教师历史评教", "evaluateTeachHistory()", "info.png");
        bar.addItem("${b.text('action.delete')}","remove()");
        bar.addItem("${b.text('action.export')}","exportData()");
        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="staff.code" title="教师工号"/]
            [@b.col property="staff.person.name.formatedName" title="教师姓名"/]
            [@b.col title="性别" property="staff.person.gender.name" width="5%"]${(evaluate.staff.staff.gender.name)!}[/@]
            [@b.col title="教师类型" property="staff.staffType.name" width="8%"/]
            [@b.col title="部门" property="staff.state.department.name" width="13%"/]
            [@b.col title="职称" property="staff.titleInfo.title.name" width="10%"/]
            [@b.col title="职称等级" property="staff.titleInfo.title.grade.name" width="7%"/]
            [@b.col title="在职状态" property="staff.state.status.name" width="7%"/]
            [#--[@b.col title="任课" property="teaching" width="4%"]${evaluate.staff.state.status?string("是", "否")}[/@]--]
            [#--[@b.col title="学生评分" property="stdEvaluate" width="7%"/]--]
            [#--[@b.col title="部门评分" property="depEvaluate" width="7%"/]--]
            [@b.col property="score" title="总分" width="6%"]${teacherEvalStat.score}[/@]
            [@b.col property="departRank" title="院系排名" width="8%"/]
            [@b.col property="rank" title="全校排名" width="8%" /]
        [/@]
    [/@]
[/@]
<script type="text/javaScript">

    var form = document.courseEvaluteStatSearchForm;
    
    function evaluateTeachHistory(){
        var questionnaireStatIds = bg.input.getCheckBoxValues("teacherEvalStat.id");
        if(questionnaireStatIds == "" || questionnaireStatIds.split(",").length !=1){
                alert("请选择一个进行操作！");
                return false;
        }
        bg.form.addInput(form,"teacherEvalStat.id",questionnaireStatIds);
        bg.form.submit(form,"${b.url('!evaluateTeachHistory')}");
    }
    
    
    function info(){
        var questionnaireStatIds = bg.input.getCheckBoxValues("teacherEvalStat.id");
        if(questionnaireStatIds == "" || questionnaireStatIds.split(",").length !=1){
                alert("请选择一个进行操作！");
                return false;
        }
        bg.form.addInput(form,"teacherEvalStat.id",questionnaireStatIds);
        bg.form.submit(form,"${b.url('teacher-eval-search!info')}");
    }
    
    function remove(){
        var questionnaireStatIds = bg.input.getCheckBoxValues("teacherEvalStat.id");
        bg.form.addInput(form,"teacherEvalStats.id",questionnaireStatIds);
        bg.form.submit(form,"${b.url('!remove')}");
        }
        
    function exportData(){
        bg.form.addHiddens(form,action.page.paramstr);
        bg.form.addParamsInput(form,action.page.paramstr);
        bg.form.addInput(form, "keys", "staff.code,staff.name,staff.staff.gender.name,staff.teacherType.name,staff.department.name,staff.title.name,teacher.title.level.name,teacher.state.name,teaching,stdEvaluate,depEvaluate,score,departRank,rank");
        bg.form.addInput(form, "titles", "教师工号,教师姓名,性别,教师类型,部门,职称,职称等级,在职状态,任课,学生评分,部门评分,总分,院系排名,全校排名");
        bg.form.addInput(form, "fileName", "评教汇总统计");
        form.target = "_News";
        bg.form.submit(form, "courseEvaluateStat!export.action");
    }
            
</script>
[@b.foot/]