[#ftl]
[@b.head/]
[#--[@b.navmenu]
    [@b.navitem title="问卷评教" href="/evaluateStd"/]
    [@b.navitem title="文字评教" href="/textEvaluateStudent"/]
[/@]--]
[@b.toolbar title="课程问卷评教" /]

<div class="search-container">
  <div class="search-panel">
            [@b.form name="evaluateIndexForm" action="!search" target="contentDiv" theme="search"]
            [@edu.semester  name="semester.id" label="学年学期" value=currentSemester/]
            [/@]
  </div>
  <div class="search-list">
            [@b.div id="contentDiv"  href="!search?&semester.id=${(semester.id)!}" /]
  </div>
</div>
<script type="text/javascript">
    function changeSemester(num){
        var evaluateIndexForm = document.evaluateIndexForm;
        bg.form.addInput(evaluateIndexForm, "semester.id", num);
        bg.form.submit(evaluateIndexForm);
    }
</script>
[@b.foot/]
