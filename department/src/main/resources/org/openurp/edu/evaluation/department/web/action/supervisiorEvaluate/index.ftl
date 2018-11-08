[#ftl]
[@b.head]
   <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css"/>
[/@]
[@b.toolbar title='督导组评教管理' id='supervisiorEvaluateBar']
  bar.addItem("导入被评教师名单","importTeachers()");
[/@]
<table class="indexpanel">
    <tr>
      <td class="index_view">
        [@b.form action="!search?orderBy=supervisiorEvaluate.teacher.user.code asc" name="supervisiorEvaluateSearchForm" title="ui.searchForm" target="supervisiorEvaluateList" theme="search"]
            [@b.select style="width:100px" name="supervisiorEvaluate.semester.id" value= currentSemester label="学年学期" items=semesters?sort_by("code") option="id,code"  /]
            [@b.textfield style="width:100px" name="supervisiorEvaluate.teacher.user.code" label="工号" /]
            [@b.textfield style="width:100px" name="supervisiorEvaluate.teacher.user.name" label="姓名" /]
            [@b.select style="width:100px" name="supervisiorEvaluate.department.id" label="开课院系" items=departments empty="..."/]
            [@b.select style="width:100px" name="passed" label="是否评教" items={'1':'已评','0':'未评'} empty="..."/]
        [/@]
      </td>
      <td class="index_content">
        [@b.div id="supervisiorEvaluateList" href="!search"/]
      </td>
    </tr>
</table>
<script type="text/javascript">
  function importTeachers(){
    bg.form.submit(document.supervisiorEvaluateSearchForm , "${b.url('!importTeachers')}")
  }
</script>
[@b.foot/]
