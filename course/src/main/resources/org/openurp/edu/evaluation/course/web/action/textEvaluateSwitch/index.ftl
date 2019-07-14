[#ftl]
[@b.head /]
[@b.toolbar title='文字评教开关' id='textEvaluateSwitchBar' /]

<table class="indexpanel">
    <tr>
        <td class="index_content">
        [@b.form name="textEvaluateSwitchIndexForm" action="!search" target="contentDiv" theme="search"]
        [@edu_base.semester  name="semester.id" label="学年学期" value=currentSemester/]
        [@b.select  name="opened" label="开关状态" items={'1':'开放','0':'关闭'} value='1'  style="width:100px"/]
        <input type="hidden" name="orderBy" value="textEvaluateSwitch.id"/>
        [/@]
        </td>
        <td class="index_content">
            [@b.div id="contentDiv"  href="!search?&semester.id=${(semester.id)!}" /]
        </td>
    </tr>
</table>
[@b.foot/]
