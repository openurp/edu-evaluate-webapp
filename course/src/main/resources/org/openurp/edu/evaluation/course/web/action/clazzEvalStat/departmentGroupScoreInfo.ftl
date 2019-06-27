<#include "/template/head.ftl"/>
<#macro getAverage(questionTypeMap)>
<#assign average=((questionTypeMap[(optionStr+"A")])?default("0")+(questionTypeMap[(optionStr+"B")])?default("0")+(questionTypeMap[(optionStr+"C")])?default("0")+(questionTypeMap[(optionStr+"D")])?default("0"))/4>
<td>${average}</td>
</#macro>
<body>
<table id="bar"></table>

<table class="listTable"  border='1' width=100%  border='0'  valign='top'>
 <tr>
     <td>课程序号</td>
     <td>院系</td>
     <td>课程名称</td>
     <td>课程代码</td>
     <td>教师名称</td>
     <td>应选人数</td>
     <td>实选人数</td>
         <#list  questionTypeList as questionType>
             <td>${(questionType.name)!}</td>
         </#list>
     <td>总评</td>
 </tr>

<#list  questionnaires  as questionnaireStat>
<tr>
    <td >${(questionnaireStat.task.seqNo)!}</td>
    <td >${(questionnaireStat.teacher.user.department.name)!}</td>
    <td >${(questionnaireStat.course.name)!}</td>
    <td >${(questionnaireStat.course.code)!}</td>
    <td >${(questionnaireStat.teacher.name)!}</td>
    <td >${(questionnaireStat.task.teachclass.stdCount)!}</td>
    <td >${(questionnaireStat.validTickets)!}</td>
    <#assign optionStr= (((questionnaireStat.id)?string)+"**")/>
    <td>${((questionTypeMap[(optionStr+"A")]))?default("0")}</td>
    <td>${((questionTypeMap[(optionStr+"B")]))?default("0")}</td>
    <td>${((questionTypeMap[(optionStr+"C")]))?default("0")}</td>
    <td>${((questionTypeMap[(optionStr+"D")]))?default("0")}</td>
    <@getAverage questionTypeMap/>
</tr>
 </#list>

</table>
<BR>
<BR>

</body>
<@htm.actionForm name="actionForm" method="post" entity="questionnaireStat" action="exportEvaluate.action"/>
<script>
    var bar=new ToolBar("bar","课程大类列表",null,true,true);
       bar.setMessage('<@getMessage/>');

</script>
<#include "/template/foot.ftl"/>
