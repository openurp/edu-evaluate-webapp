[#ftl]
[@b.head/]
[@b.toolbar title='任务-问卷评教结果统计' id='evaluateDetailStatBar']
    var detailMenu = bar.addMenu("院系任务评教比较","departmentChoiceConfig()");
        detailMenu.addItem("学院历史评教", "depHistoryStat()", "info.png");
        //detailMenu.addItem("学院本次评教", "doing()", "info.png");

    bar.addItem("学校任务评教历史汇总","historyCollegeStat()");
    bar.addItem("学校任务评教分项汇总","collegeGroupItemInfo()");
    //bar.addItem("初始化有效结果","setValidResult()");
    bar.addItem("初始化/重新统计","statisticResult()");
    bar.addItem("排名统计","statisticRank()");
[/@]

[#include "searchForm.ftl"/]
<script type="text/javaScript">
    function setValidResult(){
        bg.form.submit(form, "${b.url('!initValidHome')}", "main");
    }

    function statisticResult(){
        form.target="_blank";
        bg.form.submit(form, "${b.url('!statHome')}","main");
        form.target="contentDiv";
    }

    function statisticRank(){
        bg.form.addInput(form,"semester.id",document.evaluateTeacherStatIndexForm['semester.id'].value);
        bg.form.submit(form, "${b.url('!rankStat')}");
    }

    function departmentChoiceConfig(){
        bg.form.addInput(form,"semester.id",document.evaluateTeacherStatIndexForm['semester.id'].value);
        bg.form.submit(form, "${b.url('!departmentChoiceConfig')}");
    }

    function depHistoryStat(){
        bg.form.submit(form, "${b.url('!depHistoryStat')}");
    }

    function historyCollegeStat(){
        bg.form.submit(form, "${b.url('!historyCollegeStat')}");
    }
    function collegeGroupItemInfo(){
        bg.form.addInput(form,"semester.id",document.evaluateTeacherStatIndexForm['semester.id'].value);
        bg.form.submit(form, "${b.url('!collegeGroupItemInfo')}");
    }
</script>
[@b.foot/]
