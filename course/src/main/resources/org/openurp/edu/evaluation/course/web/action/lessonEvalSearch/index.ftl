[#ftl]
[@b.head/]
[@b.toolbar title='任务-问卷评教结果' id='departmentEvaluateBar']
    bar.addBlankItem();
[/@]

<table class="indexpanel">
    <tr>
    <td class="index_view">
        [@b.form action="!search" name="courseEvaluateStatIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
       [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
       [@b.textfield style="width:100px" name="lessonEvalStat.lesson.no" label="课程序号" /]
       [@b.textfield style="width:100px" name="lessonEvalStat.course.code" label="课程代码" /]
       [@b.textfield style="width:100px" name="lessonEvalStat.course.name" label="课程名称" /]
    <input type="hidden" name="searchFormFlag" value="beenStat"/>
         [/@]
        </td>
        <td class="index_content">
            [@b.div href="!search?semester.id=${(semester.id)!}" id="contentDiv"/]
        </td> 
    </tr>
</table>
[@b.foot/]