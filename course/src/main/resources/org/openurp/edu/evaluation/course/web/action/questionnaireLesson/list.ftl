[#ftl]
[@b.head/]
[@b.form name="questionnaireLessonSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=questionnaireLessons var="questionnaireLesson" sortable="true"]
        [@b.gridbar title="课程问卷列表"]
            [#--[#if !isEvaluateSwitch??]--]
            bar.addItem("更新全部问卷","updateIds('all')","update.png");
            bar.addItem("更新选择问卷","updateIds('select')","update.png");
            bar.addItem("${b.text("action.delete")}", action.remove());
            
            function updateIds(item){
                var form = action.getForm()
                var id = bg.input.getCheckBoxValues("questionnaireLesson.id");
                if (item == "all"){
                    if (!confirm("你确定要设置当前条件下的所有教学任务吗?")){
                        return false;
                    }
                    bg.form.addInput(form, "questionnaireLesson.ids", "");
                } else {
                    if (id == "" || id.length < 1){
                        alert("请选择一些教学任务!");
                        return false;
                    }
                    if (!confirm("确定要设置所选中的这些教学任务吗?")){
                        return false;
                    }
                    bg.form.addInput(form, "questionnaireLesson.ids", id);
                }
                var description = $("#questionnaireId>option:selected").attr("description");
                var evaluateId = $("#isEvaluate").val();
                var evaluateText = "教师评教";
                if (evaluateId == 0){
                    evaluateText = "课程评教";
                }
                var alertText = "";
                if (description == undefined){
                    alertText = "你确定把这些课程的问卷清空吗?";
                } else {
                    alertText = "你确定把这些课程的问卷设置为<"+description+">,评教方式设为<"+evaluateText+">吗?";
                }
                if(!confirm(alertText)){
                    return false;
                }
                if(""!=action.page.paramstr){
                  bg.form.addHiddens(form,action.page.paramstr);
                  bg.form.addParamsInput(form,action.page.paramstr);
                }
                bg.form.addInput(form, "isAll", item);
                bg.form.addInput(form,"questionnaire.id",$("#questionnaireId>option:selected").val());
                bg.form.addInput(form,"isEvaluate",evaluateId);
                bg.form.submit(form, "${b.url('!updateQuestionnaireLesson')}");
            }
           [#-- [/#if]--]
        [/@]
        <div style="height:23px;line-height:23px;border:1px solid white;text-align:center;">
            评教方式选择:
            [@b.select name="isEvaluate" id="isEvaluate" items={'1':'教师评教','0':'课程评教'} empty="请选择..."/]
            问卷选择:
            <select id="questionnaireId" name="questionnaireId">
                <option value="">${b.text('请选择')}....</option>
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
            [@b.col property="lesson.no" title="课程序号" width="8%"/]
            [@b.col property="lesson.course.code" title="课程代码"/]
            [@b.col property="lesson.course.name" title="课程名称"/]
            [@b.col property="lesson.courseType.name" title="课程类别"/]
            [@b.col property="questionnaire.description" title="使用问卷描述"/]
            [@b.col property="evaluateByTeacher" title="评教方式" width="8%"]
                ${(questionnaireLesson.evaluateByTeacher?string("教师评教","课程评教"))!}
            [/@]
            [@b.col property="lesson.teachDepart.name" title="开课院系"/]
            [@b.col title="任课教师"]
                [#list questionnaireLesson.lesson.teachers?if_exists as teacher]
                    ${(teacher.user.name)!}
                [/#list]
            [/@]
        [/@]
    [/@]
[/@]
[@b.foot/]