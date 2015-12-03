[#ftl]
[@b.head/]
[@b.form name="evaluateResultSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=evaluateResults var="evaluateResult" sortable="true"]    
        [@b.gridbar title="学生评教结果列表"]
            bar.addItem("查看", action.info());
            var evaluateMenu = bar.addMenu('设置状态',null);
            evaluateMenu.addItem("置为有效","updateState(1)","update.png");
            evaluateMenu.addItem("置为无效","updateState(0)","update.png");
        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="lesson.no" title="课程序号"/]
            [@b.col property="lesson.course.code" title="课程代码"/]
            [@b.col property="lesson.course.name" title="课程名称"/]
            [@b.col property="student.code" title="学生学号"/]
            [@b.col property="student.person.name" title="学生姓名"/]
            [@b.col property="teacher.code" sort="teacher" title="教师工号"]
            [#list evaluateResult.lesson.teachers as teacher]
            ${(teacher.code)!}<br>
            [/#list]
            [/@]
            [@b.col property="teacher.name" sort="teacher" title="教师姓名"]
            [#list evaluateResult.lesson.teachers as teacher]
            ${(teacher.name)!}<br>
            [/#list]
            [/@]
            [@b.col property="statState" title="状态" width="4%"]
                ${(evaluateResult.statState?string("有效","无效"))!}
            [/@]
            [@b.col property="evaluateAt" title="评教时间"]
                ${(evaluateResult.evaluateAt?string("yyyy-MM-dd"))!}
            [/@]
        [/@]
    [/@]
[/@]
<script type="text/javaScript">
    var searchForm = document.evaluateResultSearchForm;
    
    function updateState(isEvaluate) {
        var id = bg.input.getCheckBoxValues("evaluateResult.id");
        if (id == "" || id.length < 1){
            alert("请至少选择一项!");
        }
        bg.form.addInput(searchForm, 'isEvaluate', isEvaluate);
        bg.form.submit(searchForm, "evaluateResult!updateState.action");
    }
    
    function updateTeacher(){
        var evaluateResultIds = bg.input.getCheckBoxValues("evaluateResult.id");
        if(evaluateResultIds == "" || evaluateResultIds.split(",").length !=1){
                alert("请选择一个进行操作！");
                return false;
        }
        bg.form.addInput(form,"evaluateResult.id",evaluateResultIds);
        bg.form.submit(form,"${b.url('evaluateDetailStat!updateTeacher')}");
    }
</script>
[@b.foot/]