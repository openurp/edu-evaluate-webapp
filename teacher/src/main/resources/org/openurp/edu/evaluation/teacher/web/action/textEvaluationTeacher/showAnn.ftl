[#ftl]
[@b.head/]
[@b.toolbar title='教师公告查询' id='annTeacherBar' /]
[@eams.semesterBar semesterValue=semester name="project.id" action="!showAnn" semesterName="semester.id" semesterEmpty="false" initCallback="changeSemester()"/]
<table class="indexpanel">
    <tr>
        <td class="index_content">
            [@b.form name="annTeacherIndexForm" action="!listAnn" target="contentDiv"]
                <input type="hidden" name="lessonId" value="${lessonId!}"/>
            [/@]
            [@b.div id="contentDiv"/]
        </td>
    </tr>
</table>
<script type="text/javaScript">
    var form = document.annTeacherIndexForm;
    
    function changeSemester(){
        bg.form.addInput(form, "semesterId", $("input[name='semester.id']").val());
        bg.form.submit(form);
    }
</script>
[@b.foot/]