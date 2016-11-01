[#ftl]
[#if evaluateSwitches?size>0 && evaluateSwitches?first.open]
[@b.head/]
    <link rel="stylesheet" href="${base}/static/score.css"/>
[@b.toolbar title="添加督导组评教"]
  bar.addItem("提交","saveTeaEvaluate()");
[/@]
<table class="infoTable">
  <tr>
   <td class="title">教师工号:</td>
   <td class="content"> ${supervisiorEvaluate.teacher.code}</td>
   <td class="title">教师姓名:</td>
   <td class="content">${supervisiorEvaluate.teacher.person.name.formatedName}</td>
   <td class="title">所在院系:</td>
   <td class="content"> ${supervisiorEvaluate.teacher.user.department.name}</td>
  </tr>
  <tr>
   <td class="title">开课院系:</td>
   <td class="content"> ${supervisiorEvaluate.department.name}</td>
   <td class="title" style="font-weight:bold;">评教总分:</td>
   <td class="content" style="color:red; font-weight:bold;">${(supervisiorEvaluate.totalScore)!}</td>
  </tr>
</table>  

[@b.form name="addTeaEvaluateForm" action="!save"]
  <input type="hidden" name="semester.id" value="${Parameters['supervisiorEvaluate.semester.id']!}">
  <div class="grid">
    <table class="gridtable" >
        <thead class="gridhead">
            <tr>
                <th width="15%">问题类型</th>
                <th width="45%">问题内容</th>
                <th width="20%">分值(${(questionnaire.totalScore!)})</th>
                <th width="20%">得分</th>
            </tr>
        </thead>
        <tbody>
            [#assign index=1]
            [#list questionTree?keys?sort_by("priority")?reverse as key]
            [#assign questions = questionTree.get(key)]
                <tr class="griddata-${(key_index%2==0)?string("even","odd")}">
                    <td rowSpan="${questions?size}" align="center">
                        ${key.name}
                    </td>
                    <td align="left">
                        ${questions[0].content}
                    </td>
                    <td align="left">
                        ${questions[0].score}
                    </td>
                    <td align="left">
                      <div class="input-group spinner">
                        <input type="hidden" name="max_score" value="${questions[0].score}">
                        <input type="text" name="${(questions[0].id)!}_score" class="form-control scoreResult"  id="${(questions[0].id)!}_score" max="${questions[0].score}" value="${(resultMap.get(questions[0]))!}"/>
                        <div class="input-group-btn-vertical">
                          <button class="btn btn-default" type="button"><i class="fa fa-caret-up"></i></button>
                          <button class="btn btn-default" type="button"><i class="fa fa-caret-down"></i></button>
                        </div>
                    </td>
                </tr>
                [#if questions?size>1]
                    [#list 1..questions?size-1 as i]
                        [#assign index=index+1]
                        <tr class="griddata-${(key_index%2==0)?string("even","odd")}"> 
                            <td align="left">
                                ${questions[i].content}
                            </td>
                            <td align="left">
                                ${questions[i].score}
                            </td>
                            <td align="left">
                                 <div class="input-group spinner">
                                 <input type="hidden" name="max_score" value="${questions[i].score}">
                                  <input type="text"  class="form-control scoreResult" name="${(questions[i].id)!}_score"  id="${(questions[i].id)!}_score" value="${(resultMap.get(questions[i]))!}" max="${questions[i].score}"/>
                                  <div class="input-group-btn-vertical">
                                    <button class="btn btn-default" type="button"><i class="fa fa-caret-up"></i></button>
                                    <button class="btn btn-default" type="button"><i class="fa fa-caret-down"></i></button>
                                  </div>
                            </td>
                        </tr>
                    [/#list]
                [/#if]
                [#assign index=index+1]
            [/#list]
            <tr style="background-color: #c7dbff;">
                <td colSpan="4">&nbsp;</td>
               </tr>
        </tbody>
    </table>
  </div>      
[/@]
<script type="text/javaScript">
    function saveTeaEvaluate(){
      var form = document.addTeaEvaluateForm;
      var flag = true;
        jQuery(".scoreResult").each(function(){
            var max = parseFloat(this.max)
            if(!this.value || jQuery.trim(this.value)=="" || isNaN(this.value) || this.value<0 || parseFloat(this.value) > max){
                alert("请输入0-"+max+"之间的数字");
                flag =false;
                jQuery(this).focus();
                return false;
            }
        })
      if(flag){
        bg.form.submit(form, "${b.url('!update?id='+supervisiorEvaluate.id)}");
      }
    }
    (function ($) {
      $('.spinner .btn:first-of-type').on('click', function() {
         var max = parseFloat($(this).parent("div").parent("div").children("input:hidden").val(),10)
         var i = $(this).parent("div").parent("div").children("input:text")
         var myValue=parseFloat(i.val(), 10);
         if(isNaN(myValue) || myValue >= max) {
           i.val(max)
         }else{
           i.val(parseFloat(i.val(), 10) + 1)
         }
      });
      $('.spinner .btn:last-of-type').on('click', function() {
         var max = parseFloat($(this).parent("div").parent("div").children("input:hidden").val())
         var i = $(this).parent("div").parent("div").children("input:text")
         var myValue=parseFloat(i.val(), 10);
         if(isNaN(myValue)) {
           i.val(max)
         }else if(myValue<=0){
           i.val(0);
         }else{
           i.val(parseFloat(i.val(), 10)  - 1)
         }
      });
    })(jQuery);
</script>
[@b.foot/]
[#elseif evaluateSwitches?size>0 && !evaluateSwitches?first.open] 请在${evaluateSwitches?first.beginOn}到${evaluateSwitches?first.endOn}内评教或者评教开关关闭
[#else] 本学期没有设置评教开关
[/#if]