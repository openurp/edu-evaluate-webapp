[#ftl]
[@b.head/]
[@b.navmenu]
    [@b.navitem title="问卷评教" href="/evaluateStd"/]
    [@b.navitem title="文字评教" href="/textEvaluateStudent"/]
[/@]
[@b.toolbar title="课程问卷评教" /]

[@eams.semesterBar semesterValue=semester name="project.id" semesterName="semester.id" semesterEmpty="false" initCallback="changeSemester(this.value)"/]
<table class="indexpanel">
    <tr>
        <td class="index_content">
            [@b.form name="evaluateIndexForm" action="!search" target="contentDiv"]
                <input type="hidden" name="semester.id" value="${(semester.id)!}"/>
            [/@]
            [@b.div id="contentDiv"/]
        </td>
    </tr>
</table>
<script type="text/javascript">
    function changeSemester(num){
        var evaluateIndexForm = document.evaluateIndexForm;
        bg.form.addInput(evaluateIndexForm, "semester.id", num);
        bg.form.submit(evaluateIndexForm);
    }
</script>
[@b.foot/]