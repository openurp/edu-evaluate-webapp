[#ftl]
[@b.head/]
[@b.toolbar title="选项组列表" /]

[@b.grid items=optionGroups var="optionGroup" sortable="false"]
    [@b.gridbar]
        bar.addItem("${b.text('action.add')}",action.add());
        bar.addItem("${b.text('action.edit')}",action.edit());
        bar.addItem("${b.text('action.delete')}",action.remove());
    [/@]
    [@b.row]
        [@b.boxcol /]
        [@b.col property="name" title="名称" width="30%"]${(optionGroup.name)!}[/@]
      [@b.col title="选项" width="60%"]
      [#list optionGroup.options?sort_by("proportion")?reverse as option]
      ${option.name}(${option.proportion?default(0)})&nbsp;
      [#else]  无内容
      [/#list]
        [/@]
    [/@]
[/@]
[@b.foot/]
