[#ftl]
[@b.head/]
[@b.toolbar title='教师-问卷评教结果统计' id='departmentEvaluateBar' ]
    var detailMenu = bar.addMenu("院系教师评教比较","departmentChoiceConfig()");
        detailMenu.addItem("学院历史评教", "depHistoryStat()", "info.png");
    bar.addItem("学校教师评教历史汇总","historyCollegeStat()");
    bar.addItem("学校教师评教分项汇总","collegeGroupItemInfo()");
    bar.addItem("初始化/重新统计","statisticResult()");
[/@]
<table class="indexpanel">
    <tr>
    <td class="index_view">
        [@b.form action="!search" name="teacherEvaluateStatIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
            [@b.textfield style="width:100px" name="teacherEvalStat.staff.code" label="工号" /]
            [@b.textfield style="width:100px" name="teacherEvalStat.staff.person.name.formatedName" label="姓名" /]
            [@b.select style="width:105px" name="teacherEvalStat.staff.state.department.id" label="部门" items=departments empty="..."/]
            [@b.select name="teacherEvalStat.questionnaire.id" label="所用问卷" items=[] ]
                [#list questionnaires as q]
                    <option value="${q.id}">${q.description}</option>
                [/#list]
            [/@]
        [/@]
        </td>
        <td class="index_content">
            [@b.div id="contentDiv" href="!search"/]
        </td> 
    </tr>
</table>
<script type="text/javaScript">
    var form = document.teacherEvaluateStatIndexForm;
    
    function departmentChoiceConfig(){
        bg.form.addInput(form,"semester.id",document.teacherEvaluateStatIndexForm['semester.id'].value);
        bg.form.submit(form, "${b.url('!departmentChoiceConfig')}");
    }
    function depHistoryStat(){
        bg.form.submit(form, "${b.url('!depHistoryStat')}");
    }
    function historyCollegeStat(){
        bg.form.submit(form, "${b.url('!historyCollegeStat')}");
    }
    
    function changeSemester(){
        bg.form.addInput(form, "semester.id", $("input[name='semester.id']").val());
        bg.form.submit(form);
    }
    function collegeGroupItemInfo(){
        bg.form.addInput(form,"semester.id",document.teacherEvaluateStatIndexForm['semester.id'].value);
        bg.form.submit(form, "${b.url('!collegeGroupItemInfo')}");
    } 
   function statisticResult(){
        form.target="_blank";
        bg.form.submit(form, "${b.url('!statHome')}","main");
        form.target="contentDiv";
    }
    

</script>
[@b.foot/]