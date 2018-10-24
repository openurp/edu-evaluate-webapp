[#ftl]
[@b.head/]
[@b.toolbar title='评教结果' id='textEvaluationTeacherBar' /]

[#--[@eams.semesterBar semesterValue=semester name="project.id" semesterName="semester.id" semesterEmpty="false" initCallback="changeSemester()"/]--]
<table class="indexpanel">
    <tr>
        <td style="width:200px" class="index_view">
        [@b.form action="!search" name="textEvaluationTeacherIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            <input type="hidden" name="lesson.project.id" value="${(project.id)!}"/>
            [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
            [@b.textfields style="width:130px" names="lesson.no;课程序号,lesson.course.name;课程名称"/]

        [/@]
        </td>
        <td class="index_content">
            [@b.div id="contentDiv"  href="!search"/]
        </td>
    </tr>
</table>
<script type="text/javaScript">
    var form = document.textEvaluationTeacherIndexForm;

    function changeSemester(){
        bg.form.addInput(form, "lesson.semester.id", $("input[name='semester.id']").val());
        bg.form.submit(form);
    }
</script>
[@b.foot/]
