[#ftl]
[@b.head/]
[@b.toolbar title='查询文字评教' id='textEvaluationBar' /]

[#--[@eams.semesterBar semesterValue=semester name="project.id" semesterName="semester.id" semesterEmpty="false" initCallback="changeSemester()"/]--]
<table class="indexpanel">
    <tr>
        <td style="width:200px" class="index_view">
        [@b.form action="!search" name="textEvaluationIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            <input type="hidden" name="textEvaluation.lesson.project.id" value="${(project.id)!}"/>
            [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
            [@b.textfields style="width:130px" names="textEvaluation.lesson.no;课程序号,textEvaluation.lesson.course.code;课程代码,textEvaluation.lesson.course.name;课程名称,textEvaluation.staff.code;教师工号,textEvaluation.staff.person.name.formatedName;教师姓名"/]
            [@b.select style="width:134px" name="textEvaluation.lesson.teachDepart.id" label="开课院系" items=departments empty="..."/]
            [@b.select style="width:134px" name="textEvaluation.state" label="是否确认" items={'1':'已确认','0':'未确认'} empty="..."/]
        [/@]
        </td>
        <td class="index_content">
            [@b.div id="contentDiv" href="!search"/]
        </td> 
    </tr>
</table>
<script type="text/javaScript">
    var form = document.textEvaluationIndexForm;
    
    function changeSemester(){
        bg.form.addInput(form, "textEvaluation.semester.id", $("input[name='semester.id']").val());
        bg.form.submit(form);
    }
</script>
[@b.foot/]