[#ftl]
[@b.head/]
[@b.toolbar title="评教开关"]bar.addBack();[/@]
  [#assign sa][#if syllabus.persisted]!update?id=${syllabus.id}[#else]!save[/#if][/#assign]
    [@b.form action=sa theme="list"  enctype="multipart/form-data"]
      [@b.select name="evaluateSwitch.semester.id" label="学年学期" items=semesters?sort_by("code") value =evaluateSwitch.semester empty="..." ]"/]
      [@b.select name="evaluateSwitch.questionnaire.id" label="问卷标题" items=questionnaires value =evaluateSwitch.questionnaire empty="..." ]"/]
      [@b.select name="evaluateSwitch.opened" label="开关状态" items={'true':'是','false':'否'} value =evaluateSwitch.opened empty="..." ]"/]
    [/@]
[@b.foot/]
