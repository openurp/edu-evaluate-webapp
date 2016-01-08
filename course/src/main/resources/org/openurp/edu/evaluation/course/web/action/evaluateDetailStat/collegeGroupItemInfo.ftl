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
    <table class="gridtable" align="center" width="605px">
        <tr align="center">
            <td >项目</td>
            <td ></td>
            [#list criterias as criteria]
            <td >${criteria.name!}(${criteria.min!}~${criteria.max!})</td>
            [/#list]
        </tr>
        [#list questionTypes as questionType]
        <tr align="center">
            <td rowspan="2">${questionType.name!}</td>
            <td >人次</td>
           [#list criterias as criteria]
           <td>
           [#list questionDeps['${criteria.id!}'] as questionDep]
           [#if questionDep[0]?string ==questionType.id?string ]
           
           ${questionDep[1]!0}
           [/#if]
           [/#list]
           </td>
           [/#list]
        </tr>
        <tr align="center">
            <td>学校比例</td>
         [#list criterias as criteria]
         <td>
           [#list questionDeps['${criteria.id!}'] as questionDep]
           [#if questionDep[0]?string ==questionType.id?string ]
           ${((questionDep[1]!0)/(persons!1))*100}%
           [/#if]
           [/#list]
           </td>
           [/#list]
        </tr>
        [/#list]
    </table>
[@b.foot/]