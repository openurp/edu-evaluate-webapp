[#ftl]
[@b.head/]
[#assign nums =0]
[@b.form name="questionnaireStatSearchForm" action="!search" target="contentDiv"]
[@b.grid items=evaluateTeaStasList var="evaluateTeaStas"]
[@b.gridbar]
    //bar.addItem("${b.text('action.info')}",action.single("teaEvaluateInfo","",null));
        //bar.addItem("${b.text('action.info')}","teaInfo();");
[/@]
    <b>学校总分加权平均分：${evaluateResults!0}</b>
<br>
<script language="javascript"> 
        var form =document.questionnaireStatSearchForm;
        function infos(){
        bg.form.submitId(form,"evaluateTeaStas.id",false,"evaluateStatistics!teaEvaluateInfo.action");
        bg.form.action="evaluateStatistics!search.action";
        }
    function getIds(){
    var str ="idStrs=";
        var checkboxs=document.getElementsByTagName("checkBox"); 
        var i; 
        var nm =0;
        for(i=0;i<checkboxs.length;i++) 
        { 
        if(checkboxs[i].type=='checkbox') 
        { 
        if(checkboxs[i].checked){
        str =str +checkboxs[i].value;
        nm = nm +1;
        }
        } 
        }
        if(nm !=1){
            alert("请选择一个！");
            return false;
        }
        return(str);
    }
    
    function teaInfo(){
           var ids = getIds();
          form.action="evaluateStatistics.action?method=teaEvaluateInfo&idStrs="+ids;
          form.submit();
       }
 </script>
[@b.row]
        [@b.boxcol name="evaluateTeaStas.id" value="${semester.id!},${(evaluateTeaStas[0])?if_exists},${(evaluateTeaStas[2])!},"/]
        [@b.col width="4%" title="全校排名"]${numTeaMaps[evaluateTeaStas[0]]!}[/@]
        [@b.col width="4%" title="当前排名"][#assign nums =nums +1]${nums!}[/@]
        [@b.col width="4%" title="工号"]${(evaluateTeaStas[0])?if_exists}[/@]
        [@b.col width="5%" title="姓名"][@b.a href="evaluateStatistics!teaEvaluateInfo?idStrs=${semester.id!},${(evaluateTeaStas[7])?if_exists},${evaluateTeaStas[2]!}," target="_blank"]${(evaluateTeaStas[1])?if_exists}[/@][/@]
        [@b.col width="10%" title="课程名称"]${(evaluateTeaStas[3])?if_exists}[/@]
        [@b.col width="10%" title="教师所在院系"]${(evaluateTeaStas[6])?if_exists}[/@]
        [@b.col width="4%" title="人次"]${(evaluateTeaStas[5])?if_exists}[/@]
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
        [/@]
[@b.foot/]
