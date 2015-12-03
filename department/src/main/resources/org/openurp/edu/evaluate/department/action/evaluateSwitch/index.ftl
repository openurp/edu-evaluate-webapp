[#ftl]
[@b.head/]
[@b.toolbar title='评教问卷开关' /]
<table  class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="evaluateSwitchSearchForm"  action="!search" target="evaluateSwitchlist" title="ui.searchForm" theme="search"]
      [@b.select name="evaluateSwitch.semester.id" label="学年学期" items=semesters?sort_by("code")  option = "id,code" empty="..."/]
      [@b.select name="evaluateSwitch.questionnaire.id" label="问卷描述" items=questionnaires option = "id,description" empty="..."/]
      [@b.select name="evaluateSwitch.opened" label="开关状态" items={'true':'开启','false':'关闭'}  empty="..."/]
      <input type="hidden" name="orderBy" value="evaluateSwitch.id"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="evaluateSwitchlist" href="!search?orderBy=evaluateSwitch.id" /]</td>
  </tr>
</table>
[@b.foot/]