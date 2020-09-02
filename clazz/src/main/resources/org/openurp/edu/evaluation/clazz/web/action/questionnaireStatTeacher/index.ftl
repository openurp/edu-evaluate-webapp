[#ftl]
[@b.head/]
[@b.toolbar title='我的评教结果' id='textEvaluationBar'/]

<div class="search-container">
  <div class="search-list">
            [@b.form name="evaluateIndexForm" action="!search" target="contentDiv"]
            <input type="hidden" name="questionnaireStat.teacher.id" value="${teacher.id}">
            [/@]
            [@b.div href="!search" id="contentDiv"/]
  </div>
</div>
[@b.foot/]
