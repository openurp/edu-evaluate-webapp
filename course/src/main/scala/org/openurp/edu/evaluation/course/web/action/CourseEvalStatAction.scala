package org.openurp.edu.evaluation.course.web.action

import scala.collection.mutable.Buffer
import scala.collection.mutable.ListBuffer
import org.beangle.commons.collection.Collections
import org.beangle.commons.collection.Order
import org.beangle.commons.lang.Strings
import org.beangle.commons.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.base.model.Semester
import org.openurp.edu.base.code.model.StdType
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.openurp.edu.evaluation.lesson.stat.model.CourseEvalStat
import org.openurp.edu.evaluation.lesson.stat.model.LessonEvalStat
import org.openurp.edu.evaluation.lesson.stat.model.LessonOptionStat
import org.openurp.edu.evaluation.lesson.stat.model.LessonQuestionStat
import org.openurp.edu.evaluation.lesson.stat.model.LessonQuestionTypeStat
import org.openurp.edu.evaluation.lesson.stat.model.OptionStat
import org.openurp.edu.evaluation.lesson.stat.model.QuestionStat
import org.openurp.edu.evaluation.lesson.stat.model.QuestionTypeStat
import org.openurp.edu.evaluation.model.Option
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.evaluation.model.QuestionType
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.edu.lesson.model.Lesson
import org.openurp.edu.base.code.model.Education
import org.openurp.edu.base.model.Course
import org.openurp.edu.evaluation.lesson.stat.model.CourseQuestionStat
import org.openurp.edu.evaluation.lesson.stat.model.CourseOptionStat
import org.openurp.edu.evaluation.lesson.stat.model.CourseQuestionTypeStat
import org.openurp.edu.base.model.Teacher

class CourseEvalStatAction extends RestfulAction[CourseEvalStat] {

