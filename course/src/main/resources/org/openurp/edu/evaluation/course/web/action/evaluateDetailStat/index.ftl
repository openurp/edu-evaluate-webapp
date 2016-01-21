[#ftl]
[@b.head/]
[@b.toolbar title='评教统计结果管理' id='evaluateDetailStatBar']
    var detailMenu = bar.addMenu("院系评教详情","departmentChoiceConfig()");
        detailMenu.addItem("学院历史评教", "depHistoryStat()", "info.png");
        //detailMenu.addItem("学院本次评教", "doing()", "info.png");
    
    bar.addItem("历史课程评教汇总","historyCollegeStat()");
    bar.addItem("学校分项评教汇总","collegeGroupItemInfo()");
    //bar.addItem("初始化有效结果","setValidResult()");
    bar.addItem("初始化/重新统计","statisticResult()");
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
    
    function departmentChoiceConfig(){
        [#--bg.form.addInput(form,"semester.id","${semester.id}");--]
        bg.form.submit(form, "${b.url('!departmentChoiceConfig')}");
    }
    
    function depHistoryStat(){
        [#--bg.form.addInput(form,"semester.id","${semester.id}");--]
        bg.form.submit(form, "${b.url('!depHistoryStat')}");
    }
    
    function historyCollegeStat(){
        
        bg.form.submit(form, "${b.url('!historyCollegeStat')}");
    }
    function collegeGroupItemInfo(){
        [#--bg.form.addInput(form,"semester.id","${semester.id}");--]
        bg.form.submit(form, "${b.url('!collegeGroupItemInfo')}");
    }
</script>
[@b.foot/]