[#--暂时无用--]
[#--
<#include "/template/head.ftl"/>
<BODY>
<#assign labInfo>评教开关信息</#assign>
  <#include "/template/back.ftl"/>
  <table class="infoTable" align="center">
        <tr>
          <td class="title"  width="30%">学年学期:
              <input type="hidden" name="switch.semester.id" value="${(switch.semester.id)?if_exists}" />
          </td>
        </tr>
         <tr>
             <td class="title"  width="30%">是否开放:</td>
             <td>${(switch.isOpen)?string("开放","关闭")}</td>
         </tr>
         <tr>
             <td class="title" id="f_openDate" width="30%">开始时间:</td>
             <td> ${(switch.beginAt?string("yyyy-MM-dd HH:mm"))?default("")}</td>
         </tr>
         <tr>
             <td class="title" id="f_closeDate">结束时间:</td>
             <td>${(switch.endAt?string("yyyy-MM-dd HH:mm"))?default("")}</td>
         </tr>
  </table>
</body>
<#include "/template/foot.ftl"/>
--]