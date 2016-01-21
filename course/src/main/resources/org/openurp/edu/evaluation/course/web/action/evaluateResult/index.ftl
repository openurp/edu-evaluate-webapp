[#ftl]
[@b.head/]
[@b.toolbar title='评教结果管理' id='evaluateResultBar' ]
 bar.addItem("将考试违纪的学生评教问卷置为无效","changeToInvalid()");
[/@]

[#--[@eams.semesterBar name="project.id" semesterEmpty=false semesterName="semester.id" semesterValue=semester/]--]
<table class="indexpanel">
    <tr>
        <td style="width:200px" class="index_view">
        [@b.form action="!search" name="evaluateRForm" title="ui.searchForm" target="contentDiv" theme="search"]
            [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
            [@b.textfields style="width:120px" names="evaluateResult.student.code;学生学号,evaluateResult.student.person.name.formatedName;学生姓名,evaluateResult.student.grade;学生年级,evaluateResult.lesson.no;课程序号,evaluateResult.lesson.course.code;课程代码,evaluateResult.lesson.course.name;课程名称"/]
            [@b.select style="width:124px" name="statType" label="是否有效" items={'1':'有效','0':'无效'} empty="..."/]
        [/@]
        </td>
        <td class="index_content">
         [@b.div id="contentDiv" href="!search"/]
            [#--[@b.div  href="!search?evaluateResult.lesson.semester.id=${(semester.id)!}" id="contentDiv"/]--]
        </td> 
    </tr>
</table>
[@b.foot/]
<script>
  function changeToInvalid(){
     var form =document.evaluateRForm;
     //form.action="${b.url('!changeToInvalid')}";
     bg.form.submit(form,"${b.url('!changeToInvalid')}");
  }
</script>







[#--
<#include "/template/head.ftl"/>
<body>
    <table width="100%" height="100%" style="padding: 0px; border-spacing: 0px" cellspacing="0" cellpadding="0">
        <tr height="2%" valign="top">
            <td style="padding: 0px; border-spacing: 0px">
                <table id="bar"></table>
            </td>
        </tr>
        <tr height="2%" valign="top">
            <td style="padding: 0px; border-spacing: 0px">
                <table class="frameTable_title">
                    <form method="post" name="actionForm" action="">
                    <tr>
                        <td></td>
                        <#include "/template/time/semester.ftl"/>
                        <input type="hidden" name="evaluateResult.semester.id" value="${semester.id}"/>
                    </tr>
                </table>
            </td>
        </tr>
        <tr valign="top">
            <td style="padding: 0px; border-spacing: 0px">
                <table class="frameTable" width="100%" height="100%">
                    <tr valign="top">
                        <td class="frameTable_view" width="22%"><#include "searchForm.ftl"/></td>
                    </form>
                        <td><iframe name="resultFrame" src="#" width="100%" frameborder="0" scrolling="no"></iframe></td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <script>
        var bar = new ToolBar("bar", "维护评教结果", null, true, true);
        bar.setMessage('<@getMessage/>');
        
        var form = document.actionForm;
        function search() {
            form.action = "evaluateResult.action?method=search";
            form.target = "resultFrame";
            form.submit();
        }
        search();
    </script>
</body>
<#include "/template/foot.ftl"/>
--]