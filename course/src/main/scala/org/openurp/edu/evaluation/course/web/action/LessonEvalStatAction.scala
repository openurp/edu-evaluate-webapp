/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.edu.evaluation.course.web.action

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.code.model.StdType
import org.openurp.edu.evaluation.model.EvaluationCriteria
import org.openurp.edu.evaluation.model.EvaluationCriteriaItem
import org.openurp.edu.evaluation.model.Option
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.evaluation.model.QuestionType
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.edu.course.model.Clazz
import org.beangle.commons.lang.Strings
import scala.collection.mutable.Buffer
import scala.collection.mutable.ListBuffer
import org.beangle.commons.collection.Order
import org.openurp.edu.evaluation.app.course.service.Ranker
import org.openurp.edu.base.model.Teacher
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.model.Project
import java.time.LocalDate
import java.time.Instant
import org.openurp.edu.evaluation.clazz.stat.model.ClazzOptionStat
import org.openurp.edu.evaluation.clazz.result.model.QuestionResult
import org.openurp.edu.evaluation.clazz.result.model.EvaluateResult
import org.openurp.edu.evaluation.clazz.stat.model.OptionStat
import org.openurp.edu.evaluation.clazz.stat.model.ClazzQuestionStat
import org.openurp.edu.evaluation.clazz.stat.model.ClazzQuestionTypeStat
import org.openurp.edu.evaluation.clazz.stat.model.QuestionStat
import org.openurp.edu.evaluation.clazz.stat.model.QuestionTypeStat
import org.openurp.edu.evaluation.clazz.stat.model.ClazzEvalStat
import org.openurp.code.edu.model.EducationLevel

class ClazzEvalStatAction extends ProjectRestfulAction[ClazzEvalStat] {

  override def index(): View = {
    var searchFormFlag = get("searchFormFlag").orNull
    if (searchFormFlag == null) {
      searchFormFlag = "beenStat"
    }
    put("searchFormFlag", searchFormFlag)
    //    put("educations", getEducationLevels())
    put("departments", findItemsBySchool(classOf[Department]))
    val query = OqlBuilder.from(classOf[Questionnaire], "questionnaire").where("questionnaire.state =:state", true)
    put("questionnaires", entityDao.search(query))
    put("semesters", getSemesters())
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    put("currentSemester", entityDao.search(semesterQuery).head)
    forward()
  }

  override def search(): View = {
    // 页面条件
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val clazzEvalStat = OqlBuilder.from(classOf[ClazzEvalStat], "clazzEvalStat")
    populateConditions(clazzEvalStat)
    clazzEvalStat.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    clazzEvalStat.where("clazzEvalStat.clazz.semester=:semester", semester)
    put("clazzEvalStats", entityDao.search(clazzEvalStat))
    forward()
  }

  override def remove(): View = {
    val questionSIds = longIds("clazzEvalStat")
    val query = OqlBuilder.from(classOf[ClazzEvalStat], "questionS")
    query.where("questionS.id in(:ids)", questionSIds)
    entityDao.remove(entityDao.search(query))
    redirect("search", "info.remove.success")
  }

