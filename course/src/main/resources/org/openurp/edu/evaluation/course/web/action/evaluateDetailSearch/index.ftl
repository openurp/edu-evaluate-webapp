[#ftl]
[@b.head/]
[@b.toolbar title='教师个人评教明细' id='departmentEvaluateBar']
    bar.addBlankItem();
[/@]

<table class="indexpanel">
    <tr>
    <td class="index_view">
        [@b.form action="!search" name="courseEvaluateStatIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
        <input type="hidden" name="semester.id" value="${(semester.id)!}"/>
       [@b.textfield style="width:100px" name="evaluateTeacher.course.code" label="课程代码" /]
       [@b.textfield style="width:100px" name="evaluateTeacher.course.name" label="课程名称" /]
    <input type="hidden" name="searchFormFlag" value="beenStat"/>
         [/@]
        </td>
        <td class="index_content">
            [@b.div href="!search?semester.id=${(semester.id)!}" id="contentDiv"/]
        </td> 
    </tr>
</table>
[@b.foot/]