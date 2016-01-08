<body>
  <table id="bar" width="100%"></table>
   <@table.table width="100%" id="listTable" sortable="true" headIndex="1">
     <tr align="center" class="darkColumn">
       <td>班级</td>
       <td>已评人次</td>
       <td>总人次</td>
       <td>完成率</td>
     </tr>
     
            <#list evaluateSearchDepartmentList?if_exists as evaluateSearchDepartment>
                <tr align="center">
                <td>${(evaluateSearchDepartment.adminClass.name)?if_exists}</td>
                   <td>${(evaluateSearchDepartment.haveFinish)?default("")}</td>
                  <td>${(evaluateSearchDepartment.countAll)?default("")}</td>
                 <td>${(evaluateSearchDepartment.finishRate)?if_exists}</td>
                </tr>
            </#list>
    
   </@>
 <@htm.actionForm name="actionForm" action="evaluateSearchDepartment.action" entity="question"/>
<script>
   var bar = new ToolBar('bar','院系评教率',null,true,true);
   bar.setMessage('<@getMessage/>');
</script>
 </body>
