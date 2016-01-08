[#ftl/]
[@b.head/]
[@b.toolbar title="专业教师模版导入"]
    bar.addItem("模板下载","downloadTemplate()","${base}/static/images/action/download.gif");
    bar.addBack();
[/@]
[@b.form name="awardImportForm" action="!importData" theme="list" enctype="multipart/form-data"]
    [@b.messages/]
    <label for="importFile" class="label"><em class="required">*</em>文件目录:</label><input type="file" name="importFile" value="" id="importFile"/>
    <br>
    <div style="padding-left: 50px; padding-top:10px;">
        [@b.submit value="system.button.submit" onSubmit="validateExtendName"/]
        <input type="reset" value="重置" />
    </div>
    <div style="color:red;font-size:2">上传文件中的所有信息均要采用文本格式。对于日期和数字等信息也是一样。</div>
[/@]
<script type="text/javaScript">
    function downloadTemplate() {
        bg.Go("departEvaluation!downDepEvaluateTemp.action","_self");
    }
    function validateExtendName(form){
        var value = form.importFile.value;
        var index = value.indexOf(".xls");
        if((index < 1) || (index < (value.length - 4))){
            alert("${b.text("filed.file")}'.xls'");
            return false;
        }
        return true;
    }
</script>
[@b.foot/]