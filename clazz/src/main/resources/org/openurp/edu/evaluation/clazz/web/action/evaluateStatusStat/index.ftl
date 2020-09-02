[#ftl]
[@b.head/]
[@b.toolbar title='评教情况统计' /]
<div class="search-container">
  <div class="search-panel">
        [@b.form action="!search" name="evaluateStatusStatIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            [@edu.semester  name="semester.id" label="学年学期" value=currentSemester/]
            [@b.select name="department.id" label="开课院系" items=departments empty="..."/]
            [@b.textfield name="clazz.crn" label="课程序号"/]
            [@b.textfield name="course.code" label="课程代码"/]
            [@b.textfield name="course.name" label="课程名称"/]
            [@b.textfield style="width:100px" name="teacher.name" label="教师姓名" /]
        [/@]
  </div>
  <div class="search-list">
            [@b.div href="!search?semester.id=${(semester.id)!}" id="contentDiv"/]
  </div>
</div>
[@b.foot/]
