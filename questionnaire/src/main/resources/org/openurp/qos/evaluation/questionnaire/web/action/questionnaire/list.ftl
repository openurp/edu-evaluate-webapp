[#ftl]
[@b.head/]
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
      [@b.col property="state" title="有效期" width="40%"]${questionnaire.beginOn?string("yyyy-MM-dd")}~${(questionnaire.endOn?string("yyyy-MM-dd"))!}[/@]
    [/@]
[/@]
[@b.foot/]
