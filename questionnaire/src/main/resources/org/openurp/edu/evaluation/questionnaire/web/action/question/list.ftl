[#ftl]
[@b.head/]
[@b.grid items=questions var="question" sortable="true"]
    [@b.gridbar]
        bar.addItem("${b.text('action.add')}",action.add());
        bar.addItem("${b.text('action.edit')}",action.edit());
        bar.addItem("${b.text('action.delete')}",action.remove());
    [/@]
    [@b.row]
        [@b.boxcol /]
        [@b.col property="contents" title="问题内容" width="50%"]${(question.contents)!}[/@]
        [@b.col property="depart" title="部门" width="10%"]${(question.depart.name)!}[/@]
      [@b.col property="score" title="问题分值" width="8%"]${(question.score)!}[/@]
      [@b.col property="questionType" title="问题类型" width="10%"]${(question.questionType.name)!}[/@]
      [@b.col property="priority" title="优先级" width="10%"]${(question.priority)!}[/@]
      [@b.col property="beginOn" title="有效期" width="12%"]${question.beginOn?string("yyyy-MM-dd")}~${(question.endOn?string("yyyy-MM-dd"))!}[/@]
    [/@]
[/@]
[@b.foot/]
