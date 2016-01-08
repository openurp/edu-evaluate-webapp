[#ftl]
[@b.head/]
[@b.toolbar title="统计评教结果"]
    bar.addBack();
[/@]
[@b.form name="statForm" title="统计评教结果"  action="!stat" theme="list"]
   [#-- [@eams.semesterCalendar title="学年学期" label="学年学期" name="semester.id" empty="false" value=semester /]--]
    [@b.field label="学历层次"]
        <table class="infoTable" align="left" style="width:350px;">
            <tr>
                <td>
                    [@b.select name="educations" items=educations multiple="multiple" style="width:150px;height:150px;"  onDblClick="selectRemoveAnyOne('educations','educationSelected')"/]
                </td>
                <td width="50" align="center">
                    <input type="button" style="width:25px" onClick="selectRemoveAll('educations','educationSelected')" value="&gt;&gt;"/><br/>
                    <input type="button" style="width:25px" onClick="selectRemoveAnyOne('educations','educationSelected')" value="&gt;"/><br/>
                    <input type="button" style="width:25px" onClick="selectRemoveAnyOne('educationSelected','educations')" value="&lt;"/><br/>
                    <input type="button" style="width:25px" onClick="selectRemoveAll('educationSelected','educations')" value="&lt;&lt;"/>
                </td>
                <td>
                    [@b.select name="educationSelected" items=educations1 multiple="multiple" size="10" style="width:150px;height:150px;" onDblClick="selectRemoveAnyOne('educationSelected','educations')"/]
                </td>
            </tr>
        </table>
        <div style="clear:both;"></div>
    [/@]
    [@b.field label="所在院系"]
        <table class="infoTable" align="left" style="width:350px;">
            <tr>
                <td>
                    [@b.select  name="departments" items=departments multiple="multiple" style="width:150px;height:150px;" onDblClick="selectRemoveAnyOne('departments','departmentSelected')"/]
                </td>
                <td width="50" align="center">
                    <input type="button" style="width:25px" onClick="selectRemoveAll('departments','departmentSelected')" value="&gt;&gt;"/><br/>
                    <input type="button" style="width:25px" onClick="selectRemoveAnyOne('departments','departmentSelected')" value="&gt;"/><br/>
                    <input type="button" style="width:25px" onClick="selectRemoveAnyOne('departmentSelected','departments')" value="&lt;"/><br/>
                    <input type="button" style="width:25px" onClick="selectRemoveAll('departmentSelected','departments')" value="&lt;&lt;"/>
                </td>
                <td>
                    [@b.select  name="departmentSelected" items=departments1 multiple="multiple" size="10" style="width:150px;height:150px;" onDblClick="selectRemoveAnyOne('departmentSelected','departments')"/]
                </td>
            </tr>
        </table>
        <div style="clear:both;"></div>
    [/@]
    <tr align="center">
        <input type="hidden" name="departIds" value=""/>
        <input type="hidden" name="educationIds" value=""/>
        [@b.submit id="btnSave" value="统计评教结果" onsubmit="doStatistic()"/]
    </tr>
    [/@]
<script type="text/javaScript">
    function doStatistic(){
        var form = document.statForm;
        var edu = form['educationSelected'];
        var dep = form['departmentSelected'];
        var eduIds = "";
        var depIds = "";
        for(var i=0; i<edu.options.length;i++){
            if(eduIds != ""){
            eduIds += ","+edu.options[i].value;
            }else{
            eduIds += edu.options[i].value;
            }
        }
        
        for(var j=0; j<dep.options.length;j++){
            if(depIds != ""){
            depIds += ","+dep.options[j].value;
            }else{
            depIds += dep.options[j].value;
            }
        }
        if(""==eduIds){
            alert("请选择学历层次");
            return false;
        }
        if(""==depIds){
            alert("请选择部门");
            return false;
        }
        
        bg.form.addInput(form,"departIds",depIds);
        bg.form.addInput(form,"educatIds",eduIds);
        form["btnSave"].disabled = true;
        bg.form.submit(form, "${b.url('!stat')}");
    }
    
    var form = document.statForm;
    function selectRemoveAll(fromSelectName, toSelectName) {
    var fromSelect = form[fromSelectName];
    
    var toSelect = form[toSelectName];
    while (fromSelect.options.length > 0) {
        toSelect.options.add(new Option(fromSelect.options[0].text, fromSelect.options[0].value));
        fromSelect.options[0] = null;
    }
    }
    
    function selectRemoveAnyOne(fromSelectName, toSelectName) {
        var fromSelect = form[fromSelectName];
        var toSelect = form[toSelectName];
        for (var i = 0; i < fromSelect.options.length;) {
            if (fromSelect.options[i].selected) {
                toSelect.options.add(new Option(fromSelect.options[i].text, fromSelect.options[i].value));
                fromSelect.options[i] = null;
            } else {
                i++;
            }
        }
    }
    
    function moveSelectedOption(olds,news){
    var form = document.statForm;
    var selectOld = form[olds];
    var selectNew = document.getElementById(news);
        for(var i=0; i<selectOld.options.length;i++){
            if(selectOld.options[i].selected == true){
                var t=document.createElement("OPTION");
                t.text=selectOld.options[i].text;
                t.value=selectOld.options[i].value;
                selectNew.add(t);
                selectOld.remove(i);
                
            }
        }
    }
</script>
[@b.foot/]