[#ftl]
[@b.head/]
[@b.toolbar title='初始有效结果' id='initValidBar']
    bar.addBack();
[/@]

[@b.form name="initValidForm" action="!setValid"]
<table class="infoTable" width="100%" align="center">
    <thead style="background:#C7DBFF;height:40px;line-height:40px;text-align:center;font-weight:bold;">
        <tr>
            <td colspan="5">初始化评教结果</td>
        </tr>
    </thead>
    <tbody style="height:25px;line-height:25px;">
        <tr style="text-align:center;">
            <td colspan="5">
                学年学期:[@eams.semesterCalendar title="学年学期" name="semester.id" empty="false" value=semester /]
            </td>
        </tr>
        <tr>
            <td class="title" style="text-align:center;" width="10%;">有效值</td>
            <td style="padding-left:10px;" width="38%;">
                <input type="text" name="percent" id="percent" />
                <font color="red" style="font-size:11px;">前后各舍去百分比。(1~50之间)</font>
            </td>
            <td class="title" style="text-align:center;" width="10%;">学年学期</td>
            <td style="padding-left:10px;" width="38%;">[@eams.semesterCalendar title="学年学期" name="semester.id" empty="false" value=semester /]</td>
            <td><input type="button" value="开始初始化" onClick="doStatistic()" class="buttonStyle" /></td>
        </tr>
    </tbody>
</table>
[/@]
<script type="text/javaScript">
    var initValidForm = document.initValidForm;
    
    function doStatistic(){
        if ($("#percent").val() == ""){
            alert("请输入百分比!");
            return false;
        } else {
            bg.form.setSearchParams(form,initValidForm);
            bg.form.submit(initValidForm);
        }
    }
</script>
[@b.foot/]







[#--<#include "/template/head.ftl"/>
<BODY>
    <table id="backBar" width="100%"></table>
    <table class="frameTable_title" width="100%">
        <tr>
           <form method="post"  name="actionForm" >
                <#include "/template/time/semester.ftl"/>
        </tr>
    </table>
    <table align="center" width="100%" class="listTable">
            <input type="hidden" name="statistic" value="statistic"/>
            <input type="hidden" name="departIdSeq" value=""/>
            <input type="hidden" name="educationTypeIdSeq" value=""/>
            <input type="hidden" name="semesterIdSeq" value="${semester.id}"/>
              <tr>
                  <td class="darkColumn" colspan="2" align="center">初始化评教结果</td>
              </tr>
              <tr>
                  <td  align="center" class="grayStyle" width="10%">有效值：</td>
                  <td class="grayStyle">
                                前后各舍去百分比:<input type="text" name="percent" value=""/>
                 </td>
              </tr>
              <tr>
                  <td align="center" colspan="2" class="darkColumn">
                      <input type="button" value="开始初始化" name="button1" onClick="doStatistic(this.form)" class="buttonStyle" />
                  </td>
              </tr>
          </form>
    </table>
    <script>
        var bar = new ToolBar('backBar','统计评教结果',null,true,true);
        bar.setMessage('<@getMessage/>');
        bar.addBack("<@text name="action.back"/>");
    
        function doStatistic(form){
            if(""==form.percent.value){
                alert("请出入百分比");
                return;
            }
            if(confirm("确认开始初始化吗?")){
                form["button1"].disabled=true;
                form.action ="evaluateDetailStat.action?method=setValid";
                '<font color="red" size="5"><@text name="workload.statisticInfo"/></font>'; 
                form.submit();
            }
        }
    </script>
</body>
<#include "/template/foot.ftl"/>
--]