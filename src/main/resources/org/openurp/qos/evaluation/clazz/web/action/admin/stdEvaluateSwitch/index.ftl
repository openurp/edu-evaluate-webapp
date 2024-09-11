[#ftl]
[@b.head /]
[@b.toolbar title='学生问卷评教开关' /]
<div class="search-container">
  <div class="search-panel">
     [@b.form name="evaluateSwitchSearchForm"  action="!search" target="evaluateSwitchlist" title="ui.searchForm" theme="search" ]
     [@base.semester  name="semester.id" label="学年学期" value=currentSemester/]
     [@b.select  name="evaluateSwitch.opened" label="开关状态" items={'1':'开放','0':'关闭'} value='1'/]
     <input type="hidden" name="orderBy" value="stdEvaluateSwitch.id"/>
    [/@]
  </div>
  <div class="search-list">
     [@b.div id="evaluateSwitchlist" href="!search?evaluateSwitch.opened=" +1 /]
  </div>
</div>
[@b.foot/]
