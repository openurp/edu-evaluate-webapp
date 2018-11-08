[#ftl]
[@b.head/]
[@b.toolbar title='院系任务评教结果比较' id='departmentChoiceConfigBar']
    bar.addBack();
[/@]
    <div style="width: 80%;margin:0 auto;">
        <table align="center">
            <tbody>
                <tr>
                    <td style="font-size: 10pt; text-align:center; font-weight: bold;">${semester.schoolYear!}第${semester.name!}学期课程质量评价学院统计汇总表</td>
                </tr>
            </tbody>
        </table>
    [@b.grid items=departments var="department" sortable="false"]
        [@b.row]
            [@b.col title="院系名称" width="30%" ]${(department.name)!}[/@]
            [@b.col title="总数" width="8%" ]
            [#assign nums = 0]
            [#list questionNums as questionN]
            [#if questionN[0]?string == department.id?string]
            [#assign nums = questionN[1]!0]
            [/#if]
            [/#list]
            ${nums!0}
            [/@]
            [#list criterias as criteria]
            [@b.col title="${criteria.name!}(${criteria.min!}~${criteria.max!})"]
            [#list questionDeps['${criteria.id}'] as questionDep]
            [#if questionDep[0]?string == department.id?string ]
            [#list questionNums as questionN]
            [#if questionN[0]?string == department.id?string]
            ${questionDep[1]!0}(${((questionDep[1]!0)/(questionN[1]!1))*100}%)
            [/#if]
            [/#list]
            [/#if]
            [/#list]
            [/@]
            [/#list]
        [/@]
    [/@]
    </div>
[@b.foot/]
