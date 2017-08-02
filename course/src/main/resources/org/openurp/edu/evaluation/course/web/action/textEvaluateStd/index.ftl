[#ftl]
[@b.head/]
[#--[@b.navmenu]
    [@b.navitem title="问卷评教" href="/evaluateStd"/]
    [@b.navitem title="文字评教" href="/textEvaluateStudent"/]
[/@]--]
[@b.toolbar title='评教' id='textEvaluateStudentBar' /]
 
[#--[@eams.semesterBar semesterValue=semester name="project.id" semesterName="semester.id" semesterEmpty="false" initCallback="changeSemester(this.value)"/]--]
<table class="indexpanel">
    <tr>
        <td class="index_content">
            [@b.form name="textEvaluateStudentIndexForm" action="!search" target="contentDiv"  theme="search"]
             [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
            [/@]
        </td>
        <td class="index_content">
            [@b.div id="contentDiv"  href="!search?&semester.id=${(semester.id)!}" /]
        </td> 
    </tr>
</table>
<script type="text/javascript">
    function changeSemester(num){
        var textEvaluateStudentIndexForm = document.textEvaluateStudentIndexForm;
        bg.form.addInput(textEvaluateStudentIndexForm, "semester.id", num);
        bg.form.submit(textEvaluateStudentIndexForm);
    }
</script>
[@b.foot/]





[#--
<#include "/template/head.ftl"/>
<BODY LEFTMARGIN="0" TOPMARGIN="0">
<table id="taskListBar" width="100%"> </table>
<script>
   var bar = new ToolBar("taskListBar","文字评教",null,true,true);
   bar.setMessage('<@getMessage/>');
   bar.addItem("<@text name="action.help"/>","help()","help.png");
</script>
   <table class="frameTable_title" width="100%" border="0">
    <form name="calendarForm" action="textEvaluateStudent.action?method=index" method="post">
         <tr style="font-size: 10pt;" align="left">
         <td>&nbsp;</td>
            <#include "/template/time/semester.ftl"/>
        </tr>
    </form>
    </table>
    <table width="100%" height="90%" class="frameTable">
       <tr><td valign="top">
         <iframe  src="textEvaluateStudent.action?method=list&semester.id=${semester.id}"
         id="examTableFrame" name="examTableFrame" scrolling="no"
         marginwidth="0" marginheight="0" frameborder="0"  height="100%" width="100%">
         </iframe>
        </td>
     </tr>
    </table>
     <script language="javascript">
        function help(){
            self.location = "${base}/static/pages/textEvaluateStdHelp.html";
        }
    </script>
</body>
<#include "/template/foot.ftl"/>
--]