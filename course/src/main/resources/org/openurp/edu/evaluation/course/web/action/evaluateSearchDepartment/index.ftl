[#--<#include "/template/head.ftl"/>--]
<BODY LEFTMARGIN="0" TOPMARGIN="0">
<table id="bar"></table>
   <table class="frameTable">
   <tr>
    <td  style="width:160px"  class="frameTable_view">
   <form name="actionForm" action="evaluateSearchDepartment.action?method=search" method="post" target="contentFrame">
     <table class="searchTable">
       <tr>
       <td>学期:</td>
       <td>
         <select name="evaluateSearchDepartment.semester.id" style="width:100px">
         <#list semesters?sort_by("schoolYear")?reverse as semester1>
        <option value="${semester1.id}" title="${semester1.schoolYear}&nbsp;${semester1.name}" <#if semester.id == semester1.id> selected</#if>>${semester1.schoolYear}&nbsp;${semester1.name}</option>
         </#list>
         </select>
       </td>
      </tr>
      <tr>
       <td>部门:</td>
       <td>
         <select name="evaluateSearchDepartment.department.id" style="width:100px">
         <#list departments?if_exists?sort_by("name")?reverse as department>
         <option value="${department.id}" title="${department.name}">${department.name}</option>
         </#list>
         </select>
       </td>
      </tr>
      
      
      <tr>
       <td colspan="2" align="center">
         <button onclick="this.form.submit()">查询</button>
       </td>
      </tr>
      </table>
      </form>
      </td>
    <td valign="top">
     <iframe  src="#"
     id="contentFrame" name="contentFrame" scrolling="no"
     marginwidth="0" marginheight="0"      frameborder="0"  height="100%" width="100%">
     </iframe>
    </td>
   </tr>
  </table>
<script language="javascript">
   var bar=new ToolBar('bar','班级评教率',null,true,true);
   bar.setMessage('<@getMessage/>');
   function search(){
           document.actionForm.submit();
   }
   search();
 </script>
</body>
<#include "/template/foot.ftl"/>