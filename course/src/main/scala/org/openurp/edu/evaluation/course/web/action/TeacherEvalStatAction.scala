package org.openurp.edu.evaluation.course.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.openurp.hr.base.model.Staff
import org.beangle.webmvc.api.view.View
import org.openurp.base.model.Semester
import java.text.DecimalFormat
import org.springframework.beans.support.PropertyComparator
import java.text.FieldPosition
import java.util.ArrayList
import org.beangle.commons.collection.Collections
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.base.model.Department
import org.openurp.edu.evaluation.lesson.stat.model.TeacherEvalStat
import org.openurp.edu.base.code.model.Education
import org.openurp.edu.base.code.model.StdType
import org.beangle.commons.lang.Strings
import scala.collection.mutable.Buffer
import scala.collection.mutable.ListBuffer
import org.openurp.edu.evaluation.model.QuestionType
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.evaluation.model.Option
import org.openurp.edu.evaluation.lesson.model.DepartEvaluation
import org.openurp.edu.evaluation.lesson.stat.model.TeacherQuestionStat
import org.openurp.edu.evaluation.lesson.stat.model.OptionStat
import org.openurp.edu.evaluation.lesson.stat.model.QuestionStat
import org.openurp.edu.evaluation.lesson.stat.model.QuestionTypeStat
import org.openurp.edu.evaluation.lesson.stat.model.TeacherOptionStat
import org.openurp.edu.evaluation.lesson.stat.model.TeacherQuestionTypeStat
import org.beangle.commons.collection.Order
import org.openurp.edu.evaluation.model.EvaluationCriteriaItem
import org.openurp.edu.evaluation.course.service.Ranker

class TeacherEvalStatAction extends RestfulAction[TeacherEvalStat] {

 override def  index():String = {
//    put("stdTypeList", getStdTypes());
//    put("departmentList", getColleges());
//    put("departments", getDeparts());
   put("departments", entityDao.search(OqlBuilder.from(classOf[Department],"dep").where("dep.teaching =:tea",true)))
    /** 本学期是否评教 */
    val builder = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult");
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)
    if (semesters != null) {
      builder.where("evaluateResult.lesson.semester in (:ids)", semesters);
    }
    builder.select("distinct questionnaire");
    put("questionnaires", entityDao.search(builder));
    forward();
  }

  
