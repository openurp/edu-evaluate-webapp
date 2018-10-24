<#include "/template/head.ftl"/>
<body>
<table id="bar"></table>
<#list  questionnaires as questionnaireStat>
<table class="listTable"  border='1' width=100%  border='0'  valign='top'>
<tr><td colspan="10">${(questionnaireStat.course.name)!}</td></tr>
<tr>
    <td>选课号</td>
    <td>${(questionnaireStat.task.seqNo)!}</td>
    <td>教师</td>
    <td colspan="3">${(questionnaireStat.teacher.name)!}</td>
    <td>院系</td>
    <td colspan="3">${(questionnaireStat.depart.name)!}</td>
</tr>
<tr>
    <td>职称</td>
    <td>${(questionnaireStat.teacher.title.name)!}</td>
    <td>&nbsp;</td>
    <td colspan="3">&nbsp;</td>
    <td>总分</td>
    <td colspan="3">${questionnaireStat.disPlayAvgScore}</td>
</tr>
<tr>
    <td>问卷</td>
    <td>下发:${(questionnaireStat.release)?default("0")}/回收:${(questionnaireStat.allTickets)?default("0")}/有效:${(questionnaireStat.validTickets)?default("0")}</td>
    <td>&nbsp;</td>
    <td>选A</td>
    <td>选B</td>
    <td>选C</td>
    <td>选D</td>
    <td>选E</td>
    <td>均值</td>
    <td>标准差</td>
</tr>
<#assign validNum=0/>
<#assign tt=(questionnaireStat.id)?string/>

<#assign questionList=questionMap[tt]/>
<#list  questionList as questionDetailStat>
<#if ((questionDetailStat.question.addition)?string!="true")>
<tr>
    <td colspan="3">${(questionDetailStat.question.content)!}</td>
    <#assign optionStr= (tt+"*"+((questionDetailStat.id)?string)+"*")/>
    <td>${((optionMap[(optionStr+"A")]).amount)?default("0")}</td>
    <td>${((optionMap[(optionStr+"B")]).amount)?default("0")}</td>
    <td>${((optionMap[(optionStr+"C")]).amount)?default("0")}</td>
    <td>${((optionMap[(optionStr+"D")]).amount)?default("0")}</td>
    <td>${((optionMap[(optionStr+"E")]).amount)?default("0")}</td>
    <td>${(questionDetailStat.average)!}</td>
    <td>${(questionDetailStat.stddev)!}</td>
</tr>
<#else>
    <#assign validNum =validNum+1/>
</#if>

</#list>
<#if (validNum>0) >
<tr><td colspan="10">附加题部分</td></tr>
    <#list  questionList as questionDetailStat>
        <#if ((questionDetailStat.question.addition)?string=="true")>
        <tr>
            <td colspan="3">${(questionDetailStat.question.content)!}</td>
            <#assign optionStr= (tt+"*"+((questionDetailStat.id)?string)+"*")/>
            <td>${((optionMap[(optionStr+"A")]).amount)?default("0")}</td>
            <td>${((optionMap[(optionStr+"B")]).amount)?default("0")}</td>
            <td>${((optionMap[(optionStr+"C")]).amount)?default("0")}</td>
            <td>${((optionMap[(optionStr+"D")]).amount)?default("0")}</td>
            <td>${((optionMap[(optionStr+"E")]).amount)?default("0")}</td>
            <td>${(questionDetailStat.average)!}</td>
            <td>${(questionDetailStat.stddev)!}</td>
        </tr>
        </#if>
    </#list>
</#if>

<tr>
<td colspan="10">指标代码含义:A.完全同意 B.同意 C. 一般 D.不同意 E.完全不同意</td>
</tr>

</table>
<BR>
<BR>
</#list>
</body>
<@htm.actionForm name="actionForm" method="post" entity="questionnaireStat" action="exportEvaluate.action"/>
<script>
    var bar=new ToolBar("bar","课程详细列表",null,true,true);
       bar.setMessage('<@getMessage/>');
    bar.addItem('导出详细', 'dataout()');

       function dataout(){
               addInput(form,"questionnaireStatId","${(questionnaireStatId)!}");;
               form.action="exportEvaluate.action?method=export&type=xls";
               addInput(form, "template", "questionnaireStat.xls", "hidden");
            form.submit();
          }

</script>
<#include "/template/foot.ftl"/>
