[#ftl]
[@b.head/]
[@b.toolbar title='课程问卷' /]

[#--[@eams.semesterBar semesterValue=semester name="project.id" semesterName="semester.id" semesterEmpty="false" initCallback="changeSemester()" semesters=semesters/]--]
<table class="indexpanel">
    <tr>
        <td style="width:200px" class="index_view">
        [@b.form action="!search" name="questionnaireLessonIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            [@b.textfields style="width:130px" names="lesson.no;课程序号,lesson.course.code;课程代码,lesson.course.name;课程名称"/]
            [@b.select style="width:134px" name="lesson.courseType.id" label="课程类别" items=courseTypes empty="..."/]
            [@b.select style="width:134px" name="lesson.teachDepart.id" label="开课院系" items=departments empty="..."/]
            [@b.select style="width:134px" name="lesson.examMode.id" label="考核方式" items=examModes empty="..."/]
            [@b.select style="width:134px" name="hasTeacher" label="有无教师" items={'1':'有','0':'无'} empty="..."/]
            [#--[@eams.numRange label="起始周"   name="startWeekFrom,startWeekEnd" style="width:50px" minRange=1 /]
            [@eams.numRange label="上课人数" name="stdCountFrom,stdCountEnd" style="width:50px" minRange=0/]--]
            [@b.field label='有无问卷']<select style="width:134px" name="questionnaire.id">
                      <option value="-1" style="background:#F5EDDB">有问卷(所有)</option>
                      <option value="0" style="background:#F5EDDB">无问卷(所有)</option>
                      [#list questionnaires?if_exists as questionnaire]
                      <option value="${(questionnaire.id)!}">${(questionnaire.description)!}</option>
                      [/#list]
                </select>
            [/@]
        [/@]
        </td>
        <td class="index_content">
            [@b.div id="contentDiv" href="!search?&lesson.semester.id=${(semester.id)!}" /]
        </td> 
    </tr>
</table>
[@b.foot/]