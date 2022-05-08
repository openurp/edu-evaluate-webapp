[@b.head/]
[@b.nav class="nav-tabs nav-tabs-compact"]
  [@b.navitem href="feedback"]过程文字评价[/@]
  [@b.navitem href="final-comment"]期末文字评价[/@]
[/@]
[@urp_base.semester_bar value=currentSemester/]
<script>
   document.semesterForm.method="get";
</script>
<div class="search-container">
    <div class="search-panel">
        [@b.form action="!search?orderBy=finalComment.updatedAt desc" name="finalCommentIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            <input type="hidden" name="finalComment.semester.id" value="${currentSemester.id}"/>
            [@b.textfields names="finalComment.crn;课程序号,finalComment.course.code;课程代码,finalComment.course.name;课程名称,finalComment.teacher.user.code;教师工号,finalComment.teacher.user.name;教师姓名"/]
            [@b.select name="finalComment.teachDepart.id" label="开课院系" items=departments empty="..."/]
        [/@]
    </div>
    <div class="search-list">
            [@b.div id="contentDiv" href="!search?orderBy=finalComment.updatedAt desc&finalComment.semester.id="+currentSemester.id/]
    </div>
</div>
[@b.foot/]
