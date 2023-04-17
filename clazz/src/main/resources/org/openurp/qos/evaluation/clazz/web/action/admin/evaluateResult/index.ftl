[#ftl]
[@b.head/]
[@b.toolbar title='评教结果管理' id='evaluateResultBar' ]
 bar.addItem("将考试违纪的学生评教问卷置为无效","changeToInvalid()");
[/@]
<div class="search-container">
  <div class="search-panel">
        [@b.form action="!search" name="evaluateResultIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            [@base.semester name="semester.id" label="学年学期"  value=currentSemester required="true"/]
            [@b.textfields names="evaluateResult.student.code;学生学号,evaluateResult.student.name;学生姓名,evaluateResult.student.grade;学生年级,evaluateResult.clazz.crn;课程序号,evaluateResult.clazz.course.code;课程代码,evaluateResult.clazz.course.name;课程名称,evaluateResult.teacher.name;教师姓名"/]
            [@b.select name="statType" label="是否有效" items={'1':'有效','0':'无效'} empty="..."/]
        [/@]
  </div>
  <div class="search-list">
         [@b.div id="contentDiv" href="!search?semester.id="+currentSemester.id/]
  </div>
</div>
[@b.foot/]
<script>
  function changeToInvalid(){
     var form =document.evaluateResultIndexForm;
     //form.action="${b.url('!changeToInvalid')}";
     bg.form.submit(form,"${b.url('!changeToInvalid')}");
  }
</script>
