[#ftl]
[@b.head/]
[@b.toolbar title='管理文字评教' id='textEvaluationBar' /]

[#--[@eams.semesterBar semesterValue=semester name="project.id" semesterName="semester.id" semesterEmpty="false" initCallback="changeSemester()"/]--]
<table class="indexpanel">
    <tr>
        <td style="width:200px" class="index_view">
        [@b.form action="!search" name="textEvaluationIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            <input type="hidden" name="lesson.project.id" value="${(project.id)!}"/>
            [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
            [@b.textfields style="width:130px" names="lesson.no;课程序号,lesson.course.code;课程代码,lesson.course.name;课程名称,teacher.user.name;教师名称"/]
            [@b.select style="width:134px" name="lesson.teachDepart.id" label="开课院系" items=departments empty="..."/]
            [@b.select style="width:134px" name="state" label="是否确认" items={'1':'已确认','0':'未确认'}  empty="..."/]
        [/@]
        </td>
        <td class="index_content">
            [@b.div id="contentDiv" href="!search"/]
        </td> 
    </tr>
</table>
[@b.foot/]








[#--
<#include "/template/head.ftl"/>
<BODY topmargin=0 leftmargin=0>
  <table id="bar" width="100%"></table>
   <table  class="frameTable_title">
      <tr>
       <td  style="width:50px" >
          <font color="blue"><@text name="action.advancedQuery"/></font>
       </td>
       <td>|</td>
      <form name="evaluateForm" method="post" action="textEvaluation.action?method=index" action="" >
      <input type="hidden" name="textEvaluation.semester.id" value="${semester.id}" />
      <#include "/template/time/semester.ftl"/>
     </tr>
   </table>
    <table   width="100%"  class="frameTable"> 
        <tr>
            <td width="20%" class="frameTable_view" valign="top">
                <#include "searchTable.ftl"/>
                </form>
            </td>
            <td valign="top">
                <iframe name="displayFrame" src="#" width="100%" frameborder="0" scrolling="no"></iframe>
            </td>
        </tr>
    </table>
    <script language="javascript">
        function query(form){
            form.action="textEvaluateResult.action?method=search";
            form.target="displayFrame";
            form.submit();
        }
       query(document.evaluateForm);
       var bar = new ToolBar('bar','<@text name="textEvaluation.idea"/>',null,true,true);
    </script>
</body>
<#include "/template/foot.ftl"/>
--]