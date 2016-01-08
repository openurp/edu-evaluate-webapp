[#ftl]
[@b.head/]
[@b.toolbar title="文字评教开关设置"]
    bar.addBack();
[/@]
[@b.form name="textEvaluationSwitchEditForm" action="!save" title="文字评教开关" theme="list"]
    <input type="hidden" id="semesterId" name="textEvaluationSwitch.semester.id" value="${semesterId!}" />
    <input type="hidden" id="projectId" name="textEvaluationSwitch.project.id" value="${(textEvaluationSwitch.project.id)!}"/>
    [@b.select label="是否开放" required="true" name="textEvaluationSwitch.opened" value=((textEvaluationSwitch.opened)?string("1","0"))! items={'1':'是','0':'否'} empty="..."/]
    [@b.select label="是否开放(教师查询)" required="true" name="textEvaluationSwitch.openedTeacher" value=((textEvaluationSwitch.openedTeacher)?string("1","0"))! items={'1':'是','0':'否'} empty="..."/]
    [@b.select label="是否开放(学生评教)" required="true" name="textEvaluationSwitch.textEvaluateOpened" value=((textEvaluationSwitch.textEvaluateOpened)?string("1","0"))! items={'1':'是','0':'否'} empty="..."/]
    [@b.datepicker label="开始时间" required="true" name="textEvaluationSwitch.beginAt" id="_beginAt" format="yyyy-MM-dd HH:mm" maxDate="#F{$dp.$D(\\'_endAt\\')}" value=(textEvaluationSwitch.beginAt?string("yyyy-MM-dd HH:mm"))! maxlength="10" style="width:200px"/]
    [@b.datepicker label="结束时间" required="true" name="textEvaluationSwitch.endAt" id="_endAt" format="yyyy-MM-dd HH:mm" minDate="#F{$dp.$D(\\'_beginAt\\')}" value=(textEvaluationSwitch.endAt?string("yyyy-MM-dd HH:mm"))! maxlength="10" style="width:200px"/]
    [@b.formfoot]
    
        [@b.submit value="${b.text('action.save')}"/]
        <input type="reset" value="${b.text("action.reset")}" />
    [/@]
[/@]
<script type="text/javaScript">
    $("select[name='textEvaluationSwitch.openedTeacher']").parent().css("height","30px");
    $("select[name='textEvaluationSwitch.textEvaluateOpened']").parent().css("height","30px");
</script>
[@b.foot/]
[#--
<#include "/template/head.ftl"/>
<script language="JavaScript" type="text/JavaScript" src="${base}/static/scripts/common/Validator.js"></script>
 <body>
  <#assign labInfo>文字评教查询开放设置</#assign>
  <#include "/template/back.ftl"/>
  <table width="100%" class="formTable">
   <form name="commonForm" method="post" action="" >
   <@searchParams/>
    <input type="hidden" name="textEvaluationSwitch.id" value="${textEvaluationSwitch.id?if_exists}"/>
       <tr>
               <td class="title" width="20%" id="f_credits"><font color="red">*</font>是否开放:</td>
               <td>
                   <input type="radio" name="textEvaluationSwitch.opened" id="opened1" value="1" <#if textEvaluationSwitch.opened> checked </#if> />是
                   <input type="radio" name="textEvaluationSwitch.opened" id="opened0" value="0" <#if !(textEvaluationSwitch.opened)> checked </#if> onClick="changeOpened(this.value)" />否
               </td>
               <td class="title" width="20%" id="f_credits"><font color="red">*</font>是否开放教师查询:</td>
               <td>
                   <input type="radio" name="textEvaluationSwitch.openedTeacher" id="openedTeacher1" value="1" <#if textEvaluationSwitch.openedTeacher> checked </#if> onClick="changeOpened(this.value)" />是
                   <input type="radio" name="textEvaluationSwitch.openedTeacher" id="openedTeacher0" value="0" <#if !(textEvaluationSwitch.openedTeacher)> checked </#if>/>否
               </td>
       </tr>
       <tr>
             <td class="title" width="20%" id="f_credits">开始时间:</td>
             <td width="30%"><input name="textEvaluationSwitch.openAt" id="openAt" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" class="Wdate" style="width:150px" value="${(textEvaluationSwitch.openAt?string("yyyy-MM-dd HH:mm"))?default('')}" readOnly /></td> 
              <td class="title" width="20%" id="f_credits">结束时间:</td>
            <td width="30%"><input name="textEvaluationSwitch.closeAt" id="closeAt" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" class="Wdate" style="width:150px" value="${(textEvaluationSwitch.closeAt?string("yyyy-MM-dd HH:mm"))?default('')}" readOnly /></td> 
       </tr> 
       <tr>
               <td class="title" width="20%" id="f_credits"><font color="red">*</font>是否开放学生文字评教:</td>
               <td colSpan="3">
                   <input type="radio" name="textEvaluationSwitch.textEvaluateOpened" id="textEvaluateOpened1" value="1" <#if textEvaluationSwitch.textEvaluateOpened> checked </#if> />是
                   <input type="radio" name="textEvaluationSwitch.textEvaluateOpened" id="textEvaluateOpened0" value="0" <#if !(textEvaluationSwitch.textEvaluateOpened)> checked </#if> />否
               </td>
       </tr>
       <tr>
             <td colspan="4" align="center" class="darkColumn">
                   <button onClick='return save(this.form)'><@text name="system.button.submit"/></button>
                   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                   <input type="button" onClick='javascript:history.back()' value="取消"  class="buttonStyle"/> 
             </td>
       </tr>
   </form>
  </table>
  <script language="javascript" >
    var form = document.commonForm;
    function save(form) {
        if($('closeAt').value!="" && $('openAt').value!="" && $('closeAt').value < $('openAt').value){
             alert("结束时间必须在开始时间之后"); 
             return;
        }
        form.action = "textEvaluationSwitch!save.action";
        form.submit();
    }
    
    function changeOpened(opened){
        if("0"==opened){
            $('openedTeacher1').checked = false;
            $('openedTeacher0').checked = true;
        }else{
            $('opened1').checked = true;
            $('opened0').checked = false;
        }
    }
  </script>
 </body>
<#include "/template/foot.ftl"/>
--]