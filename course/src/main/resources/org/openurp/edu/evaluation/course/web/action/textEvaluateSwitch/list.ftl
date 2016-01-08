[#ftl]
[@b.head/]
[@b.form name="textEvaluateSwitchSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=textEvaluationSwitchs var="evaluateSwitch" sortable="true"]    
        [@b.gridbar title="文字评教开关列表"]
            bar.addItem("${b.text('action.add')}",action.add());
            bar.addItem("${b.text('action.edit')}",action.edit());
            bar.addItem("${b.text('action.delete')}",action.remove());
        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="opened" title="是否开放"]
                ${(evaluateSwitch.opened?string("开放","关闭"))!}
            [/@]
            [@b.col property="beginAt" title="开始时间"]
                ${(evaluateSwitch.beginAt?string("yyyy-MM-dd HH:mm"))!}
            [/@]
            [@b.col property="endAt" title="结束时间"]
                ${(evaluateSwitch.endAt?string("yyyy-MM-dd HH:mm"))!}
            [/@]
            [@b.col title="是否开放(教师查询)"]
                ${(evaluateSwitch.openedTeacher?string("开放","关闭"))!}
            [/@]
            [@b.col title="是否开放(学生评教)[手动设置,不限时间]"]
                ${(evaluateSwitch.textEvaluateOpened?string("开放","关闭"))!}
            [/@]
        [/@]
    [/@]
[/@]
[@b.foot/]


[#--
[#ftl]
[@b.head/]
[@b.div id="textEvaluationSwitchDiv"]
[@b.toolbar title='文字评教开关' id='textEvaluationSwitchBar']
    bar.addItem("${b.text('action.edit')}","edit()");
[/@]

[@b.messages slash='3'/]
<table class="infoTable" width="100%">
    <tr>
        <td class="title" style="text-align:center;">开始时间</td>
        <td style="padding-left:10px;">${(textEvaluationSwitch.openAt?string("yyyy-MM-dd HH:mm"))!}</td>
        <td class="title" style="text-align:center;">结束时间</td>
        <td style="padding-left:10px;">${(textEvaluationSwitch.closeAt?string("yyyy-MM-dd HH:mm"))!}</td>
    </tr>
    <tr>
        <td class="title" style="text-align:center;width:20%;">是否开放</td>
        <td style="padding-left:10px;">${(textEvaluationSwitch.opened?string("是","否"))!}</td>
        <td class="title" style="text-align:center;width:20%;">是否开放(教师查询)</td>
        <td style="padding-left:10px;">${(textEvaluationSwitch.openedTeacher?string("是","否"))!}</td>
    </tr>
    <tr>
        <td class="title" style="text-align:center;">是否开放(学生评教)<br/>[手动设置,不限时间]</td>
        <td colspan="3" style="padding-left:10px;">${(textEvaluationSwitch.textEvaluateOpened?string("是","否"))!}</td>
    </tr>
</table>
[@b.form name="actionForm" action="!edit"/]
<script type="text/javaScript">
    function edit(){
        var actionForm = document.actionForm;
        bg.form.submit(actionForm);
    }
</script>
[/@]
[@b.foot/]
--]