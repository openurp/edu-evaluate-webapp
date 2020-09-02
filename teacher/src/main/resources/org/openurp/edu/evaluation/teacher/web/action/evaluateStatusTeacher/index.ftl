[#ftl]
[@b.head/]
[@b.toolbar title='我的评教回收情况' id='textEvaluationBar'/]

<div class="search-container">
  <div class="search-panel">
            [@b.form name="evaluateSearchIndexForm" action="!search" target="contentDiv" theme="search"]
            [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
            [/@]
  </div>
  <div class="search-list">
            [@b.div id="contentDiv"  href="!search" /]
  </div>
</div>
[@b.foot/]
