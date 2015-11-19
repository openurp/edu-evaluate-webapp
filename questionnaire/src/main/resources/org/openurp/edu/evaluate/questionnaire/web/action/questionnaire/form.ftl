[#ftl]
[@b.head/]
[@b.toolbar title="问卷列表" /]


[@b.grid items=questionnaires var="questionnaire" sortable="false"]
    [@b.gridbar]
        bar.addItem("${b.text('action.add')}",action.add());
        bar.addItem("${b.text('action.edit')}",action.edit());
        bar.addItem("${b.text('action.delete')}",action.remove());
    [/@]
    [@b.row]
        [@b.col property="description" title="问卷描述" width="30%"]${(questionnaire.description)!}[/@]
	    [@b.col property="depart" title="制作部门" width="20%"]${(questionnaire.depart.name)!}[/@]
	    [@b.col property="state" title="问卷状态" width="20%"]${(questionnaire.state?string("有效","无效")}[/@]
    [/@]
[/@]
[@b.foot/]
