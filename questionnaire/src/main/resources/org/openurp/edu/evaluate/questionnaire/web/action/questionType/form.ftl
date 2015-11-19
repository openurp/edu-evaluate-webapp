[#ftl]
[@b.head/]
[@b.toolbar title="添加/修改列表"]
    bar.addBack();
[/@]

[@b.form name="questionTypeForm" title="制定问题类别详细信息" action="!save" theme="list" ]
    [@b.textfield id="questionTypeName" label="${b.text('attr.name')}" required="true" check="maxLength(50)" name="questionType.name" value="${(questionType.name)?default('')}" maxlength="50"/]
    [@b.textfield label="${b.text('attr.enName')}" check="maxLength(40)"name="questionType.enName" value="${(questionType.enName)?default('')}" maxlength="40"/]
    [@b.textfield label="问题优先级" required="true" check="match('integer', '问题优先级为非负整数')" name="questionType.priority" value="${(questionType.priority)?default(1)}" comment="<font color='red'>越高显示越靠前</font>" maxlength="5" /]
    [@b.radios label="是否可用" required="true" name="questionType.state" value="${(questionType.state?string('1','0'))?default('1')}"/]
    [@b.datepicker label="生效日期" required="true" name="questionType.beginOn" id="_beginOn" format="yyyy-MM-dd HH:mm:ss" maxDate="#F{$dp.$D(\\'_endOn\\')}" value=(questionType.beginOn?string("yyyy-MM-dd HH:mm:ss"))! maxlength="10" style="width:200px"/]
    [@b.datepicker label="失效日期" name="questionType.endOn"  id="_endOn" format="yyyy-MM-dd HH:mm:ss" minDate="#F{$dp.$D(\\'_beginOn\\')}" value=(questionType.endOn?string("yyyy-MM-dd HH:mm:ss"))! maxlength="10" style="width:200px"/]

    [@b.textarea label="${b.text('field.evaluate.remark')}"  check="maxLength(200)" name="questionType.remark" value="${(questionType.remark)?default('')}" style="width:500px"maxlength="200"/]
    [@b.formfoot]
        [#if questionType.persisted]
        <input name="questionType.id"  value="${questionType.id}" type="hidden"/>
        [/#if]
           
        [@b.submit value="action.submit"/]&nbsp;
        <input type="reset"  name="reset1" value="${b.text("action.reset")}" class="buttonStyle" />
    [/@]
[/@]
  
[@b.foot/]