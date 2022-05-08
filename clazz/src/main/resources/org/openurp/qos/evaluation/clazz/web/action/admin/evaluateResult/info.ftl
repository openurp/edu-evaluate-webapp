[#ftl]
[@b.head/]
[@b.toolbar title='课程问卷详细结果' id='evaluateResultInfoBar']
    bar.addBack();
[/@]
[@b.grid id="evaluateTB" items=questionResults var="questionResult" sortable="true"]
    [@b.row]
        [@b.col property="indicator.name" title="问题类型" width="15%"/]
        [@b.col property="question.contents" title="问题内容" style="text-align:left;padding-left:5px;"/]
        [@b.col property="option.name" title="所选项" width="8%"/]
        [@b.col property="score" title="所得分" width="6%"/]
    [/@]
[/@]
<br/>
<strong>备注：</strong>
<p style="padding:0 10px;">${(remark?html)!}</p>
<script type="text/javaScript">
    function mergeCells(){
        var firstTd = null;
        $("#evaluateTB>tbody>tr").each(function(i){
            var td = $(this).find("td:eq(0)");
            if (firstTd != null && firstTd.html() == td.html()){
                td.remove();
                firstTd.prop("rowSpan",firstTd.prop("rowSpan")+1);
            } else {
                firstTd = td;
            }
        });
    }

    mergeCells();
</script>
[@b.foot/]
