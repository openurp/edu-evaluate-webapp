[#ftl]
[@b.head/]
[@b.toolbar title='课程评教统计详细信息' id='adClassStateInfoBar']
    bar.addBack();
[/@]
<div style="margin-bottom:30px;margin-top:5px;border:1px solid #006CB2;">
    <table class="infoTable" width="90%" align="center">
        <tr>
            <td class="title" style="text-align:center;">课程序号</td>
            <td style="padding-left:10px;">${(questionnaireStat.crn)!}</td>
            <td class="title" style="text-align:center;">课程名称</td>
            <td style="padding-left:10px;">${(questionnaireStat.course.name)!}</td>
            <td class="title" style="text-align:center;">开课院系</td>
            <td style="padding-left:10px;">${(questionnaireStat.teachDepart.name)!}</td>
        </tr>
        <tr>
            <td class="title" style="text-align:center;">教师姓名</td>
            <td style="padding-left:10px;">${(questionnaireStat.teacher.name)!}</td>
            <td class="title" style="text-align:center;">问卷有效数量</td>
            <td style="padding-left:10px;">${questionnaireStat.tickets}</td>
            <td class="title" style="text-align:center;">平均分</td>
            <td style="padding-left:10px;">${(questionnaireStat.score)!}</td>
        </tr>
    </table>
    [@b.grid items=questionnaireStat.questionStats var="qs" sortable="false"]
        [@b.row]
            [@b.col title="问题内容" width="30%"]${qs.question.contents}[/@]
            [#list options?sort_by("proportion")?reverse as option]
            [@b.col title="${option.name!}"]
                [#list qs.optionStats as os]
                    [#if os.option==option]
                      ${os.amount}[#break/]
                    [/#if]
                [/#list]
                [/@]
            [/#list]
            [@b.col title="平均得分"]${qs.score}[/@]
        [/@]
    [/@]
</div>
[@b.foot/]
