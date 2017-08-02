<#include "/template/head.ftl"/>
<script language="JavaScript" type="text/JavaScript" src="scripts/validator.js"></script>
<#include "/pages/evaluate/ealuateJs.ftl"/>
<BODY LEFTMARGIN="0" TOPMARGIN="0">
     <table id="backBar" width="100%"></table>
      <table cellpadding="0" cellspacing="0" align="center" width="100%" border="0">
        <tr>
            <td>
                <form name="evaluateForm" method="post" action="" >
                    <input type="hidden" name="doSave" value="save">
                    <table width="100%" align="center" class="listTable">
                        <tr>
                            <td align="center" class="darkColumn" colspan="2"><@text name="field.questionnaireStatistic.addEvaluateResult"/><font color="red"><@text name="field.questionnaireStatistic.addEvaluateResultLastResult"/></font>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" class="grayStyle" width="30%" id="f_remark">学年度学期:
                            </td>
                            <td align="left" class="brightStyle">
                                <select id="semesterId" name="semesterId" style="width=300px;" disabled onchange="doButtonEstate('semesterId',true)">
                                    <#list semesters?sort_by("start")?if_exists as semester>
                                        <#if semester.id==nearCalendar.id>
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
                            <td align="center" class="grayStyle" width="30%">开课院系:
                            </td>
                            <td align="left" class="brightStyle">
                                <select id="collegeId" name="questionnaireStatistic.openPart.id" style="width:200px;">
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
                                <input type="hidden" name="taskId">
                                <input type="text" name="courseName" maxlength="20" style="width:300px;" onfocus="this.blur();">
                                <input type="button" name="buttonCollege" value="<@text name="field.select.course"/>" class="buttonStyle" onclick="loadCourses()">
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
                            <td align="center" class="grayStyle" id="f_validTicket">有效票数:<font color="red">*</font></td>
                            <td align="left" class="brightStyle"><input type="text" name="validTicket" value="0" width="150px;"></td>
                        </tr>
                        <tr>
                            <td align="center" class="grayStyle" width="30%" id="f_questionMark">
                                <@text name="field.questionnaireStatistic.totleEvaluate"/>:<font color="red">*</font>
                            </td>
                            <td align="left" class="brightStyle">
                                <input type="text" name="totleEvaluate" maxlength="3" style="width:200px;">
                                <font color="red"><@text name="field.questionnaireStatistic.addEvaluateResultLastResultRemark"/></font>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" class="darkColumn" colspan="2">
                                <button name="button1" class="buttonStyle" onclick="doAction(this.form)"><@text name="system.button.add"/></button>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
    </table>
    <script>
           var bar = new ToolBar('backBar','<@text name="field.questionnaireStatistic.addEvaluateResult"/>',null,true,true);
           bar.setMessage('<@getMessage/>');
           
           function loadCourses() {
            var collegeId = document.evaluateForm['questionnaireStatistic.openPart.id'].value;
            var semesterId = document.evaluateForm.semesterId.value;
            var url = "questionnaireStat.action?method=getCourses&collegeId="+collegeId+"&semesterId="+semesterId;
            window.open(url, '', 'scrollbars=yes,left=0,top=0,width=500,height=550,status=yes');
        }
        
        function setTeachTaskIdAndDescriptions(ids, descriptions) {
               document.evaluateForm['taskId'].value = ids;
               document.evaluateForm['courseName'].value = descriptions.replace(/,/gi, "\n");
            }
            
        function addTargetValue(id, value) {
            var target = document.getElementById(id);
        }
        
        function doAction(form){
            var a_fields = {
             'courseName':{'l':'<@text name="field.select.course"/>', 'r':true, 't':'f_teachTaskId'},
             'teacherId':{'l':'<@text name="field.questionnaireStatistic.selectTecher"/>', 'r':true, 't':'f_teacher'},
             'validTicket':{'l':'有效票数', 'r':true, 't':'f_validTicket'},
             'totleEvaluate':{'l':'<@text name="field.questionnaireStatistic.totleEvaluate"/>', 'r':true, 't':'f_questionMark','f':'real','mx':'3'}
             };
             var v = new validator(form, a_fields, null);
             if (v.exec()) {
              var mark = new Number(document.evaluateForm.totleEvaluate.value);
              if(mark>100){
                  alert("百分制不能大于100分");
                  return;
              }
             form.action ="questionnaireStat.action?method=saveStatisticEvaluateInfo";
            form.submit();
             }
        }
        
        function setTeacherIdAndDescriptions(ids, descriptions){
               document.evaluateForm['teacherId'].value = ids;
               document.evaluateForm['teacherName'].value = descriptions.replace(/,/gi, "\n");
        }
        
        function addOption(selectId, data){
            var select = document.getElementById(selectId);
            dwr.util.removeAllOptions(selectId);
            var datas = data.split(",");
            select.add(new Option(datas[1],datas[0]));
        }
        
        function doButtonEstate(inputId,isOpen){
            var input = document.getElementById(inputId);
            input.disabled=isOpen;
        }    
    </script>
</body>
<#include "/template/foot.ftl"/>