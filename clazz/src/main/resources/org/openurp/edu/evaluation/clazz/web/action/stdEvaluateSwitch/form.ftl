[#ftl]
[@b.head/]
[@b.toolbar title="学生问卷评教开关信息"]bar.addBack();[/@]
  [#assign sa][#if stdEvaluateSwitch.persisted]!update?id=${stdEvaluateSwitch.id}[#else]!save[/#if][/#assign]
 [@b.form action=sa theme="list"  enctype="multipart/form-data"]
    [@edu.semester  name="stdEvaluateSwitch.semester.id" label="学年学期" value=stdEvaluateSwitch.semester!/]
    [@b.select label="开放状态" name="stdEvaluateSwitch.opened" items={'1':'开放','0':'关闭'} value=((stdEvaluateSwitch.opened)?string("1","0"))! required="true" empty="..."/]
    [@b.startend label="开始结束时间"
    name="stdEvaluateSwitch.beginAt,stdEvaluateSwitch.endAt" required="true,true"
    start=stdEvaluateSwitch.beginAt end=stdEvaluateSwitch.endAt format="yyyy-MM-dd HH:mm" /]
    [@b.formfoot]
        <input name="stdEvaluateSwitch.project.id" type="hidden" value="${project.id}"/>
        [#if stdEvaluateSwitch.persisted]<input type="hidden" name="stdEvaluateSwitch.id" value="${stdEvaluateSwitch.id!}" />[/#if]
        [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
[/@]
[@b.foot/]
