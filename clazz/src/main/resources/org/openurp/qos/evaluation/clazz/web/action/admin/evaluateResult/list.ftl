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
            [@b.col property="clazz.crn" title="课程序号" width="7%"/]
            [@b.col property="clazz.course.code" title="课程代码" width="12%"/]
            [@b.col property="clazz.course.name" title="课程名称" width="20%"/]
            [@b.col property="student.code" title="学生学号" width="13%"/]
            [@b.col property="student.name" title="学生姓名" width="10%"]
               [@b.a href="!info?id=${evaluateResult.id}"]${(evaluateResult.student.person.name)!}[/@]
            [/@]
            [@b.col property="teacher.name" title="教师姓名" width="10%"/]
            [@b.col property="score" title="分数" width="8%"/]
            [@b.col property="evaluateAt" title="评教时间" width="9%"]${(evaluateResult.evaluateAt?string("yyyy-MM-dd"))!}[/@]
            [@b.col property="statType" title="是否有效" width="7%"]
              [#if evaluateResult.statType==1]有效
              [#else]无效
              [/#if]
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
