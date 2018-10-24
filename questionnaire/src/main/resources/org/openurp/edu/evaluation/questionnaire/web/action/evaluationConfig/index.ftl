[#ftl]
[@b.head/]
<script language="JavaScript" type="text/JavaScript" src="${base}/static/scripts/itemSelect.js"></script>
[@b.toolbar title="问卷设置" /]
    <table class="indexpanel">
        <tr>
            <td class="index_view">
            <form id="evaluationConfigForm" name="evaluationConfigForm" action="" method="post" target="contentDiv">
                <table class="search-widget" id="viewTables" width="95%">
                    <tr><td><em>信息分类</em></td></tr>
                    <tr>
                         <td id="infoTd" class="toolbar-item" width="95%" onclick="info(this,'${b.url("questionnaire!search")}')">
                         &nbsp;&nbsp;问卷信息
                         </td>
                       </tr>
                    <tr>
                         <td id="infoTd" class="toolbar-item" width="95%" onclick="info(this,'${b.url("question!search")}')">
                         &nbsp;&nbsp;问题信息
                       </td>
                       </tr>
                    <tr>
                         <td id="infoTd" class="toolbar-item" width="95%" onclick="info(this,'${b.url("question-type!search")}')" >
                         &nbsp;&nbsp;问题类别
                         </td>
                       </tr>
                    <tr>
                         <td id="infoTd" class="toolbar-item" width="95%" onclick="info(this,'${b.url("option-group!search")}')" >
                         &nbsp;&nbsp;选项组
                         </td>
                       </tr>
                    <tr>
                         <td id="infoTd" class="toolbar-item" width="95%" onclick="info(this,'${b.url("evaluation-criteria!search")}')">
                         &nbsp;&nbsp;评价标准
                         </td>
                       </tr>
                </table>
            </form>
               </td>
            <td class="index_content">
                [@b.div id="contentDiv" href="questionnaire!search" /]
            </td>
        </tr>
    </table>
<script language="JavaScript">
    function info(td,action){
        var viewTables = document.getElementById("viewTables");
        clearSelected(viewTables,td);
        setSelectedRow(viewTables,td);
        bg.form.submit("evaluationConfigForm",action);
    }
    jQuery(function(){
        jQuery("#evaluationConfigForm #infoTd").eq(3).css("fontStyle","italic").css("color","blue").css("backgroundColor","#e9f2f8");
        jQuery("#evaluationConfigForm #infoTd").hover(
            function(){
                jQuery(this).toggleClass("toolbar-item-transfer");
            }
        );
    });
</script>
[@b.foot/]
