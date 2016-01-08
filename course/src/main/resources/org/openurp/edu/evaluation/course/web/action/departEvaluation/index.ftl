[#ftl]
[@b.head/]
[@b.toolbar title='院系评教管理' id='departmentEvaluateBar']
    bar.addItem("${b.text('action.import')}","importData()");
    bar.addItem("下载模板","downloadTem()","${base}/static/images/action/download.gif");
[/@]
[#--[@eams.semesterBar semesterValue=semester name="project.id" semesterName="semester.id" semesterEmpty="false" initCallback="changeSemester()"/]--]
<table class="indexpanel">
    <tr>
    <td class="index_view">
        [@b.form action="!search" name="departmentEvaluationIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
        <input type="hidden" name="departEvaluation.course.project.id" value="${(project.id)!}"/>
            [@b.textfield style="width:100px" name="departEvaluation.teacher.code" label="工号" /]
            [@b.textfield style="width:100px" name="departEvaluation.teacher.person.name.formatedName" label="姓名" /]
            [@b.textfield style="width:100px" name="departEvaluation.course.code" label="课程代码" /]
            [@b.textfield style="width:100px" name="departEvaluation.course.name" label="课程名称" /]
            [@b.select style="width:105px" name="departEvaluation.teacher.staff.department.id" label="所在院系" items=departments empty="..."/]
            [@b.select style="width:105px" name="passed" label="是否测评" items={'1':'已评','0':'未评'} /]
        [/@]
        </td>
        <td class="index_content">
            [@b.div id="contentDiv"/]
        </td> 
    </tr>
</table>
<script type="text/javaScript">
    var form = document.departmentEvaluationIndexForm;
    
    function changeSemester(){
        bg.form.addInput(form, "departmentEvaluation.semester.id", $("input[name='semester.id']").val());
        bg.form.submit(form);
    }
    
    function importData(){
        var form = document.departmentEvaluationIndexForm;
        bg.form.submit(form, "departEvaluation!importForm.action");
    }
    
    function downloadTem(){
        var form = document.departmentEvaluationIndexForm;
        bg.form.submit(form, "departEvaluation!downDepEvaluateTemp.action","_blank");
    }
</script>
[@b.foot/]