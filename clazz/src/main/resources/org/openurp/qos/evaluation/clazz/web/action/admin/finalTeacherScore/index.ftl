[#ftl]
[@b.head/]
[@b.toolbar title='教师综合评教结果' id='finalTeacherScoreBar']
bar.addItem("初始化/重新统计","statisticResult()");
bar.addItem("排名统计","statisticRank()");
    bar.addBlankItem();
[/@]

<div class="search-container">
  <div class="search-panel">
        [@b.form action="!search" name="finalTeacherScoreIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
         [@urp_base.semester  name="semester.id" label="学年学期" value=currentSemester/]
         [@b.select style="width:134px" name="finalTeacherScore.teacher.department.id" label="教师所属院系" items=departments empty="..."/]
         [@b.textfield style="width:100px" name="finalTeacherScore.teacher.staff.code" label="教师工号" /]
         [@b.textfield style="width:100px" name="finalTeacherScore.teacher.name" label="教师姓名" /]
         [/@]
  </div>
  <div class="search-list">
             [@b.div id="contentDiv" href="!search"/]
  </div>
</div>
<script type="text/javaScript">
    var form = document.finalTeacherScoreIndexForm;
    function statisticResult(){
        form.target="_blank";
        bg.form.submit(form, "${b.url('!statHome')}","main");
        form.target="contentDiv";
    }
    function statisticRank(){
        bg.form.addInput(form,"semester.id",document.finalTeacherScoreIndexForm['semester.id'].value);
        bg.form.submit(form, "${b.url('!rankStat')}");
    }

</script>
[@b.foot/]
