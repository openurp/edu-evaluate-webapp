[#ftl]
[@b.head/]
[@b.toolbar title="统计评教结果"]
    bar.addBack();
[/@]
[@b.form name="statForm" title="统计评教结果"  action="!stat" theme="list"]
    [@edu.semester  name="semester.id" label="学年学期" value=currentSemester /]
    [@b.formfoot]
        [@b.submit id="btnSave" value="统计评教结果" onsubmit="doStatistic()"/]
    [/@]
[/@]
<script type="text/javaScript">
    function doStatistic(){
        var form = document.statForm;
        form["btnSave"].disabled = true;
        bg.form.submit(form, "${b.url('!stat')}");
    }
</script>
[@b.foot/]
