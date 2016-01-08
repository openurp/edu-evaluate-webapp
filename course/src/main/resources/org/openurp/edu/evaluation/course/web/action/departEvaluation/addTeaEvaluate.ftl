[#ftl]
[@b.head/]
[@b.form name="addTeaEvaluateForm" action="!saveTeaEvaluate" target="contentDiv"]

    <input type="hidden" name="semester.id" value="${semester.id!}">
    [#assign teacherIds = ""]
    [@b.grid items=teachers var="teacher" sortable="false"]    
        [@b.gridbar title="添加部门评教"]
        bar.addItem("提交","saveTeaEvaluate()");
        [/@]
        [@b.row]
            [@b.col property="code" title="教师工号" /]
            [@b.col property="name" title="教师姓名" /]
            [@b.col title="性别" property="staff.gender.name" width="5%"/]
            [@b.col title="出生日期" width="8%" property="person.birthday"]${(teacher.staff.birthday?string("yyyy-MM-dd"))!}[/@]
            [@b.col title="教师类型" property="teacherType.name" width="8%"/]
            [@b.col title="部门" property="department.name" width="20%"/]
            [@b.col title="职称" property="title.name" width="10%"]${(teacher.title.name)!}[/@]
            [@b.col title="职称等级" property="title.level.name" sort="level.name" width="7%"]${(teacher.title.level.name)!}[/@]
            [@b.col title="在职状态" property="state.name" width="7%"/]
            [@b.col title="任课" property="teaching" width="4%"]${teacher.teaching?string("是", "否")}[/@]
            [@b.col title="入校日期"  width="8%" property="beginOn"]${(teacher.beginOn?string("yyyy-MM-dd"))!}[/@]
            [@b.col title="评分"  width="8%" property=""]
            <input type="text" name="${(teacher.id)!}_score" id="${(teacher.id)!}_score" maxLength="7" width="50px" value=""/>
            [/@]
            [#if teacherIds?string != "" ]
            [#assign teacherIds = teacherIds + "," +teacher.id]
            [#else]
            [#assign teacherIds = teacherIds + teacher.id]
            [/#if]
            
        [/@]
    [/@]
[/@]
<script type="text/javaScript">
    function saveTeaEvaluate(){
        var form = document.addTeaEvaluateForm;
        var str = "";
        [#list teachers as teacher]
        var v= document.getElementById("${(teacher.id)!}"+"_score");
        var reg=/^[0-9]*\.?[0-9]*$/;
        var num = 0;
        if(v.value != "" && v.value != null){
            if(!reg.test(v.value)){
                v.value="";
                num = num + 1;
            }else{
              if(v.value<0 || v.value >100){
                  v.value="";
                  num = num + 1;
            }
            }
        }else{
            num = num + 1;
        }
      
        if(num >0){
          if(str != null && str != ""){
          str = str + ","+"${(teacher.name)!}"+"["+"${(teacher.code)!}"+"]";
          }else{
          str = "${(teacher.name)!}"+"["+"${(teacher.code)!}"+"]";
          }
        }
        [/#list]
        if(str != "" && str != null){
        alert(str + "\r"+"请正确输入0~100的分值！");
        }else{
        bg.form.addInput(form, "teacher.ids","${teacherIds!}");
        bg.form.addInput(form, "semester.id", $("input[name='semester.id']").val());
        bg.form.submit(form, "departEvaluation!saveTeaEvaluate.action");
        }
    }
</script>
[@b.foot/]