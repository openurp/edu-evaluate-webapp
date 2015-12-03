[#ftl]
[@b.head/]
[@b.form name="evaluateSwitchSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=stdEvaluateSwitchs var="evaluateSwitch" sortable="true"]    
        [@b.gridbar title="评教开关列表"]
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
            [@b.col title="已设置问卷课程数"]
                ${questionnaireCount}
            [/@]
            [@b.col title="未设置问卷课程数"]
                ${lessonCount}
            [/@]
        [/@]
    [/@]
[/@]
[@b.foot/]