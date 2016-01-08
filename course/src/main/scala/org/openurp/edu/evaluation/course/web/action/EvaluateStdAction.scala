package org.openurp.edu.evaluation.course.web.action

import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.course.service.LessonFilterStrategyFactory
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.lesson.model.CourseTake
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.edu.evaluation.lesson.model.QuestionnaireLesson
import org.openurp.edu.evaluation.course.service.StdEvaluateSwitchService
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.lesson.model.Lesson
import org.openurp.edu.base.model.Student
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.evaluation.model.Option
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.openurp.hr.base.model.Staff
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.beangle.commons.collection.Collections
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.openurp.hr.base.model.Staff
import org.beangle.webmvc.api.view.View




class EvaluateStdAction extends RestfulAction[EvaluateResult]{
   
//  public List<Object[]> getLessonIdAndTeacherIdOfResult(Student student, Semester semester) {
//    List<Object[]> results = CollectUtils.newArrayList();
//    if (null != student && null != semester) {
//      String hql = "select evaluateResult.lesson.id,evaluateResult.teacher.id"
//          + " from org.openurp.edu.teach.evaluate.course.model.EvaluateResult evaluateResult"
//          + " where evaluateResult.student =:student and evaluateResult.lesson.semester =:semester";
//      OqlBuilder<Object[]> query = OqlBuilder.from(hql);
//      query.param("student", student);
//      query.param("semester", semester);
//      results = entityDao.search(query);
//    }
//    return results;
//  }

  def  getResultByStdIdAndLessonId(stdId:Long, lessonId:Long, teacherId:Long):EvaluateResult= {
    val query = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult")
    query.where("evaluateResult.student.id =:stdId", stdId);
    query.where("evaluateResult.lesson.id =:lessonId", lessonId);
    if (0 != teacherId) {
      query.where("evaluateResult.staff.id =:teacherId", teacherId);
    } else {
      query.where("evaluateResult.staff is null");
    }
    val result = entityDao.search(query);
    
    if(result.size > 0 ) result.head  else null.asInstanceOf[EvaluateResult];
  }
  
  def  getLessonIdAndTeacherIdOfResult(student:Student, semester:Semester):collection.Map[String,String]={
    val query =OqlBuilder.from(classOf[EvaluateResult],"evaluateResult")
//    query.select("evaluateResult.lesson.id,evaluateResult.staff.id")
    query.where("evaluateResult.student = :student ",student)
    query.where("evaluateResult.lesson.semester = :semester",semester)
    val a = entityDao.search(query)
    a.map ( obj => (obj.lesson.id + "_" + (if (null == obj.staff) "0" else obj.staff.id),"1" )).toMap
  }
  
  
    def getStdLessons(student: Student, semester: Semester): Seq[Lesson] = {
      
      val query = OqlBuilder.from(classOf[CourseTake], "courseTake")
      query.select("distinct courseTake.lesson.id ")
      query.where("courseTake.std=:std", student)
      query.where("courseTake.semester =:semester", semester)
      val lessonIds = entityDao.search(query)
      var stdLessons: Seq[Lesson] = Seq()
      if (!lessonIds.isEmpty) {
        val entityquery = OqlBuilder.from(classOf[Lesson], "lesson").where("lesson.id in (:lessonIds)", lessonIds)
        stdLessons = entityDao.search(entityquery)
      }    
      stdLessons
    }

  var  lessonFilterStrategyFactory:LessonFilterStrategyFactory=_



  var  evaluateSwitchService:StdEvaluateSwitchService=_

  override def indexSetting(): Unit = {
    val std = entityDao.get(classOf[Student],68285L)
    if (std==null) { forward("error.std.stdNo.needed") }

    forward();
  }

  
  override def search(): String = {
    val std = entityDao.get(classOf[Student],68285L)
    val semesterId = 20141
    val semester = entityDao.get(classOf[Semester],semesterId)
    val lessonList = getStdLessons(std,semester)
    // 获得(课程问卷,根据学生,根据教学任务)
    var myLessons:Seq[QuestionnaireLesson] = Seq();
    if (!lessonList.isEmpty) {
      val query = OqlBuilder.from(classOf[QuestionnaireLesson],"questionnaireLesson")
      query.where("questionnaireLesson.lesson in (:lessonList)", lessonList)
      val myquestionnaires=entityDao.search(query)
      myLessons = myquestionnaires
    }
    // 获得(评教结果,根据学生)
    val evaluateMap =getLessonIdAndTeacherIdOfResult(std, semester);
//    for (Object[] objects : evaluateLessonIds) {
//      evaluateMap.put(objects[0] + "_" + (null == objects[1] ? "0" : objects[1]), "1");
//    }
    put("evaluateMap", evaluateMap);
    put("questionnaireLessons", myLessons);
    forward();
  }


