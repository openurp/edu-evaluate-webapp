[#ftl]
[@b.head/]
[@b.grid items=assessCriterias var="assessCriteria" sortable="false"]
    [@b.gridbar]
      bar.addItem("${b.text("action.new")}",action.add());
      bar.addItem("${b.text("action.modify")}",action.edit());
      bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    [/@]
    [@b.row]
        [@b.boxcol /]
        [@b.col property="name" title="名称" width="30%"]${(assessCriteria.name)!}[/@]
        [@b.col property="beginOn" title="有效期" width="42%"]${assessCriteria.beginOn?string("yyyy-MM-dd")}~${(assessCriteria.endOn?string("yyyy-MM-dd"))!}[/@]
      [@b.col title="对应内容" width="40%"]
      [#list assessCriteria.grades?sort_by("minScore") as item]
      ${item.name}[${item.minScore}~${item.maxScore})&nbsp;&nbsp;
      [#else]  无内容
      [/#list]
        [/@]
    [/@]
[/@]
[@b.foot/]
