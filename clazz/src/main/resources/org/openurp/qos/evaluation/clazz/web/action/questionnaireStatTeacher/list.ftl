[#ftl]
[@b.head/]
[@b.form name="questionnaireStatTeachersForm" action="!search" target="contentDiv"]
    [@b.grid items=questionnaireStatTeachers var="teacherStat"]
    [@b.gridbar]
           bar.addItem("${b.text('action.info')}","info()");
    [/@]
    [@b.row]
        [@b.boxcol/]
        [@b.col property="version" title="学年学期" width="10%"]
           ${teacherStat.clazz.semester.schoolYear?if_exists}(${teacherStat.clazz.semester.name?if_exists})[/@]
        [@b.col property="clazz.crn" title="课程序号" width="5%"/]
        [@b.col property="clazz.course.code" title="课程代码" width="8%"/]
        [@b.col property="course.name" title="课程名称" width="15%"]
        <A href="#" onclick="evaluatePersonInfo('${teacherStat.id}')">${teacherStat.clazz.course.name}</A>
        [/@]
        [@b.col property="" title="任课教师" width="10%"]
        [#list (teacherStat.clazz.teachers)?if_exists as teacher]
                       ${teacher.user.name}[#if teacher_has_next],[/#if]
                   [/#list]
        [/@]
        [@b.col property="clazz.teachclass.name" title="教学班" width="12%"/]
        [#--[#list questionTypeList?if_exists as questionType]
             [@b.col property="press.name" title="${questionType.name!}"  width="17%"]
             ${teacherStat.getTypeScoreDisplay(criteria,questionType.id)}
             [/@]
         [/#list]--]
        [#if teacherStat?exists]
            [#list teacherStat.questionTypeStats as questionType]
            [@b.col property="" title="${questionType.questionType.name}"]${questionType.score!}[/@]
            [/#list]
        [/#if]
        [@b.col property="score" title="得分" width="10%"/]
        <input type="hidden" id="isEvaluateDetail_${(teacherStat.id)!}" value="${isEvaluateDetail?if_exists?string("1", "0")}"/>
    [/@]
[/@]
[/@]
    [#--[@htm.actionForm name="actionForm" action="questionnaireStatTeacher.action" entity="teacherStat"]
        <input type="hidden" name="teacherStatId" value=""/>
    [/@]--]
    <script>
    var searchForm = document.questionnaireStatTeachersForm;
        function evaluatePersonInfo(teacherStatId) {
           // form.target="_blank";
           // form.action = "questionnaireStatTeacher.action?method=evaluatePersonInfo";
           // form["teacherStatId"].value = teacherStatId;
           // form.submit();
           bg.form.addInput(searchForm,"teacherStatId",$("input[name='teacherStat.id']").val());
           bg.form.submit(searchForm, "questionnaire-stat-teacher!evaluatePersonInfo.action");
        }

        function info() {
            bg.form.submit(searchForm, "questionnaire-stat-teacher!info.action");
            //form["teacherStat.id"].value = getIds();
            //form.target="_blank";
            //form.action = "questionnaireStatTeacher.action?method=info";
            //form.submit();

        }

        function getIds(){
            var str ="";
            var checkboxs=document.getElementsByTagName("checkBox");
            var i;
            var nm =0;
            for(i=0;i<checkboxs.length;i++){
                if(checkboxs[i].type=='checkbox'){
                    if(checkboxs[i].checked){
                        str =str +checkboxs[i].value;
                        nm = nm +1;
                    }
                }
            }
            if(nm !=1){
                alert("请选择一个！");
                return false;
            }
            return(str);
        }
    </script>
[@b.foot/]
