[#ftl]
[@b.head/]
    <input type="hidden" name="semester.id" value="${semesterId!}">
    [@b.grid items=departEvaluations var="departEvaluation" sortable="true"]    
        [@b.gridbar title="部门评教列表"]
            bar.addItem("${b.text('action.edit')}","editTeaEvaluate()");
            bar.addItem("${b.text('action.delete')}",action.remove());
            bar.addItem("${b.text('action.export')}","exportData()");
            
            function editTeaEvaluate(){
                var ids = bg.input.getCheckBoxValues("departEvaluation.id");
                if(ids ==""){
                    alert("请选择一个或多个进行操作！");
                }else{
                    var form = action.getForm();
                    bg.form.addInput(form, "departEvaluation.ids", ids);
                    bg.form.addInput(form, "semester.id", $("input[name='semester.id']").val());
                    bg.form.submit(form, "${b.url('!edit')}");
                }
            }
            function exportData(){
                var form = action.getForm();
                bg.form.addInput(form, "keys", "teacher.code,teacher.name,teacher.department.name,score");
                bg.form.addInput(form, "titles", "教师工号,教师姓名,所在院系,评分");
                bg.form.addInput(form, "fileName", "部门评教结果导出");
                bg.form.submit(form, "${b.url('!export')}","_self");
            }
        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="teacher.code" title="工号" /]
            [@b.col property="teacher.name" title="姓名" /]
            [@b.col title="性别" property="teacher.staff.gender.name" width="5%"]${(departEvaluation.teacher.staff.gender.name)!}[/@]
            [@b.col title="出生日期" width="8%" property="teacher.staff.birthday"]${(departEvaluation.teacher.staff.birthday?string("yyyy-MM-dd"))!}[/@]
            [@b.col title="教师类型" property="teacher.teacherType.name" width="8%"/]
            [@b.col title="所在院系" property="teacher.department.name" width="20%"/]
            [@b.col title="职称" property="teacher.title.name" width="10%"/]
            [@b.col title="职称等级" property="teacher.title.level.name" sort="teacher.title.level" width="7%"/]
            [@b.col title="在职状态" property="teacher.state.name" width="7%"/]
            [@b.col title="任课" property="teacher.teaching" width="4%"]${departEvaluation.teacher.teaching?string("是", "否")}[/@]
            [@b.col title="入校日期"  width="8%" property="teacher.beginOn"]${(departEvaluation.teacher.beginOn?string("yyyy-MM-dd"))!}[/@]
            [@b.col title="评分" property="score" width="7%"/]
        [/@]
    [/@]
[@b.foot/]