  override def index(): String = {
    val stdType = entityDao.get(classOf[StdType], 5)
    put("stdTypeList", stdType)
    val department = entityDao.get(classOf[Department], 20)
    put("departmentList", department)

    var searchFormFlag = get("searchFormFlag").orNull
    if (searchFormFlag == null) {
      searchFormFlag = "beenStat"
    }
    put("searchFormFlag", searchFormFlag)
    //    put("educations", getEducations())
    put("departments", entityDao.search(OqlBuilder.from(classOf[Department], "dep").where("dep.teaching =:tea", true)))
    val query = OqlBuilder.from(classOf[Questionnaire], "questionnaire").where("questionnaire.state =:state", true)
    put("questionnaires", entityDao.search(query))
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)
    forward()
  }

  override def search(): String = {
    // 页面条件
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val courseEvalStat = OqlBuilder.from(classOf[CourseEvalStat], "courseEvalStat")
    populateConditions(courseEvalStat)
    courseEvalStat.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    courseEvalStat.where("courseEvalStat.semester=:semester", semester)
    //    get("evaluateTeacherStat.teacher.person.name.formatedName") foreach{ n=>
    //      lessonEvalStat.where("lessonEvalStat.teacher.person.name.formatedName=:formatedName",n)
    //    }
    put("courseEvalStats", entityDao.search(courseEvalStat))
    forward()
  }

  /**
   * 教师历史评教
   */
  def evaluateTeachHistory(): String = {
    val id = getLong("courseEvalStat.id").get
    val questionnaires = entityDao.get(classOf[CourseEvalStat], id)
    val query = OqlBuilder.from(classOf[CourseEvalStat], "questionnaires")
    query.where("questionnaires.teacher.id=:teaIds", questionnaires.teacher.id)
    query.orderBy("questionnaires.semester.beginOn")
    put("teacher", questionnaires.teacher)
    put("courseEvaluates", entityDao.search(query))
    forward()
  }

  /**
   * 跳转(统计首页面)
   */
  def statHome(): String = {
    put("stdTypeList", entityDao.getAll(classOf[StdType]))
    put("departmentList", entityDao.getAll(classOf[Department]))

    put("educations", entityDao.getAll(classOf[Education]))
    val teachingDeparts = entityDao.search(OqlBuilder.from(classOf[Department], "depart").where("depart.teaching =:tea", true))
    put("departments", teachingDeparts)

    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)
    forward()
  }

  override def remove(): View = {
    val questionSIds = longIds("courseEvalStat")
    //    val idStr = get("questionnaireStat.id").orNull
    //    val Ids = idStr.split(",")
    //    val questionSIds = new Long[Ids.length]

    //    for (i = 0 i < Ids.length i++) {
    //      questionSIds[i] = Long.valueOf(Ids[i])
    //    }
    val query = OqlBuilder.from(classOf[CourseEvalStat], "questionS")
    query.where("questionS.id in(:ids)", questionSIds)
    entityDao.remove(entityDao.search(query))
    redirect("search", "info.remove.success")
  }

  /**
   * 清除统计数据
   */
  def remove(educationTypeIds: List[Integer], departmentIds: List[Integer], semesterId: Int) {
    val query = OqlBuilder.from(classOf[CourseEvalStat], "questionS")
    query.where("questionS.semester.id=:semesterId", semesterId)
    entityDao.remove(entityDao.search(query))
  }
  /**
   * 统计(任务评教结果)
   *
   * @return
   */
  def stat(): View = {
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
    que.select("questionR.result.teacher.id,questionR.result.lesson.course.id,questionR.question.id,sum(questionR.score),avg(questionR.score),count(questionR.id)")
    que.groupBy("questionR.result.teacher.id,questionR.result.lesson.course.id,questionR.question.id")
    val wtStatMap = new collection.mutable.HashMap[Tuple2[Any, Any], Buffer[Tuple4[Long, Number, Number, Number]]]
    entityDao.search(que) foreach { a =>
      val buffer = wtStatMap.getOrElseUpdate((a(0), a(1)), new ListBuffer[Tuple4[Long, Number, Number, Number]])
      buffer += Tuple4(a(2).asInstanceOf[Long], a(3).asInstanceOf[Number], a(4).asInstanceOf[Number], a(5).asInstanceOf[Number])
    }
    // 问卷得分统计
    val quer = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    quer.where("questionR.result.lesson.semester.id=:semesterId", semesterId)
    quer.where("questionR.result.statType is 1")
    quer.where("questionR.result.department.id in(:depIds)", departmentIds)
    //    quer.where("questionR.result.lesson.course.education.id in(:eduIds)", educationTypeIds)
    quer.where("questionR.question.addition is false")
    quer.select("questionR.result.lesson.course.id,questionR.result.teacher.id,questionR.result.questionnaire.id," + "sum(questionR.score),case when questionR.result.statType =1 then count(questionR.result.id) end," + "count(distinct questionR.result.id),case when questionR.result.statType =1 then sum(questionR.score) end," + "sum(questionR.score)/count(distinct questionR.result.id)")
    quer.groupBy("questionR.result.lesson.course.id,questionR.result.teacher.id,questionR.result.questionnaire.id,questionR.result.statType")
    val wjStat = entityDao.search(quer)
    // 排名
    ////    val query = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    //    val query = "select qR.result.department.id,qR.result.lesson.id,qR.result.teacher.id,"+
    //    "sum(qR.score)/count(distinct qR.result.id), "+
    //    "rank()over(order by sum(qR.score)/count(distinct qR.result.id) desc), "+
    //    "rank()over(partition by qR.result.department.id order by sum(qR.score)/count(distinct qR.result.id) desc) "+
    //    " from  org.openurp.edu.evaluation.lesson.result.model.QuestionResult qR "+
    //    "where qR.result.lesson.semester.id="+ semesterId +" " +
    //    "and qR.result.statType is 1 "+
    //    "and qR.result.teacher is not null "+
    ////    query.where(" questionR.result.lesson.semester.id=:semesterId",semesterId)
    ////    query.where( "questionR.result.statType is 1")
    ////    query.where ("questionR.result.teacher is not null")
    ////     query.where("questionR.result.department.id in(:depIds)", departmentIds)
    ////    query.where("questionR.result.lesson.course.education.id in(:eduIds)", educationTypeIds)
    //    "and qR.question.addition is false "+
    ////    query.select("questionR.result.department.id,questionR.result.lesson.id,questionR.result.teacher.id,"+
    ////        "sum(questionR.score)/count(distinct questionR.result.id) as x,"+
    ////        "rank() over(order by x desc),"+
    ////        "rank() over(partition by questionR.result.department.id order by x desc) ")
    //    "group by qR.result.department.id,qR.result.lesson.id,qR.result.teacher.id"
    ////    query.orderBy("sum(questionR.score)/count(distinct questionR.result.id) desc,questionR.result.department.id,questionR.result.teacher.id")
    //    val pmStatMap = new collection.mutable.HashMap[Tuple2[Any,Any],Tuple3[Number,Integer,Integer]]
    //    entityDao.search[Array[Any]](query) foreach { a =>
    //    pmStatMap.getOrElseUpdate((a(1),a(2)),Tuple3(a(3).asInstanceOf[Number],a(4).asInstanceOf[Integer],a(5).asInstanceOf[Integer]))
    //    }
    // 问题类别统计
    val tyquery = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    tyquery.where("questionR.result.lesson.semester.id=:semesterId", semesterId)
    tyquery.where("questionR.result.statType is 1")
    tyquery.where("questionR.result.teacher is not null")
    tyquery.where("questionR.result.department.id in(:depIds)", departmentIds)
    tyquery.where("questionR.question.addition is false")
    //    tyquery.where("questionR.result.lesson.course.education.id in(:eduIds)", educationTypeIds)
    tyquery.select("questionR.result.lesson.course.id,questionR.result.teacher.id,questionR.question.questionType.id,sum(questionR.score)/count(distinct questionR.result.id)")
    tyquery.groupBy("questionR.result.lesson.course.id,questionR.result.teacher.id,questionR.question.questionType.id")

    val typeStatMap = new collection.mutable.HashMap[Tuple2[Any, Any], Buffer[Tuple2[Long, Number]]]
    entityDao.search(tyquery) foreach { a =>
      val buffer = typeStatMap.getOrElseUpdate((a(0), a(1)), new ListBuffer[Tuple2[Long, Number]])
      buffer += Tuple2(a(2).asInstanceOf[Long], a(3).asInstanceOf[Number])
    }
    // 选项统计
    val opQuery = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    opQuery.where("questionR.result.lesson.semester.id=:semesterId", semesterId)
    opQuery.where("questionR.result.statType is 1")
    opQuery.where("questionR.result.department.id in(:depIds)", departmentIds)
    opQuery.where("questionR.question.addition is false")
    //    opQuery.where("questionR.result.lesson.course.education.id in(:eduIds)", educationTypeIds)
    opQuery.select("questionR.result.lesson.course.id," + "questionR.result.teacher.id,questionR.question.id,questionR.option.id,count(questionR.id)")
    opQuery.groupBy("questionR.result.lesson.course.id,questionR.result.teacher.id,questionR.question.id,questionR.option.id")
    val optionStatMap = new collection.mutable.HashMap[Tuple3[Any, Any, Any], Buffer[Tuple2[Long, Number]]]
    entityDao.search(opQuery) foreach { a =>
      val buffer = optionStatMap.getOrElseUpdate((a(0), a(1), a(2)), new ListBuffer[Tuple2[Long, Number]])
      buffer += Tuple2(a(3).asInstanceOf[Long], a(4).asInstanceOf[Number])
    }

    val questionMap = entityDao.getAll(classOf[Question]).map(o => (o.id, o)).toMap
    val questiontyMap = entityDao.getAll(classOf[QuestionType]).map(o => (o.id, o)).toMap
    val optionMap = entityDao.getAll(classOf[Option]).map(o => (o.id, o)).toMap

    // 任务
    val lquery = OqlBuilder.from(classOf[Lesson], "le")
    lquery.where("le.semester.id=:seId", semesterId)
    //  lquery.where("le.course.education.id in(:eduIds)", educationTypeIds)
    lquery.where("le.teachDepart.id in(:depIds)", departmentIds)
    val lessonList = entityDao.search(lquery)
    //任务问卷得分统计
    wjStat foreach { evaObject =>
      val questionS = new CourseEvalStat
      questionS.teacher = new Teacher()
      questionS.teacher.id = evaObject(1).asInstanceOf[Long]
      questionS.semester = semester
      questionS.statAt = new java.util.Date()
      questionS.course = new Course()
      questionS.course.id = evaObject(0).asInstanceOf[Long]
      questionS.score = evaObject(7).toString().toFloat * 10
      questionS.validScore = evaObject(6).toString().toFloat * 10
      questionS.validTickets = Integer.valueOf(evaObject(4).toString())
      questionS.allTickets = Integer.valueOf(evaObject(5).toString())
      // 添加问卷
      questionS.questionnaire = new Questionnaire()
      questionS.questionnaire.id = evaObject(2).asInstanceOf[Long]
      // 添加问题得分统计
      val questionDetailStats = Collections.newBuffer[QuestionStat]
      wtStatMap.get((questionS.teacher.id, questionS.course.id)) foreach { buffer =>
        buffer foreach { wt =>
          val detailStat = new CourseQuestionStat
          // 添加问题
          detailStat.question = questionMap(wt._1)
          detailStat.total = wt._2.toString().toFloat * 100
          detailStat.average = wt._3.toString().toFloat * 100
          //            detailStat.stddev=stddev
          detailStat.evalStat = questionS

          // 添加选项统计
          val optionStates = Collections.newBuffer[OptionStat]
          optionStatMap.get((questionS.course.id, questionS.teacher.id, detailStat.question.id)) foreach { buffer =>
            buffer foreach { os =>
              val optionstat = new CourseOptionStat
              optionstat.amount = os._2.intValue()
              optionstat.option = optionMap(os._1)
              optionstat.questionStat = detailStat
              optionStates += optionstat
            }
          }
          detailStat.optionStats = optionStates
          questionDetailStats += detailStat
        }
      }
      questionS.questionStats = questionDetailStats
      //           添加排名

      //            pmStatMap.get(questionS.lesson.id,questionS.teacher.id) foreach { pm =>
      //                questionS.rank= pm._2.intValue()
      //                questionS.departRank= pm._3.intValue()
      //            }
      //           添加问题类别统计
      val questionTypeStats = Collections.newBuffer[QuestionTypeStat]
      typeStatMap.get((questionS.course.id, questionS.teacher.id)) foreach { buffer =>
        buffer foreach { os =>
          val questionTs = new CourseQuestionTypeStat
          questionTs.score = os._2.toString().toFloat * 100
          questionTs.evalStat = questionS
          questionTs.questionType = questiontyMap(os._1)
          questionTypeStats += questionTs
        }
      }
      questionS.questionTypeStats = questionTypeStats
      entityDao.saveOrUpdate(questionS)
    }

    redirect("index", "info.action.success")
  }
}
