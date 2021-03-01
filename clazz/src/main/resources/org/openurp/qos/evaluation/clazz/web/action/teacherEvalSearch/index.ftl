[#ftl]
[@b.head/]
[@b.toolbar title='教师-问卷评教结果' id='departmentEvaluateBar']
    bar.addBlankItem();
[/@]

<div class="search-container">
  <div class="search-panel">
        [@b.form action="!search" name="courseEvaluateStatIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
       [@urp_base.semester  name="semester.id" label="学年学期" value=currentSemester/]
       [@b.textfield style="width:100px" name="teacherEvalStat.teacher.user.code" label="教师工号" /]
       [@b.textfield style="width:100px" name="teacherEvalStat.teacher.user.name" label="教师姓名" /]
    <input type="hidden" name="searchFormFlag" value="beenStat"/>
         [/@]
  </div>
  <div class="search-list">
            [@b.div href="!search?semester.id=${(semester.id)!}" id="contentDiv"/]
  </div>
</div>
[@b.foot/]
