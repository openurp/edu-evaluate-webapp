[#ftl]
[@b.head/]
[#assign nums =0]
[@b.grid items=evaluateTeaStasList var="evaluateTeaStas"]
[@b.gridbar]
        //bar.addItem("${b.text('action.info')}","info()");
        bar.addItem("${b.text('action.export')}","evaluateTeaResultsExport()");
    [/@]
    <b>学校总分加权平均分：${evaluateResults!0}</b>
<br>
<script language="javascript"> 
        var form =document.textbookIndexForm;
        function evaluateTeaResultsExport(){
          form.action="evaluateStatistics.action?method=evaluateTeaResultsExport";
          form.submit();
       }
       function info(){
          form.action="evaluateStatistics.action?method=teaEvaluateInfo&semester.id=${semester.id}";
          form.submit();
       }
     </script>
[@b.row]
        [#assign nums =nums +1][@b.col width="6%" title="排名"]${nums!}[/@]
        [@b.col width="10%" title="姓名"]${(evaluateTeaStas[1])?if_exists}[/@]
        [@b.col width="5%" title="总分"]${(evaluateTeaStas[2])?if_exists}[/@]
        [@b.col width="5%" title="人次"]${(evaluateTeaStas[3])?if_exists}[/@]
         [#assign numes=0]
        [#list questionList as ques]
          [#assign numes=numes +1]
        [@b.col width="6%" text="${ques.content!}" title="评分${numes!}"]
        [#assign str = "${evaluateTeaStas[0]!}"+"_"+"${ques.id?string}"]
        ${(evaluateRes.get(str?string))!0}
                  [/@]
                  [/#list]
        [/@]
        [/@]

[@b.foot/]