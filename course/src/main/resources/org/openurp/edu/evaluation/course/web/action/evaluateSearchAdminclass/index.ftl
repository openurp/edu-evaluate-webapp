[#ftl]
[@b.head/]
[@b.toolbar title="学生评教情况" /]

[#--[@eams.semesterBar semesterValue=semester name="project.id" semesterName="semester.id" semesterEmpty="false" /]--]
   <table class="indexpanel">
    <tr>
        <td class="index_view">
              [@b.form name="evaluateSearchIndexForm" action="!search" title="学生条件查询" target="contentDiv" theme="search"]
                  <input type="hidden" name="semester.id" value="${(semester.id)!}"/>
                [@b.select  name="department.id" label="院系" items=teachDeparts  /]
                [@b.textfield name="adminclass.name" label="班级名称"/]
            [/@]
        </td>
        <td class="index_content">
            [@b.div  id="contentDiv"/]
        </td>
    </tr>
</table>
<script language="javascript">
   jQuery(function(){
      bg.form.submit(document.evaluateSearchIndexForm, "evaluateSearchAdminClass!search.action");
   });
 </script>
[@b.foot/]