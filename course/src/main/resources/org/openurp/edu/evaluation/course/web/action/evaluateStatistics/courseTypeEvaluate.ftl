[#ftl]
[@b.head/]
[#assign nums =0]
[@b.grid items=evaluateTeaStasList var="evaluateTeaStas"]
[@b.gridbar]
    [/@]
    <b>学校总分加权平均分：${evaluateResults!0}</b>
<br>
<script language="javascript"> 
        var form =document.textbookIndexForm;
        function info(){
          form.action="evaluateStatistics.action?method=lessonEvaluate&semester.id=${semester.id}";
          form.submit();
       }
       function evaluateResultsExport(){
          form.action="evaluateStatistics.action?method=evaluateResultsExport";
          form.submit();
       }
     </script>
[@b.row]
        [@b.col width="6%" title="当前排名"][#assign nums =nums +1]${nums!}[/@]
        [@b.col width="5%" title="姓名"]${(evaluateTeaStas[1])?if_exists}[/@]
        [@b.col width="10%" title="课程名称"]${(evaluateTeaStas[3])?if_exists}[/@]
        [@b.col width="5%" title="人次"]${(evaluateTeaStas[5])?if_exists}[/@]
         [#assign numes=0]
        [#list questionList as ques]
          [#assign numes=numes +1]
        [@b.col width="5%" text="${ques.content!}" title="评分${numes!}"]
        [#assign str = "${evaluateTeaStas[0]!}"+"_"+"${evaluateTeaStas[2]!}"+"_"+"${ques.id?string}"]
                ${(evaluateRes.get(str?string))!0}
                  [/@]
                  [/#list]
         [@b.col width="5%" title="总分"]${(evaluateTeaStas[4])?default("")}[/@]
        [/@]
        [/@]
[@b.foot/]
