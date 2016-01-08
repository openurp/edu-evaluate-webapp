[#ftl]
[@b.head /]
[@b.toolbar title='文字评教开关' id='textEvaluateSwitchBar' /]

[@eams.semesterBar semesterValue=semester name="project.id" semesterName="semester.id" semesterEmpty="false" initCallback="changeSemester()" semesters=semesters/]
<table class="indexpanel">
    <tr>
        <td class="index_content">
            [@b.div id="contentDiv" href="!search?textEvaluateSwitch.semester.id=${semester.id}"/]
        </td> 
    </tr>
</table>
[@b.foot/]