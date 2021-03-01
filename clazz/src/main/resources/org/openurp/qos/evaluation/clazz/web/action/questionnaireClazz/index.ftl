[#ftl]
[@b.head/]
[@b.toolbar title='课程问卷' /]
<div class="search-container">
  <div class="search-panel">
        [@b.form action="!search" name="questionnaireClazzIndexForm" title="ui.searchForm" target="contentDiv" theme="search"]
            [@urp_base.semester  name="semester.id" label="学年学期" value=semester/]
            [@b.textfields names="questionnaireClazz.clazz.crn;课程序号,questionnaireClazz.clazz.course.code;课程代码,questionnaireClazz.clazz.course.name;课程名称"/]
            [@b.textfield name="teacher" label="教师姓名" /]
            [@b.select name="questionnaireClazz.clazz.courseType.id" label="课程类别" items=courseTypes empty="..."/]
            [@b.select name="questionnaireClazz.clazz.teachDepart.id" label="开课院系" items=departments empty="..."/]
            [@b.select name="questionnaireClazz.evaluateByTeacher" label="评教方式" items={'1':'教师评教','0':'课程评教'} empty="..."/]
            [@b.field label='使用问卷']
                      <select name="questionnaire.id">
                      <option value="-1" style="background:#F5EDDB">有问卷(所有)</option>
                      <option value="0" style="background:#F5EDDB">无问卷(所有)</option>
                      [#list questionnaires?if_exists as questionnaire]
                      <option value="${(questionnaire.id)!}">${(questionnaire.description)!}</option>
                      [/#list]
                </select>
            [/@]
        [/@]
  </div>
  <div class="search-list">
            [@b.div id="contentDiv" href="!search?semester.id=${(semester.id)!}" /]
  </div>
</div>
[@b.foot/]