  /**
   * 跳转(问卷页面)
   */
  def  loadQuestionnaire():String= {
    val evaluateId = get("evaluateId").get
    val evaluateState = get("evaluateState").get
    val ids = get("evaluateId").get.split(",")
    // 获得(教学任务)
    val lesson = entityDao.get(classOf[Lesson],ids(0).toLong)
    if (null == lesson) {
      addMessage("找不到该课程!");
      return forward("errors");
    }
    val evaluateSwitch = evaluateSwitchService.getEvaluateSwitch(lesson.semester,lesson.project)
    if (null == evaluateSwitch) {
      addMessage("现在还没有开放课程评教!");
      return forward("errors");
    }
//    if (!evaluateSwitch.checkOpen(new Date())) {
//      addMessage("不在课程评教开放时间内,开放时间为：!" + evaluateSwitch.beginAt + "～" + evaluateSwitch.endAt);
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
    // 获得(课程问卷,根据教学任务)
    val questionnaireLessons = entityDao.findBy(classOf[QuestionnaireLesson],"lesson.id",List(lesson.id));
    if (questionnaireLessons.isEmpty) {
      addMessage("缺失评教问卷!");
      return forward("errors");
    }
    
    val questionnaireLesson =questionnaireLessons.head
    var questions = questionnaireLesson.questionnaire.questions
      questions.sortWith((x, y) => x.priority < y.priority)
    
    // 获得(教师列表,根据学生教学任务)
    val teachers = Collections.newBuffer[Staff]
    if (questionnaireLesson.evaluateByTeacher) {
      val teacher = entityDao.get(classOf[Staff], ids(1).toLong)
      teachers += teacher
    } else {
      teachers ++= lesson.teachers
    }

    // 判断(是否更新)
    if ("update".equals(evaluateState)) {
      var teacherId:Long = 0;
      if (questionnaireLesson.evaluateByTeacher) {
        teacherId = ids(1).toLong
      }
//      val std = getLoginStudent();
      val std=entityDao.get(classOf[Student],68285L)
      val evaluateResult = getResultByStdIdAndLessonId(std.id,lesson.id, teacherId);
      if (null == evaluateResult) {
        addMessage("error.dataRealm.insufficient");
        forward("errors");
      }
      // 组装(问题结果)
      val questionMap = evaluateResult.questionResults.map(q => (q.question.id.toString,q.option.id)).toMap
      put("questionMap", questionMap);
      put("evaluateResult", evaluateResult);
    }
    
    put("lesson", lesson);
    put("teachers", teachers);
    put("questions", questions);
    //questionnaire = entityDao.get(classOf[Questionnaire], questionnaireLesson.questionnaire.id);
    put("questionnaire", questionnaireLesson.questionnaire);
    put("evaluateState", evaluateState);
    forward();
  }

