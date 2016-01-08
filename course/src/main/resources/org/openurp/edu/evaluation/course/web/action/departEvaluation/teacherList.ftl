[#ftl]
[@b.head/]
<input type="hidden" name="semester.id" value="${semesterId!}">
[@b.grid items=teachers var="teacher" sortable="true"]    
    [@b.gridbar title="部门评教列表"]
        bar.addItem("${b.text('action.add')}","add()","new.png");
        
        function add(){
            var form = action.getForm();
            var ids = bg.input.getCheckBoxValues("teacher.id");
            if(ids ==""){
                alert("请选择一个或多个进行操作！");
            }else{
                bg.form.addInput(form, "teacher.ids", ids);
                bg.form.addInput(form, "semester.id", $("input[name='semester.id']").val());
                bg.form.submit(form, "${b.url('!addTeaEvaluate')}");
            }
        }
    [/@]
    [@b.row]
        [@b.boxcol/]
        [@b.col property="code" title="教师工号" /]
        [@b.col property="name" title="教师姓名" /]
        [@b.col title="性别" property="staff.gender.name" width="5%"/]
        [@b.col title="出生日期" width="8%" property="staff.birthday"]${(teacher.staff.birthday?string("yyyy-MM-dd"))!}[/@]
        [@b.col title="教师类型" property="teacherType.name" width="8%"/]
        [@b.col title="部门" property="department.name" width="20%"/]
        [@b.col title="职称" property="title.name" width="10%"]${(teacher.title.name)!}[/@]
        [@b.col title="职称等级" property="title.level.name" sort="title.level" width="7%"]${(teacher.title.level.name)!}[/@]
        [@b.col title="在职状态" property="state.name" width="7%"/]
        [@b.col title="任课" property="teaching" width="4%"]${teacher.teaching?string("是", "否")}[/@]
        [@b.col title="入校日期"  width="8%" property="beginOn"]${(teacher.beginOn?string("yyyy-MM-dd"))!}[/@]
    [/@]
[/@]
[@b.foot/]