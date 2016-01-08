package org.openurp.edu.evaluation.course.web.action

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.base.model.Semester
import org.openurp.edu.base.code.model.StdType
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.openurp.edu.evaluation.lesson.stat.model.EvalStat
import org.openurp.edu.evaluation.lesson.stat.model.LessonEvalStat
import org.openurp.edu.evaluation.lesson.stat.model.LessonOptionStat
import org.openurp.edu.evaluation.lesson.stat.model.LessonQuestionStat
import org.openurp.edu.evaluation.lesson.stat.model.LessonQuestionTypeStat
import org.openurp.edu.evaluation.lesson.stat.model.OptionStat
import org.openurp.edu.evaluation.lesson.stat.model.OptionStat
import org.openurp.edu.evaluation.lesson.stat.model.OptionStat
import org.openurp.edu.evaluation.lesson.stat.model.QuestionStat
import org.openurp.edu.evaluation.lesson.stat.model.QuestionTypeStat
import org.openurp.edu.evaluation.model.EvaluationCriteria
import org.openurp.edu.evaluation.model.EvaluationCriteriaItem
import org.openurp.edu.evaluation.model.EvaluationCriteriaItem
import org.openurp.edu.evaluation.model.EvaluationCriteriaItem
import org.openurp.edu.evaluation.model.EvaluationCriteriaItem
import org.openurp.edu.evaluation.model.EvaluationCriteriaItem
import org.openurp.edu.evaluation.model.EvaluationCriteriaItem
import org.openurp.edu.evaluation.model.EvaluationCriteriaItem
import org.openurp.edu.evaluation.model.Option
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.evaluation.model.QuestionType
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.edu.lesson.model.Lesson
import org.openurp.hr.base.model.Staff
import org.openurp.edu.base.code.model.Education
import org.openurp.code.edu.model.EducationLevel
import org.beangle.commons.lang.Strings
import scala.collection.mutable.Buffer
import scala.collection.mutable.ListBuffer

class EvaluateDetailStatAction  extends RestfulAction[LessonEvalStat]{
//
//
//  protected QuestionTypeService questionTypeService
//
//  protected QuestionnairStatService questionnairStatService
//
  override def  index():String= {
    val stdType = entityDao.get(classOf[StdType],5)
    put("stdTypeList", stdType)
    val department = entityDao.get(classOf[Department],20)
    put("departmentList", department)

    var searchFormFlag = get("searchFormFlag").orNull
    if (searchFormFlag==null) {
      searchFormFlag = "beenStat"
    }
    put("searchFormFlag", searchFormFlag)
//    put("educations", getEducations())
    put("departments", entityDao.getAll(classOf[Department]))
    val query= OqlBuilder.from(classOf[Questionnaire], "questionnaire").where("questionnaire.state =:state",true)
    put("questionnaires", entityDao.search(query))
    val semesterId = 20141
      put("semester",entityDao.get(classOf[Semester], semesterId))
    forward()
  }

  override def  remove():View = {
    val questionSIds= longIds("questionnaireStat")
//    val idStr = get("questionnaireStat.id").orNull
//    val Ids = idStr.split(",")
//    val questionSIds = new Long[Ids.length]
   
//    for (i = 0 i < Ids.length i++) {
//      questionSIds[i] = Long.valueOf(Ids[i])
//    }
    val query = OqlBuilder.from(classOf[LessonEvalStat], "questionS")
    query.where("questionS.id in(:ids)", questionSIds)
    val li = entityDao.search(query)
    try {
      li foreach { questionnaireStat =>
//      for (QuestionnaireStat questionnaireStat : li) {
        if (questionnaireStat.questionStats.size > 0) {
          val questionStats = questionnaireStat.questionStats
          questionStats foreach { questionstat =>
//          for (QuestionDetailStat questionstat : questionStats) {
            val options = questionstat.optionStats
          options foreach { optionStat =>
//            for (OptionStat optionStat : options) {
//              questionstat.getOptionStats().remove(optionStat)
//              optionStats.add(optionStat)
               entityDao.remove(optionStat)
            }
//            questionnaireStat.getQuestionStats().remove(questionstat)
            entityDao.remove(questionstat)
          }

        }
        if (questionnaireStat.questionTypeStats.size > 0) {
          val questionTS = questionnaireStat.questionTypeStats
//          questionnaireStat.getQuestionTypeStats().removeAll(questionTS)
          entityDao.remove(questionTS)
        }
        entityDao.remove(questionnaireStat)
      }
    } catch {
      case e: Exception =>
      // TODO: handle exception
      return redirect("search", "删除失败！")
    }
    redirect("search", "delete.action.success")
  }

