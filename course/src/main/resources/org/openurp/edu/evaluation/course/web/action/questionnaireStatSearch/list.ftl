[#ftl]
[@b.head/]
[@b.form name="questionnaireStatSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=questionnaireStats var="questionnaireStat" sortable="true"]
        [@b.gridbar title="院系教师评教详细信息"]
            //bar.addItem("${b.text('action.export')}","exportData()");
            
            function exportData(){
                alert("有待完善!");
            }
        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="lesson.no" title="课程序号"/]
            [@b.col property="lesson.course.name" title="课程名称"/]
            [@b.col property="teacher.name" title="任课教师"/]
            [@b.col property="lesson.teachDepart.name" title="开课院系"/]
            [#list questionTypes?if_exists as questionType]
            [@b.col title="${(questionType.name)!}"]
            [#list questionnaireStat.scores?if_exists as sor]
            [#if sor.type.id == questionType.id]
            ${(sor.score)!}
            [/#if]
            [/#list]
            [/@]
            [/#list]
            [@b.col property="score" title="总评"]
            [#if criteria?exists]
                ${(questionnaireStat.getScoreDisplay(criteria))!}
            [/#if]
            [/@]
        [/@]
    [/@]
[/@]
<script type="text/javaScript">
    $(".gridempty").html("");
</script>
[@b.foot/]

[#--
<#include "/template/head.ftl"/>
 <script language="JavaScript" type="text/JavaScript" src="pages/quality/evaluate/evaluateStat.js"></script>
 <BODY LEFTMARGIN="0" TOPMARGIN="0" >
 <table id="backBar" width="100%"></table>
<script>
   var bar = new ToolBar('backBar','<@text name="field.questionnaireStatistic.collegeTeacherDetailInfo"/>',null,true,true);
   bar.setMessage('<@getMessage/>');
   bar.addPrint("<@text name="action.print"/>");
</script>

         <@table.table   width="100%" id="listTable" sortable="true">
    <@table.thead>
      <@table.selectAllTd id="questionnaireStatId"/>
      <@table.sortTd  name="workload.college" id="questionnaireStat.depart.name"/>
      <@table.sortTd  name="field.questionnaireStatistic.teacher" id="questionnaireStat.teacher.name"/>
      <@table.sortTd  name="应评人数" id="questionnaireStat.teacher.name"/>
      <@table.sortTd  name="实评人数" id="questionnaireStat.validTickets"/>
      <@table.sortTd  name="field.select.selectQueryCourseName"  id="questionnaireStat.course.name"/>
      <#list questionTypeList?if_exists as questionType>
         <@table.td text="${questionType.name}"/>
       </#list>
       <@table.td text="总评"/>
       <@table.td text="学年度学期"/>
    </@>
    <@table.tbody datas=questionnaireStats;questionnaireStat>
      <@table.selectTd id="questionnaireStatId" value="${questionnaireStat.id}"/>
          <td>${questionnaireStat.depart.name}</td>
          <td>${questionnaireStat.teacher.name}</td>
          <td>${questionnaireStat.task.teachclass.stdCount}</td>
          <td>${questionnaireStat.validTickets}</td>
          <td>${questionnaireStat.course.name}</td>
        <#list questionTypeList?if_exists as questionType>
            <td>${questionnaireStat.getTypeScoreDisplay(criteria,questionType.id)}</td>
        </#list>
        <td>${questionnaireStat.getScoreDisplay(criteria)}</td>
        <td>${questionnaireStat.semester.schoolYear},${questionnaireStat.semester.name}</td>
      </@>
    </@>
  </body>
<#include "/template/foot.ftl"/>
--]