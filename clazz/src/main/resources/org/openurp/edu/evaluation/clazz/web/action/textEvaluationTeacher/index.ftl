[#ftl]
[@b.head/]
[@b.toolbar title='评教结果' id='textEvaluationTeacherBar' /]

<div class="search-container">
  <div class="search-panel">
        [@b.form action="!search" name="textEvaluationTeacherIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            <input type="hidden" name="clazz.project.id" value="${(project.id)!}"/>
            [@urp_base.semester  name="semester.id" label="学年学期" value=currentSemester/]
            [@b.textfields style="width:130px" names="clazz.crn;课程序号,clazz.course.name;课程名称"/]
        [/@]
  </div>
  <div class="search-list">
            [@b.div id="contentDiv"  href="!search"/]
  </div>
</div>
<script type="text/javaScript">
    var form = document.textEvaluationTeacherIndexForm;

    function changeSemester(){
        bg.form.addInput(form, "clazz.semester.id", $("input[name='semester.id']").val());
        bg.form.submit(form);
    }
</script>
[@b.foot/]
