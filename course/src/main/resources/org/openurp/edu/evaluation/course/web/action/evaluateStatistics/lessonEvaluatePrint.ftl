[#ftl]
[@b.head/]

[@b.toolbar title='评教统计' ]
    bar.addPrint();
    bar.addClose();
[/@]
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
         <td  colspan="${((questionList?size)+5)}" align="center" ><b>${semester.schoolYear!}(${semester.name!}) 评教汇总</b></td>
        </tr>
        <tr>
        <td  colspan="${((questionList?size)+5)}" align="left" ><b>学校总分加权平均分：${evaluateResults!0}</b></td>
        </tr>
</table>
<table class="planTable" width="95%">
     <tr align="center" class="darkColumn">
         <td rowspan="2">全校排名</td>
         <td rowspan="2">当前排名</td>
       <td rowspan="2"> 姓名</td>
       <td rowspan="2"> 课程名称</td>
       <td rowspan="2">人次</td>
       <td colspan="${((questionList?size))}" align="center">各项得分</td>
      <td rowspan="2">总分</td>
     </tr>
     <tr align="center"> [#assign num=0]
       [#list questionList as ques]
       [#assign num=num +1]
       <td title="${ques.content!}">评分${num!}</td>
       [/#list]
       </tr>
     [#assign nums =0]
            [#list evaluateTeaStasList?if_exists as evaluateTeaStas]
            [#assign nums =nums +1]
                <tr align="center">
                <td>${numTeaMaps[evaluateTeaStas[0]]!}</td>
                <td>${nums!}</td>
                <td>${(evaluateTeaStas[1])?if_exists}</td>
                <td>${(evaluateTeaStas[3])?if_exists}</td>
                <td>${(evaluateTeaStas[5])?if_exists}</td>
                   [#list questionList as ques]
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
