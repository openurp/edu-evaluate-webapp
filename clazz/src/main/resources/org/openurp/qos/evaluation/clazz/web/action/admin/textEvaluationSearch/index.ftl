[#ftl]
[@b.head/]
[@b.toolbar title='查询文字评教' id='textEvaluationBar' /]

<div class="search-container">
  <div class="search-panel">
        [@b.form action="!search" name="textEvaluationIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            <input type="hidden" name="textEvaluation.clazz.project.id" value="${(project.id)!}"/>
            [@urp_base.semester  name="semester.id" label="学年学期" value=currentSemester/]
            [@b.textfields style="width:130px" names="textEvaluation.clazz.crn;课程序号,textEvaluation.clazz.course.code;课程代码,textEvaluation.clazz.course.name;课程名称,textEvaluation.teacher.staff.code;教师工号,textEvaluation.teacher.name;教师姓名"/]
            [@b.select style="width:134px" name="textEvaluation.clazz.teachDepart.id" label="开课院系" items=departments empty="..."/]
            [@b.select style="width:134px" name="textEvaluation.audited" label="是否确认" items={'1':'已确认','0':'未确认'} empty="..."/]
        [/@]
  </div>
  <div class="search-list">
            [@b.div id="contentDiv" href="!search"/]
  </div>
</div>
<script type="text/javaScript">
    var form = document.textEvaluationIndexForm;

    function changeSemester(){
        bg.form.addInput(form, "textEvaluation.semester.id", $("input[name='semester.id']").val());
        bg.form.submit(form);
    }
</script>
[@b.foot/]
