[#ftl]
[@b.head/]
<table class="indexpanel">
    <tr>
        <td style="width:200px" class="index_view">
        [@b.form action="!search" name="evaluateTeacherStatIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            <input type="hidden" name="searchFormFlag" value="${searchFormFlag!}"/>
            [#if searchFormFlag?? || searchFormFlag == "beenStat"]
            <input type="hidden" name="evaluateTeacherStat.clazz.project.id" value="${(project.id)!}"/>
            [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
            [@b.textfields style="width:130px" names="clazzEvalStat.clazz.crn;课程序号,clazzEvalStat.clazz.course.code;课程代码,clazzEvalStat.clazz.course.name;课程名称,clazzEvalStat.teacher.user.code;教师工号,clazzEvalStat.teacher.user.name;教师姓名"/]
            [@b.select style="width:134px" name="clazzEvalStat.clazz.teachDepart.id" label="开课院系" items=departments empty="..."/]
            [@b.select style="width:134px" name="clazzEvalStat.questionnaire.id" label="问卷类型" items=questionnaires option="id,description" empty="..."/]
            [#else]
            [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
            <input type="hidden" name="clazzEvalStat.clazz.project.id" value="${(project.id)!}"/>
            [@b.textfields style="width:130px" names="clazzEvalStat.clazz.crn;课程序号,clazzEvalStat.clazz.course.code;课程代码,clazzEvalStat.clazz.course.name;课程名称,clazzEvalStat.teacher.user.code;教师工号,clazzEvalStat.teacher.user.name;教师姓名"/]
            [@b.select style="width:134px" name="clazzEvalStat.depart.id" label="开课院系" items=departments empty="..."/]
            [@b.select style="width:134px" name="clazzEvalStat.questionnaire.id" label="问卷类型" items=questionnaires option="id,description" empty="..."/]
            [/#if]
        [/@]
        </td>
        <td class="index_content">
            [@b.div id="contentDiv" href="!search"/]
        </td>
    </tr>
</table>
<script type="text/javaScript">
    [#if !searchFormFlag?? || searchFormFlag == "beenStat"]
    var form = document.evaluateTeacherStatIndexForm;

    function changeSemester(){
        bg.form.addInput(form, "evaluateTeacherStat.semester.id", $("input[name='semester.id']").val());
        bg.form.submit(form);
    }
    [#else]
    var form = document.evaluateResultIndexForm;

    function changeSemester(){
        bg.form.addInput(form, "evaluateResult.semester.id", $("input[name='semester.id']").val());
        bg.form.submit(form);
    }
    [/#if]
</script>
[@b.foot/]
