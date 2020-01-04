[#ftl]
[@b.head/]
[#--[@b.navmenu]
    [@b.navitem title="问卷评教" href="/evaluateStd"/]
    [@b.navitem title="文字评教" href="/textEvaluateStudent"/]
[/@]--]
[@b.toolbar title="课程问卷评教" /]

<table class="indexpanel">
    <tr>
        <td class="index_content">
            [@b.form name="evaluateIndexForm" action="!search" target="contentDiv" theme="search"]
            [@edu_base.semester  name="semester.id" label="学年学期" value=currentSemester/]
            [/@]
        </td>
       <td class="index_content">
            [@b.div id="contentDiv"  href="!search?&semester.id=${(semester.id)!}" /]
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
