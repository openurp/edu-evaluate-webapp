[#ftl]
[@b.head/]
[@b.toolbar title="添加/修改选项组"]
    bar.addItem("${b.text('action.add')}","addRow()");
    bar.addItem("${b.text('action.delete')}","deleteRow()");
    bar.addBack();
[/@]

<script>
    function checkNum() {
        var reg = new RegExp(/^(\d*\.)?\d+$/);
        return reg.test(document.optionGroupForm["optionGroup.oppoVal"].value);
    }
</script>
    [@b.form name="optionGroupForm" title="" action=b.rest.save(optionGroup) theme="list"]
           [@b.textfield id="name" label="名称" required="true" name="optionGroup.name" value="${(optionGroup.name?html)!}" maxlength="30" /]
           [#--][@b.textfield label="倾向性权重" required="true" check="assert(checkNum(), '倾向性权重格式有误');" name="optionGroup.oppoVal" value="${(optionGroup.oppoVal?html)?default('')}" maxlength="3" /][--]
           [@b.field label="选项"]
            <table class="formTable" width="60%"  id="optionTable">
                <tr align="center">
                    <td class="gridselect"><input type="checkBox" id="optionIdBox" class="box" onClick="bg.input.toggleCheckBox(document.getElementsByName('optionId'),event);"></td>
                       <td align="center" style="background-color: #c7dbff;width:35%">选项名</td>
                       <td align="center" style="background-color: #c7dbff;width:62%">所占比重</td>
                </tr>
                [#list optionGroup.options?sort_by("proportion")?reverse as option]
                <tr [#if (option_index+1)%2==0]class="griddata-odd" [#else]class="griddata-even"[/#if]>
                    <td class="gridselect">
                        <input class="box" name="optionId" value="${(option.id)?if_exists}" type="checkbox">
                        <input class="optId" type="hidden" name="option${option_index}.id" value="${(option.id)?if_exists}"/>
                    </td>
                    <td><input class="optName" name="option${option_index}.name" maxlength="20" value="${(option.name?html)!'--'}"/></td>
                    <td><input class="optProportion" name="option${option_index}.proportion" maxlength="10" value="${(option.proportion)?if_exists}"/></td>
                </tr>
                [/#list]

            </table>
           [/@]
           [@b.formfoot]
            [@b.submit value="action.submit" onsubmit="check"/]&nbsp;
            [@b.reset/]
        [/@]
   [/@]
 <script language="JavaScript">
       var form =document.optionGroupForm;
       var index=${optionGroup.options?size};

    var checkAll = document.getElementById("optionIdBox");
    if(checkAll.checked){
        checkAll.checked=false;
    }

    function addRow(){
         var tr = jQuery("#optionTable>tbody>tr:last");
         var str="";
         if(index%2==0){
             str="<tr class='griddata-even'>";
         }else{
             str="<tr class='griddata-odd'>";
         }
         str+="<td class='gridselect'><input class='box' type='checkBox' name='optionId' value='"+index+"'></td><td><input name='option"+index+".name' maxlength='20' class='optName' value=''/></td><td><input name='option"+index+".proportion' class='optProportion' maxlength='10' value=''/></td>";
        tr.after(str);
        index++;
    }

    function check(form){
        var str="";
        var flag= true;
           jQuery("#optionTable input[name='optionId']").each(function(i){
            if(jQuery(this).parent().parent().find(":text:first").val()==""){
                str+="第"+(i+1)+"行 选项名称没有填写\n";
                flag=false;
            }
              if(!/^\d+\.?\d*$/.test(jQuery(this).parent().parent().find(":text:last").val())){
                 str+="第"+(i+1)+"行 比重格式不对\n";
                 flag=false;
              }else{
                 if(new Number(jQuery(this).parent().parent().find(":text:last").val())>1){
                    str+="第"+(i+1)+"行 比重太大,不能大于1\n";
                    flag=false;
                 }
            }
           });
           bg.form.addInput(form,"optionCount",jQuery("#optionTable input[name='optionId']").length);
           if(!flag){
               alert(str);
           }
           return flag;
    }

    function deleteRow(){
        var ids = bg.input.getCheckBoxValues("optionId");
         if(ids==null || ids==""){
             alert("请选择一条");
             return;
         }
         jQuery("#optionTable input[name='optionId']:checked").parent().parent().remove();
         jQuery("#optionTable input[name='optionId']").each(function(i){
            jQuery(this).parent().parent().removeClass();
            jQuery(this).parent().find(".optId").attr("name","option"+i+".id");
            jQuery(this).parent().parent().find(".optName").attr("name","option"+i+".name");
            jQuery(this).parent().parent().find(".optProportion").attr("name","option"+i+".proportion");
            if((i+1)%2==0){
                jQuery(this).parent().parent().addClass("griddata-odd");
            }else{
               jQuery(this).parent().parent().addClass("griddata-even");
            }
        });
        index = jQuery("#optionTable input[name='optionId']").length;
        if(checkAll.checked){
            checkAll.checked=false;
        }
    }

 </script>
[@b.foot/]
