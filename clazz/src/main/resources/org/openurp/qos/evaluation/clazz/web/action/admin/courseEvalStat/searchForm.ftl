[#ftl]
[@b.head/]
<div class="search-container">
  <div class="search-panel">
        [@b.form action="!search?orderBy=courseEvalStat.score desc" name="evaluateTeacherStatIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            <input type="hidden" name="searchFormFlag" value="${searchFormFlag!}"/>
            [#if searchFormFlag?? || searchFormFlag == "beenStat"]
            <input type="hidden" name="evaluateTeacherStat.clazz.project.id" value="${(project.id)!}"/>
            [@urp_base.semester name="semester.id" label="学年学期" value=currentSemester /]
            [@b.textfields names="courseEvalStat.clazz.crn;课程序号,courseEvalStat.clazz.course.code;课程代码,courseEvalStat.clazz.course.name;课程名称,courseEvalStat.teacher.user.code;教师工号,courseEvalStat.teacher.user.name;教师姓名"/]
            [@b.select name="courseEvalStat.clazz.teachDepart.id" label="开课院系" items=departments empty="..."/]
            [@b.select name="courseEvalStat.questionnaire.id" label="所用问卷" items=[] ]
                [#list questionnaires as q]
                    <option value="${q.id}">${q.description}</option>
                [/#list]
            [/@]
            [#else]
            [@urp_base.semester name="semester.id" label="学年学期" value=currentSemester /]
            <input type="hidden" name="courseEvalStat.clazz.project.id" value="${(project.id)!}"/>
            [@b.textfields names="courseEvalStat.clazz.crn;课程序号,courseEvalStat.clazz.course.code;课程代码,courseEvalStat.clazz.course.name;课程名称,courseEvalStat.teacher.user.code;教师工号,courseEvalStat.teacher.user.name;教师姓名"/]
            [@b.select name="courseEvalStat.depart.id" label="开课院系" items=departments empty="..."/]
            [@b.select name="courseEvalStat.questionnaire.id" label="所用问卷" items=[] ]
                [#list questionnaires as q]
                    <option value="${q.id}">${q.description}</option>
                [/#list]
            [/@]
            [/#if]
        [/@]
  </div>
  <div class="search-list">
            [@b.div id="contentDiv" href="!search?orderBy=courseEvalStat.score desc&semester.id="+currentSemester.id /]
  </div>
</div>
<script type="text/javaScript">
    [#if !searchFormFlag?? || searchFormFlag == "beenStat"]
    var form = document.evaluateTeacherStatIndexForm;

    function changeSemester(){
        bg.form.addInput(form, "evaluateTeacherStat.semester.id", $("input[name='semester.id']").val());
        bg.form.submit(form);
    }
    [#else]
    var form = document.evaluateResultIndexForm;

    function changeSemester(){
        bg.form.addInput(form, "evaluateResult.semester.id", $("input[name='semester.id']").val());
        bg.form.submit(form);
    }
    [/#if]
</script>
[@b.foot/]
