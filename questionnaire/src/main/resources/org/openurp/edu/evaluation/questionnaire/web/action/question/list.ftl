[#ftl]
[@b.head/]
[@b.toolbar title="问题列表" /]


[@b.grid items=questions var="question" sortable="false"]
    [@b.gridbar]
        bar.addItem("${b.text('action.add')}",action.add());
        bar.addItem("${b.text('action.edit')}",action.edit());
        bar.addItem("${b.text('action.delete')}",action.remove());
    [/@]
    [@b.row]
        [@b.boxcol /]
        [@b.col property="content" title="问题内容" width="30%"]${(question.content)!}[/@]
        [@b.col property="depart" title="部门" width="5%"]${(question.depart.name)!}[/@]
	    [@b.col property="score" title="问题分值" width="5%"]${(question.score)!}[/@]
	    [@b.col property="questionType" title="问题类型" width="10%"]${(question.questionType.name)!}[/@]
	    [@b.col property="priority" title="优先级" width="10%"]${(question.priority)!}[/@]
	    [@b.col property="state" title="是否可用" width="40%"]${question.state?string("有效","无效")}[/@]
    [/@]
[/@]
[@b.foot/]