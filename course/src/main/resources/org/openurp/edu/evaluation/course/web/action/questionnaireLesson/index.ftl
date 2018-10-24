[#ftl]
[@b.head/]
[@b.toolbar title='课程问卷' /]

<table class="indexpanel">
    <tr>
        <td style="width:200px" class="index_view">
        [@b.form action="!search" name="questionnaireClazzIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            [@b.select  name="semester.id" label="学年学期" items=semesters?sort_by("code") option = "id,code" empty="..."/]
            [@b.textfields style="width:130px" names="questionnaireClazz.lesson.no;课程序号,questionnaireClazz.lesson.course.code;课程代码,questionnaireClazz.lesson.course.name;课程名称"/]
            [@b.textfield style="width:100px" name="teacher" label="教师姓名" /]
            [@b.select style="width:134px" name="questionnaireClazz.lesson.courseType.id" label="课程类别" items=courseTypes empty="..."/]
            [@b.select style="width:134px" name="questionnaireClazz.lesson.teachDepart.id" label="开课院系" items=departments empty="..."/]
            [#--[@b.select style="width:134px" name="questionnaireClazz.lesson.examMode.id" label="考核方式" items=examModes empty="..."/]--]
            [@b.select style="width:134px" name="questionnaireClazz.evaluateByTeacher" label="评教方式" items={'1':'教师评教','0':'课程评教'} empty="..."/]
            [#--[@b.textfield style="width:130px" name="startWeekFrom"  label="起始周" minRange=1 /]
            [@eams.numRange label="起始周"   name="startWeekFrom,startWeekEnd" style="width:50px" minRange=1 /]
            [@eams.numRange label="上课人数" name="stdCountFrom,stdCountEnd" style="width:50px" minRange=0/]--]
            [@b.field label='使用问卷']<select style="width:134px" name="questionnaire.id">
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
