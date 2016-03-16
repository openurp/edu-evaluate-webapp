[#ftl]
[@b.head/]
[@b.toolbar title='评教情况统计' /]

[#--[@eams.semesterBar name="project.id" semesterEmpty=false semesterName="semester.id" semesterValue=semester/]--]
<table class="indexpanel">
    <tr>
        <td class="index_view">
        [@b.form action="!search" name="evaluateStatusStatIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
            [@b.select name="department.id" label="院系" items=departments empty="..."/]
        [/@]
        </td>
        
        <td class="index_content">
            [@b.div href="!search?semester.id=${(semester.id)!}" id="contentDiv"/]
        </td> 
    </tr>
    </table>
[@b.foot/]