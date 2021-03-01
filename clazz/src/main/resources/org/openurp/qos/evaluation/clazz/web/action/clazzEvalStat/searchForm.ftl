[#ftl]
[@b.head/]
<div class="search-container">
  <div class="search-panel">
        [@b.form action="!search?orderBy=clazzEvalStat.avgScore desc" name="evaluateTeacherStatIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            <input type="hidden" name="searchFormFlag" value="${searchFormFlag!}"/>
            [#if searchFormFlag?? || searchFormFlag == "beenStat"]
            <input type="hidden" name="evaluateTeacherStat.clazz.project.id" value="${(project.id)!}"/>
            [@urp_base.semester name="semester.id" label="学年学期" value=currentSemester /]
            [@b.textfields names="clazzEvalStat.clazz.crn;课程序号,clazzEvalStat.clazz.course.code;课程代码,clazzEvalStat.clazz.course.name;课程名称,clazzEvalStat.teacher.user.code;教师工号,clazzEvalStat.teacher.user.name;教师姓名"/]
            [@b.select name="clazzEvalStat.clazz.teachDepart.id" label="开课院系" items=departments empty="..."/]
            [@b.select name="clazzEvalStat.questionnaire.id" label="所用问卷" items=[] ]
                [#list questionnaires as q]
                    <option value="${q.id}">${q.description}</option>
                [/#list]
            [/@]
            [#else]
            [@urp_base.semester name="semester.id" label="学年学期" value=currentSemester /]
            <input type="hidden" name="clazzEvalStat.clazz.project.id" value="${(project.id)!}"/>
            [@b.textfields names="clazzEvalStat.clazz.crn;课程序号,clazzEvalStat.clazz.course.code;课程代码,clazzEvalStat.clazz.course.name;课程名称,clazzEvalStat.teacher.user.code;教师工号,clazzEvalStat.teacher.user.name;教师姓名"/]
            [@b.select name="clazzEvalStat.depart.id" label="开课院系" items=departments empty="..."/]
            [@b.select name="clazzEvalStat.questionnaire.id" label="所用问卷" items=[] ]
                [#list questionnaires as q]
                    <option value="${q.id}">${q.description}</option>
                [/#list]
            [/@]
            [/#if]
        [/@]
  </div>
  <div class="search-list">
            [@b.div id="contentDiv" href="!search?orderBy=clazzEvalStat.avgScore desc&semester.id="+currentSemester.id /]
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
