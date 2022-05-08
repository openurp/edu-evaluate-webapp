[#ftl]
[@b.head/]
    <input type="hidden" name="semester.id" value="${Parameters['supervisiorEvaluate.semester.id']!}">
    [@b.grid items=supervisiorEvaluates var="supervisiorEvaluate" sortable="true"]
        [@b.gridbar title="督导组评教列表"]
          bar.addItem("${b.text("评教")}",action.edit(),"new.png");
          var m=bar.addMenu("导入",action.method("importForm"));
          m.addItem("下载导入模版",action.method("importTemplate",null,null,false));
        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="teacher.user.code" title="工号" width="10%"/]
            [@b.col property="teacher.user.name" title="姓名" width="10%"/]
            [@b.col title="性别" property="teacher.user.gender.name" width="10%"/]
            [@b.col title="所在院系" property="teacher.user.department.name" width="20%"/]
            [@b.col title="开课院系" property="department.name" width="20%"/]
            [@b.col title="评教状态" width="10%"]
              [#if (supervisiorEvaluate.totalScore) ??]已评
              [#else]未评
              [/#if]
            [/@]
            [@b.col title="评教总分" property="totalScore" width="10%"/]
        [/@]
    [/@]
[@b.foot/]
