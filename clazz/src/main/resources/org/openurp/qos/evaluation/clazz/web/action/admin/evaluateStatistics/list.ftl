[#ftl]
[@b.head/]
[@b.grid items=evaluateStasList var="evaluateStas"]
[@b.gridbar text="学院统计"]
        bar.addItem("${b.text('action.info')}","info()");
        bar.addItem("${b.text('action.export')}","evaluateResultsExport()");
    [/@]
    <b>学校总分加权平均分：${evaluateResults!0}</b>
<br>
<script language="javascript">
        var form =document.textbookIndexForm;
        function info(){
          form.action="evaluateStatistics.action?method=clazzEvaluateTwo&semester.id=${semester.id}&lookType=2";
          form.submit();
       }
       function evaluateResultsExport(){
          form.action="evaluateStatistics.action?method=evaluateResultsExport";
          form.submit();
       }
     </script>
[@b.row]
        [@b.boxcol name="department.id" id="department.id" value="${evaluateStas[0]!}"/]
        [@b.col width="15%" title="学院"]${(evaluateStas[1])?if_exists}[/@]
        [@b.col width="6%" title="总分"]${(evaluateStas[2])?if_exists}[/@]
        [@b.col width="6%" title="人次"]${(evaluateStas[3])?if_exists}[/@]
         [#assign numes=0]
        [#list questionList as ques]
          [#assign numes=numes +1]
        [@b.col width="6%" text="${ques.content!}" title="评分${numes!}"]
        [#assign strs = "${evaluateStas[0]!}"+"_"+"${ques.id?string}"]
        ${(evaluateSMaps.get(strs?string))!}
                  [/@]
                  [/#list]
        [/@]
        [/@]

[@b.foot/]
