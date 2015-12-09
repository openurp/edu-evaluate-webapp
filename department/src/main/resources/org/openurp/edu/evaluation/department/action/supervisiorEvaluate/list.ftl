[#ftl]
[@b.head/]
    <input type="hidden" name="semester.id" value="${Parameters['supervisiorEvaluate.semester.id']!}">
    [@b.grid items=staffs var="staff" sortable="true"]    
        [@b.gridbar title="督导组评教列表"]
           bar.addItem("${b.text('评教')}","add()","new.png");
        function add(){
            var form = action.getForm();
            var id = bg.input.getCheckBoxValues("staff.id");
            if(id ==""){
                alert("请选择一个或多个进行操作！");
            }else{
                bg.form.addInput(form, "staff.id", id);
                bg.form.addInput(form, "semester.id", "${Parameters['supervisiorEvaluate.semester.id']!}");
                bg.form.submit(form, "${b.url('!addTeaEvaluate')}");
            }
        }
        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="code" title="工号" width="10%"/]
            [@b.col property="person.name.formatedName" title="姓名" width="10%"/]
            [@b.col title="性别" property="person.gender.name" width="10%"/]
            [@b.col title="所在院系" property="state.department.name" width="20%"/]
            [@b.col title="评教状态" width="20%"]
              [#if evaluateMap.get(staff) ??]已评
              [#else]未评
              [/#if]
            [/@]
            [@b.col title="评教总分" width="20%"]${(totalScoreMap.get(staff.id))!}[/@]
        [/@]
    [/@]
[@b.foot/]