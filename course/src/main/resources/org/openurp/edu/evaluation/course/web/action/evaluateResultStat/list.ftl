[#ftl]
[@b.head/]
[@b.form name="questionnaireStatSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=questionnaireStats var="questionnaireStat" sortable="true"]
        [@b.gridbar title="院系教师评教详细信息"]
            bar.addItem('查看', 'showInfo()');
            bar.addItem("初始化有效结果","setValidResult()");
            bar.addItem("统计评教结果","statisticResult()");
            bar.addItem("${b.text('action.export')}","exportData()");
    
        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="semester.name" title="学期"/]
            [@b.col property="lesson.no" title="选课号"/]
            [@b.col property="lesson.course.name" title="课程名称"/]
            [@b.col property="teacher.code" title="教师工号"/]
            [@b.col property="teacher.name" title="教师姓名"/]
            [@b.col property="depart.name" title="院系名称"/]
            [@b.col property="questionnaire.description" title="问卷名称"/]
            [@b.col property="validTickets" title="问卷数"/]
            [@b.col property="release" title="选课人数"/]
            [@b.col property="(questionnaireStat.validScore/(questionnaireStat.validTickets+0.01))" title="总分"/]
            [@b.col property="(questionnaireStat.addScore/(questionnaireStat.validTickets+0.01))" title="附加问题分值"/]
        [/@]
    [/@]
[/@]
<script type="text/javaScript">
    var searchForm = document.questionnaireStatSearchForm;

    $(".gridempty").html("");
            
    function exportData(){
        alert("有待完善!");
    }
    function setValidResult(){
        bg.form.submit(searchForm, "questionnaireStat!initValidHome.action", "main");
    }
    function statisticResult(){
        bg.form.submit(searchForm, "questionnaireStat!statHome.action", "main");
    }
    function showInfo(){
        var id = bg.input.getCheckBoxValues("questionnaireStat.id");
        if(id == "" || null == id){
            alert("请选择一项!");
            return false;
        }
        bg.form.addInput(searchForm, "questionnaireStat.ids", id);
        bg.form.submit(searchForm, "evaluateResultStat!info.action");
    }
</script>
[@b.foot/]

[#--
<#include "/template/head.ftl"/>
<body>
<table id="bar"></table>
<@table.table id="listTable" width="100%" sortable="true">
   <@table.thead>
     <@table.selectAllTd id="questionnaireStatId"/>
     <@table.sortTd width="15%" id="questionnaireStat.semester.name" text="学期"/>
     <@table.sortTd width="12%" id="questionnaireStat.task.teachclass.name" text="选课号"/>
     <@table.sortTd width="10%" id="questionnaireStat.task.course.name" text="课程名称"/>
     <@table.sortTd width="7%" id="questionnaireStat.teacher.code" text="教师工号"/>
     <@table.sortTd width="7%" id="questionnaireStat.teacher.name" text="教师姓名"/>
     <@table.sortTd width="7%" id="questionnaireStat.depart.name" text="院系名称"/>
     <@table.sortTd width="7%" id="questionnaireStat.questionnaire.description" text="问卷名称"/>
     <@table.sortTd width="3%" id="questionnaireStat.validTickets" text="问卷数"/>
     <@table.sortTd width="3%" id="questionnaireStat.release" text="选课人数"/>
     <@table.sortTd width="5%" id="(questionnaireStat.validScore/(questionnaireStat.validTickets+0.01))" text="总分"/>
     <@table.sortTd width="5%" id="(questionnaireStat.addScore/(questionnaireStat.validTickets+0.01))" text="附加问题分值"/>
    </@>
    <@table.tbody datas=questionnaireStats;questionnaireStat>
        <@table.selectTd id="questionnaireStatId" value=questionnaireStat.id />
         <td>${(questionnaireStat.semesterStr)!}</td>
        <td>${(questionnaireStat.task.teachclass.name)!}</td>
        <td>${(questionnaireStat.task.course.name)!}</td>
        <td>${(questionnaireStat.teacher.code)!}</td>
        <td>${(questionnaireStat.teacher.name)!}</td>
        <td>${(questionnaireStat.depart.name)!}</td>
        <td>${(questionnaireStat.questionnaire.description)!}</td>
        <td>${(questionnaireStat.validTickets)!}</td>
        <td>${(questionnaireStat.release)!}</td>
        <td>${(questionnaireStat.disPlayAvgScore)!}</td>
        <td>${(questionnaireStat.disPlayAddScore)!}</td>
    </@>
  </@>
  <@htm.actionForm name="actionForm" entity="questionnaireStat" action="evaluateResultStat.action">
  </@>
<script>
    var bar=new ToolBar("bar","课程导师列表",null,true,true);
       bar.setMessage('<@getMessage/>');
    bar.addItem("<@text name="action.export"/>","exportData()");
    bar.addItem('查看详细', 'detail()');
    bar.addItem("初始化有效结果","setValidResult()");
     bar.addItem("统计评教结果","statisticResult()");
    var action="questionnaireStat.action";
      function exportData(){
       if(${totalSize}>10000){alert("数据量超过一万不能导出");return;}
       addInput(form,"keys","semesterStr,task.teachclass.name,task.course.name,teacher.code,teacher.name,depart.name,questionnaire.description,validTickets,release,disPlayAvgScore,disPlayAddScore");
       addInput(form,"titles","学年学期,选课号,课程名称,教师工号,教师姓名,院系名称,问卷类型,问卷回收数,选课人数,总分,附加问题分值");
       addInput(form,"fileName","课程评估统计表");
       exportList();
     }
       function dataout(){
               var id = getSelectIds("questionnaireStatId");
               addInput(form,"questionnaireStat.id",id);
               form.action="evaluateResultStat.action?method=export&questionnaireStatId="+id+"&type=xls";
               addInput(form, "template", "questionnaireStat.xls", "hidden");
            form.submit();
          }
           function detail(){
               var id = getSelectIds("questionnaireStatId");
               addInput(form,"questionnaireStat.id",id);
               form.action="evaluateResultStat.action?method=detailEvaluateResult&questionnaireStatId="+id;
            form.submit();
          }
          function setValidResult(){
            form.action=action+"?method=initValidHome";
            form.target="";
            form.submit();
        }
        function statisticResult(){
            form.action=action+"?method=statHome";
            form.target="";
            form.submit();
        }
</script>
</body>
<#include "/template/foot.ftl"/>
--]