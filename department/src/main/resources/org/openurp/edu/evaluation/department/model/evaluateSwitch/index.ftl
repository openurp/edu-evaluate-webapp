[#ftl]
[@b.head/]
[@b.toolbar title='评教开关' /]
<table  class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="evaluateSwitchSearchForm"  action="!search" target="evaluateSwitchlist" title="ui.searchForm" theme="search"]
      [@b.select name="evaluateSwitch.semester.id" label="学年学期" items=semesters  empty="..."/]
      [@b.select name="evaluateSwitch.questionnaire.id" label="问卷标题" items=questionnaires  empty="..."/]
      [@b.select name="evaluateSwitch.opened" label="开关状态" items={'true':'是','false':'否'}  empty="..."/]
      <input type="hidden" name="orderBy" value="evaluateSwitch.id"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="evaluateSwitchlist" href="!search?orderBy=evaluateSwitch.id" /]</td>
  </tr>
</table>
[@b.foot/]