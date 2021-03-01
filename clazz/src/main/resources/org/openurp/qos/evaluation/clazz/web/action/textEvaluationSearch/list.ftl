[#ftl]
[@b.head/]
[@b.form name="textEvaluationSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=textEvaluations var="textEvaluation" sortable="true"]
        [@b.gridbar title="学生意见列表"]
            bar.addItem("${b.text('action.export')}","exportData()");

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
                bg.form.addInput(form, "keys", "clazz.crn,clazz.course.name,teacher.name,isForCourse,context,semester.schoolYear,semester.name,isAffirm");
                bg.form.addInput(form, "titles", "课程序号,课程名称,教师名称,是否课程评教,评教内容,学年度,学期,是否确认");
                bg.form.addInput(form, "fileName", "学生意见表");
                [#--bg.form.submit(form,"${b.url('!export')}","_self");--]
            }
        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="clazz.crn" title="课程序号" width="13%"/]
            [@b.col property="clazz.course.code" title="课程代码"/]
            [@b.col property="clazz.course.name" title="课程名称"/]
            [@b.col property="teacher.user.name" title="教师姓名"/]
            [@b.col property="contents" title="评教内容" width="35%"/]
            [@b.col property="audited" title="是否确认" width="8%"]
                ${((textEvaluation.audited)?string("已确认","未确认"))!}
            [/@]
        [/@]
    [/@]
[/@]
[@b.foot/]
