[#ftl]
[@b.head/]
[@b.toolbar title='教师综合评教结果' id='departmentEvaluateBar']
    bar.addBlankItem();
[/@]

<table class="indexpanel">
    <tr>
    <td class="index_view">
        [@b.form action="!search" name="finalTeacherScoreIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
       [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
       [@b.select style="width:134px" name="staff.state.department.id" label="教师所属院系" items=departments empty="..."/]
       [@b.textfield style="width:100px" name="staff.code" label="教师工号" /]
       [@b.textfield style="width:100px" name="staff.person.name.formatedName" label="教师姓名" /]
    <input type="hidden" name="searchFormFlag" value="beenStat"/>
         [/@]
        </td>
        <td class="index_content">
            [@b.div href="!search?semester.id=${(semester.id)!}" id="contentDiv"/]
        </td> 
    </tr>
</table>
[@b.foot/]