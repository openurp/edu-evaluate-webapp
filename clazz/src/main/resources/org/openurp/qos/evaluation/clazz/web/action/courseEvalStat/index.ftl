[#ftl]
[@b.head/]
[@b.toolbar title='课程-问卷评教结果统计' id='departmentEvaluateBar' ]
    //var detailMenu = bar.addMenu("院系课程评教比较","departmentChoiceConfig()");
    //    detailMenu.addItem("学院历史评教", "depHistoryStat()", "info.png");
    //bar.addItem("学校教师评教历史汇总","historyCollegeStat()");
    //bar.addItem("学校教师评教分项汇总","collegeGroupItemInfo()");
    bar.addItem("初始化/重新统计","statisticResult()");
[/@]
<div class="search-container">
  <div class="search-panel">
        [@b.form action="!search?orderBy=courseEvalStat.totalScore desc" name="courseEvaluateStatIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            [@urp_base.semester name="courseEvalStat.semester.id" label="学年学期" value=currentSemester /]
            [@b.textfields names="courseEvalStat.course.code;课程代码,courseEvalStat.course.name;课程名称,courseEvalStat.teacher.user.code;教师工号,courseEvalStat.teacher.user.name;教师姓名"/]
            [@b.select name="courseEvalStat.teacher.user.department.id" label="教师院系" items=departments empty="..."/]
            [@b.select name="courseEvalStat.questionnaire.id" label="所用问卷" items=[] ]
                [#list questionnaires as q]
                    <option value="${q.id}">${q.description}</option>
                [/#list]
            [/@]
        [/@]
  </div>
  <div class="search-list">
            [@b.div id="contentDiv" href="!search?orderBy=courseEvalStat.totalScore desc&courseEvalStat.semester.id="+currentSemester.id/]
  </div>
</div>
<script type="text/javaScript">
    var form = document.courseEvaluateStatIndexForm;

    function departmentChoiceConfig(){
        bg.form.addInput(form,"semester.id",document.courseEvaluateStatIndexForm['semester.id'].value);
      [#--  bg.form.submit(form, "${b.url('!departmentChoiceConfig')}");--]
    }
    function depHistoryStat(){
       [#-- bg.form.submit(form, "${b.url('!depHistoryStat')}");--]
    }
    function historyCollegeStat(){
       [#-- bg.form.submit(form, "${b.url('!historyCollegeStat')}");--]
    }

    function changeSemester(){
        bg.form.addInput(form, "semester.id", $("input[name='semester.id']").val());
        bg.form.submit(form);
    }
    function collegeGroupItemInfo(){
        bg.form.addInput(form,"semester.id",document.courseEvaluateStatIndexForm['semester.id'].value);
        [#-- bg.form.submit(form, "${b.url('!collegeGroupItemInfo')}");--]
    }
   function statisticResult(){
        form.target="_blank";
        bg.form.submit(form, "${b.url('!statHome')}","main");
        form.target="contentDiv";
    }

</script>
[@b.foot/]
