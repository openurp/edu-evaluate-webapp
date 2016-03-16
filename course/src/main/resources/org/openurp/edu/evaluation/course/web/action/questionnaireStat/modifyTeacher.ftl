[#ftl]
[@b.head/]
[@b.toolbar title="更改教师"]
    bar.addBack();
[/@]
[@b.form name="modifyTeacherForm" action="!saveTeacher" title="更改教师" theme="list"]
<input type="hidden" name="questionnaireStat.id" value="${(questionnaireStat.id)!}">
    <li>
        <label class="title">现有教师:</label>${(questionnaireStat.staff.person.name.formatedName)!}
    </li>
    [@b.field label="修改教师" required="true"]
        [@b.textfield title="正确工号" theme="html" id="teacherCode" value="" name="teacherCode" comment="<input type='button' value='查询' onClick='searchTeacher()'/>输入教师工号查询"/]
        [@b.textfield title="正确工号" theme="html" id="teacherId" required="true" name="teacher.id" value="" style="display:none;"/]
    [/@]
    [@b.field label="教师信息"]
    <table class="infoTable" id="studentInfoTable" style="width:40%;">
        <tbody style="line-height:23px;text-align:center;">
            <tr>
                <td class="title" width="15%" style="text-align:center;">教师姓名</td>
                <td id="teacherIdTD"></td>
            </tr>
            <tr>
                <td class="title" width="15%" style="text-align:center;">教师院系</td>
                <td id="teacherDepartTD"></td>
            </tr>
        </tbody>
    </table>
    [/@]
    [@b.formfoot]
        [@b.submit value="action.submit" onsubmit="doPost()"/]
        <input type="reset" name="teacherReset" value="${b.text("action.reset")}" />
    [/@]
[/@]
<script type="text/javaScript">
    var modifyTeacherForm = document.modifyTeacherForm;

    function searchTeacher(){
        var response = $.post("searchTeacher",{teacherCode:$("#teacherCode").val()},function(){
            if(response.readyState == 4 && response.status == 200){
                var teacherId = response.responseText;
                if (teacherId == ""){
                    $("#teacherId").val("");
                    $("#teacherId").parent().find(".error").remove();
                    $("#teacherId").parent().append($("<label class='error' for='stdId'>查无此人!</label>"));
                    $("#teacherIdTD").html("");
                    $("#teacherDepartTD").html("");
                } else {
                    var info = teacherId.split("_")
                    $("#teacherId").parent().find(".error").remove();
                    $("#teacherId").val(info[0]);
                    $("#teacherIdTD").html(info[1]);
                    $("#teacherDepartTD").html(info[2]);
                }
            }
        },"text");
    }
    function doPost(){
        bg.form.setSearchParams(form,modifyTeacherForm);
    }
</script>
[@b.foot/]