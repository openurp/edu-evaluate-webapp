[#ftl]
[@b.head/]
[@b.toolbar title='本次课程质量评价全校分项统计汇总表' id='collegeGroupItemInfoBar']
    bar.addBack();
[/@]
    <table align="center" width="100%">
            <tr>
                <td style="font-size: 13.5pt;text-align:center; font-weight: bold; text-align: center;">本次课程质量评价全校分项统计汇总表</td>
            </tr>
    </table>
    [@b.grid items=questionTypes var="questionType" sortable="false"]
        [@b.row]
            [@b.col title="问题类别" width="30%"]${(questionType.name)!}[/@]
            [@b.col title="总数" width="8%" ]
            [#assign nums = 0]
            [#list quesTypeNums as questionN]
            [#if questionN[0]?string ==  questionType.id?string]
            [#assign nums = questionN[1]!0]
            [/#if]
            [/#list]
            ${nums!0}
            [/@]
            [#list criterias as criteria]
            [@b.col title="${criteria.name!}(${criteria.min!}~${criteria.max!})"]
            [#list questionDeps['${criteria.id}'] as questionDep]
            [#if questionDep[0]?string == questionType.id?string ]
            [#list quesTypeNums as questionN]
            [#if questionN[0]?string == questionType.id?string]
            ${questionDep[1]!0}(${((questionDep[1]!0)/(questionN[1]!1))*100}%)
            [/#if]
            [/#list]
            [/#if]
            [/#list]
            [/@]
            [/#list]
        [/@]
    [/@]
[@b.foot/]
