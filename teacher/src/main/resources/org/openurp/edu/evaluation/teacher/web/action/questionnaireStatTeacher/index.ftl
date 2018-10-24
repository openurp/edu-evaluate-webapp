[#ftl]
[@b.head/]
[@b.toolbar title='我的评教结果' id='textEvaluationBar'/]

      <table width="100%" class="frameTable">
        <tr>
            <td class="index_content">
            [@b.form name="evaluateIndexForm" action="!search" target="contentDiv"]
            <input type="hidden" name="questionnaireStat.teacher.id" value="${teacher.id}">
            [/@]
            [@b.div href="!search" id="contentDiv"/]
        </td>
        </tr>
    </table>
[@b.foot/]