//  override protected def  getQueryBuilder():OqlBuilder[CourseEvalStat]={
////    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
//
////    OrElse(entityDao.search(semesterQuery).head.id)
//    val queryBuilder = OqlBuilder.from(classOf[CourseEvalStat],"courseEvalStat")
//    queryBuilder.where("courseEvalStat.semester.id=:semesterId", semesterId);
//    populateConditions(queryBuilder);
//    queryBuilder.limit(getPageLimit).orderBy("courseEvalStat.rank asc");
//  }

  
  override def search():String= {
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val queryBuilder = OqlBuilder.from(classOf[TeacherEvalStat],"teacherEvalStat")
    populateConditions(queryBuilder);
    queryBuilder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    queryBuilder.where("teacherEvalStat.semester =:semester", semester);
//    val builder=queryBuilder.limit(getPageLimit).orderBy("teacherEvalStat.rank asc");
    put("teacherEvalStats", entityDao.search(queryBuilder));
    forward();
  }
  
  
   override def  remove():View = {
    val questionSIds= longIds("evaluate")
    val query = OqlBuilder.from(classOf[TeacherEvalStat], "questionS")
    query.where("questionS.id in(:ids)", questionSIds)
    entityDao.remove(entityDao.search(query))
    redirect("search", "info.remove.success")
  }
  
  /**
   * 清除统计数据
   **/
  def remove(educationTypeIds:List[Integer], departmentIds: List[Integer], semesterId:Int) {
    var semester = entityDao.get(classOf[Semester],semesterId)
    val query = OqlBuilder.from(classOf[TeacherEvalStat], "questionS")
    query.where("questionS.semester =:semester", semester)
    val results =entityDao.search(query)
    entityDao.remove(results)
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
    
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)
    forward()
  }

    /**
   * 统计(教师评教结果)
   * 
   * @return
   */
  def  stat():View = {
    val eduStr = get("educatIds").get
    val depStr = get("departIds").get
    val eduIds = eduStr.split(",")
    val depIds = depStr.split(",")
    val educationTypeIds = Strings.transformToInteger(eduIds).toList
    val departmentIds = Strings.transformToInteger(depIds).toList
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    // 删除历史统计数据
    remove(educationTypeIds, departmentIds, semesterId)
    
        // 问题得分统计
    val que = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    que.where("questionR.result.lesson.semester.id=:semesterId", semesterId)
    que.where("questionR.result.statType is 1")
    que.where("questionR.question.addition is false")
    que.where("questionR.result.department.id in(:depIds)", departmentIds)
//  que.where("questionR.result.lesson.course.education.id in(:eduIds)", educationTypeIds)
    que.select("questionR.result.lesson.semester.id,questionR.result.staff.id,questionR.question.id,sum(questionR.score),avg(questionR.score),count(questionR.id)")
    que.groupBy("questionR.result.lesson.semester.id,questionR.result.staff.id,questionR.question.id")
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
    quer.select("questionR.result.lesson.semester.id,questionR.result.staff.id,questionR.result.questionnaire.id,"
        + "sum(questionR.score),case when questionR.result.statType =1 then count(distinct questionR.result.id) end," 
        + "count(distinct questionR.result.id),case when questionR.result.statType =1 then sum(questionR.score) end," 
        + "sum(questionR.score)/count(distinct questionR.result.id)")
    quer.groupBy("questionR.result.lesson.semester.id,questionR.result.staff.id,questionR.result.questionnaire.id,questionR.result.statType")
    val wjStat = entityDao.search(quer)  
    
    // 问题类别统计
    val tyquery = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    tyquery.where("questionR.result.lesson.semester.id=:semesterId", semesterId)
    tyquery.where("questionR.result.statType is 1")
    tyquery.where("questionR.result.staff is not null")
    tyquery.where("questionR.result.department.id in(:depIds)", departmentIds)
    tyquery.where("questionR.question.addition is false")
//    tyquery.where("questionR.result.lesson.course.education.id in(:eduIds)", educationTypeIds)
    tyquery.select("questionR.result.lesson.semester.id,questionR.result.staff.id,questionR.question.questionType.id,sum(questionR.score)/count(distinct questionR.result.id)")
    tyquery.groupBy("questionR.result.lesson.semester.id,questionR.result.staff.id,questionR.question.questionType.id")
    
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
    opQuery.select("questionR.result.lesson.semester.id,questionR.result.staff.id,questionR.question.id,questionR.option.id,count(questionR.id)")
    opQuery.groupBy("questionR.result.lesson.semester.id,questionR.result.staff.id,questionR.question.id,questionR.option.id")
    val optionStatMap = new collection.mutable.HashMap[Tuple3[Any,Any,Any],Buffer[Tuple2[Long,Number]]]
    entityDao.search(opQuery) foreach { a =>
      val buffer = optionStatMap.getOrElseUpdate((a(0),a(1),a(2)),new ListBuffer[Tuple2[Long,Number]])
      buffer += Tuple2(a(3).asInstanceOf[Long],a(4).asInstanceOf[Number])
     }
    
    val questionMap = entityDao.getAll(classOf[Question]).map ( o => (o.id,o )).toMap
    val questiontyMap = entityDao.getAll(classOf[QuestionType]).map ( o => (o.id,o )).toMap
    val optionMap = entityDao.getAll(classOf[Option]).map ( o => (o.id,o )).toMap
    // 教师
    val lquery = OqlBuilder.from(classOf[Staff], "sf")
//  lquery.where("le.course.education.id in(:eduIds)", educationTypeIds)
    lquery.where("sf.state.department.id in(:depIds)", departmentIds)
    val staffList = entityDao.search(lquery)
    //教师问卷得分统计
     wjStat foreach { evaObject =>
          val questionS = new TeacherEvalStat
          questionS.staff = new Staff()
          questionS.staff.id=evaObject(1).asInstanceOf[Long]
          questionS.semester=semester   
          questionS.statAt = new java.util.Date()
//          questionS.lesson= new Lesson()
//          questionS.lesson.id=evaObject(0).asInstanceOf[Long]
          questionS.score=evaObject(7).toString().toFloat*10
          questionS.validScore=evaObject(6).toString().toFloat*10/Integer.valueOf(evaObject(5).toString())
          questionS.validTickets=Integer.valueOf(evaObject(4).toString())
          questionS.allTickets=Integer.valueOf(evaObject(5).toString())
          // 添加问卷
          questionS.questionnaire= new Questionnaire()
          questionS.questionnaire.id=evaObject(2).asInstanceOf[Long]
          // 添加问题得分统计
          val questionDetailStats =  Collections.newBuffer[QuestionStat]
          wtStatMap.get((questionS.semester.id,questionS.staff.id)) foreach { buffer =>
          buffer foreach { wt =>
              val detailStat = new TeacherQuestionStat
              // 添加问题
              detailStat.question=questionMap(wt._1)
              detailStat.total=wt._2.toString().toFloat*100
              detailStat.average=wt._3.toString().toFloat*100
//            detailStat.stddev=stddev
              detailStat.evalStat=questionS
              
              // 添加选项统计
              val optionStates = Collections.newBuffer[OptionStat]
              optionStatMap.get((questionS.semester.id,questionS.staff.id,detailStat.question.id)) foreach{ buffer=>
                buffer foreach{ os =>
                val optionstat = new TeacherOptionStat
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
          typeStatMap.get((questionS.semester.id,questionS.staff.id)) foreach{ buffer=>
              buffer foreach{ os =>
              val questionTs = new TeacherQuestionTypeStat
              questionTs.score=os._2.toString().toFloat*100
              questionTs.evalStat = questionS
              questionTs.questionType=questiontyMap(os._1)
              questionTypeStats +=questionTs
              }
          }
          questionS.questionTypeStats = questionTypeStats
          entityDao.saveOrUpdate(questionS)
    }
    

    //    排名
//     val rankQuery = OqlBuilder.from(classOf[TeacherEvalStat], "teacherEvalStat")
//     rankQuery.where("teacherEvalStat.semester.id=:semesterId", semesterId)
//     val evals = entityDao.search(rankQuery)
//     Ranker.over(evals){(x,r) => 
//       x.rank=r;
//     }
//     val departEvalMaps = evals.groupBy ( x => x.staff.state.department )
//     departEvalMaps.values foreach{ departEvals =>
//         Ranker.over(departEvals){(x,r) => 
//         x.departRank=r;
//       }
//     }
//     entityDao.saveOrUpdate(evals);

        redirect("index", "info.action.success")
  }
  
    def rankStat():View = {
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    //    排名
     val rankQuery = OqlBuilder.from(classOf[TeacherEvalStat], "teacherEvalStat")
     rankQuery.where("teacherEvalStat.semester.id=:semesterId", semesterId)
     val evals = entityDao.search(rankQuery)
     Ranker.over(evals){(x,r) => 
       x.rank=r;
     }
     val departEvalMaps = evals.groupBy ( x => x.staff.state.department )
     departEvalMaps.values foreach{ departEvals =>
         Ranker.over(departEvals){(x,r) => 
         x.departRank=r;
       }
     }
     entityDao.saveOrUpdate(evals);
     redirect("index", "info.action.success")
  }
  
  
  
   /**
   * 教师历史评教
   */
  def  evaluateTeachHistory():String= {
    val id = getLong("teacherEvalStat.id").get
    val questionnaires = entityDao.get(classOf[TeacherEvalStat], id)
    val query = OqlBuilder.from(classOf[TeacherEvalStat], "questionnaires")
    query.where("questionnaires.staff.id=:teaIds", questionnaires.staff.id)
    query.orderBy("questionnaires.semester.beginOn")
    put("teacher", questionnaires.staff)
    put("teachEvaluates", entityDao.search(query))
    forward()
  }
  
   /**
   * 院系教师评教统计
   */
  def  departmentChoiceConfig():String= {
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val lis = entityDao.search(OqlBuilder.from (classOf[EvaluationCriteriaItem], "criteriaItem").where("criteriaItem.criteria.id =:id",1L))
    if ( lis.size < 1)   { redirect("search", "未找到评价标准！") }
    put("criterias", lis)
    put("departments", entityDao.search(OqlBuilder.from(classOf[Department], "depart").where("depart.teaching =:teaching", true)))
    put("semester", semester)
    
//    val que = OqlBuilder.from[Any](classOf[EvaluateResult].getName + " evaluateResult,"
//        + classOf[QuestionResult].getName + " questionResult")
//    que.select("sum(questionResult.score)/count(distinct evaluateResult.id)")
//    que.where("evaluateResult.id=questionResult.result.id")
//    que.where("evaluateResult.lesson.semester.id=" + semesterId)
//    val lit = entityDao.search(que)
//    var fl = 0f
//    if (lit.size > 0) {
//      if (lit(0) != null) {
//        fl = lit(0).toString().toFloat
//      }
//    }
//    put("evaluateResults", fl)
    
    val query =OqlBuilder.from(classOf[TeacherEvalStat],"evaluateR")
    query.select("evaluateR.staff.state.department.id,count( evaluateR.staff.id)")
    query.where("evaluateR.semester.id =:semesterId ", semesterId)
    query.groupBy("evaluateR.staff.state.department.id,evaluateR.semester.id")
//    val hql = "select evaluateR.lesson.teachDepart.id,count( evaluateR.staff.id) from" +
//    " org.openurp.edu.evaluation.lesson.stat.model.LessonEvalStat evaluateR "  + 
//    "where evaluateR.lesson.semester.id=" + semesterId + " " + 
//    "group by evaluateR.lesson.teachDepart.id,evaluateR.lesson.semester.id "
    put("questionNums", entityDao.search(query))
    
    val maps = Collections.newMap[String, Seq[TeacherEvalStat]]
    lis foreach { evaluationCriteriaItem =>
      val query = OqlBuilder.from(classOf[TeacherEvalStat], "questionnaireStat")
      query.where("questionnaireStat.semester.id=:semesterId", semesterId)
      query.select("questionnaireStat.staff.state.department.id,count(questionnaireStat.staff.id)")
      query.where("questionnaireStat.score>=" + evaluationCriteriaItem.min
          + " and questionnaireStat.score<" + evaluationCriteriaItem.max)
      query.groupBy("questionnaireStat.staff.state.department.id")
      maps.put(evaluationCriteriaItem.id.toString(), entityDao.search(query))
    }
    put("questionDeps", maps)
    forward()
  }
  
  /**
   * 院系历史评教
   */
  def  depHistoryStat():String= {
    val lis = entityDao.search(OqlBuilder.from (classOf[EvaluationCriteriaItem], "criteriaItem").where("criteriaItem.criteria.id =:id",1L))
    if (lis.size < 1) { redirect("search", "未找到评价标准！") }
    put("criterias", lis)
    val depId = getInt("department.id").getOrElse(20)
//    val depId=20
//    if (getInt("department.id") != null) {
//      depId = getInt("department.id").get
//    }
    put("departId", depId)
    put("departments",entityDao.search(OqlBuilder.from(classOf[Department],"dep").where("dep.teaching=true")))
    
    val evaquery = OqlBuilder.from(classOf[EvaluateResult], "evaluateR")
    evaquery.select("distinct evaluateR.lesson.semester.id")
    evaquery.where("evaluateR.staff.state.department.id=:depId", depId)
    val semesterIds = entityDao.search(evaquery)
    val qur = OqlBuilder.from(classOf[Semester], "semester")
    qur.where("semester.beginOn<=:dat", new java.util.Date())
    
    val quetionQuery = OqlBuilder.from(classOf[TeacherEvalStat], "questionnaireS")
    if (semesterIds.size > 0) {
      qur.where("semester.id in(:ids)", semesterIds)
      quetionQuery.where("questionnaireS.semester.id in(:semesterIds)", semesterIds)
    } else {
      qur.where("semester.id is null")
      quetionQuery.where("questionnaireS.semester.id is null")
    }
    quetionQuery.where("questionnaireS.staff.state.department.id=:depId", depId)
    put("evaSemesters", entityDao.search(qur))
    quetionQuery.select("questionnaireS.semester.id,count(questionnaireS.staff.id)")
    quetionQuery.groupBy("questionnaireS.semester.id")
    put("questionNums", entityDao.search(quetionQuery))
    val maps = Collections.newMap[String, Seq[TeacherEvalStat]]
    lis foreach { evaluationCriteriaItem =>
      val query = OqlBuilder.from(classOf[TeacherEvalStat], "questionnaireStat")
      query.select("questionnaireStat.semester.id,count(questionnaireStat.staff.id)")
      query.where("questionnaireStat.score>=" + evaluationCriteriaItem.min + " and questionnaireStat.score<" + evaluationCriteriaItem.max)
      query.where("questionnaireStat.staff.state.department.id=:depId", depId)
      query.groupBy("questionnaireStat.semester.id")
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
   
    val quetionQuery = OqlBuilder.from(classOf[TeacherEvalStat], "questionnaireS")
    if (semesterIds.size > 0) {
      qur.where("semester.id in(:ids)", semesterIds)
      quetionQuery.where("questionnaireS.semester.id in(:semesterIds)", semesterIds)
    } else {
      qur.where("semester.id is null")
      quetionQuery.where("questionnaireS.semester.id is null")
    }
    put("evaSemesters", entityDao.search(qur))
    quetionQuery.select("questionnaireS.semester.id,count(questionnaireS.staff.id)")
    quetionQuery.groupBy("questionnaireS.semester.id")
    put("questionNums", entityDao.search(quetionQuery))
    val maps =Collections.newMap[String, Seq[TeacherEvalStat]]
    lis foreach { evaluationCriteriaItem =>
//    for (EvaluationCriteriaItem evaluationCriteriaItem : lis) {
      val query = OqlBuilder.from(classOf[TeacherEvalStat], "questionnaireStat")
      query.select("questionnaireStat.semester.id,count(questionnaireStat.staff.id)")
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
    
    val evaquery = OqlBuilder.from(classOf[TeacherEvalStat], "questionnaireStat")
      evaquery.where("questionnaireStat.semester.id=:semesId", getInt("semester.id").get)
      evaquery.join("questionnaireStat.questionTypeStats", "questionType")
      evaquery.select("distinct questionType.questionType.id")
    val queTypeIds = entityDao.search(evaquery)
    
    val quTqur = OqlBuilder.from(classOf[QuestionType], "questionType")
    val quetionQuery = OqlBuilder.from(classOf[TeacherEvalStat], "questionnaireS")
    quetionQuery.join("questionnaireS.questionTypeStats","questionTypeStat")
    if (queTypeIds.size > 0) {
      quTqur.where("questionType.id in(:ids)", queTypeIds)
      quetionQuery.where("questionTypeStat.questionType.id in(:queTypeIds)", queTypeIds)
    } else {
      quTqur.where("questionType.id is null")
      quetionQuery.where("questionTypeStat.questionType.id is null")
    }
    put("questionTypes", entityDao.search(quTqur))
    quetionQuery.select("questionTypeStat.questionType.id,count(questionnaireS.staff.id)")
    quetionQuery.groupBy("questionTypeStat.questionType.id")
    put("quesTypeNums", entityDao.search(quetionQuery))
    
    val maps = Collections.newMap[String, Seq[TeacherEvalStat]]
    lis foreach { evaluationCriteriaItem =>
      val query = OqlBuilder.from(classOf[TeacherEvalStat], "questionnaireStat")
      query.where("questionnaireStat.semester.id=:semesId", getInt("semester.id").get)
      query.join("questionnaireStat.questionTypeStats", "questionTypeStat")
      query.select("questionTypeStat.questionType.id,count(questionnaireStat.staff.id)")
      query.where("questionTypeStat.score>=" + evaluationCriteriaItem.min + " and questionTypeStat.score<"+ evaluationCriteriaItem.max)
      query.groupBy("questionTypeStat.questionType.id")
      maps.put(evaluationCriteriaItem.id.toString(), entityDao.search(query))
    }
    put("questionDeps", maps)
    val que = OqlBuilder.from(classOf[EvaluateResult], "evaluateR")
    que.where("evaluateR.lesson.semester.id=:seiD", getInt("semester.id").get)
    // que.where("evaluateR.statType is 1")
    que.select("distinct evaluateR.staff.id")
    val list = entityDao.search(que)
    put("persons", list.size)
//    put("questionTypes", entityDao.getAll(classOf[QuestionType]))
    forward()
  }

//    
//    /** 获取本学期所有已使用的问卷 **/
//    val builder = OqlBuilder.from[Questionnaire](classOf[EvaluateResult].getName, "evaluateResult");
//    builder.where("evaluateResult.lesson.semester.id=:semesterId", semesterId);
//    builder.select("distinct questionnaire");
//    val questionnaires = entityDao.search(builder);
//    /** 统计每份问卷的评教结果 **/
//    questionnaires foreach { questionnaire =>
//      val questionnaireId = questionnaire.id
//      val query = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
//          + classOf[QuestionResult].getName + " questionResult," + classOf[Staff].getName + " teach");
//      query.select("teach.id,sum(questionResult.score)/count(distinct evaluateResult.id)");
//      query.join("evaluateResult.lesson.teachers", "tea");
//      query.where("tea.id = teach.id");
//      query.where("evaluateResult.id=questionResult.result.id ");
//      query.where("evaluateResult.lesson.semester.id=" + semesterId);
//      query.where("evaluateResult.questionnaire.id=:ids", questionnaireId);
//      query.groupBy("teach.id");
//      query.orderBy("sum(questionResult.score)/count(distinct evaluateResult.id) desc");
//      val li = entityDao.search(query);
////////      val depEvaluates = entityDao.search(OqlBuilder.from(classOf[DepartEvaluate], "de").where("de.semester.id=:id", semesterId))
//      val teaList = entityDao.search(OqlBuilder.from(classOf[Staff], "staff").where("staff.state.id=:id",1L))
//      val evaluates =Collections.newBuffer[TeacherEvalStat]
//      val df = new DecimalFormat("#.00 ");
//      teaList foreach { teacher =>
//        var fl = 0f;
//        val evaluate = new TeacherEvalStat
//        evaluate.staff=teacher
//        evaluate.semester=entityDao.get(classOf[Semester], semesterId)
//        evaluate.questionnaire=questionnaire
//        li foreach { bo =>
//          if (bo(0).toString().equals(teacher.id.toString())) {
//            if (bo(1) != null && !"".equals(bo(1))) {
//              val d = new StringBuffer(" ");
//              df.format(bo(1).toString().toFloat, d, new FieldPosition(2));
//              evaluate.stdEvaluate=d.toString().toFloat
//              fl += bo(1).toString().toFloat * 0.5f;
//            }
//          }
//        }
//        depEvaluates foreach {departEvaluation =>
//          
//          if (departEvaluation.staff.id.toString().equals(teacher.id.toString())) {
//            if (departEvaluation.totalScore!= null) {
//
//              evaluate.depEvaluate=departEvaluation.totalScore
//
//              fl += departEvaluation.totalScore * 0.5f;
//            }
//          }
//        
//        }
//        for (DepartEvaluation departEvaluation : depEvaluates) {
//          if (departEvaluation.getStaff().getId().toString().equals(teacher.getId().toString())) {
//            if (departEvaluation.getScore() != null) {
//
//              evaluate.setDepEvaluate(departEvaluation.getScore());
//
//              fl += departEvaluation.getScore() * 0.5f;
//            }
//          }
//        }
//        val dd = new StringBuffer(" ");
//        df.format(fl, dd, new FieldPosition(2));
//        evaluate.score=dd.toString().toFloat
//        evaluates += evaluate
//////      }
////////      Collections.sort(evaluates, new PropertyComparator("score desc"));
//      
//      for (i<-0 to  evaluates.size) {
//        val courseEvalStat = evaluates(i)
//        courseEvalStat.rank=i + 1
//        var num = 0;
//        for (j <- 0 to evaluates.size) {
//          val courseEvaluate = evaluates(j);
//          val t1 = courseEvaluate.staff
//          val t2 = courseEvalStat.staff
//          if (t1.state.department != null && t2.state.department != null) {
//            if (t1.state.department.id.toString().equals(t2.state.department.id.toString())) {
//              num += 1;
//              if (courseEvaluate.staff.id.toString().equals(courseEvalStat.staff.id.toString())) {
//                courseEvalStat.departRank=num
//              }
//            }
//          }
//
//        }
//      }
//      val list = Collections.newBuffer[CourseEvalStat]
//      evaluates foreach {courseEvalStat =>
//        
//        if ((courseEvalStat.staff.state.department != null)) {
//          list +=courseEvalStat
//        } else {
//          val lists = entityDao.getAll(classOf[Department])
//          lists foreach {department =>
//            
//            val teacher = courseEvalStat.staff
//            if (null != teacher.state.department) {
//              if (teacher.state.department.id.toString().equals(department.id.toString())) {
//                list += courseEvalStat
//              }
//            }
//          
//          }
//        }
//      
//      }
//      /** 删除本学期某一问卷的统计结果 **/
//      val cBuilder = OqlBuilder.from(classOf[TeacherEvalStat], "stat")
//          .where("stat.semester.id= :semesterId", semesterId)
//          .where("stat.questionnaire.id=:questionnaireId", questionnaireId);
//      entityDao.remove(entityDao.search(cBuilder));
//      saveOrUpdate(list);
//    }
//    redirect("index", "统计完成");
//  }



}