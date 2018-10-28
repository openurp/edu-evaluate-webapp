[#ftl]
[@b.head/]

[@b.toolbar title='评教统计' ]
    bar.addPrint();
    bar.addItem("导出","clazzTeaEvaluateExport()");
    bar.addClose();
[/@]
<script language="javascript">
        var form =document.textbookIndexForm;
        function clazzTeaEvaluateExport(){
          form.action="evaluateStatistics.action?method=clazzTeaEvaluateExport";
          form.submit();
       }

     </script>
<style>
.planTable {
    border: 1px solid #006CB2;
    border-collapse: collapse;
    font-size: 10pt;
    font-style: normal;
    vertical-align: middle;
    table-layout:fixed;
}
.planTable td {
    border: 1px solid #006CB2;
    border-collapse: collapse;
    overflow: hidden;
    word-wrap:break-word;
    padding:2px 0px;
}
.planTable thead tr {
    background-color: #C7DBFF;
    color: #000000;
    letter-spacing: 0;
    text-decoration: none;
}
</style>
<table width="99%" align="center">
<table width="95%">
         <tr>
         <td  colspan="${((questionTypes?size)+5)}" align="center" ><b>${semester.schoolYear!}(${semester.name!}) 评教汇总</b></td>
        </tr>
        <tr>
        <td  colspan="${((questionTypes?size)+5)}" align="left" ><b>学校总分加权平均分：${evaluateResults!0}</b></td>
        </tr>
</table>
<table class="planTable" width="95%">
     <tr align="center" class="darkColumn">
         <td >当前排名</td>
         <td >开课院系</td>
       <td > 姓名</td>
       <td > 课程名称</td>
       <td > 课程序号</td>
       <td > 课程代码</td>
       <td >有效票数</td>
       [#assign num=0]
       [#list questionTypes as ques]
       [#assign num=num +1]
       <td title="">${ques.name!}</td>
       [/#list]
      <td >总评</td>
     </tr>
     [#assign nums =0]
            [#list evaluateTeaStasList?if_exists as evaluateTeaStas]
            [#assign nums =nums +1]
                <tr align="center">
                <td>${nums!}</td>
                <td>${(evaluateTeaStas[7])?if_exists}</td>
                <td>${(evaluateTeaStas[1])?if_exists}</td>
                <td>${(evaluateTeaStas[3])?if_exists}</td>
                <td>${(evaluateTeaStas[6])?if_exists}</td>
                <td>${(evaluateTeaStas[8])?if_exists}</td>
                <td>${(evaluateTeaStas[5])?if_exists}</td>
                   [#list questionTypes as ques]
                  <td>
                  [#assign str = "${evaluateTeaStas[0]!}"+"_"+"${evaluateTeaStas[2]!}"+"_"+"${ques.id?string}"]
                ${(evaluateRes.get(str?string))!0}
                  </td>
                  [/#list]
                  <td>${(evaluateTeaStas[4])?default("")}</td>
                </tr>
            [/#list]
</table>
</table>

[@b.foot/]