  /**
   * 清除统计数据
   **/
  def remove(educationTypeIds:List[Long], departmentIds: List[Long], semesterId:Int) {
    val query = OqlBuilder.from(classOf[LessonEvalStat], "questionS")
    query.where("questionS.lesson.semester.id=:semesterId", semesterId)
//    query.where("questionS.lesson.course.education.id in(:eduIds)", educationTypeIds)
    // query.where("questionS.department.id in(:depIds)", departmentIds)
    val li = entityDao.search(query)
    try {
      li  foreach { questionnaireStat =>
//      for (QuestionnaireStat questionnaireStat : li) {
        val questionStats =questionnaireStat.questionStats
        val optionStats = Collections.newBuffer[OptionStat]
        questionStats foreach { questionstat =>
//        for (QuestionDetailStat questionstat : questionStats) {
          val options = questionstat.optionStats
//          questionstat.optionStats.removeAll(options)
          optionStats ++=options
          // entityDao.remove(options)
        }
        if (optionStats.size > 0) {
          entityDao.remove(optionStats)
        }
//        questionnaireStat.getQuestionStats().removeAll(questionStats)
         entityDao.remove(questionStats)
        val questionTypeStats = Collections.newBuffer[QuestionTypeStat]
        if (questionnaireStat.questionTypeStats.size > 0) {
          val questionTS = questionnaireStat.questionTypeStats
//          questionnaireStat.getQuestionTypeStats().removeAll(questionTS)
          questionTypeStats ++=questionTS
           entityDao.remove(questionTS)
        }
        if (questionTypeStats.size > 0) {
          entityDao.remove(questionTypeStats)
        }
        entityDao.remove(questionnaireStat)
      }
    } catch {
      case e: Exception =>
      // TODO: handle exception
    }
  }

  /**
   * 院系评教统计
   */
  def  departmentChoiceConfig():String= {
    val semesterId = 20141
    val lis = entityDao.search(OqlBuilder.from (classOf[EvaluationCriteriaItem], "criteriaItem").where("criteriaItem.criteria.id =:id",1L))
    if ( lis.size < 1)   { redirect("search", "未找到评价标准！") }
    put("criterias", lis)
    put("departments", entityDao.search(OqlBuilder.from(classOf[Department], "depart").where("depart.teaching =:teaching", true)))
    put("semester", entityDao.get(classOf[Semester], semesterId))
    val que = OqlBuilder.from[Any](classOf[EvaluateResult].getName + " evaluateResult,"
        + classOf[QuestionResult].getName + " questionResult")
    que.select("sum(questionResult.score)/count(distinct evaluateResult.id)")
    que.where("evaluateResult.id=questionResult.result.id")
    que.where("evaluateResult.lesson.semester.id=" + semesterId)
    val lit = entityDao.search(que)
    var fl = 0f
    if (lit.size > 0) {
      if (lit(0) != null) {
        fl = lit(0).toString().toFloat
      }
    }
    put("evaluateResults", fl)
    val query =OqlBuilder.from(classOf[LessonEvalStat],"evaluateR")
    query.select("evaluateR.lesson.teachDepart.id,count( evaluateR.staff.id)")
    query.where("evaluateR.lesson.semester.id =:semesterId ", semesterId)
    query.groupBy("evaluateR.lesson.teachDepart.id,evaluateR.lesson.semester.id")
//    val hql = "select evaluateR.lesson.teachDepart.id,count( evaluateR.staff.id) from" +
//    " org.openurp.edu.evaluation.lesson.stat.model.LessonEvalStat evaluateR "  + 
//    "where evaluateR.lesson.semester.id=" + semesterId + " " + 
//    "group by evaluateR.lesson.teachDepart.id,evaluateR.lesson.semester.id "
    put("questionNums", entityDao.search(query))
    val maps = Collections.newMap[String, Seq[LessonEvalStat]]
    lis foreach { evaluationCriteriaItem =>
      val query = OqlBuilder.from(classOf[LessonEvalStat], "questionnaireStat")
      query.where("questionnaireStat.semester.id=:semesterId", semesterId)
      query.select("questionnaireStat.lesson.teachDepart.id,count(questionnaireStat.staff.id)")
      query.where("questionnaireStat.score>=" + evaluationCriteriaItem.min
          + " and questionnaireStat.score<" + evaluationCriteriaItem.max)
      query.groupBy("questionnaireStat.lesson.teachDepart.id")
      maps.put(evaluationCriteriaItem.id.toString(), entityDao.search(query))
    }
    put("questionDeps", maps)
    forward()
  }

