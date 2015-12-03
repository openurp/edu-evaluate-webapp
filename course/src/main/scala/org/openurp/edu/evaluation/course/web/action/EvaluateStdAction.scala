package org.openurp.edu.evaluation.course.web.action

class EvaluateStdAction {
//  var  lessonFilterStrategyFactory:LessonFilterStrategyFactory=_
//
//  var  lessonService:LessonService=_
//
//  var  evaluateResultService:EvaluateResultService=_
//
//  var  evaluateSwitchService:StdEvaluateSwitchService=_
//
//  @Override
//  public String innerIndex() {
//    Student std = getLoginStudent();
//    if (null == std) { return forwardError("error.std.stdNo.needed"); }
//
//    return forward();
//  }
//
//  @Override
//  public String search() {
//    Student std = getLoginStudent();
//    Integer semesterId = getInt("semester.id");
//    Semester semester = entityDao.get(Semester.class, semesterId);
//    List<Lesson> lessonList = lessonService.getLessonByCategory(std.getId(),
//        lessonFilterStrategyFactory.getLessonFilterCategory(LessonFilterStrategy.STD),
//        Collections.singleton(semester));
//    // 获得(课程问卷,根据学生,根据教学任务)
//    List<QuestionnaireLesson> myLessons = CollectUtils.newArrayList();
//    if (CollectUtils.isNotEmpty(lessonList)) {
//      OqlBuilder<QuestionnaireLesson> query = OqlBuilder.from(QuestionnaireLesson.class,
//          "questionnaireLesson");
//      query.where("questionnaireLesson.lesson in (:lessonList)", lessonList);
//      myLessons = entityDao.search(query);
//    }
//    // 获得(评教结果,根据学生)
//    Map<String, String> evaluateMap = CollectUtils.newHashMap();
//    List<Object[]> evaluateLessonIds = evaluateResultService.getLessonIdAndTeacherIdOfResult(std, semester);
//    for (Object[] objects : evaluateLessonIds) {
//      evaluateMap.put(objects[0] + "_" + (null == objects[1] ? "0" : objects[1]), "1");
//    }
//    put("evaluateMap", evaluateMap);
//    put("questionnaireLessons", myLessons);
//    return forward();
//  }
//
//  /**
//   * 跳转(问卷页面)
//   */
//  public String loadQuestionnaire() {
//    String evaluateId = get("evaluateId");
//    String evaluateState = get("evaluateState");
//    Long[] ids = Strings.splitToLong(evaluateId);
//    // 获得(教学任务)
//    Lesson lesson = entityDao.get(Lesson.class, ids[0]);
//    if (null == lesson) {
//      addMessage("找不到该课程!");
//      return forward("errors");
//    }
//    EvaluateSwitch evaluateSwitch = evaluateSwitchService.getEvaluateSwitch(lesson.getSemester(),
//        lesson.getProject());
//    if (null == evaluateSwitch) {
//      addMessage("现在还没有开放课程评教!");
//      return forward("errors");
//    }
//    if (!evaluateSwitch.checkOpen(new Date())) {
//      addMessage("不在课程评教开放时间内,开放时间为：!" + evaluateSwitch.getOpenAt() + "～" + evaluateSwitch.getCloseAt());
//      return forward("errors");
//    }
//    OqlBuilder<NotEvaluateStudentBean> que = OqlBuilder.from(NotEvaluateStudentBean.class, "notevaluate");
//    que.where("notevaluate.std=:std", this.getLoginStudent());
//    que.where("notevaluate.semester=:semesterId", lesson.getSemester());
//    List<NotEvaluateStudentBean> notList = entityDao.search(que);
//    if (notList.size() > 0) {
//      addMessage("您并非参评学生，不可评教!");
//      return forward("errors");
//    }
//    // 获得(课程问卷,根据教学任务)
//    List<QuestionnaireLesson> questionnaireLessons = entityDao.get(QuestionnaireLesson.class, "lesson.id",
//        lesson.getId());
//    if (questionnaireLessons.isEmpty()) {
//      addMessage("缺失评教问卷!");
//      return forward("errors");
//    }
//    QuestionnaireLesson questionnaireLesson = questionnaireLessons.get(0);
//    Questionnaire questionnaire = questionnaireLesson.getQuestionnaire();
//    List<Question> questions = CollectUtils.newArrayList();
//    questions.addAll(questionnaire.getQuestions());
//    Collections.sort(questions);
//    // 获得(教师列表,根据学生教学任务)
//    List<Staff> teachers = CollectUtils.newArrayList();
//    if (questionnaireLesson.isEvaluateByTeacher()) {
//      Staff teacher = entityDao.get(Staff.class, ids[1]);
//      teachers.add(teacher);
//    } else {
//      teachers.addAll(lesson.getTeachers());
//    }
//
//    // 判断(是否更新)
//    if ("update".equals(evaluateState)) {
//      Long teacherId = null;
//      if (questionnaireLesson.isEvaluateByTeacher()) {
//        teacherId = ids[1];
//      }
//      Student std = getLoginStudent();
//      EvaluateResult evaluateResult = evaluateResultService.getResultByStdIdAndLessonId(std.getId(),
//          lesson.getId(), teacherId);
//      if (null == evaluateResult) {
//        addMessage("error.dataRealm.insufficient");
//        return forward("errors");
//      }
//      // 组装(问题结果)
//      Map<String, Long> questionMap = CollectUtils.newHashMap();
//      for (QuestionResult questionResult : evaluateResult.getQuestionResultSet()) {
//        questionMap.put(questionResult.getQuestion().getId().toString(), questionResult.getOption().getId());
//      }
//      put("questionMap", questionMap);
//      put("evaluateResult", evaluateResult);
//    }
//    put("lesson", lesson);
//    put("teachers", teachers);
//    put("questions", questions);
//    questionnaire = entityDao.get(Questionnaire.class, questionnaireLesson.getQuestionnaire().getId());
//    put("questionnaire", questionnaire);
//    put("evaluateState", evaluateState);
//    return forward();
//  }
//
//  @Override
//  public String save() throws Exception {
//    Student std = getLoginStudent();
//    // 页面参数
//    Long lessonId = getLong("lesson.id");
//    Long teacherId = getLong("teacherId");
//    Integer semesterId = getInt("semester.id");
//    Long[] teacherIds = getLongIds("teacher");
//    // 获得(课程问卷,根据教学任务)
//    OqlBuilder<QuestionnaireLesson> query = OqlBuilder.from(QuestionnaireLesson.class, "questionnaireLesson");
//    query.where("questionnaireLesson.lesson.id =:lessonId", lessonId);
//    List<QuestionnaireLesson> questionnaireLessons = entityDao.search(query);
//    if (questionnaireLessons.isEmpty()) {
//      addMessage("field.evaluate.questionnaire");
//      return forward("errors");
//    }
//    QuestionnaireLesson questionnaireLesson = questionnaireLessons.get(0);
//    // 查询(评教结果)
//    List<EvaluateResult> evaluateResults = CollectUtils.newArrayList();
//    OqlBuilder<EvaluateResult> queryResult = OqlBuilder.from(EvaluateResult.class, "evaluateResult");
//    queryResult.where("evaluateResult.lesson.id =:lessonId", lessonId);
//    // queryResult.where("evaluateResult.lesson.semester.id =:semesterId",
//    // semesterId);
//    queryResult.where("evaluateResult.student =:std", std);
//    // 判断(课程评教VS教师评教)
//    if (null == getLong("teacherId")) {
//      evaluateResults = entityDao.search(queryResult);
//    } else if (!questionnaireLesson.isEvaluateByTeacher()) {
//      evaluateResults = entityDao.search(queryResult);
//    } else {
//      teacherId = getLong("teacherId");
//      queryResult.where("evaluateResult.teacher.id =:teacherId", teacherId);
//      evaluateResults = entityDao.search(queryResult);
//    }
//
//    Lesson lesson = null;
//    Staff teacher = null;
//    try {
//      // 判断(更新VS保存)
//      if (evaluateResults.size() > 0) {
//        EvaluateResult evaluateResult = evaluateResults.get(0);
//        lesson = evaluateResult.getLesson();
//        teacher = evaluateResult.getStaff();
//        // 修改(问题选项)
//        Set<QuestionResult> questionResults = evaluateResult.getQuestionResultSet();
//        Set<Question> questions = questionnaireLesson.getQuestionnaire().getQuestions();
//        // 判断(是否添加问题)
//        Set<Question> oldQuestions = CollectUtils.newHashSet();
//        for (QuestionResult questionResult : questionResults) {
//          oldQuestions.add(questionResult.getQuestion());
//        }
//        for (Question question : questions) {
//          if (!oldQuestions.contains(question)) {
//            Option option = entityDao.get(Option.class, getLong("select" + question.getId()));
//            QuestionResult questionResult = new QuestionResult(question, option);
//            evaluateResult.addQuestionResult(questionResult);
//          }
//        }
//        // 重新赋值
//        evaluateResult.setRemark(get("evaluateResult.remark"));
//        questionResults = evaluateResult.getQuestionResultSet();
//        // 修改
//        for (QuestionResult questionResult : questionResults) {
//          Question question = questionResult.getQuestion();
//          Long optionId = getLong("select" + question.getId());
//          if (optionId == null) {
//            questionResult.setResult(null);
//            questionResults.remove(questionResult);
//            entityDao.remove(questionResult);
//            continue;
//          }
//          if (!questionResult.getOption().getId().equals(optionId)) {
//            Option option = entityDao.get(Option.class, optionId);
//            questionResult.setOption(option);
//            questionResult.setScore(question.getScore() * option.getProportion().floatValue());
//          }
//        }
//        entityDao.saveOrUpdate(questionResults);
//      } else {
//        lesson = entityDao.get(Lesson.class, lessonId);
//        List<Staff> teachers = entityDao.get(Staff.class, teacherIds);
//        // 设置(教师,仅当页面教师唯一,作为文字评教对象)
//        if (teachers.size() == 1) {
//          teacher = teachers.get(0);
//        }
//        // 获得(问卷)
//        Questionnaire questionnaire = questionnaireLesson.getQuestionnaire();
//        if (questionnaire == null || questionnaire.getQuestions() == null) {
//          addMessage("评教问卷有误!");
//          return forward("errors");
//        }
//        Staff evaluateTeacher = teacher;
//        if (!questionnaireLesson.isEvaluateByTeacher()) {
//          evaluateTeacher = null;
//        }
//        EvaluateResult evaluateResult = new EvaluateResult(lesson, std, evaluateTeacher);
//        for (Question question : questionnaire.getQuestions()) {
//          Option option = entityDao.get(Option.class, getLong("select" + question.getId()));
//          QuestionResult questionResult = new QuestionResult(question, option);
//          evaluateResult.setQuestionnaire(questionnaire);
//          evaluateResult.addQuestionResult(questionResult);
//        }
//        evaluateResult.setRemark(get("evaluateResult.remark"));
//        entityDao.saveOrUpdate(evaluateResult);
//      }
//      return redirect("search", "info.action.success", "&semester.id=" + lesson.getSemester().getId());
//    } catch (Exception e) {
//      e.printStackTrace();
//      return redirect("search", "info.action.failure", "&semester.id=" + lesson.getSemester().getId());
//    }
//  }
//
//  public void setLessonService(LessonService lessonService) {
//    this.lessonService = lessonService;
//  }
//
//  public void setEvaluateResultService(EvaluateResultService evaluateResultService) {
//    this.evaluateResultService = evaluateResultService;
//  }
//
//  public void setEvaluateSwitchService(EvaluateSwitchService evaluateSwitchService) {
//    this.evaluateSwitchService = evaluateSwitchService;
//  }
//
//  public void setLessonFilterStrategyFactory(LessonFilterStrategyFactory lessonFilterStrategyFactory) {
//    this.lessonFilterStrategyFactory = lessonFilterStrategyFactory;
//  }
//}
}