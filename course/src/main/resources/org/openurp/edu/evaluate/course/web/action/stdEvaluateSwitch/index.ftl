[#ftl]
[@b.head /]
[@b.toolbar title='评教开关' id='evaluateSwitchBar' /]

[@eams.semesterBar semesterValue=semester name="project.id" semesterName="semester.id" semesterEmpty="false" initCallback="changeSemester()" semesters=semesters/]
<table class="indexpanel">
    <tr>
        <td class="index_content">
            [@b.div id="contentDiv" href="!search?evaluateSwitch.semester.id=${semester.id}"/]
        </td> 
    </tr>
</table>
[@b.foot/]