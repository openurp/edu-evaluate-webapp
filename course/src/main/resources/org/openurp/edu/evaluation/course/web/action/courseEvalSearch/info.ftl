[#ftl]
[@b.head/]
[@b.toolbar title='课程评教统计详细信息' id='adClassStateInfoBar']
    bar.addBack();
[/@]
<div style="margin-bottom:30px;margin-top:5px;border:1px solid #006CB2;">
    <table class="infoTable" width="90%" align="center">
        <tr>
            <td class="title" style="text-align:center;">课程代码</td>
            <td style="padding-left:10px;">${(questionnaireStat.course.code)!}</td>
            <td class="title" style="text-align:center;">课程名称</td>
            <td style="padding-left:10px;">${(questionnaireStat.course.name)!}</td>
        [#--<td class="title" style="text-align:center;">开课院系</td>
            <td style="padding-left:10px;">${(questionnaireStat.lesson.teachDepart.name)!}</td> --]
        </tr>
        <tr>
            <td class="title" style="text-align:center;">教师姓名</td>
            <td style="padding-left:10px;">${(questionnaireStat.teacher.user.name)!}</td>
        [#--<td class="title" style="text-align:center;">职称</td>
            <td style="padding-left:10px;">${(questionnaireStat.teacher.title.name)!}</td>--]
            <td class="title" style="text-align:center;">总分</td>
            <td style="padding-left:10px;">${(questionnaireStat.score)!}</td> 
        </tr>
        <tr>
            <td class="title" style="text-align:center;">问卷下发数量</td>
            <td style="padding-left:10px;">${numbers!0}</td>
            <td class="title" style="text-align:center;">问卷回收数量</td>
            <td style="padding-left:10px;">${(number2)!}</td>
            <td class="title" style="text-align:center;">问卷有效数量</td>
            <td style="padding-left:10px;">${(number1)!}</td> 
        </tr>
    </table>
    [@b.grid items=questionResults var="questionResult" sortable="false"]
        [@b.row]
            [@b.col title="问题内容" width="30%"]${(questionResult[1])!}[/@]
            [#list options as option]
            [@b.col title="${option.name!}"]
            [#assign numb =0 /]
                [#list questionRs as questionR]
                    [#if questionR[0]==questionResult[0]]
                            [#if questionR[1]==option.id]
                            [#assign numb=questionR[2] /]
                            [/#if]
                    [/#if]
                [/#list]
                ${numb!0}
                [/@]
            [/#list]
            [@b.col title="平均得分"]${(questionResult[2])!}[/@]
        [/@]
    [/@]
</div>
[@b.foot/]