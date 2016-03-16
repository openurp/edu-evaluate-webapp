[#ftl]
[@b.head/]
[#--[@eams.semesterBar semesterValue=semester name="project.id" semesterName="semester.id" semesterEmpty="false" initCallback="changeSemester()"/]--]
<table class="indexpanel">
    <tr>
        <td style="width:200px" class="index_view">
        [@b.form action="!search" name="evaluateTeacherStatIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            <input type="hidden" name="searchFormFlag" value="${searchFormFlag!}"/>
            [#if searchFormFlag?? || searchFormFlag == "beenStat"]
            <input type="hidden" name="evaluateTeacherStat.lesson.project.id" value="${(project.id)!}"/>
            [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
            [@b.textfields style="width:130px" names="lessonEvalStat.lesson.no;课程序号,lessonEvalStat.lesson.course.code;课程代码,lessonEvalStat.lesson.course.name;课程名称,lessonEvalStat.staff.code;教师工号,lessonEvalStat.staff.person.name.formatedName;教师姓名"/]
            [@b.select style="width:134px" name="lessonEvalStat.lesson.teachDepart.id" label="开课院系" items=departments empty="..."/]
            [@b.select style="width:134px" name="lessonEvalStat.questionnaire.id" label="问卷类型" items=questionnaires option="id,description" empty="..."/]
            [#else]
            [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
            <input type="hidden" name="lessonEvalStat.lesson.project.id" value="${(project.id)!}"/>
            [@b.textfields style="width:130px" names="lessonEvalStat.lesson.no;课程序号,lessonEvalStat.lesson.course.code;课程代码,lessonEvalStat.lesson.course.name;课程名称,lessonEvalStat.staff.code;教师工号,lessonEvalStat.staff.person.name.formatedName;教师姓名"/]
            [@b.select style="width:134px" name="lessonEvalStat.depart.id" label="开课院系" items=departments empty="..."/]
            [@b.select style="width:134px" name="lessonEvalStat.questionnaire.id" label="问卷类型" items=questionnaires option="id,description" empty="..."/]
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


