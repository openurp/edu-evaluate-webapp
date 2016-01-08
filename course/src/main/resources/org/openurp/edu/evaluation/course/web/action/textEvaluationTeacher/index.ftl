[#ftl]
[@b.head/]
[@b.toolbar title='评教结果' id='textEvaluationTeacherBar' /]

[#--[@eams.semesterBar semesterValue=semester name="project.id" semesterName="semester.id" semesterEmpty="false" initCallback="changeSemester()"/]--]
<table class="indexpanel">
    <tr>
        <td style="width:200px" class="index_view">
        [@b.form action="!search" name="textEvaluationTeacherIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            <input type="hidden" name="lesson.project.id" value="${(project.id)!}"/>
            [@b.textfields style="width:130px" names="lesson.no;课程序号,lesson.course.name;课程名称"/]
            [@b.select style="width:100px" name="textEvaluation.lesson.semester.id" value= currentSemester label="学年学期" items=semesters option="id,code"  /]
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