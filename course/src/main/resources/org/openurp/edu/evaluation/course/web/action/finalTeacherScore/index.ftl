[#ftl]
[@b.head/]
[@b.toolbar title='教师综合评教结果' id='finalTeacherScoreBar']
bar.addItem("初始化/重新统计","statisticResult()");
bar.addItem("排名统计","statisticRank()");
    bar.addBlankItem();
[/@]

<table class="indexpanel">
    <tr>
    <td class="index_view">
        [@b.form action="!search" name="finalTeacherScoreIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
	       [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
	       [@b.select style="width:134px" name="finalTeacherScore.teacher.state.department.id" label="教师所属院系" items=departments empty="..."/]
	       [@b.textfield style="width:100px" name="finalTeacherScore.teacher.code" label="教师工号" /]
	       [@b.textfield style="width:100px" name="finalTeacherScore.teacher.person.name.formatedName" label="教师姓名" /]
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
    function statisticRank(){
        bg.form.addInput(form,"semester.id",document.finalTeacherScoreIndexForm['semester.id'].value);
        bg.form.submit(form, "${b.url('!rankStat')}");
    }
    
</script>
[@b.foot/]