  /**
   * 历史评教统计
   */
  def  historyCollegeStat():String= {
    val lis = entityDao.search(OqlBuilder.from (classOf[EvaluationCriteriaItem], "criteriaItem").where("criteriaItem.criteria.id =:id",1L))
    if (lis.size < 1) { redirect("search", "未找到评价标准！") }
    put("criterias", lis)
    val evaquery = OqlBuilder.from(classOf[EvaluateResult], "evaluateR")
    evaquery.select("distinct evaluateR.lesson.semester.id")
    val semesterIds = entityDao.search(evaquery)
    val qur = OqlBuilder.from(classOf[Semester], "semester")
    qur.where("semester.beginOn<=:dat", new java.util.Date())
    val quetionQuery = OqlBuilder.from(classOf[EvaluateResult], "questionnaireS")
    if (semesterIds.size > 0) {
      qur.where("semester.id in(:ids)", semesterIds)
      quetionQuery.where("questionnaireS.lesson.semester.id in(:semesterIds)", semesterIds)
    } else {
      qur.where("semester.id is null")
      quetionQuery.where("questionnaireS.lesson.semester.id is null")
    }
    put("evaSemesters", entityDao.search(qur))
    quetionQuery.select("questionnaireS.lesson.semester.id,count(distinct questionnaireS.staff.id)")
    quetionQuery.groupBy("questionnaireS.lesson.semester.id")
    put("questionNums", entityDao.search(quetionQuery))
    val maps =Collections.newMap[String, Seq[LessonEvalStat]]
    lis foreach { evaluationCriteriaItem =>
//    for (EvaluationCriteriaItem evaluationCriteriaItem : lis) {
      val query = OqlBuilder.from(classOf[LessonEvalStat], "questionnaireStat")
      query.select("questionnaireStat.semester.id,count(distinct questionnaireStat.staff.id)")
      query.where("questionnaireStat.score>=" + evaluationCriteriaItem.min
          + " and questionnaireStat.score<" + evaluationCriteriaItem.max)
      query.groupBy("questionnaireStat.semester.id")
      maps.put(evaluationCriteriaItem.id.toString(), entityDao.search(query))
    }
    put("questionDeps", maps)
    forward()
  }

  /**
   * 分项评教汇总
   */
  def  collegeGroupItemInfo():String= {
    val lis = entityDao.search(OqlBuilder.from (classOf[EvaluationCriteriaItem], "criteriaItem").where("criteriaItem.criteria.id =:id",1L))
    if (lis.size < 1) { redirect("search", "未找到评价标准！") }
    put("criterias", lis)
    val maps = Collections.newMap[String, Seq[LessonEvalStat]]
    lis foreach { evaluationCriteriaItem =>
//    for (EvaluationCriteriaItem evaluationCriteriaItem : lis) {
      val query = OqlBuilder.from(classOf[LessonEvalStat], "questionnaireStat")
      query.where("questionnaireStat.semester.id=:semesId", getInt("semester.id").get)
      query.join("questionnaireStat.questionTypeStats", "questionType")
      query.select("questionType.questionType.id,count(distinct questionnaireStat.staff.id)")
      query.where("questionType.score>=" + evaluationCriteriaItem.min + " and questionType.score<"+ evaluationCriteriaItem.max)
      query.groupBy("questionType.questionType.id")
      maps.put(evaluationCriteriaItem.id.toString(), entityDao.search(query))
    }
    put("questionDeps", maps)
    val que = OqlBuilder.from(classOf[EvaluateResult], "evaluateR")
    que.where("evaluateR.lesson.semester.id=:seiD", getInt("semester.id").get)
    // que.where("evaluateR.statType is 1")
    que.select("distinct evaluateR.staff.id")
    val list = entityDao.search(que)
    put("persons", list.size)
    put("questionTypes", entityDao.getAll(classOf[QuestionType]))
    forward()
  }

