[#ftl]
[@b.head/]
<style>
    #reMessageDiv {
        position: absolute;
        width: 70%;
        top:40%;
        left: 50%;
        border:#006CB2 solid 1px;
        background-color: #FFFFFF;
        z-index: 20;
        margin-top:-100px;
        margin-left: -35%;
        font-size:13px;
    }
    #infoDiv {
        position: absolute;
        width: 70%;
        top:40%;
        left: 50%;
        border:#006CB2 solid 1px;
        background-color: #FFFFFF;
        z-index: 20;
        margin-top:-100px;
        margin-left:-35%;
        font-size:13px;
    }
    #msg{
        word-wrap:break-word;word-break:break-all;
        font-size: 12px;
        overflow:auto;
        background-color: #fff;
        color: #000;
        padding-right:5px;
        padding-left:5px;
        font-family: courier;
        letter-spacing:0;
        line-height:12px;
        border-style:1px gray solid;
        width:100%;
        height:100px;
        padding:0px;
    }
    .t_area{
        width:100%;
        height:70px;
        padding:0px;
        border:1px solid gray;
        font-size:12px;
    }
</style>
[@b.toolbar title='教师文字评教列表' id='textEvaluationListBar']
    [#if isCurrent && textEvaluations?size > 0]
    bar.addItem("发布公告","teacherAnnounce()");
    [/#if]
    bar.addItem('已发布公告',"showAnn()");
    bar.addBack();
[/@]
[@b.messages slash="4"/]
<table class="infoTable" align="center">
    <tr>
        <td class="title" style="text-align:center;">学年学期</td>
        <td style="padding-left:10px;">${(lesson.semester.schoolYear)!}学年${(lesson.semester.name)!}学期</td>
        <td class="title" style="text-align:center;">课程序号</td>
        <td style="padding-left:10px;">${(lesson.no)!}</td>
        <td class="title" style="text-align:center;">课程名称</td>
        <td style="padding-left:10px;">${(lesson.course.name)!}</td>
        <td class="title" style="text-align:center;">学生文字评教数</td>
        <td style="padding-left:10px;">${(textEvaluations?size)!"0"}</td>
    </tr>
</table>

<div class="grid" style="margin-top:3px;">
    [@b.messages slash="4"/]
    [@b.form name="evaluateForm"  target="contentDiv"]
    <div style="border:1px #006CB2 solid;background:#C7DBFF;text-align:center;height:25px;line-height:25px;font-weight:bold;border-bottom:none;">
        评估意见和建议
    </div>
    <table class="gridtable">
        [#assign i = 0/]
        [#assign styleFlag = true/]
        [#assign remsgFlag = true/]
        [#if textEvaluations??]
        <tbody>
        [#list textEvaluations as textEvaluation]
            [#if i==0 && textEvaluation?exists]
                  [#assign textEvaluationId = textEvaluation.id/]
              [/#if]
              [#if (textEvaluation.teacherRemessages?size == 0)]
                  [#assign remsgFlag = true/]
              [#elseif (textEvaluation.teacherRemessages?size > 0) && i == 0]
                  [#list textEvaluation.teacherRemessages as teacherRemsg]
                      [#if teacherRemsg.visible]
                          [#assign remsgFlag = false/]
                      [/#if]
                  [/#list]
              [#else]
                  [#assign remsgFlag = false/]
              [/#if]
            <tr class='${(i % 2 == 0)?string("griddata-odd","griddata-even")}'>
                <td style="width:90%;">
                    <div [#if remsgFlag]style="display:none"[/#if] id="showRemessageDivTag${i}">
                        <a onClick="showRemsg('${i}');" id="showRemessage${i}" style="float:left;cursor:pointer;">+</a>
                    </div>
                    <div style="text-align:left;margin-left:2%">${(textEvaluation.context)!}</div>
                </td>
                [#if isCurrent]
                <td style="width:10%;">
                    <input type="button" class="buttonStyle" value="回复" onClick="teacherRemessage('${i}','${textEvaluation.id}','${(textEvaluation.context)?default('')?replace('(\r\n|\n)','','r')}','${(textEvaluation.std.id)!}')">
                    <input type="hidden" value="${textEvaluation.id}" id="annTextEvaluationId${i}">
                </td>
                [/#if]
            </tr>
            <tr class='${(i % 2 == 0)?string("griddata-odd","griddata-even")}'>
                  <td id="resultMsgTd${i}" ${isCurrent?string("colspan='2'","")} style="padding-left:5%;display:none;">
                  [#list textEvaluation.teacherRemessages as remsg]
                      [#if remsg.visible]
                      <div style="line-height:17px;text-align:left;">
                          ${remsg.createdAt?string("yyyy-MM-dd HH:mm")} 回复：
                          <font color="blue">
                          [#if !(remsg.remessage)?? || (remsg.remessage?length < 21)]
                              <a href="#" onClick="showMessage('${(remsg.remessage)?default('')}')" title="${(remsg.remessage)?default('')}">
                                  ${(remsg.remessage)?default('')}
                              </a>
                          [#else]
                              <a href="#" onClick="showMessage('${(remsg.remessage)?default('')}')" title="${(remsg.remessage)?default('')}">
                              ${(remsg.remessage)?default('')[0..20]}...
                              </a>
                          [/#if]
                          </font>
                      </div>
                      [/#if]
                  [/#list]
                  </td>
              </tr>
              [#assign i = i+1/]    
        [/#list]
        </tbody>
        [/#if]
    </table>
    [#if !textEvaluations?? || textEvaluations?size <= 0]
    <div class="gridempty" style="height: 112px;">
        <div style="padding-top: 40px;">没有查询结果</div>
    </div>
    [/#if]
    [/@]
</div>

<div id="reMessageDiv" style="display:none;">
    [@b.form name="textEvaluationEditForm" theme="list"]
        <div id="questionDiv" style="display:none;">
        [@b.textarea label="意见和建议" class="t_area" check="maxLength(200)" id="question" name="question" readOnly="readOnly" style="width:85%"/]
        </div>
        [@b.select label="发送对象" onChange="getValue(this.value);" name="sendObj" id="sendObj" items={'':'学生','adminClass':'班级'}/]
        [@b.textarea label="回复内容" class="t_area" check="maxLength(200)" id="reMessage" name="reMessage" value="" style="width:85%"/]
        [@b.field label="注意"]
            <font color='red'>若"发布公告"或"发送对象"为"班级",请使用鼠标点击选择下面的班级列表，默认为全选。</font>
            <table width="80%" id="teachclassList" style="display:none;">
                [#list teachclasses as teachclass]
                <tr style="background-color:yellow;cursor:pointer;" onClick="getAdminClasses(this)">
                    <td>${(teachclass.name)!}</td>
                </tr>
                [/#list]
            </table>
        [/@]
        [@b.formfoot]
            <input type="hidden" id="semesterId" name="semesterId" value=""/>
            <input type="hidden" id="lessonId" name="lessonId" value="${(lesson.id)!}"/>
            <input type="hidden" id="textEvaluationId" name="textEvaluationId" value="${textEvaluationId!}"/>
            <input type="button" value="提交" onClick="saveInfo();" class="buttonStyle"  id="submitToAction"/>
            <input type="button" value="关闭" onClick="hiddenInfo();" class="buttonStyle" id="closeBack" />
        [/@]
    [/@]
</div>
<div id="infoDiv" style="display:none;">
    [@b.form name="textEvaluationInfoForm" action="!save" theme="list"]
         [@b.textarea label="详细内容" check="maxLength(200)" id="msg" name="msg" readOnly="readOnly" style="width:85%"/]
         [@b.formfoot]
             <input type="button" value="关闭" onClick="hiddenInfo();" class="buttonStyle" id="closeBack" />
        [/@]
    [/@]
</div>
[@b.form name="actionForm"]
    <input type="hidden" id="lessonId" name="lessonId" value="${(lesson.id)!}"/>
[/@]
<script type="text/javaScript">
    $("#semesterId").val($("input[name='semester.id']").val());

    var nowLine;
    var isAnn = false;
    var resultMsgTd;
    
    function showRemsg(line){
        var remsgTd = $("#resultMsgTd" + line);
        var showRemessageDiv = $("#showRemessage" + line);
        if (remsgTd.css("display") == "none"){
            remsgTd.show("fast");
            showRemessageDiv.html("-");
        } else {
            remsgTd.hide("fast");
            showRemessageDiv.html("+");
        }
    }
    function showMessage(message){
        hiddenInfo();
        var infoDiv = $("#infoDiv");
        $("#msg").val(message);
        infoDiv.show();
    }
    function teacherRemessage(line, id, message, std){
        hiddenInfo();
        nowLine = line;
        var reMessageDiv = $("#reMessageDiv");
        $("#reMessage").parent().find("label").html("回复内容<br/>(200字)");
        $("#question").val(message);
        $("#sendObj>option:eq(0)").val(std);
        $("#sendObj>option:eq(0)").prop("selected",true);
        $("#sendObj").prop("disabled",false);
         $("#textEvaluationId").val(id);
        reMessageDiv.show();
        $("#questionDiv").show();
        isAnn = false;
    }
    function hiddenInfo(){
        $("#questionDiv").hide();
        $("#reMessageDiv").hide();
        $("#infoDiv").hide();
        $("#teachclassList").hide();
        $("#reMessage").val("");
    }
    function teacherAnnounce(){
        hiddenInfo();
        var reMessageDiv = $("#reMessageDiv");
        $("#reMessage").parent().find("label").html("公告内容<br/>(200字)");
        reMessageDiv.show();
        $("#sendObj>option:eq(1)").prop("selected",true);
        $("#sendObj").prop("disabled",true);
        $("#teachclassList").show();
        isAnn = true;
    }
    function getAdminClasses(tr) {
        if(tr.style.backgroundColor == "white") {
            tr.style.backgroundColor = "yellow";
        } else {
            tr.style.backgroundColor = "white";
        }
    }
    function getValue(num) {
        if(num == "adminClass") {
            $("#teachclassList").show();
        } else {
            $("#teachclassList").hide();
        }
    }
    function saveInfo() {
    jQuery('#submitToAction').prop('disabled',true).val('提交...');
        var textEvaluationEditForm = document.textEvaluationEditForm;
        var classNames = "";
        var reMessage = $("#reMessage").val();
        if($.trim(reMessage) == "") {
            alert("请填写回复或公告内容!");
            return false;
        }
        if(reMessage.length > 200){
            alert("你输入的内容过多!");
        }
        bg.form.addInput(textEvaluationEditForm, "isAnn", isAnn);
        if(!isAnn && $("#sendObj").val() != "adminClass") {
            bg.form.submit(textEvaluationEditForm, "textEvaluationTeacher!saveEvaluateRemessageToStd.action");
        } else {
            $("#teachclassList>tbody>tr").each(function(){
                if (this.style.backgroundColor == "yellow"){
                    classNames = classNames + $(this).find("td:eq(0)").html() + ",";
                }
            });
            if(classNames == "") {
                alert("请选择您要发送的班级!");
                return false;
            }
            if (!isAnn){
                bg.form.addInput(textEvaluationEditForm, "stdId", $("#sendObj>option:eq(0)").val());
            } else {
                bg.form.addInput(textEvaluationEditForm, "stdId", "");
            }
            bg.form.addInput(textEvaluationEditForm, "classNames", classNames);
            bg.form.submit(textEvaluationEditForm, "textEvaluationTeacher!saveEvaluateRemessageToClass.action");
        }
    }
    function showAnn(){
        var actionForm = document.actionForm;
        bg.form.addInput(actionForm, "semesterId", $("input[name='semester.id']").val());
        bg.form.submit(actionForm, "textEvaluationTeacher!listAnn.action");
    }
</script>
[@b.foot/]