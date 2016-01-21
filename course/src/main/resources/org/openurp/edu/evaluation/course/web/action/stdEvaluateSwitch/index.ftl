[#ftl]
[@b.head /]
[@b.toolbar title='学生问卷评教开关' /]
<table class="indexpanel">
 <tr>
  <td class="index_content">
     [@b.form name="evaluateSwitchSearchForm"  action="!search" target="evaluateSwitchlist" title="ui.searchForm" theme="search"]
     [@b.select  name="evaluateSwitch.semester.id" label="学年学期" items=semesters?sort_by("code")  option = "id,code" empty="..."/]
     [@b.select  name="evaluateSwitch.project.id" label="教学项目" items=projects?sort_by("id") value=defaultProject   option = "name" empty="..."/]
     [@b.select  name="evaluateSwitch.opened" label="开关状态" items={'1':'开放','0':'关闭'} value='1'  empty="..."/]
     <input type="hidden" name="orderBy" value="stdEvaluateSwitch.id"/>
    [/@]
    </td> 
  <td class="index_content">
     [@b.div id="evaluateSwitchlist" href="!search?evaluateSwitch.opened=" +1 /]
    </td>
    </tr>
</table>
[@b.foot/]