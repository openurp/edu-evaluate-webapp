[#ftl]
[@b.head/]

[#if currentSemester??]
  [#if semesters?size==1]
    [#assign title]${currentSemester.schoolYear} ${currentSemester.name} 课程问卷评教[/#assign]
  [#else]
    [#assign title]有${semesters?size}个学期需要评教[/#assign]
  [/#if]
  [@b.toolbar title=title/]
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
    function changeSemester(){
        var evaluateIndexForm = document.evaluateIndexForm;
        bg.form.submit(evaluateIndexForm);
    }
</script>
[#else]
   当前没有开放评教。
[/#if]
[@b.foot/]
