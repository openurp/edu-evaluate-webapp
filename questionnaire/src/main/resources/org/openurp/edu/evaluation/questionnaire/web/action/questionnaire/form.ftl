[#ftl]
[@b.head/]
[@b.toolbar title="添加/修改评教问卷"]
    bar.addItem('添加问题','addAllquestions()');
    bar.addBack();
[/@]
[#assign sa][#if questionnaire.persisted]!update?id=${questionnaire.id}[#else]!save[/#if][/#assign]
[@b.form name="questionnaireForm" title="" action=sa theme="list" ]
    [@b.textfield label="问卷描述" required="true" check="maxLength(80)" name="questionnaire.description" value="${(questionnaire.description?html)?default('')}" /]
    [@b.select label="问卷部门" required="true" name="questionnaire.depart.id" value=(questionnaire.depart.id)! empty="..." items=departments?if_exists/]
    [@b.radios label="问卷状态" required="true" name="questionnaire.state" titles="1:有效,0:无效" value="${(questionnaire.state?string('1','0'))?default('1')}"/]
    [@b.textarea label="问卷表头" check="maxLength(300)" name="questionnaire.title" value="${(questionnaire.title?html)?default('')}" style="width:500px"/]
    [@b.textarea label="备注" check="maxLength(300)" name="questionnaire.remark" value="${(questionnaire.remark?html)?default('')}" style="width:500px" /]
     [@b.datepicker label="生效日期" required="true" name="questionnaire.beginOn" id="_beginOn" format="yyyy-MM-dd" maxDate="#F{$dp.$D(\\'_endOn\\')}" value=(questionnaire.beginOn?string("yyyy-MM-dd"))! maxlength="10" style="width:200px"/]
    [@b.datepicker label="失效日期" name="questionnaire.endOn" id="_endOn" format="yyyy-MM-dd" minDate="#F{$dp.$D(\\'_beginOn\\')}" value=(questionnaire.endOn?string("yyyy-MM-dd"))! maxlength="10" style="width:200px"/]
    
    [@b.field label="问题列表"]
        <table class="gridtable" id="questionnaireTable">
            <thead class="gridhead">
                <tr>
                    <th width="10%">问题类型</th>
                    <th>问题内容</th>
                    <th width="15%" colspan="2">操作</th>
                </tr>
            </thead>
            <tbody id="tbodyId">
                [#assign keyIndex=1]
                [#list questionTree?keys?sort_by("priority")?reverse as key]
                [#assign value=questionTree.get(key)/]
                    <tr class="griddata-${(key_index%2==0)?string("even","odd")}" id="question${keyIndex}" name="${value[0].questionType.name}">
                        <td rowSpan="${questionTree.get(key)?size}" align="center">
                            ${key.name}
                        </td>
                        <td align="left">
                            <span>${keyIndex}:</span>${value[0].content}
                        </td>
                        <td align="center">
                            <a href="#" onclick="removeTr('question${keyIndex}','${value[0].id}','${key.name}')">删除</a>
                        </td>
                        <td rowSpan="${questionTree.get(key)?size}" align="center">
                            <a href="#" onclick="addTr('${(value[0].questionType.id)?if_exists}')">添加</a>
                        </td>
                    </tr>
                    [#if questionTree.get(key)?size>1]
                        [#list 1..questionTree.get(key)?size-1 as i]
                            [#assign keyIndex= keyIndex+1]
                            <tr class="griddata-${(key_index%2==0)?string("even","odd")}" id="question${keyIndex}"  name="${value[0].questionType.name}"> 
                                <td align="left">
                                    <span>${keyIndex}:</span>${value[i].content}
                                </td>
                                <td align="center">
                                    <a href="#" onclick="removeTr('question${keyIndex}','${value[i].id}','${value[i].questionType.name}')">删除</a>
                                </td>
                            </tr>
                        [/#list]
                    [/#if]
                    [#assign keyIndex= keyIndex+1]
                [/#list]
            </tbody>
        </table>
    
    [/@]
    [@b.formfoot]
         [#if questionnaire.persisted]
		  <input type="hidden"  name="questionnaire.id"  value="${questionnaire.id?default('')}" />
         [/#if]
        [@b.submit value=(questionnaire.id)?exists?string("保存","修改") onsubmit="doValidate"/]&nbsp;
        <input type="button" name="btnReset" value="${b.text("action.reset")}" class="buttonStyle" onClick="doReset()" />
    [/@]
    [@b.field label="注意"]
        <font color="red">
         1.操作完毕，请注意保存<br>
         2.<b>可以不填写问卷表头</b>,问卷表头主要是在学生评教的时候显示在问卷上面给学生看,填写表头信息时请注意用enter键换行
         </font>
    [/@]
[/@]
<table class="gridtable" id="questionnaireTableReset" style="display:none;">
    <thead class="gridhead">
        <tr>
            <th>问题类型</th>
            <th>问题内容</th>
            <th colspan="2">操作</th>
        </tr>
    </thead>
    <tbody id="tbodyIdReset">
        [#assign keyIndex=1]
        [#list questionTree?keys?sort_by("priority")?reverse as key]
         [#assign value=questionTree.get(key)/]
            <tr class="griddata-${(key_index%2==0)?string("even","odd")}" id="question${keyIndex}" name="${value[0].questionType.name}">
                <td rowSpan="${value?size}" align="center">
                    ${value[0].questionType.name}
                </td>
                <td align="left">
                    <span>${keyIndex}:</span>${value[0].content}
                </td>
                <td align="center">
                    <a href="#" onclick="removeTr('question${keyIndex}','${value[0].id}','${value[0].questionType.name}')">${b.text('action.delete')}</a>
                </td>
                <td rowSpan="${value?size}" align="center">
                    <a href="#" onclick="addTr('${(value[0].questionType.id)?if_exists}')">添加</a>
                </td>
            </tr>
            [#if questionTree.get(key)?size>1]
                [#list 1..questionTree.get(key)?size-1 as i]
                    [#assign keyIndex= keyIndex+1]
                    <tr class="griddata-${(key_index%2==0)?string("even","odd")}" id="question${keyIndex}"  name="${value[0].questionType.name}"> 
                        <td align="left">
                            <span>${keyIndex}:</span>${value[i].content}
                        </td>
                        <td align="center">
                            <a href="#" onclick="removeTr('question${keyIndex}','${value[i].id}','${value[i].questionType.name}')">${b.text('action.delete')}</a>
                        </td>
                    </tr>
                [/#list]
            [/#if]
            [#assign keyIndex= keyIndex+1]
        [/#list]
    </tbody>
</table>
<script language="JavaScript">
    var questionIds = ",";
    var questionIdsReset = ",";
    [#list questions?if_exists?reverse as question]
        questionIds += "${question.id?if_exists},";
        questionIdsReset += "${question.id?if_exists},";
    [/#list]
    var tempClassName="";
    $("#tbodyId").data("index",${questionTree?keys?size});
    function addContext(values){
        var index = $("#tbodyId").data("index");
        tempClassName="griddata-"+(index%2==0?"even":"odd");
        var html="";
        var flag=true;
        var targetTr;
        jQuery("#tbodyId tr").each(function(i){
            if(jQuery(this).find("td").length==4 && jQuery(this).find("td:first").text().indexOf(values[1])!=-1){
                flag=false;
                targetTr=jQuery(this);
                return false;
            }
        });
        if(flag){
            html = "<tr class='"+tempClassName+"' id='"+new String(values[0])+"' name='"+values[1]+"'><td>"+values[1]+"</td><td align='left'><span></span>"+values[2]+"</td><td><a href='#' onclick='removeTr(\""+values[0]+"\",\""+values[0]+"\",\""+values[1]+"\")'>删除</a></td><td><a href='#' onclick='addTr(\""+values[3]+"\")'>添加</a></td></tr>";
            var tbody = jQuery("#tbodyId");
            tbody.append(html);
            index++;
        }else{
            html = "<tr class='"+targetTr.prop("className")+"' id='"+new String(values[0])+"' name='"+values[1]+"'><td align='left'><span></span>"+values[2]+"</td><td><a href='#' onclick='removeTr(\""+values[0]+"\",\""+values[0]+"\",\""+values[1]+"\")'>删除</a></td></tr>";
            var rowSpan = targetTr.find("td:first").prop("rowSpan");
            rowSpan++;
            targetTr.find("td:first").prop("rowSpan",rowSpan);
            targetTr.find("td:last").prop("rowSpan",rowSpan);
            targetTr.after(html);
        }
        $("#tbodyId").data("index",index);
           questionIds += values[0]+',';
           $("#tbodyId tr td span").each(function(i,obj){
               $(obj).html((i+1)+":");
           });
    }
    
    //删除一行
    function removeTr(trId,questionId,typeName){
        var tr = jQuery("#"+trId);
        var index = $("#tbodyId").data("index");
        if(tr.find("td").length==4){
            var rowSpan = tr.find("td:first").prop("rowSpan");
            if(rowSpan != null && rowSpan>1){
                rowSpan--;
                var tdFirst = tr.find("td:first").prop("rowSpan",rowSpan);
                var tdLast = tr.find("td:last").prop("rowSpan",rowSpan);
                tr.next().prepend(tdFirst);
                tr.next().append(tdLast);
                tr.remove();
            }else{
                  tr.remove();
                  index--;
                  var j=0;
                  var tempClass;
                jQuery("#tbodyId tr").each(function(i){
                    if(jQuery(this).find("td").length==4){
                        tempClass="griddata-"+((j%2==0)?"even":"odd");
                        jQuery(this).removeClass().addClass(tempClass);
                        j++;
                    }else{
                        jQuery(this).removeClass().addClass(tempClass);
                    }
                  });
                  $("#tbodyId").data("index",index);
            }
        }else{
            var typeName = tr.prop("name");
            var fisrtTypeTr = tr.parent().find("tr[name='" + typeName + "']:eq(0)");
            var rowSpan = fisrtTypeTr.find("td:first").prop("rowSpan");
            rowSpan--;
            fisrtTypeTr.find("td:first").prop("rowSpan",rowSpan);
            fisrtTypeTr.find("td:last").prop("rowSpan",rowSpan);
            tr.remove();
        }
        questionIds=removeId(questionIds,questionId);
           $("#tbodyId tr td span").each(function(i,obj){
               $(obj).html((i+1)+":");
           });
    }
    //添加一行
    function addTr(questionTypeId){
         var url="${b.url('!searchQuestion')}?questionTypeId="+questionTypeId+"&questionSeq="+questionIds.substring(1,questionIds.length);
         window.open(url,'','scrollbars=auto,width=720,height=480,left=200,top=200,status=no');
    }
    //ids是串 id是单个id
    function removeId(ids,id){
        var index = ids.indexOf(","+id+",");
         if(index!=-1){
             ids=ids.replace(","+id+",",",");
         }
        return ids;
    }
    
    function addAllquestions(){
        var url="${b.url('!searchQuestion')}?questionSeq="+questionIds.substring(1,questionIds.length);
        window.open(url,'','scrollbars=yes,width=720,height=480,left=200,top=200,status=yes');
    }

    function doValidate(){
        var errors ="";
        var flag= true;
        if(jQuery("#tbodyId").find("tr").length==0){
            errors+="请选择一些问题\n";
        }
        if(errors!=""){
            alert(errors);
            flag=false;
        }
        bg.form.addInput(document.questionnaireForm,"questionnaire.questionIds",questionIds.substring(1,questionIds.length-1));
        return flag;
    }
    
    function doReset(){
        var questionnaireForm = document.questionnaireForm;
        questionnaireForm.reset();
        $("#tbodyId").html($("#tbodyIdReset").html());
        questionIds = questionIdsReset;
    }
    
</script>
[@b.foot/]