[#ftl]
[@b.head/]
[@b.toolbar title='评教结果' id='textEvaluationTeacherBar' /]

<table class="indexpanel">
    <tr>
        <td style="width:200px" class="index_view">
        [@b.form action="!search" name="textEvaluationTeacherIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            <input type="hidden" name="clazz.project.id" value="${(project.id)!}"/>
            [@edu_base.semester  name="semester.id" label="学年学期" value=currentSemester/]
            [@b.textfields style="width:130px" names="clazz.crn;课程序号,clazz.course.name;课程名称"/]
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
        bg.form.addInput(form, "clazz.semester.id", $("input[name='semester.id']").val());
        bg.form.submit(form);
    }
</script>
[@b.foot/]
