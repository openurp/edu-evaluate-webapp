[#ftl]
[@b.head/]
[@b.toolbar title='教师历史评教信息' id='adClassStateInfoBar']
    bar.addBack();
[/@]
<div style="margin-bottom:30px;margin-top:5px;border:1px solid #006CB2;">
    <table class="infoTable" width="90%" align="center">
        <tr>
            <td class="title" style="text-align:center;">教师姓名</td>
            <td style="padding-left:10px;">${(teacher.user.name)!}</td>
            <td class="title" style="text-align:center;">职称</td>
            <td style="padding-left:10px;">${(teacher.title.name)!}</td>
            <td class="title" style="text-align:center;">院系</td>
            <td style="padding-left:10px;">${(teacher.state.department.name)!}</td>
        </tr>
    </table>
    [@b.grid items=teachEvaluates var="teachEvaluate" sortable="false"]
        [@b.row]
            [@b.col title="学年学期" width="10%"]${(teachEvaluate.semester.schoolYear)!}(${(teachEvaluate.semester.name)!})[/@]
            [#--[@b.col title="课程序号"]${(teachEvaluate.clazz.crn)!}[/@]
            [@b.col title="课程名称"]${(teachEvaluate.clazz.course.name)!}[/@]
            [@b.col title="课程类别"]${(teachEvaluate.clazz.course.courseType.name)!}[/@]--]
            [@b.col title="得分"]${(teachEvaluate.score)!0}[/@]
            [@b.col title="有效票数"]${(teachEvaluate.validTickets)!0}[/@]
            [@b.col title="院系排名"]${(teachEvaluate.departRank)!0}[/@]
            [@b.col title="全校排名"]${(teachEvaluate.rank)!0}[/@]
        [/@]
    [/@]
</div>
[@b.foot/]