  /**
   * 清除统计数据
   */
  private def removeStats(project: Project, semesterId: Int) {
    val query = OqlBuilder.from(classOf[ClazzEvalStat], "les")
    query.where("les.clazz.semester.id=:semesterId", semesterId)
    query.where("les.clazz.project=:project", project)
    entityDao.remove(entityDao.search(query))
  }
  /**
   * 院系历史评教
   */
  def depHistoryStat(): View = {
    val lis = entityDao.search(OqlBuilder.from(classOf[EvaluationCriteriaItem], "criteriaItem").where("criteriaItem.criteria.id =:id", 1L))
    if (lis.size < 1) { redirect("search", "未找到评价标准！") }
    put("criterias", lis)
    val depId = getInt("department.id")
    put("departId", depId)
    put("departments", entityDao.search(OqlBuilder.from(classOf[Department], "dep").where("dep.teaching=true")))
    val evaquery = OqlBuilder.from(classOf[EvaluateResult], "evaluateR")
    evaquery.select("distinct evaluateR.clazz.semester.id")
    evaquery.where("evaluateR.clazz.teachDepart.id=:depId", depId)
    val semesterIds = entityDao.search(evaquery)
    val qur = OqlBuilder.from(classOf[Semester], "semester")
    qur.where("semester.beginOn<=:dat", new java.util.Date())
    val quetionQuery = OqlBuilder.from(classOf[ClazzEvalStat], "questionnaireS")
    if (semesterIds.size > 0) {
      qur.where("semester.id in(:ids)", semesterIds)
      quetionQuery.where("questionnaireS.clazz.semester.id in(:semesterIds)", semesterIds)
    } else {
      qur.where("semester.id is null")
      quetionQuery.where("questionnaireS.clazz.semester.id is null")
    }
    quetionQuery.where("questionnaireS.clazz.teachDepart.id=:depId", depId)
    put("evaSemesters", entityDao.search(qur))
    quetionQuery.select("questionnaireS.clazz.semester.id,count(questionnaireS.teacher.id)")
    quetionQuery.groupBy("questionnaireS.clazz.semester.id")
    put("questionNums", entityDao.search(quetionQuery))
    val maps = Collections.newMap[String, Seq[ClazzEvalStat]]
    lis foreach { evaluationCriteriaItem =>
      val query = OqlBuilder.from(classOf[ClazzEvalStat], "questionnaireStat")
      query.select("questionnaireStat.semester.id,count(questionnaireStat.teacher.id)")
      query.where("questionnaireStat.score>=" + evaluationCriteriaItem.min + " and questionnaireStat.score<" + evaluationCriteriaItem.max)
      query.where("questionnaireStat.clazz.teachDepart.id=:depId", depId)
      query.groupBy("questionnaireStat.semester.id")
      maps.put(evaluationCriteriaItem.id.toString(), entityDao.search(query))
    }
    put("questionDeps", maps)
    forward()
  }
  /**
   * 院系评教统计
   */
  def departmentChoiceConfig(): View = {
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val lis = entityDao.search(OqlBuilder.from(classOf[EvaluationCriteriaItem], "criteriaItem").where("criteriaItem.criteria.id =:id", 1L))
    if (lis.size < 1) { redirect("search", "未找到评价标准！") }
    put("criterias", lis)
    put("departments", entityDao.search(OqlBuilder.from(classOf[Department], "depart").where("depart.teaching =:teaching", true)))
    put("semester", semester)
    val que = OqlBuilder.from[Any](classOf[EvaluateResult].getName + " evaluateResult,"
      + classOf[QuestionResult].getName + " questionResult")
    que.select("sum(questionResult.score)/count(distinct evaluateResult.id)")
    que.where("evaluateResult.id=questionResult.result.id")
    que.where("evaluateResult.clazz.semester.id=" + semesterId)
    val lit = entityDao.search(que)
    var fl = 0f
    if (lit.size > 0) {
      if (lit(0) != null) {
        fl = lit(0).toString().toFloat
      }
    }
    put("evaluateResults", fl)
    val query = OqlBuilder.from(classOf[ClazzEvalStat], "evaluateR")
    query.select("evaluateR.clazz.teachDepart.id,count( evaluateR.teacher.id)")
    query.where("evaluateR.clazz.semester.id =:semesterId ", semesterId)
    query.groupBy("evaluateR.clazz.teachDepart.id,evaluateR.clazz.semester.id")
    //    val hql = "select evaluateR.clazz.teachDepart.id,count( evaluateR.teacher.id) from" +
    //    " org.openurp.edu.evaluation.clazz.stat.model.ClazzEvalStat evaluateR "  +
    //    "where evaluateR.clazz.semester.id=" + semesterId + " " +
    //    "group by evaluateR.clazz.teachDepart.id,evaluateR.clazz.semester.id "
    put("questionNums", entityDao.search(query))
    val maps = Collections.newMap[String, Seq[ClazzEvalStat]]
    lis foreach { evaluationCriteriaItem =>
      val query = OqlBuilder.from(classOf[ClazzEvalStat], "questionnaireStat")
      query.where("questionnaireStat.clazz.semester.id=:semesterId", semesterId)
      query.select("questionnaireStat.clazz.teachDepart.id,count(questionnaireStat.teacher.id)")
      query.where("questionnaireStat.score>=" + evaluationCriteriaItem.min
        + " and questionnaireStat.score<" + evaluationCriteriaItem.max)
      query.groupBy("questionnaireStat.clazz.teachDepart.id")
      maps.put(evaluationCriteriaItem.id.toString(), entityDao.search(query))
    }
    put("questionDeps", maps)
    forward()
  }

