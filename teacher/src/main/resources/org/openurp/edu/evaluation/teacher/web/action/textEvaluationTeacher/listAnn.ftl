[#ftl]
[@b.head/]
<style>
    #infoDiv {
        position: absolute;
        width: 70%;
        top:70%;
        left: 50%;
        border:#006CB2 solid 1px;background-color: #FFFFFF;
        z-index: 20;
        margin-top:-100px;
        margin-left:-35%;
        text-align:center;font-size:13px;
    }
    #msg{
        word-wrap:break-word;
        word-break:break-all;
        width: 100%;
        height:70px;
        padding:0px;
        border:1px solid gray;
        font-size:12px;
    }
</style>
[@b.toolbar ]
   bar.addBack();
[/@]
[@b.form name="annTeacherSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=teacherRemessages var="teacherRemessages" sortable="true"]
        [@b.gridbar title="教师公告列表"]
        [/@]
        [@b.row]
            [@b.col property="id" title="公告内容"]
                [#if !(teacherRemessages.remessage)?exists || (teacherRemessages.remessage?length<21)]
                <a href="#" onClick="showMessage('${(teacherRemessages.remessage)?default('')}')" title="${(teacherRemessages.remessage)?default('')}">
                    ${(teacherRemessages.remessage)!}
                </a>
                [#else]
                <a href="#" onClick="showMessage('${(teacherRemessages.remessage)?default('')}')" title="${(teacherRemessages.remessage)?default('')}">
                    ${(teacherRemessages.remessage)?default('')[0..20]}...
                </a>
                [/#if]
            [/@]
            [@b.col property="createdAt" title="发布时间" width="20%"]
                ${((teacherRemessages.createdAt)?string("yyyy-MM-dd HH:mm"))!}
            [/@]
        [/@]
    [/@]
[/@]
<div id="infoDiv" style="display:none;">
    [@b.form name="annTeacherInfoForm" action="!save" theme="list"]
         [@b.textarea label="详细内容" check="maxLength(200)" id="msg" name="msg" readOnly="readOnly" style="width:85%"/]
         [@b.formfoot]
             <input type="button" value="关闭" onClick="hiddenInfo();" class="buttonStyle" id="closeBack" />
        [/@]
    [/@]
</div>
<script type="text/javaScript">
    function showMessage(message){
        hiddenInfo();
        var infoDiv = $("#infoDiv");
        $("#msg").val(message);
        infoDiv.show();
    }
    function hiddenInfo(){
        $("#infoDiv").hide();
    }
</script>
[@b.foot/]