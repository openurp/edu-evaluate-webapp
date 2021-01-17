[#ftl]
[@b.head/]
[@b.toolbar title="文字评教开关设置"]bar.addBack();[/@]
  [#assign sa][#if textEvaluateSwitch.persisted]!update?id=${textEvaluateSwitch.id}[#else]!save[/#if][/#assign]
[@b.form action=sa title="文字评教开关" theme="list"  enctype="multipart/form-data"]
    [@urp_base.semester  name="textEvaluateSwitch.semester.id" label="学年学期" value=textEvaluateSwitch.semester/]
    [@b.select name="textEvaluateSwitch.project.id" label="教学项目" items=projects?sort_by("id") option = "name" value =textEvaluateSwitch.project empty="..."/]
    [@b.select label="开放状态" required="true" name="textEvaluateSwitch.opened" value=((textEvaluationSwitch.opened)?string("1","0"))! items={'1':'开放','0':'关闭'} empty="..." /]
    [@b.select label="教师查询" required="true" name="textEvaluateSwitch.openedTeacher" value=((textEvaluateSwitch.openedTeacher)?string("1","0"))! items={'1':'开放','0':'关闭'} empty="..." /]
    [@b.select label="不限时开放学生文字评教" name="textEvaluateSwitch.textEvaluateOpened"  items={'1':'开放','0':'关闭'}  value=((textEvaluateSwitch.textEvaluateOpened)?string("1","0"))! required="true"  empty="..." /]
    [@b.startend label="开始结束时间"
    name="textEvaluateSwitch.beginAt,textEvaluateSwitch.endAt" required="true,true"
    start=textEvaluateSwitch.beginAt end=textEvaluateSwitch.endAt format="yyyy-MM-dd HH:mm" /]
    [@b.formfoot]
        [#if textEvaluateSwitch.persisted]<input type="hidden" name="textEvaluateSwitch.id" value="${textEvaluateSwitch.id!}" />[/#if]
        [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
[/@]
<script type="text/javaScript">
    $("select[name='textEvaluateSwitch.openedTeacher']").parent().css("height","40px");
    $("select[name='textEvaluateSwitch.textEvaluateOpened']").parent().css("height","50px");
</script>
[@b.foot/]