  /**
   * 历史评教统计
   */
  def historyCollegeStat(): View = {
    val lis = entityDao.search(OqlBuilder.from(classOf[EvaluationCriteriaItem], "criteriaItem").where("criteriaItem.criteria.id =:id", 1L))
    if (lis.size < 1) { redirect("search", "未找到评价标准！") }
    put("criterias", lis)
    val evaquery = OqlBuilder.from(classOf[EvaluateResult], "evaluateR")
    evaquery.select("distinct evaluateR.clazz.semester.id")
    val semesterIds = entityDao.search(evaquery)
    val qur = OqlBuilder.from(classOf[Semester], "semester")
    qur.where("semester.beginOn<=:dat", new java.util.Date())
    val quetionQuery = OqlBuilder.from(classOf[ClazzEvalStat], "questionnaireS")
    if (semesterIds.size > 0) {
      qur.where("semester.id in(:ids)", semesterIds)
      quetionQuery.where("questionnaireS.clazz.semester.id in(:semesterIds)", semesterIds)
    } else {
      qur.where("semester.id is null")
      quetionQuery.where("questionnaireS.clazz.semester.id is null")
    }
    put("evaSemesters", entityDao.search(qur))
    quetionQuery.select("questionnaireS.clazz.semester.id,count(questionnaireS.teacher.id)")
    quetionQuery.groupBy("questionnaireS.clazz.semester.id")
    put("questionNums", entityDao.search(quetionQuery))
    val maps = Collections.newMap[String, Seq[ClazzEvalStat]]
    lis foreach { evaluationCriteriaItem =>
      //    for (EvaluationCriteriaItem evaluationCriteriaItem : lis) {
      val query = OqlBuilder.from(classOf[ClazzEvalStat], "questionnaireStat")
      query.select("questionnaireStat.clazz.semester.id,count(questionnaireStat.teacher.id)")
      query.where("questionnaireStat.score>=" + evaluationCriteriaItem.min
        + " and questionnaireStat.score<" + evaluationCriteriaItem.max)
      query.groupBy("questionnaireStat.clazz.semester.id")
      maps.put(evaluationCriteriaItem.id.toString(), entityDao.search(query))
    }
    put("questionDeps", maps)
    forward()
  }

