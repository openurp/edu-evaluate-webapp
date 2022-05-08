<#include "/template/head.ftl"/>
<body>
<table id="bar"></table>

<table class="listTable"  border='1' width=100%  border='0'  valign='top'>
<tr><td colspan="10">${(questionnaireStat.course.name)!}</td></tr>
<tr>
    <td>选课号</td>
    <td>教师</td>
    <td>下发</td>
    <td>有效</td>
    <td>院系</td>
    <#list  indicatorList as indicator>
        <td>${(indicator.name)!}</td>
    </#list>
    <td>总分</td>
</tr>

<#list  questionnaires as questionnaireStat>
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
