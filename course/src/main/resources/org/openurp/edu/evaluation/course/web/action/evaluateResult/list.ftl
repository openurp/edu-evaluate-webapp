[#ftl]
[@b.head/]
[@b.form name="evaluateResultSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=evaluateResults var="evaluateResult" sortable="true"]    
        [@b.gridbar title="学生评教结果列表"]
            [#--bar.addItem("查看", action.info());--]
            var evaluateMenu = bar.addMenu('设置状态',null);
            evaluateMenu.addItem("置为有效","updateState(1)","update.png");
            evaluateMenu.addItem("置为无效","updateState(0)","update.png");
        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="lesson.no" title="课程序号"][@b.a href="!info?id=${evaluateResult.id}"]${(evaluateResult.lesson.no)!}[/@][/@]
            [@b.col property="lesson.course.code" title="课程代码"/]
            [@b.col property="lesson.course.name" title="课程名称"/]
            [@b.col property="student.code" title="学生学号"/]
            [@b.col property="student.person.name.formatedName" title="学生姓名"][/@]
        [#--[@b.col sort="teacher" title="教师工号"]
            [#list evaluateResult.lesson.teachers as teacher]
            ${(teacher.code)!}<br>
            [/#list]
            [/@]--]
            [@b.col property="staff.person.name.formatedName" title="教师姓名"/]
        [#--[@b.col title="教师姓名"]
            [#list (evaluateResult.lesson.teachers)?if_exists as teacher]
            ${(teacher.person.name.formatedName)!}[#if teacher_has_next],[/#if]
            [/#list]
            [/@]--]
            [@b.col property="statType" title="是否有效"]
              [#if evaluateResult.statType==1 ]有效
              [#else]无效
              [/#if]
              [/@]
            [@b.col property="evaluateAt" title="评教时间"]${(evaluateResult.evaluateAt?string("yyyy-MM-dd"))!}[/@]
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
        bg.form.addParamsInput(searchForm,action.page.paramstr);
        [#--bg.form.submit(searchForm, "evaluateResult!updateState.action");--]
        bg.form.submit(searchForm, "${b.url('!updateState')}")
    }
    
    function updateTeacher(){
        var evaluateResultIds = bg.input.getCheckBoxValues("evaluateResult.id");
        if(evaluateResultIds == "" || evaluateResultIds.split(",").length !=1){
                alert("请选择一个进行操作！");
                return false;
        }
        bg.form.addInput(form,"evaluateResult.id",evaluateResultIds);
       [#-- bg.form.submit(form,"${b.url('evaluateDetailStat!updateTeacher')}");--]
    }
</script>
[@b.foot/]