  /**
   * 分项评教汇总
   */
  def collegeGroupItemInfo(): View = {
    val lis = entityDao.search(OqlBuilder.from(classOf[EvaluationCriteriaItem], "criteriaItem").where("criteriaItem.criteria.id =:id", 1L))
    if (lis.size < 1) { redirect("search", "未找到评价标准！") }
    put("criterias", lis)

    val evaquery = OqlBuilder.from(classOf[ClazzEvalStat], "questionnaireStat")
    evaquery.where("questionnaireStat.clazz.semester.id=:semesId", getInt("semester.id").get)
    evaquery.join("questionnaireStat.questionTypeStats", "questionType")
    evaquery.select("distinct questionType.questionType.id")
    val queTypeIds = entityDao.search(evaquery)

    val quTqur = OqlBuilder.from(classOf[QuestionType], "questionType")
    val quetionQuery = OqlBuilder.from(classOf[ClazzEvalStat], "questionnaireS")
    quetionQuery.join("questionnaireS.questionTypeStats", "questionTypeStat")
    if (queTypeIds.size > 0) {
      quTqur.where("questionType.id in(:ids)", queTypeIds)
      quetionQuery.where("questionTypeStat.questionType.id in(:queTypeIds)", queTypeIds)
    } else {
      quTqur.where("questionType.id is null")
      quetionQuery.where("questionTypeStat.questionType.id is null")
    }
    put("questionTypes", entityDao.search(quTqur))
    quetionQuery.select("questionTypeStat.questionType.id,count(questionnaireS.teacher.id)")
    quetionQuery.groupBy("questionTypeStat.questionType.id")
    put("quesTypeNums", entityDao.search(quetionQuery))

    val maps = Collections.newMap[String, Seq[ClazzEvalStat]]
    lis foreach { evaluationCriteriaItem =>
      val query = OqlBuilder.from(classOf[ClazzEvalStat], "questionnaireStat")
      query.where("questionnaireStat.clazz.semester.id=:semesId", getInt("semester.id").get)
      query.join("questionnaireStat.questionTypeStats", "questionTypeStat")
      query.select("questionTypeStat.questionType.id,count(questionnaireStat.teacher.id)")
      query.where("questionTypeStat.score>=" + evaluationCriteriaItem.min + " and questionTypeStat.score<" + evaluationCriteriaItem.max)
      query.groupBy("questionTypeStat.questionType.id")
      maps.put(evaluationCriteriaItem.id.toString(), entityDao.search(query))
    }
    put("questionDeps", maps)
    val que = OqlBuilder.from(classOf[EvaluateResult], "evaluateR")
    que.where("evaluateR.clazz.semester.id=:seiD", getInt("semester.id").get)
    que.where("evaluateR.statType is 1")
    que.select("distinct evaluateR.teacher.id")
    val list = entityDao.search(que)
    put("persons", list.size)
    //    put("questionTypes", entityDao.getAll(classOf[QuestionType]))
    forward()
  }

  /**
   * 教师历史评教
   */
  def evaluateTeachHistory(): View = {
    val id = getLong("clazzEvalStat.id").get
    val questionnaires = entityDao.get(classOf[ClazzEvalStat], id)
    val query = OqlBuilder.from(classOf[ClazzEvalStat], "questionnaires")
    query.where("questionnaires.teacher.id=:teaIds", questionnaires.teacher.id)
    query.orderBy("questionnaires.semester.beginOn")
    put("teacher", questionnaires.teacher)
    put("teachEvaluates", entityDao.search(query))
    forward()
  }

  def teachQuestionDetailStat(): View = {
    put("questionnaires", entityDao.get(classOf[ClazzEvalStat], getLong("questionnaireStat.id").get))
    forward()
  }

  /**
   * 跳转(统计首页面)
   */
  def statHome(): View = {
    put("stdTypeList", entityDao.getAll(classOf[StdType]))
    put("departmentList", entityDao.getAll(classOf[Department]))

    put("educations", entityDao.getAll(classOf[EducationLevel]))
    val teachingDeparts = entityDao.search(OqlBuilder.from(classOf[Department], "depart").where("depart.teaching =:tea", true))
    put("departments", teachingDeparts)

    val semesters = getSemesters()
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    put("currentSemester", entityDao.search(semesterQuery).head)
    forward()
  }

  /**
   * 跳转(初始有效值页面)
   */
  def initValidHome(): View = {
    forward()
  }

  /**
   * 设置有效记录
   */
  def setValid(): View = {
    forward()
  }

