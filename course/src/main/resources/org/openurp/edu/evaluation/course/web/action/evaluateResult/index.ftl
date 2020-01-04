[#ftl]
[@b.head/]
[@b.toolbar title='评教结果管理' id='evaluateResultBar' ]
 bar.addItem("将考试违纪的学生评教问卷置为无效","changeToInvalid()");
[/@]

<table class="indexpanel">
    <tr>
        <td  style="width:180px" class="index_view">
        [@b.form action="!search" name="evaluateResultIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            [@edu_base.semester name="semester.id" label="学年学期"  value=currentSemester required="true"/]
            [@b.textfields names="evaluateResult.student.user.code;学生学号,evaluateResult.student.user.name;学生姓名,evaluateResult.student.grade;学生年级,evaluateResult.clazz.crn;课程序号,evaluateResult.clazz.course.code;课程代码,evaluateResult.clazz.course.name;课程名称,evaluateResult.teacher.user.name;教师姓名"/]
            [@b.select name="statType" label="是否有效" items={'1':'有效','0':'无效'} empty="..."/]
        [/@]
        </td>
        <td class="index_content">
         [@b.div id="contentDiv" href="!search?semester.id="+currentSemester.id/]
        </td>
    </tr>
</table>
[@b.foot/]
<script>
  function changeToInvalid(){
     var form =document.evaluateResultIndexForm;
     //form.action="${b.url('!changeToInvalid')}";
     bg.form.submit(form,"${b.url('!changeToInvalid')}");
  }
</script>
