[#ftl]
[@b.head]
   <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css"/>
[/@]
[@b.toolbar title='院系评教管理' id='departmentEvaluationBar']
[/@]
<table class="indexpanel">
    <tr>
      <td class="index_view">
        [@b.form action="!search?orderBy=departEvaluate.teacher.code asc" name="departEvaluateSearchForm" title="ui.searchForm" target="departEvaluateList" theme="search"]
            [@b.select style="width:100px" name="departEvaluate.semester.id" value= currentSemester label="学年学期" items=semesters?sort_by("code") option="id,code"  /]
            [@b.textfield style="width:100px" name="departEvaluate.teacher.code" label="工号" /]
            [@b.textfield style="width:100px" name="departEvaluate.teacher.person.name.formatedName" label="姓名" /]
            [@b.select style="width:100px" name="passed" label="是否评教" items={'1':'已评','0':'未评'} empty="..."/]
        [/@]
      </td>
      <td class="index_content">
        [@b.div id="departEvaluateList" href="!search"/]
      </td> 
    </tr>
</table>
[@b.foot/]