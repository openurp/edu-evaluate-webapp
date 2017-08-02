[#ftl]
[@b.head/]
[@b.toolbar title="添加/修改评价标准项"]
    bar.addItem("${b.text('action.add')}","addRow()");
    bar.addItem("${b.text('action.delete')}","deleteRow()");
    bar.addBack();
[/@]
  [#assign sa][#if evaluationCriteria.persisted]!update?id=${evaluationCriteria.id}[#else]!save[/#if][/#assign]
    [@b.form name="evaluationCriteriaForm" title=" " action=sa theme="list"]
           [@b.textfield id="name" label="名称" required="true"  name="evaluationCriteria.name" value="${(evaluationCriteria.name?html)!}" maxlength="30"/]
           [@b.select label="制作部门" value=(evaluationCriteria.depart.id)! empty="..." items=departmentList required="true" name="evaluationCriteria.depart.id"/]
           [@b.field label="选项"]
            <table class="formTable" width="60%"  id="evaluationCriteriaTable">
                <tr align="center"> 
                    <td class="gridselect"><input type="checkBox" id="criteriaItemBoxId" class="box" onClick="bg.input.toggleCheckBox(document.getElementsByName('criteriaItemId'),event);"></td>
                       <td align="center" style="background-color: #c7dbff;width:33%">评价名称</td>
                       <td align="center" style="background-color: #c7dbff;width:32%">最小分数(包含)</td>
                       <td align="center" style="background-color: #c7dbff;width:32%">最大分数(不包含)</td>
                </tr>
                [#list evaluationCriteria.criteriaItems?sort_by("min") as criteriaItem]
                <tr [#if (criteriaItem_index+1)%2==0]class="griddata-odd" [#else]class="griddata-even"[/#if]>         
                    <td class="gridselect">
                        <input class="box" name="criteriaItemId" value="${(criteriaItem.id)?if_exists}" type="checkbox">
                        <input class="criteriaId" type="hidden" name="criteriaItem${criteriaItem_index}.id" value="${(criteriaItem.id)?if_exists}"/>
                    </td>
                    <td><input class="criteriaName" name="criteriaItem${criteriaItem_index}.name" maxlength="20" value="${(criteriaItem.name?html)!}"/></td>
                    <td><input class="criteriaMin" name="criteriaItem${criteriaItem_index}.min" id="criteriaItem${criteriaItem_index}.min" maxlength="5" value="${(criteriaItem.min)?if_exists}"  [#if criteriaItem_index==0]readonly=true[/#if]/></td>
                    <td><input class="criteriaMax" name="criteriaItem${criteriaItem_index}.max" maxlength="5" value="${(criteriaItem.max)?if_exists}"/></td>
                </tr>
                [/#list]
            </table>
           [/@]
           [@b.formfoot]
               [#if evaluationCriteria.persisted]
               <input name="evaluationCriteria.id"  value="${evaluationCriteria.id}" type="hidden"/>
               [/#if]
              [#--] <input name="evaluationCriteria.id" value="${evaluationCriteria.id?default("")}" type="hidden"/>[--]
               
            [@b.submit value="action.submit" onsubmit="check"/]&nbsp;
            <input type="reset"  name="reset1" value="${b.text("action.reset")}" class="buttonStyle" />
        [/@]
   [/@]
 <script language="JavaScript">
       var form =document.evaluationCriteriaForm;
    var index=${evaluationCriteria.criteriaItems?size};

    var checkAll = document.getElementById("criteriaItemBoxId");
    if(checkAll.checked){
        checkAll.checked=false;
    }
        
    function addRow(){
         var tr = jQuery("#evaluationCriteriaTable>tbody>tr:last");
         var str="";
         if(index%2==0){
             str="<tr class='griddata-even'>";
         }else{
             str="<tr class='griddata-odd'>";
         }
         str+="<td class='gridselect'><input class='box' type='checkBox' name='criteriaItemId' value='" + index + "'  "+ (index==0 ? "readonly=true" : "") + "></td><td><input class='criteriaName' type='text' maxlength='20' name='criteriaItem" + index + ".name' /></td><td><input class='criteriaMin' type='text' maxlength='5' name='criteriaItem"+ index +".min' "+ (index==0 ? "readonly=true value='0'" : "") + " /></td><td><input type='text' class='criteriaMax' maxlength='5' name='criteriaItem"+ index +".max' /></td>";
        tr.after(str);
        index++;
    }
    
    function check(form){
        var str="";
        var flag= true;
        var datas=new Array();
           jQuery("#evaluationCriteriaTable input[name='criteriaItemId']").each(function(i){
            if(jQuery(this).parent().parent().find("input[name$='name']").val()==""){
                str+="第"+(i+1)+"行 选项名称没有填写\n";
                flag=false;
            }
            var min = jQuery(this).parent().parent().find("input[name$='min']").val();
            var max = jQuery(this).parent().parent().find("input[name$='max']").val();
            if(max==""){
                str+="第"+(i+1)+"行 最大值没有填写\n";
                flag=false;
            }else if(!/^\d+\.?\d*$/.test(max)){
                str+="第"+(i+1)+"行 最大值格式不对\n";
                flag=false;
            }else{
                 datas.push(parseFloat(max));
            }
              if(min==""){
                str+="第"+(i+1)+"行 最小值没有填写\n";
                flag=false;
            }else if(!/^\d+\.?\d*$/.test(min)){
                 str+="第"+(i+1)+"行 最小值格式不对\n";
                 flag=false;
              }else{
                 datas.push(parseFloat(min));
            }
           });
           if(!flag){
               alert(str);
               return;
           }
        for(var i=0;i<datas.length;i+=2){
            if(datas[i+1]>datas[i]){
        //     str+=datas[i+1]+":"+datas[i]+" 范围错误\n";
               str+="第"+(i/2+1) +"行的最小分数不能大于最大分数\n";
               flag=false;
            }
            if(i>1 && datas[i-2]!=datas[i+1]){
               //str+=datas[i-1]+":"+datas[i]+" 范围错误\n";
               str+="第"+(i/2+1) +"行的最小分数必须等于第"+(i/2)+"行的最大分数\n";
               flag=false;
            }
        }
           bg.form.addInput(form,"criteriaItemCount",jQuery("#evaluationCriteriaTable input[name='criteriaItemId']").length);
           if(!flag){
               alert(str);
           }
           return flag;
    }
    
    function deleteRow(){
        var ids = bg.input.getCheckBoxValues("criteriaItemId");
         if(ids==null || ids==""){
             alert("请选择一条");
             return;
         }
         jQuery("#evaluationCriteriaTable input[name='criteriaItemId']:checked").parent().parent().remove();
         jQuery("#evaluationCriteriaTable input[name='criteriaItemId']").each(function(i){
            jQuery(this).parent().parent().removeClass();
            jQuery(this).parent().find(".criteriaId").attr("name","criteriaItem"+i+".id");
            jQuery(this).parent().parent().find(".criteriaName").attr("name","criteriaItem"+i+".name");
            jQuery(this).parent().parent().find(".criteriaMin").attr("name","criteriaItem"+i+".min");
            jQuery(this).parent().parent().find(".criteriaMax").attr("name","criteriaItem"+i+".max");
            if((i+1)%2==0){
                jQuery(this).parent().parent().addClass("griddata-odd");
            }else{
                   jQuery(this).parent().parent().addClass("griddata-even");
            }
        });
        index = jQuery("#evaluationCriteriaTable input[name='criteriaItemId']").length;
        if(checkAll.checked){
            checkAll.checked=false;
        }
    }


 </script>
[@b.foot/]
