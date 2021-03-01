[#ftl]
[@b.head/]
[@b.grid items=evalutionCriterias var="evaluationCriteria" sortable="false"]
    [@b.gridbar]
      bar.addItem("${b.text("action.new")}",action.add());
      bar.addItem("${b.text("action.modify")}",action.edit());
      bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    [/@]
    [@b.row]
        [@b.boxcol /]
        [@b.col property="name" title="名称" width="30%"]${(evaluationCriteria.name)!}[/@]
        [@b.col property="depart" title="部门" width="30%"]${(evaluationCriteria.depart.name)!}[/@]
      [@b.col title="对应内容" width="40%"]
      [#list evaluationCriteria.criteriaItems?sort_by("min") as item]
      ${item.name}[${item.min}~${item.max})&nbsp;&nbsp;
      [#else]  无内容
      [/#list]
        [/@]
    [/@]
[/@]
[@b.foot/]
