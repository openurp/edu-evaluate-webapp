[#ftl]
[@b.head/]
[@b.form name="questionnaireClazzSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=lessons var="lesson" sortable="true"]
        [@b.gridbar title="课程问卷列表"]
            bar.addItem("添加全部问卷","updateIds('all')","update.png");
            bar.addItem("添加选择问卷","updateIds('select')","update.png");
            
            function updateIds(item){
                var form = action.getForm()
                var id = bg.input.getCheckBoxValues("lesson.id");
                if (item == "all"){
                    if (!confirm("你确定要设置当前条件下的所有教学任务吗?")){
                        return false;
                    }
                    bg.form.addInput(form, "lesson.ids", "");
                } else {
                    if (id == "" || id.length < 1){
                        alert("请选择一些教学任务!");
                        return false;
                    }
                    if (!confirm("确定要设置所选中的这些教学任务吗?")){
                        return false;
                    }
                    bg.form.addInput(form, "lesson.ids", id);
                }
                var description = $("#questionnaireId>option:selected").attr("description");
                var evaluateId = $("#isEvaluate").val();
                var evaluateText = "教师评教";
                if (evaluateId == 0){
                    evaluateText = "课程评教";
                }
                if (description == undefined){
                    alert("你当前是为没有问卷的课程新增问卷,请选择一门问卷用于指定!");
                    return false;
                }
                if (!confirm("你确定把这些课程的问卷设置为<"+description+">,评教方式设为<"+evaluateText+">吗?")){
                    return false;
                }
                if(""!=action.page.paramstr){
                  bg.form.addHiddens(form,action.page.paramstr);
                  bg.form.addParamsInput(form,action.page.paramstr);
                }
                bg.form.addInput(form, "isAll", item);
                bg.form.addInput(form,"questionnaire.id",$("#questionnaireId>option:selected").val());
                bg.form.addInput(form,"isEvaluate",evaluateId);
                bg.form.submit(form, "${b.url('!saveQuestionnaireClazz')}");
            }
        [/@]
        <div style="height:23px;line-height:23px;border:1px solid white;text-align:center;">
            评教选择:
            [@b.select name="isEvaluate" id="isEvaluate" items={'1':'教师评教','0':'课程评教'}/]
            问卷选择:
            <select id="questionnaireId" name="questionnaireId">
                <option value="">....</option>
                [#list questionnaires?if_exists as questionnaire]
                <option value="${(questionnaire.id)!}" description="${(questionnaire.description)!}">
                    ${(questionnaire.description)!}&nbsp;&nbsp;${(questionnaire.depart.name)!}
                </option>
                [/#list]
            </select>
            <font color="red">(前者是问卷描述,后者是创建部门)</font>
        </div>
        [@b.row]
            [@b.boxcol/]
            [@b.col property="no" title="课程序号" width="10%"/]
            [@b.col property="course.name" title="课程名称"/]
            [@b.col property="courseType.name" title="课程类别"/]
            [@b.col property="teachDepart.name" title="开课院系"/]
            [#--[@b.col property="schedule.firstWeek" title="起始周" width="6%"/]--]
            [@b.col title="任课教师"]
                [#list lesson.teachers?if_exists as teacher]
                    ${(teacher.user.name)!}[#if teacher_has_next],[/#if]
                [/#list]
            [/@]
        [/@]
    [/@]
[/@]
[@b.foot/]    