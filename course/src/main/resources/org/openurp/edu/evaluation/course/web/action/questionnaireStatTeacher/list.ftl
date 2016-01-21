[#ftl]
[@b.head/]
    [@b.grid items=questionnaireStatTeachers var="teacherStat"]
    [#--][@b.gridbar]
           bar.addItem("${b.text('action.info')}","info()");
    [/@][--]
    [@b.row]
        [#--][@b.boxcol/][--]
        [@b.col property="course.name" title="课程名称" width="15%"]
        <A href="#" onclick="javascript:evaluatePersonInfo('${teacherStat.id}')">${teacherStat.lesson.course.name}</A>
        [/@]
        [@b.col property="" title="任课教师" width="20%"]
        [#list (teacherStat.lesson.teachers)?if_exists as teacher]
                       ${teacher.name}[#if teacher_has_next],[/#if]
                   [/#list]
        [/@]
        [#list questionTypeList?if_exists as questionType]
             [@b.col property="press.name" title="${questionType.name!}"  width="17%"]
             ${teacherStat.getTypeScoreDisplay(criteria,questionType.id)}
             [/@]
         [/#list]
         [@b.col property="version" title="得分" width="10%"]
         ${teacherStat.getScoreDisplay(criteria)}[/@]
           [@b.col property="version" title="学年学期" width="10%"]
           ${teacherStat.semester.schoolYear?if_exists}(${teacherStat.semester.name?if_exists})[/@]
        <input type="hidden" id="isEvaluateDetail_${(teacherStat.id)!}" value="${isEvaluateDetail?if_exists?string("1", "0")}"/>
    [/@]
[/@]
    [@htm.actionForm name="actionForm" action="questionnaireStatTeacher.action" entity="teacherStat"]
        <input type="hidden" name="teacherStatId" value=""/>
    [/@]
    <script>
        function evaluatePersonInfo(teacherStatId) {
            form.target="_blank";
            form.action = "questionnaireStatTeacher.action?method=evaluatePersonInfo";
            form["teacherStatId"].value = teacherStatId;
            form.submit();
        }
        
        function info() {
            
            form["teacherStat.id"].value = getIds();
            form.target="_blank";
            form.action = "questionnaireStatTeacher.action?method=info";
            form.submit();
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