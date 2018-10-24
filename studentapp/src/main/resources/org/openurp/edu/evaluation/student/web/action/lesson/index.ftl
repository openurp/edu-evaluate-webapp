[#ftl]
[@b.head/]
[#--[@b.navmenu]
    [@b.navitem title="问卷评教" href="/evaluateStd"/]
    [@b.navitem title="文字评教" href="/textEvaluateStudent"/]
[/@]--]
[#if currentSemester??]
[#assign title]${currentSemester.schoolYear} ${currentSemester.name} 课程问卷评教[/#assign]
[@b.toolbar title=title/]
<table width="100%">
    <tr [#if semesters?size==1] style="display:none"[/#if]>
        <td class="index_content" >
            [@b.form name="evaluateIndexForm" action="!search" target="contentDiv"]
             [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
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
        var evaluateIndexForm = document.evaluateIndexForm;
        bg.form.addInput(evaluateIndexForm, "semester.id", num);
        bg.form.submit(evaluateIndexForm);
    }
</script>
[#else]
   当前没有开放评教。
[/#if]
[@b.foot/]
