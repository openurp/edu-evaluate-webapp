[#ftl]
[@b.head/]
[@b.form name="textEvaluationSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=textEvaluations var="textEvaluation" sortable="true"]
        [@b.gridbar title="学生意见列表"]
            bar.addItem("确认","updateAffirm(1)");
            bar.addItem("取消确认","updateAffirm(0)");
            bar.addItem("${b.text('action.delete')}",action.remove());
            bar.addItem("${b.text('action.export')}","exportData()");
        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="clazz.crn" title="课程序号" width="6%"/]
            [@b.col property="clazz.course.code" title="课程代码" width="10%"/]
            [@b.col property="clazz.course.name" title="课程名称" width="25%"/]
            [@b.col property="teacher.user.name" title="教师姓名" width="10%"/]
            [@b.col property="contents" title="评教内容" width="30%"/]
            [@b.col property="evaluateByTeacher" title="评价对象" width="8%"]${(textEvaluation.evaluateByTeacher?string("教师","课程"))!} [/@]
            [@b.col property="audited" title="是否确认" width="7%"]${(textEvaluation.audited?string("已确认","未确认"))!} [/@]
        [/@]
    [/@]
[/@]
<script type="text/javaScript">
    var searchForm = document.textEvaluationSearchForm;

    function updateAffirm(num){
        var id = bg.input.getCheckBoxValues("textEvaluation.id");
        if (id == "" || id.length < 1) {
            alert("你没有选择要操作的记录!");
            return false;
        }
        bg.form.addInput(searchForm, "state", num);
        bg.form.submit(searchForm, "${b.url('!updateAffirm')}");
    }
    function exportData() {
            var textEvaluationIds = bg.input.getCheckBoxValues("textEvaluation.id");
            var form = action.getForm();
            if (textEvaluationIds) {
                bg.form.addInput(form,"textEvaluationIds",textEvaluationIds);
            }else{
                if(!confirm("是否导出查询条件内的所有数据?")) return;
                    if(""!=action.page.paramstr){
                      bg.form.addHiddens(form,action.page.paramstr);
                      bg.form.addParamsInput(form,action.page.paramstr);
                    }
                bg.form.addInput(form,"textEvaluationIds","");
            }
            bg.form.addInput(form, "keys", "clazz.crn,clazz.course.name,teacher.user.name,evaluateByTeacher,contents,semester.schoolYear,semester.name,state");
            bg.form.addInput(form, "titles", "课程序号,课程名称,教师名称,是否课程评教,评教内容,学年度,学期,是否确认");
            bg.form.addInput(form, "fileName", "学生意见表");
            [#--bg.form.submit(form,"${b.url('!export')}","_self");--]
        }

</script>
[@b.foot/]
