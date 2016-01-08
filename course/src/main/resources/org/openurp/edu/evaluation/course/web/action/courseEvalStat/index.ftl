[#ftl]
[@b.head/]
[@b.toolbar title='最终评教结果统计' id='departmentEvaluateBar' ]
  bar.addItem('评教结果统计','stat()');
[/@]
[#--[@eams.semesterBar semesterValue=semester name="project.id" semesterName="semester.id" semesterEmpty="false" initCallback="changeSemester()"/]--]
<table class="indexpanel">
    <tr>
    <td class="index_view">
        [@b.form action="!search" name="courseEvaluateStatIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
        <input type="hidden" name="semester.id" value="${(semester.id)!}"/>
            [@b.textfield style="width:100px" name="courseEvalStat.staff.code" label="工号" /]
            [@b.textfield style="width:100px" name="courseEvalStat.staff.person.name.formatedName" label="姓名" /]
            [@b.select style="width:105px" name="courseEvalStat.staff.state.department.id" label="部门" items=departments empty="..."/]
            [@b.select name="courseEvalStat.questionnaire.id" label="所用问卷" items=[] ]
                [#list questionnaires as q]
                    <option value="${q.id}">${q.description}</option>
                [/#list]
            [/@]
        [/@]
        </td>
        <td class="index_content">
            [@b.div id="contentDiv"/]
        </td> 
    </tr>
</table>
<script type="text/javaScript">
    var form = document.courseEvaluateStatIndexForm;
    
    function changeSemester(){
        bg.form.addInput(form, "semester.id", $("input[name='semester.id']").val());
        bg.form.submit(form);
    }
    
    function stat(){
      var form = document.semesterForm;
      form.action="${b.url('!stat')}";
      bg.form.submit(form);
    }
</script>
[@b.foot/]