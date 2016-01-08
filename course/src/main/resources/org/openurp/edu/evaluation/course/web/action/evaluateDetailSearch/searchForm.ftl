<table width="100%">
    <tr>
        <td align="left" valign="bottom" colspan="2"><img src="images/action/info.gif" align="top"/>&nbsp;<B>评教结果查询(模糊查询)</B></td>
    </tr>
    <tr>
        <td colspan="2" style="font-size:0px"><img src="images/action/keyline.gif" height="2" width="100%" align="top"></td>
    </tr>
    <tr>
        <td><@text name="entity.educationType"/></td>
        <td>
            <select id="educationType" name="evaluateTeacher.semester.calendar.educationType.id" style="width:100px;">
            </select>
        </td>
    </tr>
    <tr>
        <td><@text name="attr.year2year"/></td>
        <td><select id="year" name="evaluateTeacher.semester.schoolYear" style="width:100px;">
            </select>
        </td>
    </tr>
    <tr>
        <td><@text name="attr.term"/>
        </td>
        <td><select id="term" name="evaluateTeacher.semester.name" style="width:100px;">
            </select>
        </td>
    </tr>
    <tr>
        <td>课程代码：</td>
        <td><input type="text" name="evaluateTeacher.course.code" value="${Parameters["evaluateTeacher.course.code"]?if_exists}" maxlength="50" style="width:100px"/></td>
    </tr>
    <tr>
        <td>课程名称：</td>
        <td><input type="text" name="evaluateTeacher.course.name" value="${Parameters["evaluateTeacher.course.name"]?if_exists}" maxlength="50" style="width:100px"/></td>
    </tr>
    <tr>
        <td colspan="2" align="center" height="50"><button onclick="search()">查询</button><br></td>
    </tr>
</table>
<input type="hidden" name="orderBy" value="evaluateTeacher.score desc"/>
<#assign yearNullable=true>
<#assign termNullable=true>
<#include "/template/semesterSelect.ftl"/>
<input type="hidden" name="searchFormFlag" value="beenStat"/>