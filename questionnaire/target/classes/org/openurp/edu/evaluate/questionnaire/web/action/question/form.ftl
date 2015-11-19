[#ftl]
[@b.head/]
[@b.toolbar title="添加/修改问题信息"]
    bar.addBack();
[/@]

<style type="text/css">
    form.listform label.title { 
        clear: left; display: block; float: left; margin: 0 0 8px; 
        padding-right: 5px; text-align: right; width: 120px; font-weight: bold;
    }
</style>
<script>
    function checkNum() {
        var reg = new RegExp("^\\d+(\\.\\d+)?$")
        return reg.test(document.questionForm["question.score"].value);
    }
</script>
    [@b.form name="questionForm" title="问题信息管理" action="!save" theme="list" ]
        [#if questionTypes?size == 0]
            <h2>没有可以使用的问题类别，请先前往问题类别菜单进行维护</h2>
        [#else]
           [@b.select label="${b.text('field.questionType.choose')}" required="true" name="question.type.id" value=(question.type.id)?if_exists items=questionTypes?if_exists/]
           [@b.textfield label="${b.text('field.question.mark')}" required="true" check="assert(checkNum(), '问题分值必须为非负浮点数');" name="question.score" value="${(question.score)?default('')}" maxlength="4" /]
           [@b.textfield label="问题优先级" required="true" check="match('integer', '问题优先级必须为非负整数')" name="question.priority" value="${question.priority?default('1')}" comment="<font color='red'>越高显示越靠前</font>" maxlength="5" /]
           [@b.radios label="是否可用" required="true" name="question.state" value="${(question.state?string('1','0'))?default('1')}"/]
           [@b.radios label="是否附加题" required="true" name="question.addition" value="${(question.addition?string('1','0'))?default('1')}"/]
           [@b.select label="创建部门" required="true" name="question.depart.id" value=(question.depart.id)! empty="..." items=departmentList?if_exists style="width:205px" /]
           [@b.select label="选项组" required="true" name="question.optionGroup.id" value=(question.optionGroup.id)! empty="..." items=optionGroups?if_exists style="width:205px" /]
           [@b.datepicker label="生效日期" required="true" name="question.beginOn" id="_beginOn" format="yyyy-MM-dd HH:mm:ss" maxDate="#F{$dp.$D(\\'_endOn\\')}" value=(question.beginOn?string("yyyy-MM-dd HH:mm:ss"))! maxlength="10" style="width:200px"/]
        [@b.datepicker label="失效日期" name="question.endOn" id="_endOn" format="yyyy-MM-dd HH:mm:ss" minDate="#F{$dp.$D(\\'_beginOn\\')}" value=(question.endOn?string("yyyy-MM-dd HH:mm:ss"))! maxlength="10" style="width:200px"/]
        
        [@b.textarea label="${b.text('field.question.questionContext')}(勿包含回车等符号)" required="true" check="maxLength(200)" name="question.content" value="${(question.content?html)!}" style="width:500px" maxlength="200"/]
           [@b.textarea label="${b.text('field.evaluate.remark')}"  check="maxLength(200)" name="question.remark" value="${(question.remark?html)!}" style="width:500px" maxlength="200" /]
           [@b.formfoot]
              <input type="hidden" name="question.id" value="${question.id?default('')}"/>
               
            [@b.submit value="action.submit"/]&nbsp;
            <input type="reset"  name="reset1" value="${b.text("action.reset")}" class="buttonStyle" />
        [/@]
        [/#if]
   [/@]
[@b.foot/]
