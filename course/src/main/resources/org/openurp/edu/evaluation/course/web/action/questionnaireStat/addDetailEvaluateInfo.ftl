<#include "/template/head.ftl"/>
<#include "/pages/evaluate/ealuateJs.ftl"/>
 <script language="JavaScript" type="text/JavaScript" src="scripts/validator.js"></script>
<BODY LEFTMARGIN="0" TOPMARGIN="0">
 <table id="backBar" width="100%"></table>
  <table cellpadding="0" cellspacing="0" align="center" width="100%" border="0">
    <tr>
    <td>
    <form name="evaluateForm" method="post" action="" >
    <input type="hidden" name="doSave" value="save">
    <table width="100%" align="center" class="listTable">
        <tr>
            <td align="center" class="darkColumn" colspan="2"><@text name="field.questionnaireStatistic.addEvaluateResult"/><font color="red"><@text name="field.questionnaireStatistic.addEvaluateResultOneQuestion"/></font>
            </td>
        </tr>
        <tr>
            <td align="center" class="grayStyle" width="30%" id="f_remark">学年度学期:
            </td>
            <td align="left" class="brightStyle">
                <select id="semesterId" name="semesterId" style="width=300px;" disabled onchange="doButtonEstate('semesterId', true)">
                    <#list semesters?sort_by("start")?if_exists as semester>
                        <#if semester.id == nearCalendar.id>
                            <option value="${semester.id}" selected>${semester.calendar.educationType.name}/${semester.schoolYear}/${semester.name}</option>
                        <#else>
                            <option value="${semester.id}">${semester.calendar.educationType.name}/${semester.schoolYear}/${semester.name}</option>
                        </#if>
                    </#list>
                </select>
                <input type="button" name="button1" value="切换学期" onclick="doButtonEstate('semesterId',false)" class="buttonStyle">
            </td>
        </tr>
        <tr>
            <td align="center" class="grayStyle" width="30%">开课院系
            </td>
            <td align="left" class="brightStyle">
                <select name="evaluateResults.department.id" style="width:200px;">
                    <#list collegeList?if_exists as college>
                        <option value="${college.id}">${college.name}</option>
                    </#list>
                </select>
            </td>
        </tr>
        <tr>
            <td align="center" class="grayStyle" width="30%" id="f_teachTaskId">
                <@text name="field.select.course"/>:<font color="red">*</font>
            </td>
            <td align="left" class="brightStyle">
                <input type="hidden" id="taskId"/>
                <input type="text" name="courseName" maxlength="20" style="width:200px;" onfocus="this.blur();"/>
                <button name="buttonCollege" class="buttonStyle" onclick="loadCourses()"><@text name="field.select.course"/></button>查询条件来自教学日历和开课院系
            </td>
        </tr>
        <tr>
            <td align="center" class="grayStyle" width="30%" id="f_teacher">
                <@text name="field.questionnaireStatistic.selectTecher"/>:<font color="red">*</font>
            </td>
            <td align="left" class="brightStyle">
                <select id="teacherId" name="teacherId" style="width:200px;">
                    <option value="">先选择课程 再选择教师</option>
                </select>
            </td>
        </tr>
        <tr>
            <td align="center" class="grayStyle" width="30%" id="f_question">
                <@text name="field.questionnaire.selectQuestion"/>:<font color="red">*</font>
            </td>
            <td align="left" class="brightStyle">
                <input type="hidden" id="isHasQuestionnaire" name="isHasQuestionnaire" value="true">
                <input type="hidden" name="questionId">
                <input type="hidden" name="questionMark">
                <input type="text" style="width:300px;" id="questionName" maxlength="30" name="questionName" readonly="true"/>
                <button name="buttonQuestion" class="buttonStyle" onclick="loadQuestion()"><@text name="field.questionnaire.selectQuestion"/></button>
            </td>
        </tr>
        <tr>
            <td align="center" class="grayStyle" width="30%" id="f_score">
                <@text name="field.questionnaireStatistic.questionMark"/>:<font color="red">*</font>
            </td>
            <td align="left" class="brightStyle">
                <input type="text" id="score" name="evaluateResults.score" maxlength="3" style="width:200px;"/>
            </td>
        </tr>
        <tr>
            <td align="center" class="darkColumn" colspan="2">
                <button name="commitButton" class="buttonStyle" onclick="doAction(this.form)"><@text name="action.add"/></button>
            </td>
        </tr>
        </table>
        </form>
        </td>
        </tr>
    </table>
     <script languae="javascript">
           var bar = new ToolBar('backBar','<@text name="field.questionnaireStatistic.addEvaluateResult"/>',null,true,true);
           bar.setMessage('<@getMessage/>');
           var qscore = 0 ;
           
        function loadCourses(){
            var semesterId = document.evaluateForm.semesterId.value;
            var collegeId = document.evaluateForm['evaluateResults.department.id'].value;
            var errors="";
            if(""==semesterId){
                errors+="请选择教学日历\n";
            }
            if(""==collegeId){
                errors+="请选择开课院系\n";
            }
            if(""!=errors){
                alert(errors);
                return;
            }
            var url="questionnaireStat.action?method=getCourses&collegeId=" +collegeId+"&semesterId="+semesterId;
            window.open(url, '', 'scrollbars=yes,left=0,top=0,width=500,height=550,status=yes');
        }
        function setTeachTaskIdAndDescriptions(ids, descriptions){      
               document.evaluateForm['taskId'].value = ids;
               document.evaluateForm['courseName'].value = descriptions.replace(/,/gi, "\n");
            }
        function setQuestionIdAndDescriptions(ids, descriptions,mark){      
               document.evaluateForm['questionId'].value = ids;
               document.evaluateForm['questionName'].value = descriptions.replace(/,/gi, "\n");
               document.evaluateForm['questionMark'].value = mark;
        }
        function loadQuestion(){
            var taskId = document.evaluateForm['taskId'].value;
            if("" == taskId){
                alert("请先选择教学任务");
                return;
            }
            var isHasQuestionnaire = document.getElementById("isHasQuestionnaire");
            if("true"==isHasQuestionnaire.value){
                alert("你选择的课程没有问卷,请重新选择");
                return;
            }
            var url="questionnaireStat.action?method=getQuestions&taskId="+taskId;
            window.open(url, '', 'scrollbars=yes,left=0,top=0,width=500,height=550,status=yes');
        }
        function addTargetValue(id,value){
            var target = document.getElementById(id);
            target.value=value;
        }
        function doAction(form){
            var a_fields = {
             'courseName':{'l':'<@text name="field.select.course"/>', 'r':true, 't':'f_teachTaskId'},
             'teacherId':{'l':'<@text name="field.questionnaireStatistic.selectTecher"/>', 'r':true, 't':'f_teacher'},
             'questionName':{'l':'<@text name="field.questionnaire.selectQuestion"/>', 'r':true, 't':'f_question'},
             'evaluateResults.score':{'l':'<@text name="field.questionnaireStatistic.questionMark"/>', 'r':true, 't':'f_score','f':'real','mx':4}
             };
             var v = new validator(form, a_fields, null);
             var questionmark=new Number(document.evaluateForm['questionMark'].value);
             var score = new Number(document.evaluateForm['evaluateResults.score'].value);
             if (v.exec()) {
                 if(score > qscore) {
                     alert("这个问题的分值为"+questionmark+",得分不能大于这个值")
                     return;
                 }
                 form.action ="questionnaireStat.action?method=saveDetailEvaluateInfo";
                form.submit();
             }
        }
        function setTeacherIdAndDescriptions(ids,descriptions){
               document.evaluateForm['teacherId'].value = ids;
               document.evaluateForm['teacherName'].value = descriptions.replace(/,/gi, "\n");
        }
        function doButtonEstate(inputId, isOpen){
            var input = document.getElementById(inputId);
            input.disabled=isOpen;
        }    
    </script>
</body>
<#include "/template/foot.ftl"/>