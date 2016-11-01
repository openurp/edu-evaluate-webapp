[#ftl]
[@b.head/]
    <input type="hidden" name="semester.id" value="${Parameters['departEvaluate.semester.id']!}">
    [@b.grid items=departEvaluates var="departEvaluate" sortable="true"]    
        [@b.gridbar title="部门评教列表"]
          bar.addItem("${b.text("评教")}",action.edit(),"new.png");
          var m=bar.addMenu("导入",action.method("importForm"));
          m.addItem("下载导入模版",action.method("importTemplate",null,null,false));
        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="teacher.code" title="工号" width="10%"/]
            [@b.col property="teacher.person.name.formatedName" title="姓名" width="10%"/]
            [@b.col title="性别" property="teacher.person.gender.name" width="10%"/]
            [@b.col title="所在院系" property="teacher.user.department.name" width="20%"/]
            [@b.col title="评教状态" width="20%"]
              [#if (departEvaluate.totalScore) ??]已评
              [#else]未评
              [/#if]
            [/@]
            [@b.col title="评教总分" width="20%"]${(departEvaluate.totalScore)!}[/@]
        [/@]
    [/@]
[@b.foot/]