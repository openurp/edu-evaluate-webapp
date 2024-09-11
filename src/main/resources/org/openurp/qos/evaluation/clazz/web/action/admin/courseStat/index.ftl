[@b.head/]
[@base.semester_bar value=currentSemester/]
<script>
   document.semesterForm.method="get";
</script>
<div class="search-container">
    <div class="search-panel">
        [@b.form action="!search?orderBy=courseEvalStat.score desc" name="courseEvalStatIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            <input type="hidden" name="courseEvalStat.semester.id" value="${currentSemester.id}"/>
            [@b.textfields names="courseEvalStat.crn;课程序号,courseEvalStat.course.code;课程代码,courseEvalStat.course.name;课程名称"/]
            [@b.textfield name="courseEvalStat.teacher.staff.code" label="教师工号" maxlength="500"/]
            [@b.textfield name="courseEvalStat.teacher.name" label="教师名称" maxlength="500"/]
            [@b.select name="courseEvalStat.teachDepart.id" label="开课院系" items=departments empty="..."/]
            [@b.select name="courseEvalStat.category.id" label="课程大类" items=categories empty="..."/]
            [@b.select name="courseEvalStat.grade.id" label="评价档次" items=grades?sort_by("grade") empty="..."/]
        [/@]
    </div>
    <div class="search-list">
        [@b.div id="contentDiv" href="!search?orderBy=courseEvalStat.score desc&courseEvalStat.semester.id="+currentSemester.id/]
    </div>
</div>
[@b.foot/]
