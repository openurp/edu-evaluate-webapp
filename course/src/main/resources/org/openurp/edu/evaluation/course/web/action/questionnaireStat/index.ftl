[#ftl]
[@b.head/]
[@b.toolbar title='课程问卷管理' id='questionnaireStatBar']
    bar.addItem("各院系评教比较","compareAllCollege()");
    //bar.addItem("各院系线图比较","lieChart()");
[/@]

[#include "queryConditions.ftl"/]
<script type="text/javaScript">
    function compareAllCollege(){
        bg.form.addInput(form,"semester.id","${semester.id}");
        bg.form.submit(form, "${b.url('!departDistributeStat')}");
    }
    function lieChart(){
        
    }
</script>
[@b.foot/]