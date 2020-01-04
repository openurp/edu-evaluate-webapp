[#ftl]
[@b.head/]
[@b.toolbar title='教师-问卷评教结果' id='departmentEvaluateBar']
    bar.addBlankItem();
[/@]

<table class="indexpanel">
    <tr>
    <td class="index_view">
        [@b.form action="!search" name="courseEvaluateStatIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
       [@edu_base.semester  name="semester.id" label="学年学期" value=currentSemester/]
       [@b.textfield style="width:100px" name="teacherEvalStat.teacher.user.code" label="教师工号" /]
       [@b.textfield style="width:100px" name="teacherEvalStat.teacher.user.name" label="教师姓名" /]
    <input type="hidden" name="searchFormFlag" value="beenStat"/>
         [/@]
        </td>
        <td class="index_content">
            [@b.div href="!search?semester.id=${(semester.id)!}" id="contentDiv"/]
        </td>
    </tr>
</table>
[@b.foot/]
