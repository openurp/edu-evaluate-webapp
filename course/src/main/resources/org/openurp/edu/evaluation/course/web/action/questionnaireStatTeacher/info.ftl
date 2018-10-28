[#ftl]
[@b.head/]

[@b.toolbar title='评教统计' ]
    bar.addPrint();
    bar.addClose();
[/@]
<style>
.planTable {
    border: 1px solid #006CB2;
    border-collapse: collapse;
    font-size: 10pt;
    font-style: normal;
    vertical-align: middle;
    table-layout:fixed;
    margin:   auto;
}
.planTable td {
    border: 1px solid #006CB2;
    border-collapse: collapse;
    overflow: hidden;
    word-wrap:break-word;
    padding:2px 0px;
}
.planTable thead tr {
    background-color: #C7DBFF;
    color: #000000;
    letter-spacing: 0;
    text-decoration: none;
}
</style>
<table width="99%" align="center">
<tr>
<td>
<table width="95%">
         <tr>
         <td  colspan="4" align="center" ><b>个人课程评价各项指标汇总</b></td>
        </tr>
        <tr>
        <td  colspan="" align="right">被评教师：</td>
        <td  colspan="" align="left" >${teacher.user.name!}</td>
        <td  colspan="" align="right">所属院系：</td>
        <td  colspan="" align="left" >${clazz.teachDepart.name!}</td>
        </tr>
        <tr>
        <td  colspan="" align="right">被评课程：</td>
        <td  colspan="" align="left" >${clazz.course.name!}</td>
        <td  colspan="" align="right">评价学期：</td>
        <td  colspan="" align="left" >${semester.schoolYear!}(${semester.name!})</td>
        </tr>
</table>
</td>
</tr>
<tr>
<td>
<table class="planTable" width="75%">
     <tr align="center" >
         <td rowspan="2" colspan="1" width="55%">问题信息</td>
     <td width="15%">个人各项</td>
       <td width="15%">院系各项</td>
       <td width="15%">全校各项</td>
     </tr>
     <tr align="center">
     <td width="15%">得分均值</td>
     <td width="15%">得分均值</td>
     <td width="15%">得分均值</td>
     </tr>
     [#assign nums =0]
            [#list questionRList?if_exists as questionR]
            [#assign nums =nums +1]
                <tr align="center">
                <td align="left">${nums!}:${(questionR[0])?if_exists}</td>
                <td width="15%">${(questionR[2])?if_exists}</td>
                 <td width="15%">
                  [#list depQRList as depQR]
                  [#if depQR[0]?string==questionR[1]?string]
                  ${depQR[1]!0}
                  [/#if]
                  [/#list]
                  </td>
                  <td width="15%">
                  [#list schQRList as schQR]
                  [#if schQR[0]?string==questionR[1]?string]
                  ${schQR[1]!0}
                  [/#if]
                  [/#list]
                  </td>
                </tr>
            [/#list]
</table>
</td>
</tr>
<tr>
<td>
<table width="85%">
        <tr>
        <td  colspan="" align="right">该课程个人得分：</td>
        <td  colspan="" align="left" >${teaScore!0}</td>
        <td  colspan="" align="right">院系平均分：</td>
        <td  colspan="" align="left" >${depScores!0}</td>
        <td  colspan="" align="right">全校平均分：</td>
        <td  colspan="" align="left" >${evaResults!0}</td>
        </tr>
        <tr>
        <td  colspan="2" align="right">该课程全校得分排名/全校参评课程总数：</td>
        <td  colspan="" align="left" >${schNum!0}/${schNums!0}</td>
        <td  colspan="2" align="right">该课程院系等分排名/院系参评课程总数：</td>
        <td  colspan="" align="left" >${depNum!0}/${depNums!0}</td>
        </tr>
</table>
</td>
</tr>
</table>

[@b.foot/]
