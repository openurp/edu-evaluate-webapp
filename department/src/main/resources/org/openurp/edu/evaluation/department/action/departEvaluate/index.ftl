[#ftl]
[@b.head]
   <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css"/>
[/@]
[@b.toolbar title='院系评教管理' id='departmentEvaluateBar'/]
<table class="indexpanel">
    <tr>
      <td class="index_view">
        [@b.form action="!search?orderBy=staff.code asc" name="departEvaluateSearchForm" title="ui.searchForm" target="departEvaluateList" theme="search"]
            [@b.select style="width:100px" name="departEvaluate.semester.id" value= currentSemester label="学年学期" items=semesters option="id,code"  /]
            [@b.textfield style="width:100px" name="departEvaluate.staff.person.code" label="工号" /]
            [@b.textfield style="width:100px" name="departEvaluate.staff.person.name" label="姓名" /]
            [@b.select style="width:100px" name="departEvaluate.staff.department.id" label="所在院系" items=departments empty="..."/]
            [@b.select style="width:100px" name="passed" label="是否评教" items={'1':'已评','0':'未评'} empty="..."/]
        [/@]
      </td>
      <td class="index_content">
        [@b.div id="departEvaluateList" href="!search?orderBy=staff.code asc&departEvaluate.semester.id=" +currentSemester.id/]
      </td> 
    </tr>
</table>
[@b.foot/]