  /**
   * 教师历史评教
   */
  def  evaluateTeachHistory():String= {
    val id = getLong("questionnaireStat.id").get
    val questionnaires = entityDao.get(classOf[LessonEvalStat], id)
    val query = OqlBuilder.from(classOf[LessonEvalStat], "questionnaires")
    query.where("questionnaires.staff.id=:teaIds", questionnaires.staff.id)
    query.orderBy("questionnaires.semester.beginOn")
    put("teacher", questionnaires.staff)
    put("teachEvaluates", entityDao.search(query))
    forward()
  }

  def  teachQuestionDetailStat():String= {
    put("questionnaires", entityDao.get(classOf[LessonEvalStat], getLong("questionnaireStat.id").get))
    forward()
  }

  /**
   * 院系历史评教
   */
  def  depHistoryStat():String= {
    val lis = entityDao.search(OqlBuilder.from (classOf[EvaluationCriteriaItem], "criteriaItem").where("criteriaItem.criteria.id =:id",1L))
    if (lis.size < 1) { redirect("search", "未找到评价标准！") }
    put("criterias", lis)
//    val depId = getInt("department.id").get
    val depId=20
//    if (getInt("department.id") != null) {
//      depId = getInt("department.id").get
//    }
    put("departId", depId)
    put("departments",entityDao.get(classOf[Department],depId))
    val evaquery = OqlBuilder.from(classOf[EvaluateResult], "evaluateR")
    evaquery.select("distinct evaluateR.lesson.semester.id")
    evaquery.where("evaluateR.lesson.teachDepart.id=:depId", depId)
    val semesterIds = entityDao.search(evaquery)
    val qur = OqlBuilder.from(classOf[Semester], "semester")
    qur.where("semester.beginOn<=:dat", new java.util.Date())
    val quetionQuery = OqlBuilder.from(classOf[EvaluateResult], "questionnaireS")
    if (semesterIds.size > 0) {
      qur.where("semester.id in(:ids)", semesterIds)
      quetionQuery.where("questionnaireS.lesson.semester.id in(:semesterIds)", semesterIds)
    } else {
      qur.where("semester.id is null")
      quetionQuery.where("questionnaireS.lesson.semester.id is null")
    }
    quetionQuery.where("questionnaireS.lesson.teachDepart.id=:depId", depId)
    put("evaSemesters", entityDao.search(qur))
    quetionQuery.select("questionnaireS.lesson.semester.id,count(distinct questionnaireS.staff.id)")
    quetionQuery.groupBy("questionnaireS.lesson.semester.id")
    put("questionNums", entityDao.search(quetionQuery))
    val maps = Collections.newMap[String, Seq[LessonEvalStat]]
    lis foreach { evaluationCriteriaItem =>
      val query = OqlBuilder.from(classOf[LessonEvalStat], "questionnaireStat")
      query.select("questionnaireStat.semester.id,count(distinct questionnaireStat.staff.id)")
      query.where("questionnaireStat.score>=" + evaluationCriteriaItem.min + " and questionnaireStat.score<" + evaluationCriteriaItem.max)
      query.where("questionnaireStat.lesson.teachDepart.id=:depId", depId)
      query.groupBy("questionnaireStat.semester.id")
      maps.put(evaluationCriteriaItem.id.toString(), entityDao.search(query))
    }
    put("questionDeps", maps)
    forward()
  }

