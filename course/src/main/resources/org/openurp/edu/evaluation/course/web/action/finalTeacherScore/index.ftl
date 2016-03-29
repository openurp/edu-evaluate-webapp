[#ftl]
[@b.head/]
[@b.toolbar title='教师综合评教结果' id='finalTeacherScoreBar']
bar.addItem("初始化/重新统计","statisticResult()");
    bar.addBlankItem();
[/@]

<table class="indexpanel">
    <tr>
    <td class="index_view">
        [@b.form action="!search" name="finalTeacherScoreIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
	       [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
	       [@b.select style="width:134px" name="finalTeacherScore.staff.state.department.id" label="教师所属院系" items=departments empty="..."/]
	       [@b.textfield style="width:100px" name="finalTeacherScore.staff.code" label="教师工号" /]
	       [@b.textfield style="width:100px" name="finalTeacherScore.staff.person.name.formatedName" label="教师姓名" /]
         [/@]
        </td>
        <td class="index_content">
             [@b.div id="contentDiv" href="!search"/]
        </td> 
    </tr>
</table>
<script type="text/javaScript">
    var form = document.finalTeacherScoreIndexForm;
    function statisticResult(){
        form.target="_blank";
        bg.form.submit(form, "${b.url('!statHome')}","main");
        form.target="contentDiv";
    }
    
</script>
[@b.foot/]