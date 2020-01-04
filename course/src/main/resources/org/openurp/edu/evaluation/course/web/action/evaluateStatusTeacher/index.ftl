[#ftl]
[@b.head/]
[@b.toolbar title='我的评教回收情况' id='textEvaluationBar'/]

      <table width="100%" class="frameTable">
        <tr>
            <td class="index_content">
            [@b.form name="evaluateSearchIndexForm" action="!search" target="contentDiv" theme="search"]
            [@edu_base.semester  name="semester.id" label="学年学期" value=currentSemester/]
            [/@]
        </td>
       <td class="index_content">
            [@b.div id="contentDiv"  href="!search" /]
        </td>
        </tr>
    </table>
[@b.foot/]