  /**
   * 跳转(统计首页面)
   */
  def  statHome() :String ={
    put("stdTypeList", entityDao.getAll(classOf[StdType]))
    put("departmentList",  entityDao.getAll(classOf[Department]))

    put("educations", entityDao.getAll(classOf[Education]))
    val teachingDeparts = entityDao.search(OqlBuilder.from(classOf[Department],"depart").where("depart.teaching =:tea",true))
    put("departments",teachingDeparts)
    forward()
  }

  /**
   * 跳转(初始有效值页面)
   */
  def  initValidHome():String= {
//    put("stdTypeList", getStdTypes())
//    put("departmentList", getColleges())

    forward()
  }

  /**
   * 设置有效记录
   **/
  def  setValid():String = {
//    redirect(new Action(classOf[EvaluateResultStatAction], "search"), "更新成功")
       forward()
  }

  /**
   * 统计(学生评教结果)
   * 
   * @return
   */
  def  stat():View = {
    val eduStr = get("educatIds").get
    val depStr = get("departIds").get
    val eduIds = eduStr.split(",")
    val depIds = depStr.split(",")
    val educationTypeIds = Strings.transformToInteger(eduIds)
    val departmentIds = Strings.transformToInteger(depIds)
//    Long[] educationTypeIds = new Long[eduIds.length]
//    Long[] departmentIds = new Long[depIds.length]
//    for (int i = 0 i < eduIds.length i++) {
//      educationTypeIds[i] = Long.valueOf(eduIds[i])
//    }
//    for (int j = 0 j < depIds.length j++) {
//      departmentIds[j] = Long.valueOf(depIds[j])
//    }
    val semesterId = 20141
    val semester = entityDao.get(classOf[Semester],semesterId)
    // 清楚历史统计数据
//    remove(educationTypeIds, departmentIds, semesterId)
    // 问题得分统计
    val que = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    que.where("questionR.result.lesson.semester.id=:semesterId", semesterId)
    que.where("questionR.result.statType is 1")
    que.where("questionR.question.addition is false")
    que.where("questionR.result.department.id in(:depIds)", departmentIds)
//    que.where("questionR.result.lesson.course.education.id in(:eduIds)", educationTypeIds)
    que.select("questionR.result.staff.id,questionR.result.lesson.id,questionR.question.id,sum(questionR.score),avg(questionR.score),count(questionR.id)")
    que.groupBy("questionR.result.staff.id,questionR.result.lesson.id,questionR.question.id")
    val wtStatMap = new collection.mutable.HashMap[Tuple2[Any,Any],Buffer[Tuple4[Long,Number,Number,Number]]]
    entityDao.search(que) foreach { a =>
      val buffer = wtStatMap.getOrElseUpdate((a(0),a(1)),new ListBuffer[Tuple4[Long,Number,Number,Number]])
      buffer += Tuple4(a(2).asInstanceOf[Long],a(3).asInstanceOf[Number],a(4).asInstanceOf[Number],a(5).asInstanceOf[Number])
    }
    // 问卷得分统计
    val quer = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    quer.where("questionR.result.lesson.semester.id=:semesterId", semesterId)
    quer.where("questionR.result.statType is 1")
    quer.where("questionR.result.department.id in(:depIds)", departmentIds)
//    quer.where("questionR.result.lesson.course.education.id in(:eduIds)", educationTypeIds)
    quer.where("questionR.question.addition is false")
    quer.select("questionR.result.lesson.id,questionR.result.staff.id,questionR.result.questionnaire.id,"+ "sum(questionR.score),case when questionR.result.statType =1 then count(questionR.result.id) end," + "count(distinct questionR.result.id),case when questionR.result.statType =1 then sum(questionR.score) end," + "sum(questionR.score)/count(distinct questionR.result.id)")
    quer.groupBy("questionR.result.lesson.id,questionR.result.staff.id,questionR.result.questionnaire.id,questionR.result.statType")
    val wjStat = entityDao.search(quer)
    // 排名
////    val query = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
//    val query = "select qR.result.department.id,qR.result.lesson.id,qR.result.staff.id,"+
//    "sum(qR.score)/count(distinct qR.result.id), "+
//    "rank()over(order by sum(qR.score)/count(distinct qR.result.id) desc), "+
//    "rank()over(partition by qR.result.department.id order by sum(qR.score)/count(distinct qR.result.id) desc) "+
//    " from  org.openurp.edu.evaluation.lesson.result.model.QuestionResult qR "+
//    "where qR.result.lesson.semester.id="+ semesterId +" " +
//    "and qR.result.statType is 1 "+
//    "and qR.result.staff is not null "+
////    query.where(" questionR.result.lesson.semester.id=:semesterId",semesterId)
////    query.where( "questionR.result.statType is 1")
////    query.where ("questionR.result.staff is not null")
////     query.where("questionR.result.department.id in(:depIds)", departmentIds)
////    query.where("questionR.result.lesson.course.education.id in(:eduIds)", educationTypeIds)
//    "and qR.question.addition is false "+
////    query.select("questionR.result.department.id,questionR.result.lesson.id,questionR.result.staff.id,"+
////        "sum(questionR.score)/count(distinct questionR.result.id) as x,"+
////        "rank() over(order by x desc),"+
////        "rank() over(partition by questionR.result.department.id order by x desc) ")
//    "group by qR.result.department.id,qR.result.lesson.id,qR.result.staff.id"
////    query.orderBy("sum(questionR.score)/count(distinct questionR.result.id) desc,questionR.result.department.id,questionR.result.staff.id")
//    val pmStatMap = new collection.mutable.HashMap[Tuple2[Any,Any],Tuple3[Number,Integer,Integer]]
//    entityDao.search[Array[Any]](query) foreach { a =>
//    pmStatMap.getOrElseUpdate((a(1),a(2)),Tuple3(a(3).asInstanceOf[Number],a(4).asInstanceOf[Integer],a(5).asInstanceOf[Integer]))
//    }
    // 问题类别统计
    val tyquery = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    tyquery.where("questionR.result.lesson.semester.id=:semesterId", semesterId)
    tyquery.where("questionR.result.statType is 1")
    tyquery.where("questionR.result.staff is not null")
    tyquery.where("questionR.result.department.id in(:depIds)", departmentIds)
    tyquery.where("questionR.question.addition is false")
//    tyquery.where("questionR.result.lesson.course.education.id in(:eduIds)", educationTypeIds)
    tyquery.select("questionR.result.lesson.id,questionR.result.staff.id,questionR.question.questionType.id,sum(questionR.score)/count(distinct questionR.result.id)")
    tyquery.groupBy("questionR.result.lesson.id,questionR.result.staff.id,questionR.question.questionType.id")
    
    val typeStatMap= new collection.mutable.HashMap[Tuple2[Any,Any],Buffer[Tuple2[Long,Number]]]
    entityDao.search(tyquery) foreach { a =>
      val buffer =typeStatMap.getOrElseUpdate((a(0),a(1)),new ListBuffer[Tuple2[Long,Number]])
      buffer += Tuple2(a(2).asInstanceOf[Long],a(3).asInstanceOf[Number])
    }
    // 选项统计
    val opQuery = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    opQuery.where("questionR.result.lesson.semester.id=:semesterId", semesterId)
    opQuery.where("questionR.result.statType is 1")
    opQuery.where("questionR.result.department.id in(:depIds)", departmentIds)
    opQuery.where("questionR.question.addition is false")
//    opQuery.where("questionR.result.lesson.course.education.id in(:eduIds)", educationTypeIds)
    opQuery.select("questionR.result.lesson.id," + "questionR.result.staff.id,questionR.question.id,questionR.option.id,count(questionR.id)")
    opQuery.groupBy("questionR.result.lesson.id,questionR.result.staff.id,questionR.question.id,questionR.option.id")
    val optionStatMap = new collection.mutable.HashMap[Tuple3[Any,Any,Any],Buffer[Tuple2[Long,Number]]]
    entityDao.search(opQuery) foreach { a =>
      val buffer = optionStatMap.getOrElseUpdate((a(0),a(1),a(2)),new ListBuffer[Tuple2[Long,Number]])
      buffer += Tuple2(a(3).asInstanceOf[Long],a(4).asInstanceOf[Number])
    }
   
    val questionMap = entityDao.getAll(classOf[Question]).map ( o => (o.id,o )).toMap
    val questiontyMap = entityDao.getAll(classOf[QuestionType]).map ( o => (o.id,o )).toMap
    val optionMap = entityDao.getAll(classOf[Option]).map ( o => (o.id,o )).toMap
    
    // 任务
    val lquery = OqlBuilder.from(classOf[Lesson], "le")
    lquery.where("le.semester.id=:seId", semesterId)
//  lquery.where("le.course.education.id in(:eduIds)", educationTypeIds)
    lquery.where("le.teachDepart.id in(:depIds)", departmentIds)
    val lessonList = entityDao.search(lquery)
    //任务问卷得分统计
     wjStat foreach { evaObject =>
          val questionS = new LessonEvalStat
          questionS.staff = new Staff()
          questionS.staff.id=evaObject(1).asInstanceOf[Long]
          questionS.semester=semester   
          questionS.statAt = new java.util.Date()
          questionS.lesson= new Lesson()
          questionS.lesson.id=evaObject(0).asInstanceOf[Long]
          questionS.score=evaObject(7).toString().toFloat
          questionS.validScore=evaObject(6).toString().toFloat
          questionS.validTickets=Integer.valueOf(evaObject(4).toString())
          questionS.allTickets=Integer.valueOf(evaObject(5).toString())
          // 添加问卷
          questionS.questionnaire= new Questionnaire()
          questionS.questionnaire.id=evaObject(2).asInstanceOf[Long]
          // 添加问题得分统计
          val questionDetailStats =  Collections.newBuffer[QuestionStat]
          wtStatMap.get((questionS.staff.id,questionS.lesson.id)) foreach { buffer =>
          buffer foreach { wt =>
              val detailStat = new LessonQuestionStat
              // 添加问题
              detailStat.question=questionMap(wt._1)
              detailStat.total=wt._2.toString().toFloat
              detailStat.average=wt._3.toString().toFloat
//            detailStat.stddev=stddev
              detailStat.evalStat=questionS
              
              // 添加选项统计
              val optionStates = Collections.newBuffer[OptionStat]
              optionStatMap.get((questionS.lesson.id,questionS.staff.id,detailStat.question.id)) foreach{ buffer=>
                buffer foreach{ os =>
                val optionstat = new LessonOptionStat
                  optionstat.amount=os._2.intValue()
                  optionstat.option=optionMap(os._1)
                  optionstat.questionStat= detailStat
                  optionStates += optionstat
                }
              }
              detailStat.optionStats = optionStates
              questionDetailStats += detailStat
          }
          }
            questionS.questionStats=questionDetailStats
//           添加排名
            
//            pmStatMap.get(questionS.lesson.id,questionS.staff.id) foreach { pm =>
//                questionS.rank= pm._2.intValue()
//                questionS.departRank= pm._3.intValue()
//            }
//           添加问题类别统计
          val questionTypeStats = Collections.newBuffer[QuestionTypeStat]
          typeStatMap.get((questionS.lesson.id,questionS.staff.id)) foreach{ buffer=>
              buffer foreach{ os =>
              val questionTs = new LessonQuestionTypeStat
              questionTs.score=os._2.toString().toFloat
              questionTs.evalStat = questionS
              questionTs.questionType=questiontyMap(os._1)
              questionTypeStats +=questionTs
              }
          }
          questionS.questionTypeStats = questionTypeStats
          entityDao.saveOrUpdate(questionS)
    }
     
    redirect("index", "info.action.success")
  }
//
//  public void setQuestionTypeService(QuestionTypeService questionTypeService) {
//    this.questionTypeService = questionTypeService
//  }
//
//  public QuestionnairStatService getQuestionnairStatService() {
//    return questionnairStatService
//  }
//
//  public void setQuestionnairStatService(QuestionnairStatService questionnairStatService) {
//    this.questionnairStatService = questionnairStatService
//  }
//
//  public QuestionTypeService getQuestionTypeService() {
//    return questionTypeService
//  }
//
//
}