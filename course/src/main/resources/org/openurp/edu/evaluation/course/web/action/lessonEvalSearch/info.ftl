[#ftl]
[@b.head/]
[@b.toolbar title='课程评教统计详细信息' id='adClassStateInfoBar']
    bar.addBack();
[/@]
<div style="margin-bottom:30px;margin-top:5px;border:1px solid #006CB2;">
    <table class="infoTable" width="90%" align="center">
        <tr>
            <td class="title" style="text-align:center;">课程序号</td>
            <td style="padding-left:10px;">${(questionnaireStat.clazz.crn)!}</td>
            <td class="title" style="text-align:center;">课程名称</td>
            <td style="padding-left:10px;">${(questionnaireStat.clazz.course.name)!}</td>
            <td class="title" style="text-align:center;">开课院系</td>
            <td style="padding-left:10px;">${(questionnaireStat.clazz.teachDepart.name)!}</td>
        </tr>
        <tr>
            <td class="title" style="text-align:center;">教师姓名</td>
            <td style="padding-left:10px;">${(questionnaireStat.teacher.user.name)!}</td>
            <td class="title" style="text-align:center;">职称</td>
            <td style="padding-left:10px;">${(questionnaireStat.teacher.title.name)!}</td>
            <td class="title" style="text-align:center;">平均分</td>
            <td style="padding-left:10px;">${(questionnaireStat.avgScore)!}</td>
        </tr>
        <tr>
            <td class="title" style="text-align:center;">问卷下发数量</td>
            <td style="padding-left:10px;">${questionnaireStat.clazz.teachclass.stdCount}</td>
            <td class="title" style="text-align:center;">问卷回收数量</td>
            <td style="padding-left:10px;">${questionnaireStat.totalTickets}</td>
            <td class="title" style="text-align:center;">问卷有效数量</td>
            <td style="padding-left:10px;">${questionnaireStat.tickets}</td>
        </tr>
    </table>
    [@b.grid items=questionnaireStat.questionStats var="qs" sortable="false"]
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
