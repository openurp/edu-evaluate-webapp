[#ftl]
[@b.head/]
[@b.toolbar title="添加/修改评价标准项"]
    bar.addItem("${b.text('action.add')}","addRow()");
    bar.addItem("${b.text('action.delete')}","deleteRow()");
    bar.addBack();
[/@]
    [@b.form name="assessCriteriaForm" title=" " action=b.rest.save(assessCriteria) theme="list"]
           [@b.textfield id="name" label="名称" required="true"  name="assessCriteria.name" value="${(assessCriteria.name?html)!}" maxlength="30"/]
           [@b.startend label="生效日期" required="true,false" name="assessCriteria.beginOn,assessCriteria.endOn" format="yyyy-MM-dd" start=assessCriteria.beginOn end=assessCriteria.end! /]
           [@b.field label="明细"]
            <table class="formTable" width="60%"  id="assessCriteriaTable">
               <thead>
                <tr align="center">
                   <td class="gridselect" style="width:5%"><input type="checkBox" id="criteriaItemBoxId" class="box" onClick="bg.input.toggleCheckBox(document.getElementsByName('criteriaItemId'),event);"></td>
                   <td align="center" style="background-color: #c7dbff;width:25%">评价名称</td>
                   <td align="center" style="background-color: #c7dbff;width:10%">数值</td>
                   <td align="center" style="background-color: #c7dbff;width:30%">详细说明</td>
                   <td align="center" style="background-color: #c7dbff;width:15%">最小分数(包含)</td>
                   <td align="center" style="background-color: #c7dbff;width:15%">最大分数(不包含)</td>
                </tr>
              </thead>
                [#list assessCriteria.grades?sort_by("minScore") as criteriaItem]
                <tr [#if (criteriaItem_index+1)%2==0]class="griddata-odd" [#else]class="griddata-even"[/#if]>
                    <td class="gridselect">
                        <input class="box" name="criteriaItemId" value="${(criteriaItem.id)?if_exists}" type="checkbox">
                        <input class="criteriaId" type="hidden" name="criteriaItem${criteriaItem_index}.id" value="${(criteriaItem.id)?if_exists}"/>
                    </td>
                    <td><input class="criteriaName" name="criteriaItem${criteriaItem_index}.name" maxlength="20" value="${(criteriaItem.name?html)!}"/></td>
                    <td><input class="criteriaGrade" name="criteriaItem${criteriaItem_index}.grade" maxlength="20" value="${(criteriaItem.grade?html)!}"/></td>
                    <td><input class="criteriaDescription" name="criteriaItem${criteriaItem_index}.description" maxlength="200" value="${(criteriaItem.description?html)!}"/></td>
                    <td><input class="criteriaMin" name="criteriaItem${criteriaItem_index}.minScore" id="criteriaItem${criteriaItem_index}.minScore" maxlength="5" value="${(criteriaItem.minScore)?if_exists}"  [#if criteriaItem_index==0]readonly=true[/#if]/></td>
                    <td><input class="criteriaMax" name="criteriaItem${criteriaItem_index}.maxScore" maxlength="5" value="${(criteriaItem.maxScore)?if_exists}"/></td>
                </tr>
                [/#list]
            </table>
           [/@]
           [@b.formfoot]
               [#if assessCriteria.persisted]
               <input name="assessCriteria.id"  value="${assessCriteria.id}" type="hidden"/>
               [/#if]
              [#--] <input name="assessCriteria.id" value="${assessCriteria.id?default("")}" type="hidden"/>[--]

            [@b.submit value="action.submit" onsubmit="check"/]&nbsp;
            <input type="reset"  name="reset1" value="${b.text("action.reset")}" class="buttonStyle" />
        [/@]
   [/@]
 <script language="JavaScript">
       var form =document.assessCriteriaForm;
    var index=${assessCriteria.grades?size};

    var checkAll = document.getElementById("criteriaItemBoxId");
    if(checkAll.checked){
        checkAll.checked=false;
    }

    function addRow(){
         var tr = jQuery("#assessCriteriaTable>tbody>tr:last");
         var str="";
         if(index%2==0){
             str="<tr class='griddata-even'>";
         }else{
             str="<tr class='griddata-odd'>";
         }
         str += "<td class='gridselect'><input class='box' type='checkBox' name='criteriaItemId' value='" + index + "'  "+ (index==0 ? "readonly=true" : "") + "></td>";
         str += "<td><input class='criteriaName' type='text' maxlength='20' name='criteriaItem" + index + ".name' /></td>";
         str += "<td><input class='criteriaGrade' type='text' maxlength='2' name='criteriaItem" + index + ".grade' /></td>";
         str += "<td><input class='criteriaDescription' type='text' maxlength='2' name='criteriaItem" + index + ".description' /></td>";
         str += "<td><input class='criteriaMin' type='text' maxlength='5' name='criteriaItem"+ index +".minScore' "+ (index==0 ? "readonly=true value='0'" : "") + " /></td>";
         str += "<td><input type='text' class='criteriaMax' maxlength='5' name='criteriaItem"+ index +".maxScore' /></td>";
        tr.after(str);
        index++;
    }

    function check(form){
        var str="";
        var flag= true;
        var datas=new Array();
           jQuery("#assessCriteriaTable input[name='criteriaItemId']").each(function(i){
            if(jQuery(this).parent().parent().find("input[name$='name']").val()==""){
                str+="第"+(i+1)+"行 选项名称没有填写\n";
                flag=false;
            }
            var min = jQuery(this).parent().parent().find("input[name$='minScore']").val();
            var max = jQuery(this).parent().parent().find("input[name$='maxScore']").val();
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
           bg.form.addInput(form,"criteriaItemCount",jQuery("#assessCriteriaTable input[name='criteriaItemId']").length);
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
         jQuery("#assessCriteriaTable input[name='criteriaItemId']:checked").parent().parent().remove();
         jQuery("#assessCriteriaTable input[name='criteriaItemId']").each(function(i){
            jQuery(this).parent().parent().removeClass();
            jQuery(this).parent().find(".criteriaId").attr("name","criteriaItem"+i+".id");
            jQuery(this).parent().parent().find(".criteriaName").attr("name","criteriaItem"+i+".name");
            jQuery(this).parent().parent().find(".criteriaMin").attr("name","criteriaItem"+i+".minScore");
            jQuery(this).parent().parent().find(".criteriaMax").attr("name","criteriaItem"+i+".maxScore");
            if((i+1)%2==0){
                jQuery(this).parent().parent().addClass("griddata-odd");
            }else{
                   jQuery(this).parent().parent().addClass("griddata-even");
            }
        });
        index = jQuery("#assessCriteriaTable input[name='criteriaItemId']").length;
        if(checkAll.checked){
            checkAll.checked=false;
        }
    }

 </script>
[@b.foot/]