  def rankStat(): View = {
    val semesterId = getInt("semester.id").getOrElse(0)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val project = this.currentProject;
    //    排名
    val rankQuery = OqlBuilder.from(classOf[ClazzEvalStat], "les")
    rankQuery.where("les.clazz.semester.id=:semesterId", semesterId)
    rankQuery.where("les.clazz.project=:project", project)
    val evals = entityDao.search(rankQuery)
    Ranker.over(evals) { (x, r) =>
      x.rank = r;
    }
    val departEvalMaps = evals.groupBy(x => x.clazz.teachDepart)
    departEvalMaps.values foreach { departEvals =>
      Ranker.over(departEvals) { (x, r) =>
        x.departRank = r;
      }
    }
    entityDao.saveOrUpdate(evals);
    redirect("index", "info.action.success")
  }

  /**
   * 统计(任务评教结果)
   * FIXME 去除最高5%和最低分数5%,以及人数少于15人的评教结果,  也没有正确计算问卷总数（只是将有效问卷和问卷总数相等）
   * @return
   */
  def stat(): View = {
    val semesterId = getInt("semester.id").getOrElse(0)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val project = this.currentProject;
    // 删除历史统计数据
    removeStats(project, semesterId)
    // teacher、clazz、question问题得分统计
    val que = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    que.where("questionR.result.clazz.project=:project", project)
    que.where("questionR.result.clazz.semester.id=:semesterId", semesterId)
    que.where("questionR.result.statType is 1")
    que.select("questionR.result.teacher.id,questionR.result.clazz.id,questionR.question.id,sum(questionR.score),avg(questionR.score)")
    que.groupBy("questionR.result.teacher.id,questionR.result.clazz.id,questionR.question.id")
    val wtStatMap = new collection.mutable.HashMap[Tuple2[Any, Any], Buffer[Tuple3[Long, Number, Number]]]
    val rs2 = entityDao.search(que)
    rs2 foreach { a =>
      val buffer = wtStatMap.getOrElseUpdate((a(0), a(1)), new ListBuffer[Tuple3[Long, Number, Number]])
      buffer += Tuple3(a(2).asInstanceOf[Long], a(3).asInstanceOf[Number], a(4).asInstanceOf[Number])
    }
    // 问卷得分统计
    val quer = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    quer.where("questionR.result.clazz.semester.id=:semesterId", semesterId)
    quer.where("questionR.result.clazz.project=:project", project)
    quer.where("questionR.result.statType is 1")
    quer.select("questionR.result.clazz.id,questionR.result.teacher.id,questionR.result.questionnaire.id,"
      + "sum(questionR.score),sum(questionR.score)/count(distinct questionR.result.id),count(distinct questionR.result.id)")
    quer.groupBy("questionR.result.clazz.id,questionR.result.teacher.id,questionR.result.questionnaire.id")

    val wjStat = entityDao.search(quer)
    // 问题类别统计
    val tyquery = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    tyquery.where("questionR.result.clazz.semester.id=:semesterId", semesterId)
    tyquery.where("questionR.result.clazz.project=:project", project)
    tyquery.where("questionR.result.statType is 1")
    tyquery.where("questionR.result.teacher is not null")
    tyquery.select("questionR.result.clazz.id,questionR.result.teacher.id,questionR.question.questionType.id,sum(questionR.score),sum(questionR.score)/count(distinct questionR.result.id)")
    tyquery.groupBy("questionR.result.clazz.id,questionR.result.teacher.id,questionR.question.questionType.id")

    val typeStatMap = new collection.mutable.HashMap[Tuple2[Any, Any], Buffer[Tuple3[Long, Number, Number]]]
    entityDao.search(tyquery) foreach { a =>
      val buffer = typeStatMap.getOrElseUpdate((a(0), a(1)), new ListBuffer[Tuple3[Long, Number, Number]])
      buffer += Tuple3(a(2).asInstanceOf[Long], a(3).asInstanceOf[Number], a(4).asInstanceOf[Number])
    }
    // 选项统计
    val opQuery = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    opQuery.where("questionR.result.clazz.semester.id=:semesterId", semesterId)
    opQuery.where("questionR.result.clazz.project=:project", project)
    opQuery.where("questionR.result.statType is 1")
    opQuery.select("questionR.result.clazz.id," + "questionR.result.teacher.id,questionR.question.id,questionR.option.id,count(questionR.id)")
    opQuery.groupBy("questionR.result.clazz.id,questionR.result.teacher.id,questionR.question.id,questionR.option.id")
    val optionStatMap = new collection.mutable.HashMap[Tuple3[Any, Any, Any], Buffer[Tuple2[Long, Number]]]
    entityDao.search(opQuery) foreach { a =>
      val buffer = optionStatMap.getOrElseUpdate((a(0), a(1), a(2)), new ListBuffer[Tuple2[Long, Number]])
      buffer += Tuple2(a(3).asInstanceOf[Long], a(4).asInstanceOf[Number])
    }

    val questionMap = entityDao.getAll(classOf[Question]).map(o => (o.id, o)).toMap
    val questiontyMap = entityDao.getAll(classOf[QuestionType]).map(o => (o.id, o)).toMap
    val optionMap = entityDao.getAll(classOf[Option]).map(o => (o.id, o)).toMap

    //任务问卷得分统计
    wjStat foreach { evaObject =>
      val questionS = new ClazzEvalStat
      questionS.clazz = new Clazz()
      questionS.clazz.id = evaObject(0).asInstanceOf[Long]
      questionS.teacher = new Teacher()
      questionS.teacher.id = evaObject(1).asInstanceOf[Long]
      questionS.semester = semester
      questionS.statAt = Instant.now
      questionS.questionnaire = new Questionnaire()
      questionS.questionnaire.id = evaObject(2).asInstanceOf[Long]

      questionS.totalScore = evaObject(3).toString().toFloat
      val avgScore = evaObject(4).toString().toFloat
      questionS.avgScore = (Math.round(avgScore * 100) * 1.0 / 100).floatValue
      questionS.tickets = Integer.valueOf(evaObject(5).toString())
      questionS.totalTickets = questionS.tickets
      // 添加问题得分统计
      val questionDetailStats = Collections.newBuffer[QuestionStat]
      wtStatMap.get((questionS.teacher.id, questionS.clazz.id)) foreach { buffer =>
        buffer foreach { wt =>
          val detailStat = new ClazzQuestionStat
          // 添加问题
          detailStat.question = questionMap(wt._1)
          detailStat.totalScore = wt._2.toString().toFloat
          detailStat.avgScore = wt._3.toString().toFloat
          detailStat.evalStat = questionS

          // 添加选项统计
          val optionStates = Collections.newBuffer[OptionStat]
          optionStatMap.get((questionS.clazz.id, questionS.teacher.id, detailStat.question.id)) foreach { buffer =>
            buffer foreach { os =>
              val optionstat = new ClazzOptionStat
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
      //添加问题类别统计
      val questionTypeStats = Collections.newBuffer[QuestionTypeStat]
      typeStatMap.get((questionS.clazz.id, questionS.teacher.id)) foreach { buffer =>
        buffer foreach { os =>
          val questionTs = new ClazzQuestionTypeStat
          questionTs.totalScore = os._2.floatValue
          questionTs.avgScore = os._3.floatValue
          questionTs.evalStat = questionS
          questionTs.questionType = questiontyMap(os._1)
          questionTypeStats += questionTs
        }
      }
      questionS.questionTypeStats = questionTypeStats
      entityDao.saveOrUpdate(questionS)
    }
    //    排名
    val rankQuery = OqlBuilder.from(classOf[ClazzEvalStat], "les")
    rankQuery.where("les.clazz.semester.id=:semesterId", semesterId)
    rankQuery.where("les.clazz.project=:project", project)
    val evals = entityDao.search(rankQuery)
    Ranker.over(evals) { (x, r) =>
      x.rank = r;
    }
    val departEvalMaps = evals.groupBy(x => x.clazz.teachDepart)
    departEvalMaps.values foreach { departEvals =>
      Ranker.over(departEvals) { (x, r) =>
        x.departRank = r;
      }
    }
    entityDao.saveOrUpdate(evals);
    redirect("index", "info.action.success")
  }

}
