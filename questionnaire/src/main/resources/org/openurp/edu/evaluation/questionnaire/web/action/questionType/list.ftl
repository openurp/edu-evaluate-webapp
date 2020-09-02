[#ftl]
[@b.head/]
[@b.grid items=questionTypes var="questionType"]
    [@b.gridbar]
        bar.addItem("${b.text('action.add')}",action.add());
        bar.addItem("${b.text('action.edit')}",action.edit());
        bar.addItem("${b.text('action.delete')}",action.remove());
    [/@]
    [@b.row]
        [@b.boxcol/]
        [@b.col property="name" title="名称"/]
        [@b.col property="priority" title="优先级"/]
        [@b.col property="beginOn" title="有效期" width="12%"]${questionType.beginOn?string("yyyy-MM-dd")}~${(questionType.endOn?string("yyyy-MM-dd"))!}[/@]
    [/@]
[/@]
[@b.foot/]
