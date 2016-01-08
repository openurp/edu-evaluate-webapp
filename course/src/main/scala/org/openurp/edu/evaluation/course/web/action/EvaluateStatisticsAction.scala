package org.openurp.edu.evaluation.course.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.lesson.stat.model.LessonEvalStat
import org.openurp.base.model.Semester
import org.springframework.beans.support.PropertyComparator
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.beangle.data.dao.OqlBuilder
import org.openurp.base.model.Department
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.beangle.commons.collection.Collections
import org.openurp.hr.base.model.Staff
import org.openurp.edu.evaluation.model.Question
import java.util.ArrayList
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.evaluation.model.QuestionType
import org.openurp.edu.lesson.model.Lesson
import org.beangle.webmvc.api.annotation.response
import org.beangle.webmvc.api.action.ServletSupport
import javax.servlet.ServletResponse

class EvaluateStatisticsAction extends RestfulAction[LessonEvalStat] with ServletSupport{

  var  list1:Seq[Array[Any]]=Seq()
  var  list2:Seq[Array[Any]]=Seq()
  var questionLists:List[Question]=List()
  var evaluateSMaps = Collections.newMap[String,Float]
  var evaluateTMaps =Collections.newMap[String,Float]
  var numTeaMaps =Collections.newMap[String,String]
//  /** 全校平均分 */
 var  evaResults:Float=_
 var  semest:Semester=_
 var  questis:Questionnaire=_
//
  override def  index():String= {
//    getSemester();
    val date = new java.util.Date()
    /** 本学期是否评教 */
    val builder = OqlBuilder.from[Questionnaire](classOf[EvaluateResult].getName, "evaluateResult");
    val semesters = entityDao.get(classOf[Semester],20141)
    ;
    if (semesters != null) {
      builder.where("evaluateResult.lesson.semester.id=:ids", semesters.id);
    }
    builder.select("distinct evaluateResult.questionnaire");
    val list = entityDao.search(builder);
//    Collections.sort(list, new PropertyComparator("id"));
    put("questionnaires", list);
    val departs = entityDao.search(OqlBuilder.from(classOf[Department],"dep").where("dep.teaching =:tea",true))
    put("departmentList", departs);
    put("semester",semesters)
    forward();
  }

