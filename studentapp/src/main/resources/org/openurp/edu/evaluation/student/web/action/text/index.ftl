[#ftl]
[@b.head/]
[@b.toolbar title='文字评教' id='textEvaluateStudentBar' /]

<table width="100%">
    <tr [#if semesters?size==1] style="display:none"[/#if]>
        <td class="index_content" >
            [@b.form name="evaluateIndexForm" action="!search" target="contentDiv"]
             学年学期:[@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code"  onchange="changeSemester(this)"/]
            [/@]
        </td>
    </tr>
    <tr>
       <td class="index_content">
            [@b.div id="contentDiv"  href="!search?&semester.id=${currentSemester.id}" /]
        </td>
    </tr>
</table>

<script type="text/javascript">
    function changeSemester(num){
        var textEvaluateStudentIndexForm = document.evaluateIndexForm;
        bg.form.submit(textEvaluateStudentIndexForm);
    }
</script>
[@b.foot/]

