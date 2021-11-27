[@b.head/]
[@urp_base.semester_bar value=currentSemester/]
<script>
   document.semesterForm.method="get";
</script>
<div class="search-container">
    <div class="search-panel">
        [@b.form action="!search?orderBy=feedback.updatedAt desc" name="feedbackIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            <input type="hidden" name="feedback.semester.id" value="${currentSemester.id}"/>
            [@b.textfields names="feedback.crn;课程序号,feedback.course.code;课程代码,feedback.course.name;课程名称,feedback.teacher.user.code;教师工号,feedback.teacher.user.name;教师姓名"/]
            [@b.select name="feedback.teachDepart.id" label="开课院系" items=departments empty="..."/]
        [/@]
    </div>
    <div class="search-list">
            [@b.div id="contentDiv" href="!search?orderBy=feedback.updatedAt desc&feedback.semester.id="+currentSemester.id/]
    </div>
</div>
[@b.foot/]
