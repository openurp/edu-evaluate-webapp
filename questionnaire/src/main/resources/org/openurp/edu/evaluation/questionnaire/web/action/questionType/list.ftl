[#ftl]
[@b.head/]
[@b.toolbar title="问题类别列表" /]
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
        [@b.col property="state" title="是否可用"]${questionType.state?string("有效","无效")}[/@]
    [/@]
[/@]
[@b.foot/]