 override def  save() :View = {
    val std = entityDao.get(classOf[Student],68285L)
    // 页面参数 
    val lessonId = getLong("lesson.id").get
    var teacherId = getLong("teacherId").get
    val semesterId = 20141
    val teacherIds = longIds("teacher")
    // 获得(课程问卷,根据教学任务)
    val query = OqlBuilder.from(classOf[QuestionnaireLesson], "questionnaireLesson");
    query.where("questionnaireLesson.lesson.id =:lessonId", lessonId);
    val questionnaireLessons = entityDao.search(query);
    if (questionnaireLessons.isEmpty) {
      addMessage("field.evaluate.questionnaire");
      forward("errors");
    }
    val questionnaireLesson = questionnaireLessons.head
    // 查询(评教结果)
    var evaluateResults :Seq[EvaluateResult]=Seq()
    val queryResult = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult");
    queryResult.where("evaluateResult.lesson.id =:lessonId", lessonId);
    // queryResult.where("evaluateResult.lesson.semester.id =:semesterId",
    // semesterId);
    queryResult.where("evaluateResult.student =:std", std);
    // 判断(课程评教VS教师评教)
    if (getLong("teacherId").get==0L) {
      evaluateResults = entityDao.search(queryResult);
    } else if (!questionnaireLesson.evaluateByTeacher) {
      evaluateResults = entityDao.search(queryResult);
    } else {
//      teacherId = getLong("teacherId").get
      queryResult.where("evaluateResult.staff.id =:teacherId", teacherId);
      evaluateResults = entityDao.search(queryResult);
    }

    var lesson :Lesson= null;
    var teacher:Staff = null;
    try {
      // 判断(更新VS保存)
      if (evaluateResults.size > 0) {
        val evaluateResult = evaluateResults.head
        lesson = evaluateResult.lesson
        teacher = evaluateResult.staff
        // 修改(问题选项)
       val questionResults = evaluateResult.questionResults
        val questions = questionnaireLesson.questionnaire.questions
        // 判断(是否添加问题)
        val oldQuestions = Collections.newBuffer[Question]
        questionResults foreach  { questionResult => 
           oldQuestions += questionResult.question
          }
//        for (QuestionResult questionResult : questionResults) {
//          oldQuestions+=questionResults.question);
//        }
        questions foreach { question =>
            if (!oldQuestions.contains(question)) {
            val optionId=getLong("select"+question.id).get
            val option = entityDao.get(classOf[Option], optionId);
            val questionResult = new QuestionResult()
            questionResult.questionType=question.questionType
            questionResult.question=question
            questionResult.option=option
            questionResult.result=evaluateResult
            evaluateResult.questionResults += questionResult
          }
        }
//        for (Question question : questions) {
//          if (!oldQuestions.contains(question)) {
//            Option option = entityDao.get(classOf[Option], getLong("select" + question.id));
//            QuestionResult questionResult = new QuestionResult(question, option);
//            evaluateResult.addQuestionResult(questionResult);
//          }
//        }
        // 重新赋值
        evaluateResult.remark = get("evaluateResult.remark").getOrElse("")
//        questionResults = evaluateResult.questionResults
        // 修改
        questionResults foreach {questionResult =>
          val question = questionResult.question
          val optionId = getLong("select" + question.id).get
          if (optionId == 0L) {
            questionResult.result=null
//            questionResults.remove(questionResult)
            entityDao.remove(questionResult);
//            continue;
          }
          if (!questionResult.option.id.equals(optionId)) {
            val option = entityDao.get(classOf[Option], optionId);
            questionResult.option = option
            questionResult.score = question.score * option.proportion.floatValue()
          }
        }
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
        entityDao.saveOrUpdate(questionResults);
      } else {
        lesson = entityDao.get(classOf[Lesson], lessonId);
        val teachers = entityDao.find(classOf[Staff], teacherIds);
        // 设置(教师,仅当页面教师唯一,作为文字评教对象)
        if (teachers.size == 1) {
          teacher = teachers.head
        }
        // 获得(问卷)
        val questionnaire = questionnaireLesson.questionnaire
        if (questionnaire == null || questionnaire.questions == null) {
          addMessage("评教问卷有误!");
          forward("errors");
        }
        var evaluateTeacher = teacher;
        if (!questionnaireLesson.evaluateByTeacher) {
          evaluateTeacher = null;
        }
        val evaluateResult = new EvaluateResult()
        evaluateResult.lesson=lesson
        evaluateResult.student=std
        evaluateResult.staff=evaluateTeacher
        questionnaire.questions foreach { question =>
           val optionId = getLong("select" + question.id).get
           val option = entityDao.get(classOf[Option], optionId);
          val questionResult = new QuestionResult()
          questionResult.question=question
          questionResult.option=option
          evaluateResult.questionnaire=questionnaire
          evaluateResult.questionResults += questionResult
          }
//        for (Question question : questionnaire.getQuestions()) {
//          Option option = entityDao.get(Option.class, getLong("select" + question.getId()));
//          QuestionResult questionResult = new QuestionResult(question, option);
//          evaluateResult.setQuestionnaire(questionnaire);
//          evaluateResult.addQuestionResult(questionResult);
//        }
        evaluateResult.remark=get("evaluateResult.remark").getOrElse("")
        entityDao.saveOrUpdate(evaluateResult)
      }
       redirect("search", "info.save.success", "&semester.id=" + 20141)
    } catch  {
      case e: Exception =>
      e.printStackTrace();
       redirect("search", "info.save.failure", "&semester.id=" + 20141);
    }
  }


}