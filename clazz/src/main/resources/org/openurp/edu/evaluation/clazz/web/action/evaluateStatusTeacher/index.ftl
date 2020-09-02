[#ftl]
[@b.head/]
[@b.toolbar title='我的评教回收情况' id='textEvaluationBar'/]

<div class="search-container">
  <div class="search-panel">
            [@b.form name="evaluateSearchIndexForm" action="!search" target="contentDiv" theme="search"]
            [@edu.semester  name="semester.id" label="学年学期" value=currentSemester/]
            [/@]
  </div>
  <div class="search-list">
            [@b.div id="contentDiv"  href="!search" /]
  </div>
</div>
[@b.foot/]
