[#ftl]
[@b.head/]
[@b.grid  items=evaluateSwitches var="evaluateSwitch" sortable="false"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="10%" property="semester.code" title="学年学期"]${(evaluateSwitch.semester.code)!}[/@]
    [@b.col width="10%" property="questionnaire.title" title="问卷描述"][@b.a href="../../questionnaire/questionnaire!info?id=${evaluateSwitch.questionnaire.id}"]${(evaluateSwitch.questionnaire.description)!}[/@][/@]
    [@b.col width="10%" property="beginOn" title="开始时间"]${(evaluateSwitch.beginOn)!}[/@]
    [@b.col width="10%" property="ednOn" title="结束时间"]${evaluateSwitch.endOn!}[/@]
    [@b.col width="10%" property="opened" title="开关状态"]${((evaluateSwitch.opened)?string('开启','关闭'))!}[/@]
  [/@]
[/@]
[@b.foot/]
