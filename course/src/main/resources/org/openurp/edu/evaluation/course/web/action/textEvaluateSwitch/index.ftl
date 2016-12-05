[#ftl]
[@b.head /]
[@b.toolbar title='文字评教开关' id='textEvaluateSwitchBar' /]

[#--[@eams.semesterBar semesterValue=semester name="project.id" semesterName="semester.id" semesterEmpty="false" initCallback="changeSemester()" semesters=semesters/]--]
<table class="indexpanel">
    <tr>
        <td class="index_content">
        [@b.form name="textEvaluateSwitchIndexForm" action="!search" target="contentDiv" theme="search"]
        [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code")  option = "id,code" empty="..."  style="width:100px"/]
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