[#ftl]
[@b.head/]
[@b.form name="addTeaEvaluateForm" action="!saveTeaEvaluate" target="contentDiv"]

    <input type="hidden" name="semester.id" value="${semester.id!}">
    [#assign departEvaluationIds = ""]
    [@b.grid items=departEvaluations var="departEvaluation" sortable="false"]    
        [@b.gridbar title="添加部门评教"]
        bar.addItem("提交","saveTeaEvaluate()");
        [/@]
        [@b.row]
            [@b.col property="teacher.code" title="教师工号" /]
            [@b.col property="teacher.name" title="教师姓名" /]
            [@b.col title="性别" property="person.gender.name" width="5%"]${(departEvaluation.teacher.staff.gender.name)!}[/@]
            [@b.col title="出生日期" width="8%" property="person.birthday"]${(departEvaluation.teacher.staff.birthday?string("yyyy-MM-dd"))!}[/@]
            [@b.col title="教师类型" property="teacher.teacherType.name" width="8%"/]
            [@b.col title="部门" property="teacher.department.name" width="20%"/]
            [@b.col title="职称" property="teacher.title.name" width="10%"/]
            [@b.col title="职称等级" property="teacher.title.level.name" sort="level.name" width="7%"/]
            [@b.col title="在职状态" property="teacher.state.name" width="7%"/]
            [@b.col title="任课" property="teaching" width="4%"]${departEvaluation.teacher.teaching?string("是", "否")}[/@]
            [@b.col title="入校日期"  width="8%" property="beginOn"]${(departEvaluation.teacher.beginOn?string("yyyy-MM-dd"))!}[/@]
            [@b.col title="评分"  width="8%" property=""]
            <input type="text" name="${(departEvaluation.id)!}_score" class="scoreResult"  id="${(departEvaluation.id)!}_score" maxLength="7" width="50px" value="${(departEvaluation.score)!0}"/>
            [/@]
            [#if departEvaluationIds?string != "" ]
            [#assign departEvaluationIds = departEvaluationIds + "," +departEvaluation.id]
            [#else]
            [#assign departEvaluationIds = departEvaluationIds + departEvaluation.id]
            [/#if]
            
        [/@]
    [/@]
[/@]
<script type="text/javaScript">
    function saveTeaEvaluate(){
        var flag = true;
        jQuery(".scoreResult").each(function(){
            if(!this.value || jQuery.trim(this.value)=="" || isNaN(this.value) || this.value<0 || this.value > 100){
                alert("请输入0-100之间的数字(最多2位小数)");
                flag =false;
                jQuery(this).focus();
                return false;
            }
        })
        if(flag){
            var form = document.addTeaEvaluateForm;
            bg.form.addInput(form, "departEvaluation.ids","${departEvaluationIds!}");
            bg.form.addInput(form, "semester.id", $("input[name='semester.id']").val());
            bg.form.submit(form, "departEvaluation!save.action");        
        }
    }
</script>
[@b.foot/]