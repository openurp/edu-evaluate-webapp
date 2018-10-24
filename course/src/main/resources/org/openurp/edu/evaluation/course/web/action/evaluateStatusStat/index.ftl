[#ftl]
[@b.head/]
[@b.toolbar title='评教情况统计' /]

<table class="indexpanel">
    <tr>
        <td class="index_view">
        [@b.form action="!search" name="evaluateStatusStatIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
            [@b.select name="department.id" label="开课院系" items=departments empty="..."/]
            [@b.textfield name="lesson.no" label="课程序号"/]
            [@b.textfield name="course.code" label="课程代码"/]
            [@b.textfield name="course.name" label="课程名称"/]
            [@b.textfield style="width:100px" name="teacher.name" label="教师姓名" /]
        [/@]
        </td>

        <td class="index_content">
            [@b.div href="!search?semester.id=${(semester.id)!}" id="contentDiv"/]
        </td>
    </tr>
    </table>
[@b.foot/]
