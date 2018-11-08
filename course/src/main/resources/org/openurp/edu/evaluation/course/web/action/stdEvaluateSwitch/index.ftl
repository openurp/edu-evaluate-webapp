[#ftl]
[@b.head /]
[@b.toolbar title='学生问卷评教开关' /]
<table class="indexpanel">
 <tr>
  <td  style="width:180px" class="index_view">
     [@b.form name="evaluateSwitchSearchForm"  action="!search" target="evaluateSwitchlist" title="ui.searchForm" theme="search" ]
     [@b.select  name="evaluateSwitch.semester.id" label="学年学期" items=semesters?sort_by("code")  option = "id,code" empty="..."/]
     [@b.select  name="evaluateSwitch.opened" label="开关状态" items={'1':'开放','0':'关闭'} value='1'/]
     <input type="hidden" name="orderBy" value="stdEvaluateSwitch.id"/>
    [/@]
    </td>
  <td class="index_content">
     [@b.div id="evaluateSwitchlist" href="!search?evaluateSwitch.opened=" +1 /]
    </td>
    </tr>
</table>
[@b.foot/]
