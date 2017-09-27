[#ftl]
[@b.head/]
[@b.toolbar title='教师评教统计详细信息' id='adClassStateInfoBar']
    bar.addBack();
[/@]
<div style="margin-bottom:30px;margin-top:5px;border:1px solid #006CB2;">
    <table class="infoTable" width="90%" align="center">
        <tr>
            <td class="title" style="text-align:center;">学年学期</td>
            <td style="padding-left:10px;">${(teacherEvalStat.semester.schoolYear)!}-${(teacherEvalStat.semester.name)!}</td>
            <td class="title" style="text-align:center;">问卷类型</td>
            <td style="padding-left:10px;">${(teacherEvalStat.questionnaire.description)!}</td> 
            <td class="title" style="text-align:center;">是否发布</td>
            <td style="padding-left:10px;">${(teacherEvalStat.published?string("是","否"))!}</td> 
        </tr>
        <tr>
            <td class="title" style="text-align:center;">教师</td>
            <td style="padding-left:10px;">${(teacherEvalStat.teacher.user.code)!} ${(teacherEvalStat.teacher.user.name)!}</td>
            <td class="title" style="text-align:center;">平均分</td>
            <td style="padding-left:10px;">${(teacherEvalStat.avgScore)!}</td> 
            <td class="title" style="text-align:center;">所在院系</td>
            <td style="padding-left:10px;">${(teacherEvalStat.teacher.user.department.name)!}</td> 
        </tr>
        <tr>
            <td class="title" style="text-align:center;">问卷下发数量</td>
            <td style="padding-left:10px;">${teacherEvalStat.stdCount!}</td>
            <td class="title" style="text-align:center;">问卷回收数量</td>
            <td style="padding-left:10px;">${teacherEvalStat.totalTickets}</td>
            <td class="title" style="text-align:center;">问卷有效数量</td>
            <td style="padding-left:10px;">${teacherEvalStat.tickets}</td> 
        </tr>
    </table>
    [@b.grid items=teacherEvalStat.questionStats var="qs" sortable="false"]
        [@b.row]
            [@b.col title="问题内容" width="30%"]${qs.question.content}[/@]
            [#list options as option]
            [@b.col title="${option.name!}"]
                [#list qs.optionStats as os]
                    [#if os.option==option]
                      ${os.amount}[#break/]
                    [/#if]
                [/#list]
                [/@]
            [/#list]
            [@b.col title="平均得分"]${qs.avgScore}[/@]
        [/@]
    [/@]
</div>
[@b.foot/]