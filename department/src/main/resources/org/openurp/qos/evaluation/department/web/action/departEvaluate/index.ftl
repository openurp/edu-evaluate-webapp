[#ftl]
[@b.head]
   <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css"/>
[/@]
[@b.toolbar title='院系评教管理' id='departmentEvaluationBar']
  bar.addItem("导入被评教师名单","importTeachers()");
[/@]
<div class="search-container">
  <div class="search-panel">
        [@b.form action="!search?orderBy=departEvaluate.teacher.user.code asc" name="departEvaluateSearchForm" title="ui.searchForm" target="departEvaluateList" theme="search"]
            [@b.select style="width:100px" name="departEvaluate.semester.id" value= currentSemester label="学年学期" items=semesters?sort_by("code") option="id,code"  /]
            [@b.textfield style="width:100px" name="departEvaluate.teacher.user.code" label="工号" /]
            [@b.textfield style="width:100px" name="departEvaluate.teacher.user.name" label="姓名" /]
            [@b.select style="width:100px" name="passed" label="是否评教" items={'1':'已评','0':'未评'} empty="..."/]
        [/@]
  </div>
  <div class="search-list">
        [@b.div id="departEvaluateList" href="!search"/]
  </div>
</div>
<script type="text/javascript">
  function importTeachers(){
    bg.form.submit(document.departEvaluateSearchForm , "${b.url('!importTeachers')}")
  }
</script>
[@b.foot/]
