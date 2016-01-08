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
            [#--[#list departments as dep]
            <option value="${dep.id!}" [#if departId?exists][#if dep.id?string == departId?string]selected="true"[/#if][/#if]>${dep.name!}</option>
            [/#list]--]
            </select >课程质量评价汇总历史对比</td>
        </tr>
        </tbody>
    </table>
    [@b.grid items=evaSemesters var="evaSemester" sortable="false"]
        [@b.row]
            [@b.col title="学年学期" width="10%"]${evaSemester.schoolYear!}(${(evaSemester.name)!})[/@]
            [#list criterias as criteria]
            [@b.col title="${criteria.name!}"]
            [#assign num1=0 /]
            [#assign nums=0 /]
            [#list questionDeps['${criteria.id}'] as questionDep]
            [#if questionDep[0]?string('') == evaSemester.id?string('#') ]
            [#assign num1=questionDep[1]!1 /]
            [/#if]
            [/#list]
            [#list questionNums as questionNum]
            [#if questionNum[0]?string('') == evaSemester.id?string('#')]
            [#assign nums=questionNum[1]!1 /]
            [/#if]
            [/#list]
            [#--${num1!}/${((num1/nums)*100)!0}%--]
            [/@]
            [/#list]
        [/@]
    [/@]
    [/@]
    <script type="text/javaScript">
    var form = document.depHistoryStatSearchForm;
    function depHistoryStat(){
        bg.form.submit(form, "evaluateDetailStat!depHistoryStat.action");
    }
</script>
[@b.foot/]