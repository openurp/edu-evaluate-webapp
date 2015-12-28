[#ftl]
[@b.head]
   <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css"/>
[/@]
[@b.toolbar title='督导组评教管理' id='departmentEvaluateBar'/]
<table class="indexpanel">
    <tr>
      <td class="index_view">
        [@b.form action="!search?orderBy=staff.code asc" name="supervisiorEvaluateSearchForm" title="ui.searchForm" target="supervisiorEvaluateList" theme="search"]
            [@b.select style="width:100px" name="supervisiorEvaluate.semester.id" value= currentSemester label="学年学期" items=semesters option="id,code"  /]
            [@b.textfield style="width:100px" name="supervisiorEvaluate.staff.person.code" label="工号" /]
            [@b.textfield style="width:100px" name="supervisiorEvaluate.staff.person.name" label="姓名" /]
            [@b.select style="width:100px" name="supervisiorEvaluate.staff.department.id" label="所在院系" items=departments empty="..."/]
            [@b.select style="width:100px" name="passed" label="是否评教" items={'1':'已评','0':'未评'} empty="..."/]
        [/@]
      </td>
      <td class="index_content">
        [@b.div id="supervisiorEvaluateList" href="!search?orderBy=staff.code asc&supervisiorEvaluate.semester.id=" +currentSemester.id/]
      </td> 
    </tr>
</table>
[@b.foot/]