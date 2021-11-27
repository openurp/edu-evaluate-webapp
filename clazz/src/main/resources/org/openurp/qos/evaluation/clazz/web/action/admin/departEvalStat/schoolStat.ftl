[#ftl]
[@b.head/]
[@b.form name="questionnaireStatSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=schoolEvalStats var="schoolEvalStat" sortable="true"]
        [@b.gridbar title="院系教师评教详细信息"]
            //var detailMenu = bar.addMenu("查看详情", "info()");
            //detailMenu.addItem("教师历史评教", "evaluateTeachHistory()", "info.png");
            //detailMenu.addItem("查看大类统计", "doing()", "info.png");
            //detailMenu.addItem("详细选项统计", "teachQuestionDetailStat()", "info.png");
            //detailMenu.addItem("学院分项汇总", "doing()", "info.png");
            //detailMenu.addItem("全校分类评教", "doing()", "info.png");
            bar.addItem("${b.text('action.delete')}","remove()");
            bar.addItem("${b.text('action.export')}","exportData()");

        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="semester" title="学年学期"]${schoolEvalStat.semester.schoolYear!}(${(schoolEvalStat.semester.name)!})[/@]
            [@b.col property="questionnaire.description" title="问卷类型"/]
            [@b.col property="totalScore" title="全校得分"]${schoolEvalStat.totalScore}[/@]
        [/@]
      [/@]
    [/@]
[@b.foot/]
