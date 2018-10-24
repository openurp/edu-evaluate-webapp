[#ftl]
[@b.head/]
[@b.toolbar title='我的评教回收情况' id='textEvaluationBar'/]

      <table width="100%" class="frameTable">
        <tr>
            <td class="index_content">
            [@b.form name="evaluateSearchIndexForm" action="!search" target="contentDiv" theme="search"]
            [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") value=currentSemester option = "id,code" empty="..."/]
            [/@]
        </td>
       <td class="index_content">
            [@b.div id="contentDiv"  href="!search" /]
        </td>
        </tr>
    </table>
[@b.foot/]
