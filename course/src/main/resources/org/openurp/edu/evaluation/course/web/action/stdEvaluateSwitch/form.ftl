[#ftl]
[@b.head/]
[@b.toolbar title="评教开关信息"]
    bar.addBack();
[/@]
[@b.form name="evaluateSwitchEditForm" action="!save" title="评教开关信息" theme="list"]
    <input type="hidden" id="semesterId" name="evaluateSwitch.semester.id" value="${semesterId!}" />
    <input type="hidden" id="projectId" name="evaluateSwitch.project.id" value="${(evaluateSwitch.project.id)!}"/>
    [@b.select label="是否开放" name="evaluateSwitch.isOpen" items={'1':'是','0':'否'} value=((evaluateSwitch.isOpen)?string("1","0"))! required="true" empty="..."/]
    [#--[@b.startend label="有效时间范围" name="evaluateSwitch.beginAt,evaluateSwitch.endAt" required="true" start=evaluateSwitch.beginAt end=evaluateSwitch.endAt format="yyyy-MM-dd HH:mm" /]--]
    [@b.datepicker label="开始时间" required="true" name="evaluateSwitch.beginAt" id="_beginAt" format="yyyy-MM-dd HH:mm" maxDate="#F{$dp.$D(\\'_endAt\\')}" value=(evaluateSwitch.beginAt?string("yyyy-MM-dd HH:mm"))! maxlength="10" style="width:200px"/]
    [@b.datepicker label="结束时间" required="true" name="evaluateSwitch.endAt" id="_endAt" format="yyyy-MM-dd HH:mm" minDate="#F{$dp.$D(\\'_beginAt\\')}" value=(evaluateSwitch.endAt?string("yyyy-MM-dd HH:mm"))! maxlength="10" style="width:200px"/]
    [@b.formfoot]
        <input type="hidden" name="evaluateSwitch.id" value="${(evaluateSwitch.id)!}" />
        [@b.submit value="action.save"/]
        <input type="reset" name="evaluateSwitchReset" value="${b.text("action.reset")}" />
    [/@]
[/@]
[@b.foot/]