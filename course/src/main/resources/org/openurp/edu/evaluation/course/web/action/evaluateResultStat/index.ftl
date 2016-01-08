[#ftl]
[@b.head/]
[@b.toolbar title='评教统计结果' id='evaluateResultStatBar' /]

<table class="indexpanel">
    <tr>
        <td style="width:200px"  class="index_view">
        [@b.form action="!search" name="evaluateResultStatIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            <input type="hidden" name="semesterIds" value=""/>
            [@b.textfields style="width:130px" names="questionnaireStat.lesson.no;选课号,questionnaireStat.lesson.course.name;课程名称,questionnaireStat.teacher.code;教师工号,questionnaireStat.teacher.name;教师名称"/]
            [@b.textfield style="width:80px" name="regainLower" label="回收率" onBlur="clearNoNum(this)"/]
            [@b.select style="width:134px" name="questionnaireStat.depart.id" label="开课院系" items=departments empty="${b.text('filed.choose')}..."/]
            [@b.select style="width:134px" name="questionnaireStat.teacher.staff.department.id" label="教师院系" items=departments empty="${b.text('filed.choose')}..."/]
            [@b.select style="width:134px" name="questionnaireStat.education.id" label="学历层次" items=educations empty="${b.text('filed.choose')}..."/]
            [@b.select style="width:134px" name="tutorDepartmentStat.questionnaire.id" label="问卷类型" items=questionnaires option="id,description" empty="${b.text('filed.choose')}..."/]
            [@eams.semesterCalendar style="width:130px" label="学年学期" id="semesterId" name="semester.id" empty="false" value=semester /]
            <tr>
                <td align="center">
                    <input type="button" value="添加学期" onClick="moveSemester()">
                    <input type="button" value="清空学期" onClick="clearSemester()">
                </td>
            </tr>
            <tr>
                <td align="center">
                    <select name="semesterSelected" id="semesterSelected" style="width:160px;height:150px;" multiple size="10" style="width:130px;" onDblClick="deleteSemester()">
                    </select>
                </td>
            </tr>
        [/@]
        </td>
        <td class="index_content">
            [@b.div id="contentDiv" href="!search"/]
        </td>
    </tr>
</table>
<script type="text/javaScript">
    $(function(){
        $("input[name='regainLower']").parent().append("<label style='font-size:11px'>(0~1之间)</label>");
    });

    function moveSemester(){
        var semesterName = $("#semesterId").val();
        var semesterId = $("input[name='semester.id']").val();
        var bl = false;
        $("#semesterSelected > option").each(function(i){
            if ($(this).val() == semesterId){
                bl = true;
                return;
            }
        });
        if (bl){
            return false;
        }
        $("<option value='" + semesterId + "'>" + semesterName + "</option>").appendTo($("#semesterSelected"));;
        setSemesterIds();
    }
    function deleteSemester(){
        $("#semesterSelected>option:selected").remove();
        setSemesterIds();
    }
    function clearSemester(){
        $("#semesterSelected").empty();
        $("input[name='semesterIds']").val("");
    }
    function setSemesterIds(){
        var semesterIds = "";
        $("#semesterSelected > option").each(function(i){
            semesterIds += $(this).val() + ",";
        });
        $("input[name='semesterIds']").val(semesterIds);
    }
    function clearNoNum(obj)
    {
        obj.value = obj.value.replace(/[^\d.]/g,"");
        obj.value = obj.value.replace(/^\./g,"");
        obj.value = obj.value.replace(/\.{2,}/g,".");
        obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
        if (obj.value > 1 || obj.value < 0){
            obj.value =　"";
        }
    }
</script>
[@b.foot/]

[#--
<#include "/template/head.ftl"/>
<BODY LEFTMARGIN="0" TOPMARGIN="0">

 <table id="bar" width="100%"></table>
 <table width="100%"  height="85%" class="frameTable">
    <tr>     
     <td style="width:20%" class="frameTable_view">
   <table width="100%" class="searchTable" id="departListTable">
   <form name="tutorForm" target="contentFrame" method="post" action=""  onkeypress="DWRUtil.onReturn(event, search)">
    <input type="hidden" name="semesterSeqIds" value=""/>
        <tr>
               <td class="title" id="f_educationType">培养类型:</td>
               <td width="75%"><select id="educationTypeOfCourse" name="questionnaireStat.task.educationType.id" style="width:100px"><option value="">...</option></select></td>
        </tr>
        <tr>
            <td id="f_department" class="title">院系:</td>
            <td><select id="department" name="questionnaireStat.depart.id" style="width:130px"><option value="">...</option></select></td>
         </tr>
        <tr style="display:none">       
            <td id="f_major" class="title">专业:</td>
            <td><select id="major" name="questionnaireStat.depart.majors.id" style="width:130px"><option value="">...</option></select></td>
        </tr>    
           <tr>
            <td>导师工号:</td><td><input type="text" name="questionnaireStat.teacher.code" style="width:100px;"></td>
        </tr>
        <tr>
            <td>导师姓名:</td><td><input type="text" name="questionnaireStat.teacher.name" style="width:100px;"></td>
        </tr>
        <tr>
            <td>选课号:</td><td><input type="text" name="questionnaireStat.task.teachclass.name" style="width:100px;"></td>
        </tr>
        <tr>
            <td>课程名称:</td><td><input type="text" name="questionnaireStat.task.course.name" style="width:100px;"></td>
        </tr>
        <tr>
            <td>回收下限率:</td><td><input type="text" name="regainLower" style="width:100px;">(0~1之间)</td>
        </tr>
        <tr>
            <td class="title" id="f_educationType">问卷类型:</td>
               <td>
                   <select id="questionnaireStat.questionnaire.id" name="questionnaireStat.questionnaire.id" style="width:130px;">
                    <option value="">....</option>
                       <#list questionnaireList?if_exists as questionnaire>
                       <option value="${questionnaire.id}">${questionnaire.description?if_exists}</option>
                    </#list>
                </select>
              </td>
        </tr>
        <tr>
            <td>学年学期:</td>
               <td>
             <select name="semesterList" id="f_semester" style="width:130px;" >
             <#list (semesters?sort_by("code")?reverse)?if_exists as semester>
             <option value="${semester.id}" title="${semester.schoolYear}&nbsp;${semester.name}">${semester.schoolYear}&nbsp;${semester.name}</option>
             </#list>
             <option value="">...</option>
             </select>
               </td> 
        </tr>
        <tr>
            <td><input type="button" value="添加" onclick="moveSelectedOptions(this.form['semesterList'], this.form['semesterSelected'])"></td>
            <td><input type="button" value="重置学期" onclick="removeSelectedOptions(this.form['semesterSelected'])"></td>
        </tr>
        <tr>
            <td colspan='2'>
                <select name="semesterSelected" MULTIPLE size="10" style="width:130px;" onDblClick="javaScript:removeSelectedOption(this.form['semesterSelected'])">
                </select>
            </td>
        </tr>
        <tr><td colspan="2" align="center"><button name="button1" onclick="search()" class="buttonStyle">查询</button>
       </form>
      </table>
     </td>
     <td valign="top" width="80%">
     <iframe  src="#"
     id="contentFrame" name="contentFrame" 
     marginwidth="0" marginheight="0" scrolling="no"
     frameborder="0"  height="100%" width="100%">
     </iframe>
     </td>
    </tr>
  <table>
  <#assign educationTypeList = educationTypes/>
  <#assign departmentList = departments/>
  <#include "/components/initStdAspectSelectData.ftl"/>
 <script>
    var bar=new ToolBar("bar","评估统计结果",null,true,true);
       bar.setMessage('<@getMessage/>');
     
     var dd = new EducationTypeDepart3Select("educationTypeOfCourse","department","major",null,true,true,true,true);
    dd.init(educationTypeArray,departArray);
    function moveSelectedOptions(srcSelect, destSelect){
        for (var i=0; i<srcSelect.length; i++){
            if (srcSelect.options[i].selected){ 
                var op = srcSelect.options[i];
                if (!hasOption(destSelect, op)){
                   destSelect.options[destSelect.length]= new Option(op.text, op.value);
                }
             }
         }      
    }
    function removeSelectedOptions(select){
        var options = select.options;
        for (var i=options.length-1; i>=0; i--){   
                options[i] = null;
        }
    }
    
    function search() {
            var form = document.tutorForm;
            form.semesterSeqIds.value=getAllOptionValue(form.semesterSelected);
            form.target = "contentFrame";
            form.action = "evaluateResultStat!search.action";
            form.submit();
    }
    search();
</script>
 </body>
<#include "/template/foot.ftl"/> 
--]