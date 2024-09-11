[#ftl]
[@b.head/]
[@b.grid items=indicators var="indicator"]
    [@b.gridbar]
        bar.addItem("${b.text('action.add')}",action.add());
        bar.addItem("${b.text('action.edit')}",action.edit());
        bar.addItem("${b.text('action.delete')}",action.remove());
    [/@]
    [@b.row]
        [@b.boxcol/]
        [@b.col property="code" title="编码"  width="10%"/]
        [@b.col property="name" title="名称"  width="20%"/]
        [@b.col property="enName" title="英文名"  width="30%"/]
        [@b.col property="beginOn" title="有效期" width="42%"]${indicator.beginOn?string("yyyy-MM-dd")}~${(indicator.endOn?string("yyyy-MM-dd"))!}[/@]
    [/@]
[/@]
[@b.foot/]
