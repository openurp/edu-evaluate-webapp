[#ftl]
[@b.head/]
[@b.toolbar title='初始有效结果']
    bar.addBack();
[/@]

[@b.form name="initValidForm" action="!setValid"]
<table class="infoTable" width="100%" align="center">
    <thead style="background:#C7DBFF;height:40px;line-height:40px;text-align:center;font-weight:bold;">
        <tr>
            <td colspan="5">初始化评教结果</td>
        </tr>
    </thead>
    <tbody style="height:25px;line-height:25px;">
        <tr>
            <td class="title" style="text-align:center;" width="10%;">有效值</td>
            <td style="padding-left:10px;" width="38%;">
                <input type="text" name="percent" id="percent" />
                <font color="red" style="font-size:11px;">前后各舍去百分比。(1~50之间)</font>
            </td>
            <td class="title" style="text-align:center;" width="10%;">学年学期</td>
            <td style="padding-left:10px;" width="38%;">[@eams.semesterCalendar title="学年学期" name="semester.id" empty="false" value=semester /]</td>
            <td><input type="button" value="开始初始化" onClick="doStatistic()" class="buttonStyle" /></td>
        </tr>
    </tbody>
</table>
[/@]
<script type="text/javaScript">
    var initValidForm = document.initValidForm;
    
    function doStatistic(){
        if ($("#percent").val() == ""){
            alert("请输入百分比!");
            return false;
        } else {
            bg.form.setSearchParams(form,initValidForm);
            bg.form.submit(initValidForm);
        }
    }
</script>
[@b.foot/]