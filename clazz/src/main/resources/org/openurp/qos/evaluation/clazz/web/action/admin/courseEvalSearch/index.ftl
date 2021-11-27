[#ftl]
[@b.head/]
[@b.toolbar title='任务-问卷评教结果' id='departmentEvaluateBar']
    bar.addBlankItem();
[/@]

<div class="search-container">
  <div class="search-panel">
       [@b.form action="!search" name="courseEvaluateStatIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
       [@urp_base.semester name="semester.id" label="学年学期" value=currentSemester /]
       [@b.textfield style="width:100px" name="courseEvalStat.clazz.crn" label="课程序号" /]
       [@b.textfield style="width:100px" name="courseEvalStat.course.code" label="课程代码" /]
       [@b.textfield style="width:100px" name="courseEvalStat.course.name" label="课程名称" /]
    <input type="hidden" name="searchFormFlag" value="beenStat"/>
         [/@]
  </div>
  <div class="search-list">
            [@b.div href="!search?semester.id=${(semester.id)!}" id="contentDiv"/]
  </div>
</div>
[@b.foot/]
