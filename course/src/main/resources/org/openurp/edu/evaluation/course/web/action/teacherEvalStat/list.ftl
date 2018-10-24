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
            [@b.col property="teacher.user.code" title="教师工号" width="5%"/]
            [@b.col property="teacher.user.name" title="教师姓名" width="8%"/]
            [@b.col title="部门" property="teacher.user.department.name" width="25%" /]
            [@b.col title="问卷类型" property="questionnaire.description" width="15%" /]
            [@b.col title="教师类型" property="teacher.teacherType.name" width="7%" /]
            [@b.col property="avgScore" title="平均分" width="10%"/]
            [@b.col property="departRank" title="院系排名" width="10%"/]
            [@b.col property="rank" title="全校排名" width="10%" /]
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
        var url= "${b.url('teacher-eval-search!info?id=aaa')}"
        url=url.replace('aaa',questionnaireStatIds);
        bg.form.submit(form,url);
    }

    function remove(){
        var questionnaireStatIds = bg.input.getCheckBoxValues("teacherEvalStat.id");
        bg.form.addInput(form,"teacherEvalStats.id",questionnaireStatIds);
        bg.form.submit(form,"${b.url('!remove')}");
    }

    function exportData(){
        bg.form.addHiddens(form,action.page.paramstr);
        bg.form.addParamsInput(form,action.page.paramstr);
        bg.form.addInput(form, "keys", "teacher.user.code,teacher.name,teacher.teacher.gender.name,teacher.teacherType.name,teacher.user.department.name,teacher.title.name,teacher.title.level.name,teacher.state.name,teaching,stdEvaluate,depEvaluate,score,departRank,rank");
        bg.form.addInput(form, "titles", "教师工号,教师姓名,性别,教师类型,部门,职称,职称等级,在职状态,任课,学生评分,部门评分,总分,院系排名,全校排名");
        bg.form.addInput(form, "fileName", "评教汇总统计");
        form.target = "_News";
        bg.form.submit(form, "courseEvaluateStat!export.action");
    }

</script>
[@b.foot/]
