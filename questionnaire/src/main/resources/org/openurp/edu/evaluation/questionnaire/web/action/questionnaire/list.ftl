[#ftl]
[@b.head/]
[@b.toolbar title="问卷列表" /]

[@b.grid items=questionnaires var="questionnaire" sortable="true"]
    [@b.gridbar]
        bar.addItem("${b.text('action.add')}",action.add());
        bar.addItem("${b.text('action.edit')}",action.edit());
        bar.addItem("${b.text('action.delete')}",action.remove());
    [/@]
    [@b.row]
        [@b.boxcol /]
        [@b.col property="description" title="问卷描述"][@b.a href="!info?id=${questionnaire.id}"]${(questionnaire.description)!}[/@][/@]
      [@b.col property="depart" title="制作部门" width="30%"]${(questionnaire.depart.name)!}[/@]
      [@b.col property="state" title="问卷状态" width="40%"]${questionnaire.state?string("有效","无效")}[/@]
    [/@]
[/@]
[@b.foot/]
