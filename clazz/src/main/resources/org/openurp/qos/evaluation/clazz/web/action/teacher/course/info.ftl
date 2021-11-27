[#ftl]
[@b.head/]
[@b.toolbar title='课程评教统计详细信息' id='adClassStateInfoBar']
    bar.addBack();
[/@]
<div style="margin-bottom:30px;margin-top:5px;border:1px solid #006CB2;">
    <table class="infoTable" width="90%" align="center">
        <tr>
            <td class="title" style="text-align:center;">课程序号</td>
            <td style="padding-left:10px;">${(stat.crn)!}</td>
            <td class="title" style="text-align:center;">课程名称</td>
            <td style="padding-left:10px;">${(stat.course.name)!}</td>
            <td class="title" style="text-align:center;">开课院系</td>
            <td style="padding-left:10px;">${(stat.teachDepart.name)!}</td>
        </tr>
        <tr>
            <td class="title" style="text-align:center;">教师姓名</td>
            <td style="padding-left:10px;">${(stat.teacher.user.name)!}</td>
            <td class="title" style="text-align:center;">参评人数</td>
            <td style="padding-left:10px;">${stat.tickets}</td>
            <td class="title" style="text-align:center;">总分</td>
            <td style="padding-left:10px;">${(stat.score)!}</td>
        </tr>
    </table>
    [@b.grid items=stat.questionStats?sort_by(["question","priority"]) var="qs" sortable="false"]
        [@b.row]
            [@b.col title="序号" width="10%"]${qs_index+1}[/@]
            [@b.col title="问题分类" width="20%"]${qs.question.indicator.name}[/@]
            [@b.col title="问题内容" width="60%"]${qs.question.contents}[/@]
            [@b.col title="平均得分"]${qs.score}[/@]
        [/@]
    [/@]
</div>
[@b.foot/]