  /** 学院与教师评教结果统计 */
  override def  search():String ={
//    val semesterId = getInt("semester.id").get
    val semesterId=20141
    val departmentId = getInt("department.id").get
    var questionnaireId = 1L;
    if (getLong("questionnaire.id").get != 0L  ) {
      questionnaireId = getLong("questionnaire.id").get
    }
    /** 本学期是否评教 */
    val builder = OqlBuilder.from[Questionnaire](classOf[EvaluateResult].getName, "evaluateResult");
    builder.where("evaluateResult.lesson.semester.id=" + semesterId);
    builder.where("evaluateResult.questionnaire.id=" + questionnaireId);
    builder.select("distinct questionnaire");
    val list = entityDao.search(builder);
    var questionList :List[Question] =List()
    if (list.size > 0) {
     list foreach { questionnaire =>
        questis = questionnaire
        questionList ++= questionnaire.questions
      }
    } else {
      redirect("search","未找到评教记录!")
    }
    put("questionList", questionList);
    if (semesterId != 0) {
      semest = entityDao.get(classOf[Semester], semesterId);
    }
    put("semester", semest);
    var searchTypes = 1;
    if (getInt("searchTypes") != null) {
      searchTypes = getInt("searchTypes").get
    }
    if (searchTypes == 1) {
      /** 院系评教总分 */
      val query = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"+
          classOf[QuestionResult].getName + " questionResult," + classOf[Department].getName + " department");
      query.select("department.id,department.name,sum(questionResult.score)/count(distinct evaluateResult.id),count(distinct evaluateResult.id)");
      query.where("evaluateResult.id=questionResult.result.id and evaluateResult.department.id=department.id");
      query.where("evaluateResult.lesson.semester.id=" + semesterId);
      if (departmentId != 0) {
        query.where("department.id=" + departmentId.toString());
      }
      query.groupBy("department.id,department.name");
      query.orderBy("sum(questionResult.score)/count(distinct evaluateResult.id) desc");
      list1 = entityDao.search(query);
      put("evaluateStasList", list1);
      /** 院系各项得分 */
      val evaluateSs = Collections.newMap[String, Float]
      val query1 = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"+ classOf[QuestionResult].getName + " questionResult");
      query1.select("evaluateResult.department.id,questionResult.question.id,sum(questionResult.score)/count(evaluateResult.id)");
      query1.where("evaluateResult.id=questionResult.result.id");
      query1.where("evaluateResult.lesson.semester.id=" + semesterId);
      if (departmentId != 0) {
        query1.where("evaluateResult.department.id=" + departmentId.toString());
      }
      query1.groupBy("questionResult.question.id,evaluateResult.department.id");
      query1.orderBy("evaluateResult.department.id,questionResult.question.id");
      val lits = entityDao.search(query1);
      lits foreach { ob =>
        evaluateSs.put(ob(0).toString()+"_"+ob(1).toString(),ob(2).toString().toFloat)
      }
//      for (Iterator<?> iter = lits.iterator(); iter.hasNext();) {
//        Object[] ob = (Object[]) iter.next();
//        if (ob.length > 0) {
//          String strs = ob[0].toString() + "_" + ob[1].toString();
//          evaluateSs.put(strs, Float.valueOf(ob[2].toString()));
//        }
//      }
      evaluateSMaps = evaluateSs;
      put("evaluateSMaps", evaluateSMaps);
      val que = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
          + classOf[QuestionResult].getName + " questionResult");
      que.select("sum(questionResult.score)/count(distinct evaluateResult.id)");
      que.where("evaluateResult.id=questionResult.result.id");
      que.where("evaluateResult.lesson.semester.id=" + semesterId);
      evaResults = entityDao.search(que)(0).toString().toFloat
      put("evaluateResults", evaResults);
      forward();
    } else if (searchTypes == 2) {
      this.teacherEvaluate();
      forward("teacherEvaluate");
    } else if (searchTypes == 3) {
      this.lessonEvaluate();
      forward("lessonEvaluate");
    } else if (searchTypes == 4) {
      this.courseTypeEvaluate(true);
      forward("courseTypeEvaluate");
    } else if (searchTypes == 5) {
      this.courseTypeEvaluate(false);
      forward("courseTypeEvaluate");
    } else if (searchTypes == 6) {
      this.evaluateHistorys();
      forward("evaluateHistorys");
    } else if (searchTypes == 7) {
      this.stuEvaluateResults();
      forward("stuEvaluateResults");
    }else{
      forward();
    }
  }

  def  teacherEvaluate():String= {
//    val semesterId = getInt("semester.id").get
    val semesterId=20141
    val departmentId = getInt("department.id").get
    var id = 1L;
    if (getLong("questionnaire.id") != null) {
      id = getLong("questionnaire.id").get
    }
    val questions =Collections.newBuffer[Question]
    val questionnaire = entityDao.get(classOf[Questionnaire], id);
    questions ++= questionnaire.questions
    questionLists = questions.toList
    put("questionList", questionLists);
    /** 本学期是否评教 */
    val builder = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult");
    builder.where("evaluateResult.lesson.semester.id=" + semesterId);
    builder.where("evaluateResult.questionnaire.id=:idd", id);
    val list = entityDao.search(builder);
    if (semesterId != 0) {
      semest = entityDao.get(classOf[Semester], semesterId);
    }
    put("semester", semest);
    /** 教师评教总分 */
    val quer = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult," + classOf[Staff].getName + " tea");
    quer.join("evaluateResult.lesson.teachers", "teach");
    quer.select("tea.code,tea.person.name.formatedName,sum(questionResult.score)/count(distinct evaluateResult.id),count(distinct evaluateResult.id)");
    quer.where("teach.id = tea.id");
//    quer.where("tea.teaching is true");
    quer.where("evaluateResult.id=questionResult.result.id ");
    quer.where("evaluateResult.lesson.semester.id=" + semesterId);
    quer.where("evaluateResult.questionnaire.id=:ids", id);
    if (departmentId != 0) {
      quer.where("tea.state.department.id=" + departmentId.toString());
    }
    quer.groupBy("tea.code,tea.person.name.formatedName");
    quer.orderBy("sum(questionResult.score)/count(distinct evaluateResult.id) desc");
    list2 = entityDao.search(quer);
    val numMaps = Collections.newMap[String, String]
    var teaNums=1
    list2 foreach { obs =>
      teaNums +=1
      numMaps.put(obs(0).toString(),teaNums.toString())
    }
//    for (int i = 0; i < list2.size(); i++) {
//      Object[] obs = (Object[]) list2.get(i);
//      val teaNums = i + 1;
//      numMaps.put(obs[0].toString(), teaNums.toString());
//    }
    numTeaMaps = numMaps;
    put("evaluateTeaStasList", list2);
    /** 教师各项得分 */
    val evaluateRs = Collections.newMap[String, Float]
    val quer1 = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult," + classOf[Staff].getName + " tea");
    quer1.join("evaluateResult.lesson.teachers", "teach");
    quer1.select("tea.code,questionResult.question.id,sum(questionResult.score)/count(evaluateResult.id)");
    quer1.where("teach.id = tea.id");
//    quer1.where("tea.teaching is true");
    quer1.where("evaluateResult.id=questionResult.result.id");
    quer1.where("evaluateResult.lesson.semester.id=" + semesterId);
    quer1.where("evaluateResult.questionnaire.id=:ids", id);
    if (departmentId != 0) {
      quer1.where("tea.state.department.id=" + departmentId.toString());
    }
    quer1.groupBy("questionResult.question.id,tea.code");
    quer1.orderBy("tea.code,questionResult.question.id");
    val lists = entityDao.search(quer1);
    lists foreach { ob =>
      val strs = ob(0).toString() + "_" + ob(1).toString()
      evaluateRs.put(strs, ob(2).toString().toFloat);
    }
//    for (Iterator<?> iter = lists.iterator(); iter.hasNext();) {
//      Object[] ob = (Object[]) iter.next();
//      if (ob.length > 0) {
//        String strs = ob[0].toString() + "_" + ob[1].toString();
//        evaluateRs.put(strs, Float.valueOf(ob[2].toString()));
//      }
//    }
    evaluateTMaps = evaluateRs;
    put("evaluateRes", evaluateRs);
    questis = entityDao.get(classOf[Questionnaire], id);
    put("questionnaires", questis);
    val que = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult");
    que.select("sum(questionResult.score)/count(distinct evaluateResult.id)");
    que.where("evaluateResult.id=questionResult.result.id");
    que.where("evaluateResult.lesson.semester.id=" + semesterId);
    que.where("evaluateResult.questionnaire.id=:ids", id);
    evaResults = entityDao.search(que)(0).toString().toFloat
    put("evaluateResults", evaResults);
    forward();
  }

  def  teaEvaluateInfo():String = {
    val str = get("idStrs").get
    val strs = str.split(",");
    val teaId = strs(1).toLong
    val semesterId = strs(0).toInt
    val lessonId = strs(2).toLong
    /** 本学期是否评教 */
    val builder = OqlBuilder.from[Any](classOf[EvaluateResult].getName, "evaluateResult");
    builder.where("evaluateResult.lesson.semester.id=" + semesterId);
    builder.select("distinct evaluateResult.questionnaire.id");
    val list = entityDao.search(builder);
    if (list.size == 1) {
    } else {
      redirect("search","未找到评教记录!")
    }
    if (semesterId != 0) {
      semest = entityDao.get(classOf[Semester], semesterId);
    }
    put("semester", semest);
    var teacher:Staff = null;
    if (teaId != 0L) {
      teacher = entityDao.get(classOf[Staff], teaId);
    }
    put("teacher", teacher);
    var lesson:Lesson = null;
    if (lessonId != 0L) {
      lesson = entityDao.get(classOf[Lesson], lessonId);
    }
    put("lesson", lesson);
    /** 院系平均分 */
    val querdep = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult");
    querdep.select("sum(questionResult.score)/count(distinct evaluateResult.id)");
    querdep.where("evaluateResult.id=questionResult.result.id");
    querdep.where("evaluateResult.lesson.semester.id=" + semesterId);
    if (lesson != null) {
      querdep.where("evaluateResult.lesson.teachDepart.id=:depId", lesson.teachDepart.id);
      // querdep.where("evaluateResult.teacher.department.id=:depId",teacher.getDepartment().getId());
    }
    put("depScores", entityDao.search(querdep)(0).toString().toFloat);
    /** 全校平均分 */
    put("evaResults", evaResults);

    /** 课程全校评教排名 */
    val querSch = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult");
    querSch.select("evaluateResult.lesson.id,sum(questionResult.score)/count(distinct evaluateResult.id)");
    querSch.where("evaluateResult.id=questionResult.result.id");
    querSch.where("evaluateResult.lesson.semester.id=" + semesterId);
    querSch.groupBy("evaluateResult.lesson.id");
    querSch.orderBy("sum(questionResult.score)/count(distinct evaluateResult.id) desc");
    val schList = entityDao.search(querSch);
    var schNums = 0;
    schList foreach {ob =>
      if (ob(0).toString().equals(lessonId.toString())) {
        schNums += 1
    }
    }
//    for (int i = 0; i < schList.size(); i++) {
//      Object[] ob = (Object[]) schList.get(i);
//      if (ob[0].toString().equals(lessonId.toString())) {
//        schNums = i + 1;
//      }
//    }
    put("schNum", schNums);
    put("schNums", schList.size);
    /** 课程院系评教排名 */
    val querydep = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult");
    querydep.select("evaluateResult.lesson.id,sum(questionResult.score)/count(distinct evaluateResult.id)");
    querydep.where("evaluateResult.id=questionResult.result.id");
    querydep.where("evaluateResult.lesson.semester.id=" + semesterId);
    if (lesson != null) {
      querdep.where("evaluateResult.lesson.teachDepart.id=:depId", lesson.teachDepart.id);
      // querdep.where("evaluateResult.teacher.department.id=:depId",teacher.getDepartment().getId());
    }
    querydep.groupBy("evaluateResult.lesson.id");
    querydep.orderBy("sum(questionResult.score)/count(distinct evaluateResult.id) desc");
    val depList = entityDao.search(querydep);
    var depNums = 0;
    depList foreach { ob =>
       if (ob(0).toString().equals(lessonId.toString())) {
        depNums += 1
      }
    }
//    for (int i = 0; i < depList.size(); i++) {
//      Object[] ob = (Object[]) depList.get(i);
//      if (ob[0].toString().equals(lessonId.toString())) {
//        depNums = i + 1;
//      }
//    }
    put("depNum", depNums);
    put("depNums", depList.size);
    /** 教师评教总分 */
    val quer = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult");
    quer.select("sum(questionResult.score)/count(distinct evaluateResult.id)");
    quer.where("evaluateResult.id=questionResult.result.id");
    quer.where("evaluateResult.lesson.semester.id=" + semesterId);

    if (teaId != 0L) {
      quer.where("evaluateResult.teacher.id=:teaId", teaId);
    }
    if (lessonId != 0L) {
      quer.where("evaluateResult.lesson.id=:lessonId", lessonId);
    }
    put("teaScore", entityDao.search(quer)(0));

    /** 教师各项得分 */
    val quer1 = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult");
    quer1
        .select("questionResult.question.content,questionResult.question.id,sum(questionResult.score)/count(evaluateResult.id)");
    quer1.where("evaluateResult.id=questionResult.result.id");
    quer1.where("evaluateResult.lesson.semester.id=" + semesterId);
    if (teaId != 0L) {
      quer1.where("evaluateResult.teacher.id=:teaId", teaId);
    }
    if (lessonId != 0L) {
      quer1.where("evaluateResult.lesson.id=:lessonId", lessonId);
    }
    quer1.groupBy("questionResult.question.id,questionResult.question.content");
    // quer1.orderBy("questionResult.question.priority desc");
    put("questionRList", entityDao.search(quer1));
    /** 院系各项得分 */
   val  depQuery = OqlBuilder.from(classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult");
    depQuery.select("questionResult.question.id,sum(questionResult.score)/count(evaluateResult.id)");
    depQuery.where("evaluateResult.id=questionResult.result.id");
    depQuery.where("evaluateResult.lesson.semester.id=" + semesterId);
    if (lesson != null) {
      depQuery.where("evaluateResult.lesson.teachDepart.id=:depsId", lesson.teachDepart.id);
    }
    depQuery.groupBy("questionResult.question.id");
    depQuery.orderBy("questionResult.question.id");
    put("depQRList", entityDao.search(depQuery));
    /** 全校各项得分 */
    val schQuery = OqlBuilder.from(classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult");
    schQuery.select("questionResult.question.id,sum(questionResult.score)/count(evaluateResult.id)");
    schQuery.where("evaluateResult.id=questionResult.result.id");
    schQuery.where("evaluateResult.lesson.semester.id=" + semesterId);
    schQuery.groupBy("questionResult.question.id");
    schQuery.orderBy("questionResult.question.id");
    put("schQRList", entityDao.search(schQuery));
     forward();
  }

  def lessonEvaluate() {
//    val semesterId = getInt("semester.id").get
    val semesterId=20141
    val departmentId = getInt("department.id").get
    val questionnaireId = getLong("questionnaire.id");
    /** 得到本学期的唯一问卷 */
    val  builder = OqlBuilder.from[Questionnaire](classOf[EvaluateResult].getName, "evaluateResult");
    builder.where("evaluateResult.lesson.semester.id=" + semesterId);
    builder.select("distinct questionnaire");
    val list = entityDao.search(builder);
    val questionList = Collections.newBuffer[Question]
    if (list.size > 0) {
      list foreach {questionnaire =>
        questionList ++=questionnaire.questions
      }
//      for (int i = 0; i < list.size(); i++) {
//        Questionnaire questionnaire = (Questionnaire) list.get(i);
//        questionList.addAll(questionnaire.getQuestions());
//      }
    } else {
      // return redirect("search","未找到评教记录!")
    }
    put("questionList", questionList);
    if (semesterId != 0) {
      semest = entityDao.get(classOf[Semester], semesterId);
    }
    put("semester", semest);
    /** 教师评教总分 */
    val  quer = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult," + classOf[Staff].getName + " teacher");
    quer.select("teacher.code,teacher.person.name.formatedName,evaluateResult.lesson.id,evaluateResult.lesson.course.name,sum(questionResult.score)/count(distinct evaluateResult.id),count(distinct evaluateResult.id),teacher.state.department.name,teacher.id");
    quer.join("evaluateResult.lesson.teachers", "tea");
    quer.where("tea.id = teacher.id");
//    quer.where("teacher.endOn is null");
    quer.where("evaluateResult.id=questionResult.result.id ");
    quer.where("evaluateResult.lesson.semester.id=" + semesterId);
    if (departmentId != 0) {
      quer.where("teacher.state.department.id=" + departmentId.toString());
    }
    quer.groupBy("teacher.code,teacher.person.name.formatedName,evaluateResult.lesson.id,evaluateResult.lesson.course.name,teacher.state.department.name,teacher.id");
    quer.orderBy("sum(questionResult.score)/count(distinct evaluateResult.id) desc");
    val liss = entityDao.search(quer);

    put("evaluateTeaStasList", entityDao.search(quer));
    val  queres = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult," + classOf[Staff].getName + " teacher");
    queres.select("teacher.code,teacher.person.name.formatedName,evaluateResult.lesson.id,evaluateResult.lesson.course.name,sum(questionResult.score)/count(distinct evaluateResult.id),count(distinct evaluateResult.id)");
    queres.join("evaluateResult.lesson.teachers", "tea");
    queres.where("tea.id = teacher.id");
//    queres.where("teacher.endOn is null");
    queres.where("evaluateResult.id=questionResult.result.id ");
    queres.where("evaluateResult.lesson.semester.id=" + semesterId);
    queres.groupBy("teacher.code,teacher.person.name.formatedName,evaluateResult.lesson.id,evaluateResult.lesson.course.name");
    queres.orderBy("sum(questionResult.score)/count(distinct evaluateResult.id) desc");
    val lits = entityDao.search(queres);
    val numMaps =Collections.newMap[String, String]
     var teaNums=1
     lits foreach {obs => 
       teaNums+= 1
        numMaps.put(obs(0).toString(), teaNums.toString());
       }
//    for (int i = 0; i < lits.size(); i++) {
//      Object[] obs = (Object[]) lits.get(i);
//      Integer teaNums = i + 1;
//      numMaps.put(obs[0].toString(), teaNums.toString());
//    }
    put("numTeaMaps", numMaps);
    /** 教师各项得分 */
    val evaluateRs = Collections.newMap[String, Float]
    val  quer1 = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult," + classOf[Staff].getName + " teacher");
    quer1.select("teacher.code,evaluateResult.lesson.id,questionResult.question.id,sum(questionResult.score)/count(evaluateResult.id)");
    quer1.join("evaluateResult.lesson.teachers", "tea");
    quer1.where("tea.id = teacher.id");
//    quer1.where("teacher.endOn is null");
    quer1.where("evaluateResult.id=questionResult.result.id");
    quer1.where("evaluateResult.lesson.semester.id=" + semesterId);
    if (departmentId != 0) {
      quer1.where("teacher.state.department.id=" + departmentId.toString());
    }
    quer1.groupBy("questionResult.question.id,teacher.code,evaluateResult.lesson.id");
    quer1.orderBy("teacher.code,questionResult.question.id");
    val lists = entityDao.search(quer1);
    lists foreach {ob =>
       if (ob.length > 0) {
        val strs = ob(0).toString() + "_" + ob(1).toString() + "_" + ob(2).toString();
        evaluateRs.put(strs, ob(3).toString().toFloat);
      }
    }
//    for (Iterator<?> iter = lists.iterator(); iter.hasNext();) {
//      Object[] ob = (Object[]) iter.next();
//      if (ob.length > 0) {
//        String strs = ob[0].toString() + "_" + ob[1].toString() + "_" + ob[2].toString();
//        evaluateRs.put(strs, Float.valueOf(ob[3].toString()));
//      }
//    }
    put("evaluateRes", evaluateRs);
    put("questionnaires", questis);
    put("evaluateResults", evaResults);

  }

  def  lessonTeaEvaluate():String = {
    val semesterId = getInt("semester.id").get
    val departmentId = getInt("department.id");
    val buil = OqlBuilder.from(classOf[QuestionType], "types");
    buil.where("types.state is true");
    put("questionTypes", entityDao.search(buil));
    if (semesterId != 0) {
      semest = entityDao.get(classOf[Semester], semesterId);
    }
    put("semester", semest);
    /** 教师评教总分 */
    val  quer = OqlBuilder.from(classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult," + classOf[Staff].getName + " teacher");
    quer.select("teacher.code,teacher.person.name.formatedName,evaluateResult.lesson.id,evaluateResult.lesson.course.name,sum(questionResult.score)/count(distinct evaluateResult.id),count(distinct evaluateResult.id)"
        + ",evaluateResult.lesson.no,evaluateResult.lesson.teachDepart.name,evaluateResult.lesson.course.code");
//    quer.where("teacher.endOn is null");
    quer.where("evaluateResult.id=questionResult.result.id and evaluateResult.teacher.code=teacher.code");
    quer.where("evaluateResult.lesson.semester.id=" + semesterId);
    if (departmentId != null) {
      quer.where("evaluateResult.lesson.teachDepart.id=" + departmentId.toString());
    }
    quer.groupBy("teacher.code,evaluateResult.lesson.id,teacher.person.name.formatedName,evaluateResult.lesson.course.name,evaluateResult.lesson.no,evaluateResult.lesson.teachDepart.name,evaluateResult.lesson.course.code");
    quer.orderBy("sum(questionResult.score)/count(distinct evaluateResult.id) desc");
    put("evaluateTeaStasList", entityDao.search(quer));
    System.out.println(quer);
    val  queres = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult," + classOf[Staff].getName + " teacher");
    queres.select("teacher.code,teacher.person.name.formatedName,evaluateResult.lesson.id,evaluateResult.lesson.course.name,sum(questionResult.score)/count(distinct evaluateResult.id),count(distinct evaluateResult.id)");
    queres.where("evaluateResult.id=questionResult.result.id and evaluateResult.teacher.code=teacher.code");
//    queres.where("teacher.endOn is null");
    queres.where("evaluateResult.lesson.semester.id=" + semesterId);
    queres.groupBy("teacher.code,evaluateResult.lesson.id,teacher.person.name.formatedName,evaluateResult.lesson.course.name");
    queres.orderBy("sum(questionResult.score)/count(distinct evaluateResult.id) desc");
    val lits = entityDao.search(queres);
   val  numMaps = Collections.newMap[String, String]
   var teaNums=1
   lits foreach { obs =>
     teaNums +=1
       numMaps.put(obs(0).toString(), teaNums.toString());
     }
//    for (int i = 0; i < lits.size(); i++) {
//      Object[] obs = (Object[]) lits.get(i);
//      Integer teaNums = i + 1;
//      numMaps.put(obs[0].toString(), teaNums.toString());
//    }
    put("numTeaMaps", numMaps);
    /** 教师各项得分 */
    val evaluateRs = Collections.newMap[String, Float]
    val  quer1 = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult");
    quer1.select("evaluateResult.teacher.code,evaluateResult.lesson.id,questionResult.question.type.id,sum(questionResult.score)/count(questionResult.question.id)");
    quer1.where("evaluateResult.id=questionResult.result.id");
    quer1.where("evaluateResult.lesson.semester.id=" + semesterId);
    if (departmentId != null) {
      quer1.where("evaluateResult.lesson.teachDepart.id=" + departmentId.toString());
    }
    quer1.groupBy("questionResult.question.type.id,evaluateResult.teacher.code,evaluateResult.lesson.id");
    quer1.orderBy("evaluateResult.teacher.code,questionResult.question.type.id");
    val lists = entityDao.search(quer1);
    lists foreach { ob =>
        if (ob.length > 0) {
        if (ob(0).toString().equals("3301")) {
          System.out.println(ob(3).toString());
        }
        val strs = ob(0).toString() + "_" + ob(1).toString() + "_" + ob(2).toString();
        evaluateRs.put(strs, ob(3).toString().toFloat);
      }
      }
//    for (Iterator<?> iter = lists.iterator(); iter.hasNext();) {
//      Object[] ob = (Object[]) iter.next();
//      if (ob.length > 0) {
//        if (ob[0].toString().equals("3301")) {
//          System.out.println(ob[3].toString());
//        }
//        String strs = ob[0].toString() + "_" + ob[1].toString() + "_" + ob[2].toString();
//        evaluateRs.put(strs, Float.valueOf(ob[3].toString()));
//      }
//    }
    put("evaluateRes", evaluateRs);
    put("questionnaires", questis);
    put("evaluateResults", evaResults);
    forward();
  }

  def  lessonTeaEvaluateExport():String ={
    val semesterId = getInt("semester.id").get
    val departmentId = getInt("department.id");
    val  buil = OqlBuilder.from(classOf[QuestionType], "types");
    buil.where("types.state is true");
    put("questionTypes", entityDao.search(buil));
    if (semesterId != 0) {
      semest = entityDao.get(classOf[Semester], semesterId);
    }
    put("semester", semest);
    /** 教师评教总分 */
    val  quer = OqlBuilder.from(classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult," + classOf[Staff].getName + " teacher");
    quer.select("teacher.code,teacher.person.name.formatedName,evaluateResult.lesson.id,evaluateResult.lesson.course.name,sum(questionResult.score)/count(distinct evaluateResult.id),count(distinct evaluateResult.id)"
        + ",evaluateResult.lesson.no,evaluateResult.lesson.teachDepart.name,evaluateResult.lesson.course.code");
    quer.where("evaluateResult.id=questionResult.result.id and evaluateResult.teacher.code=teacher.code");
//    quer.where("teacher.endOn is null");
    quer.where("evaluateResult.lesson.semester.id=" + semesterId);
    if (departmentId != null) {
      quer.where("evaluateResult.lesson.teachDepart.id=" + departmentId.toString());
    }
    quer.groupBy("teacher.code,evaluateResult.lesson.id,teacher.person.name.formatedName,evaluateResult.lesson.course.name,evaluateResult.lesson.no,evaluateResult.lesson.teachDepart.name,evaluateResult.lesson.course.code");
    quer.orderBy("sum(questionResult.score)/count(distinct evaluateResult.id) desc");
    put("evaluateTeaStasList", entityDao.search(quer));
    val  queres = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult," + classOf[QuestionResult].getName + " questionResult," + classOf[Staff].getName + " teacher");
    queres.select("teacher.code,teacher.person.name.formatedName,evaluateResult.lesson.id,evaluateResult.lesson.course.name,sum(questionResult.score)/count(distinct evaluateResult.id),count(distinct evaluateResult.id)");
    queres.where("evaluateResult.id=questionResult.result.id and evaluateResult.teacher.code=teacher.code");
    queres.where("evaluateResult.lesson.semester.id=" + semesterId);
//    queres.where("teacher.endOn is null");
    queres.groupBy("teacher.code,evaluateResult.lesson.id,teacher.person.name.formatedName,evaluateResult.lesson.course.name");
    queres.orderBy("sum(questionResult.score)/count(distinct evaluateResult.id) desc");
    val lits = entityDao.search(queres);
    val numMaps = Collections.newMap[String, String]
    var teaNums=1
    lits foreach { obs =>
       teaNums += 1
        numMaps.put(obs(0).toString(), teaNums.toString());
    }
//    for (int i = 0; i < lits.size(); i++) {
//      Object[] obs = (Object[]) lits.get(i);
//      Integer teaNums = i + 1;
//      numMaps.put(obs[0].toString(), teaNums.toString());
//    }
    put("numTeaMaps", numMaps);
    /** 教师各项得分 */
    val evaluateRs = Collections.newMap[String, Float]
    val  quer1 = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult");
    quer1.select("evaluateResult.teacher.code,evaluateResult.lesson.id,questionResult.question.type.id,sum(questionResult.score)/count(evaluateResult.id)");
    quer1.where("evaluateResult.id=questionResult.result.id");
    quer1.where("evaluateResult.lesson.semester.id=" + semesterId);
    if (departmentId != null) {
      quer1.where("evaluateResult.lesson.teachDepart.id=" + departmentId.toString());
    }
    quer1.groupBy("questionResult.question.type.id,evaluateResult.teacher.code,evaluateResult.lesson.id");
    quer1.orderBy("evaluateResult.teacher.code,questionResult.question.type.id");
    val lists = entityDao.search(quer1);
    lists foreach { ob =>
      if (ob.length > 0) {
        val strs = ob(0).toString() + "_" + ob(1).toString() + "_" + ob(2).toString();
        evaluateRs.put(strs, ob(3).toString().toFloat);
      }
    }
//    for (Iterator<?> iter = lists.iterator(); iter.hasNext();) {
//      Object[] ob = (Object[]) iter.next();
//      if (ob.length > 0) {
//        String strs = ob[0].toString() + "_" + ob[1].toString() + "_" + ob[2].toString();
//        evaluateRs.put(strs, Float.valueOf(ob[3].toString()));
//      }
//    }
    put("evaluateRes", evaluateRs);
    put("questionnaires", questis);
    put("evaluateResults", evaResults);
    response.setContentType("application/vnd.ms-excel;charset=UTF-8")
    response.setHeader("Content-Disposition", "attachment;filename=evaluateResultsExport.xls")
    return forward();
  }

  def  evaluateResultsExport():String= {
    put("evaluateStasList", list1);
    put("evaluateTeaStasList", list2);
    put("evaluateSMaps", evaluateSMaps);
    put("evaluateTMaps", evaluateTMaps);
    put("questionnaires", questis);
    put("semester", semest);
    put("evaluateResults", evaResults);
    response.setContentType("application/vnd.ms-excel;charset=UTF-8")
    response.setHeader("Content-Disposition", "attachment;filename=evaluateResultsExport.xls")
    forward();
  }

  def  lessonEvaluateTwo():String= {
    this.lessonEvaluate();
     forward("lessonEvaluate");
  }

  def  evaluateTeaResultsExport():String= {
    put("evaluateStasList", list1);
    put("evaluateTeaStasList", list2);
    put("evaluateSMaps", evaluateSMaps);
    put("evaluateTMaps", evaluateTMaps);
    put("questionnaires", questis);
    put("semester", semest);
    put("evaluateResults", evaResults);
    put("questionList", questionLists);
    response.setContentType("application/vnd.ms-excel;charset=UTF-8")
    response.setHeader("Content-Disposition", "attachment;filename=evaluateTeaResultsExport.xls");
    forward();
  }

  def  courseTypeEvaluate(courseType:Boolean):String = {
//    val semesterId = getInt("semester.id").get
    val semesterId=20141
    val departmentId = getInt("department.id").get
    val questionnaireId = getLong("questionnaire.id").get
    val  buil = OqlBuilder.from(classOf[QuestionType], "types");
    buil.where("types.state is true");
    put("questionTypes", entityDao.search(buil));
    if (semesterId != 0) {
      semest = entityDao.get(classOf[Semester], semesterId);
    }
    put("semester", semest);
    /** 教师评教总分 */
    val  quer = OqlBuilder.from(classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult," + classOf[Staff].getName + " teacher");
    quer.select("teacher.code,teacher.person.name.formatedName,evaluateResult.lesson.id,evaluateResult.lesson.course.name,sum(questionResult.score)/count(distinct evaluateResult.id),count(distinct evaluateResult.id)"
        + ",evaluateResult.lesson.no,evaluateResult.lesson.teachDepart.name,evaluateResult.lesson.course.code");
    quer.join("evaluateResult.lesson.teachers", "tea");
//    quer.where("teacher.endOn is null");
    quer.where("evaluateResult.id=questionResult.result.id and tea.id=teacher.id");
    quer.where("evaluateResult.lesson.semester.id=" + semesterId);
    quer.where("evaluateResult.questionnaire.id=" + questionnaireId);
    if (departmentId != 0) {
      quer.where("evaluateResult.lesson.teachDepart.id=" + departmentId.toString());
    }
    quer.groupBy("teacher.code,evaluateResult.lesson.id,teacher.person.name.formatedName,evaluateResult.lesson.course.name,evaluateResult.lesson.no,evaluateResult.lesson.teachDepart.name,evaluateResult.lesson.course.code");
    quer.orderBy("sum(questionResult.score)/count(distinct evaluateResult.id) desc");
    put("evaluateTeaStasList", entityDao.search(quer));
    val  queres = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult," + classOf[Staff].getName + " teacher");
    queres.select("teacher.code,teacher.person.name.formatedName,evaluateResult.lesson.id,evaluateResult.lesson.course.name,sum(questionResult.score)/count(distinct evaluateResult.id),count(distinct evaluateResult.id)");
    queres.join("evaluateResult.lesson.teachers", "tea");
//    queres.where("teacher.endOn is null");
    queres.where("evaluateResult.id=questionResult.result.id and tea.id=teacher.id");
    queres.where("evaluateResult.lesson.semester.id=" + semesterId);
    queres.where("evaluateResult.questionnaire.id=" + questionnaireId);
    if (courseType != false) {
      queres.where("evaluateResult.lesson.course.courseType.practical=:courseTId", !courseType);
    }
    queres.groupBy("teacher.code,evaluateResult.lesson.id,teacher.person.name.formatedName,evaluateResult.lesson.course.name");
    queres.orderBy("sum(questionResult.score)/count(distinct evaluateResult.id) desc");
   val  lits = entityDao.search(queres);
   val numMaps = Collections.newMap[String, String]
   var teaNums =1
   lits foreach {obs =>
     teaNums+=1
      numMaps.put(obs(0).toString(), teaNums.toString());
   }
//    for (int i = 0; i < lits.size(); i++) {
//      Object[] obs = (Object[]) lits.get(i);
//      Integer teaNums = i + 1;
//      numMaps.put(obs[0].toString(), teaNums.toString());
//    }
    put("numTeaMaps", numMaps);
    /** 教师各项得分 */
    val evaluateRs = Collections.newMap[String, Float]
    val  quer1 = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult," + classOf[Staff].getName + " teacher");
    quer1.select("teacher.code,evaluateResult.lesson.id,questionResult.question.id,sum(questionResult.score)/count(questionResult.question.id)");
    quer1.join("evaluateResult.lesson.teachers", "tea");
    quer1.where("tea.id = teacher.id");
//    quer1.where("teacher.endOn is null");
    quer1.where("evaluateResult.id=questionResult.result.id");
    quer1.where("evaluateResult.lesson.semester.id=" + semesterId);
    quer1.where("evaluateResult.questionnaire.id=" + questionnaireId);
    if (departmentId != 0) {
      quer1.where("evaluateResult.lesson.teachDepart.id=" + departmentId.toString());
    }
    if (courseType != false) {
      quer1.where("evaluateResult.lesson.course.courseType.practical=:courseTId", !courseType);
    }
    quer1.groupBy("questionResult.question.id,teacher.code,evaluateResult.lesson.id");
    quer1.orderBy("teacher.code");
    val lists = entityDao.search(quer1);
    lists foreach {ob =>
       if (ob.length > 0) {
        val strs = ob(0).toString() + "_" + ob(1).toString() + "_" + ob(2).toString();
        evaluateRs.put(strs, ob(3).toString().toFloat);
      }
      }
//    for (Iterator<?> iter = lists.iterator(); iter.hasNext();) {
//      Object[] ob = (Object[]) iter.next();
//      if (ob.length > 0) {
//        // if (ob[0].toString().equals("3301")) {
//        // System.out.println(ob[3].toString());
//        // }
//        String strs = ob[0].toString() + "_" + ob[1].toString() + "_" + ob[2].toString();
//        evaluateRs.put(strs, Float.valueOf(ob[3].toString()));
//      }
//    }
    put("evaluateRes", evaluateRs);
    put("questionnaires", questis);
    put("evaluateResults", evaResults);
    return forward();
  }

  def  stuEvaluateResults():String = {
//    val semesterId = getInt("semester.id");
    val semesterId=20141
    val depId = getInt("department.id").get
    val questionnaireId = getLong("questionnaire.id").get
    /** 得到本学期的唯一问卷 */
    val  builder = OqlBuilder.from[Questionnaire](classOf[EvaluateResult].getName, "evaluateResult");
    builder.select("distinct evaluateResult.questionnaire");
    builder.where("evaluateResult.lesson.semester.id=" + semesterId);
    builder.where("evaluateResult.questionnaire.id=" + questionnaireId);
    val list = entityDao.search(builder);
    val questionList =Collections.newBuffer[Question]
    if (list.size > 0) {
      list foreach {questionnaire =>
        questionList ++= questionnaire.questions
      }
//      for (int i = 0; i < list.size(); i++) {
//        Questionnaire questionnaire = (Questionnaire) list.get(i);
//        questionList.addAll(questionnaire.getQuestions());
//      }
    } else {
      redirect("search","未找到评教记录!")
    }
    put("questionList", questionList);
    val  query = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult");
    query.where("evaluateResult.lesson.semester.id=:semesterId", semesterId);
    if (depId != 0) {
      query.where("evaluateResult.department.id=:depId", depId);
    }
    query.limit(getPageLimit)
    put("evaluateRList", entityDao.search(query));
    /** 教师各项得分 */
    val evaluateRs = Collections.newMap[String, String]
    val  quer1 = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult");
    quer1
        .select("evaluateResult.id,questionResult.question.id,questionResult.score,questionResult.option.name");
    quer1.where("evaluateResult.id=questionResult.result.id");
    quer1.where("evaluateResult.lesson.semester.id=" + semesterId);
    if (depId != 0) {
      quer1.where("evaluateResult.department.id=:depId", depId);
    }
    quer1
        .groupBy("evaluateResult.id,questionResult.question.id,questionResult.score,questionResult.option.name");
    val lists = entityDao.search(quer1);
    lists foreach { ob =>
      if (ob.length > 0) {
        val strs = ob(0).toString() + "_" + ob(1).toString();
        if (ob(2).toString().toFloat > 0) {
          evaluateRs.put(strs, ob(2).toString());
        } else {
          evaluateRs.put(strs, ob(3).toString());
        }
      }
    }
//    for (Iterator<?> iter = lists.iterator(); iter.hasNext();) {
//      Object[] ob = (Object[]) iter.next();
//      if (ob.length > 0) {
//        String strs = ob[0].toString() + "_" + ob[1].toString();
//        if (Float.valueOf(ob[2].toString()) > 0) {
//          evaluateRs.put(strs, ob[2].toString());
//        } else {
//          evaluateRs.put(strs, ob[3].toString());
//        }
//      }
//    }
    put("evaluateRes", evaluateRs);
    forward();
  }

  def  stuEvalutateResultsExport():String = {
    val semesterId = getInt("semester.id");
    val depId = getInt("department.id");
    /** 得到本学期的唯一问卷 */
    val  builder = OqlBuilder.from[Questionnaire](classOf[EvaluateResult].getName, "evaluateResult");
    builder.where("evaluateResult.lesson.semester.id=" + semesterId);
    builder.select("distinct questionnaire");
    val  list = entityDao.search(builder);
    val questionList = Collections.newBuffer[Question]
    if (list.size > 0) {
      list foreach { questionnaire =>
          questionList ++= questionnaire.questions
      }
    } else {
      redirect("search","未找到评教记录!")
    }
    put("questionList", questionList);
    val  query = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult");
    query.where("evaluateResult.lesson.semester.id=:semesterId", semesterId);
    if (depId != null) {
      query.where("evaluateResult.department.id=:depId", depId);
    }
    put("evaluateRList", entityDao.search(query));
    /** 教师各项得分 */
    val evaluateRs = Collections.newMap[String, String]
    val  quer1 = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult");
    quer1
        .select("evaluateResult.id,questionResult.question.id,questionResult.score,questionResult.option.name");
    quer1.where("evaluateResult.id=questionResult.result.id");
    quer1.where("evaluateResult.lesson.semester.id=" + semesterId);
    if (depId != null) {
      quer1.where("evaluateResult.department.id=:depId", depId);
    }
    quer1
        .groupBy("evaluateResult.id,questionResult.question.id,questionResult.score,questionResult.option.name");
    val lists = entityDao.search(quer1);
    lists foreach { ob =>
            if (ob.length > 0) {
        val strs = ob(0).toString() + "_" + ob(1).toString();
        if (ob(2).toString().toFloat > 0) {
          evaluateRs.put(strs, ob(2).toString());
        } else {
          evaluateRs.put(strs, ob(3).toString());
        }
      }
    }
//    for (Iterator<?> iter = lists.iterator(); iter.hasNext();) {
//      Object[] ob = (Object[]) iter.next();
//      if (ob.length > 0) {
//        String strs = ob[0].toString() + "_" + ob[1].toString();
//        if (Float.valueOf(ob[2].toString()) > 0) {
//          evaluateRs.put(strs, ob[2].toString());
//        } else {
//          evaluateRs.put(strs, ob[3].toString());
//        }
//      }
//    }
    put("evaluateRes", evaluateRs);
    response.setContentType("application/vnd.ms-excel;charset=UTF-8");
    response.setHeader("Content-Disposition", "attachment;filename=stuEvaluateResultsExport.xls");
    forward();
  }

  def  evaluateHistorys() :String ={
    forward();
  }
}