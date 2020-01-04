[#ftl]
[@b.head/]
[@b.toolbar title='历史课程评教汇总' id='departmentChoiceConfigBar']
    bar.addBack();
[/@]
[@b.form name="depHistoryStatSearchForm" action="!search" target="contentDiv"]
    <table align="center">
        <tbody>
        <tr>
            <td style="font-size: 10pt;text-align:center; font-weight: bold;">
            <select name="department.id" id="department.id" onchange="depHistoryStat()">
            [#list departments as dep]
            <option value="${dep.id!}" [#if departId?exists][#if dep.id?string == departId?string]selected="true"[/#if][/#if]>${dep.name!}</option>
            [/#list]
            </select >课程质量评价汇总历史对比</td>
        </tr>
        </tbody>
    </table>
    [@b.grid items=evaSemesters var="evaSemester" sortable="false"]
        [@b.row]
            [@b.col title="学年学期" width="10%"]${evaSemester.schoolYear!}(${(evaSemester.name)!})[/@]
            [@b.col title="总数" width="8%" ]
            [#assign nums = 0]
            [#list questionNums as questionN]
            [#if questionN[0]?string ==  evaSemester.id?string]
            [#assign nums = questionN[1]!0]
            [/#if]
            [/#list]
            ${nums!0}
            [/@]
            [#list criterias as criteria]
            [@b.col title="${criteria.name!}(${criteria.min!}~${criteria.max!})"]
            [#list questionDeps['${criteria.id}'] as questionDep]
            [#if questionDep[0]?string == evaSemester.id?string ]
            [#list questionNums as questionN]
            [#if questionN[0]?string == evaSemester.id?string]
            ${questionDep[1]!0}(${((questionDep[1]!0)/(questionN[1]!1))*100}%)
            [/#if]
            [/#list]
            [/#if]
            [/#list]
            [/@]
            [/#list]
        [/@]
    [/@]
    [/@]
    <script type="text/javaScript">
    var form = document.depHistoryStatSearchForm;
    function depHistoryStat(){
        bg.form.submit(form, "${b.url('!depHistoryStat')}");
    }
</script>
[@b.foot/]
