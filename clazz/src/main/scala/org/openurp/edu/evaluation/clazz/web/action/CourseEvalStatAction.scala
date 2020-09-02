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
package org.openurp.edu.evaluation.clazz.web.action

import java.time.{Instant, LocalDate}

import org.beangle.commons.collection.{Collections, Order}
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.openurp.base.model.Department
import org.openurp.code.edu.model.EducationLevel
import org.openurp.edu.base.code.model.StdType
import org.openurp.edu.base.model.{Course, Semester, Teacher}
import org.openurp.edu.clazz.model.Clazz
import org.openurp.edu.evaluation.app.course.service.Ranker
import org.openurp.edu.evaluation.clazz.result.model.QuestionResult
import org.openurp.edu.evaluation.clazz.stat.model._
import org.openurp.edu.evaluation.model.{Option, Question, QuestionType, Questionnaire}

import scala.collection.mutable.{Buffer, ListBuffer}

class CourseEvalStatAction extends ProjectRestfulAction[CourseEvalStat] {

  override def index(): View = {
    val project = getProject
    put("project",project)
    put("stdTypeList", project.stdTypes)
    put("departmentList", project.departments)

    var searchFormFlag = get("searchFormFlag").orNull
    if (searchFormFlag == null) {
      searchFormFlag = "beenStat"
    }
    put("searchFormFlag", searchFormFlag)
    //    put("educations", getEducationLevels())
    put("departments", entityDao.search(OqlBuilder.from(classOf[Department], "dep").where("dep.teaching =:tea", true)))
    val query = OqlBuilder.from(classOf[Questionnaire], "questionnaire")
    put("questionnaires", entityDao.search(query))
    put("currentSemester", getCurrentSemester)
    forward()
  }

  override def search(): View = {
    val courseEvalStat = OqlBuilder.from(classOf[CourseEvalStat], "courseEvalStat")
    populateConditions(courseEvalStat)
    courseEvalStat.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    put("courseEvalStats", entityDao.search(courseEvalStat))
    forward()
  }

  /**
   * 教师历史评教
   */
  def evaluateTeachHistory(): View = {
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
  def statHome(): View = {
    put("stdTypeList", entityDao.getAll(classOf[StdType]))
    put("departmentList", entityDao.getAll(classOf[Department]))

    put("educations", entityDao.getAll(classOf[EducationLevel]))
    val teachingDeparts = entityDao.search(OqlBuilder.from(classOf[Department], "depart").where("depart.teaching =:tea", true))
    put("departments", teachingDeparts)

    put("currentSemester", this.getCurrentSemester)
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
  def remove(educationTypeIds: Seq[Int], departmentIds: Seq[Int], semesterId: Int): Unit = {
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
    val educationTypeIds = Strings.splitToInt(eduStr)
    val departmentIds = Strings.splitToInt(depStr)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    // 删除历史统计数据
    remove(educationTypeIds, departmentIds, semesterId)
    // 问题得分统计
    val que = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    que.where("questionR.result.clazz.semester.id=:semesterId", semesterId)
    que.where("questionR.result.statType is 1")
    que.where("questionR.question.addition is false")
    que.where("questionR.result.department.id in(:depIds)", departmentIds)
    //  que.where("questionR.result.clazz.course.education.id in(:eduIds)", educationTypeIds)
    que.select("questionR.result.teacher.id,questionR.result.clazz.course.id,questionR.question.id,sum(questionR.score),avg(questionR.score),count(questionR.id)")
    que.groupBy("questionR.result.teacher.id,questionR.result.clazz.course.id,questionR.question.id")
    val wtStatMap = new collection.mutable.HashMap[Tuple2[Any, Any], Buffer[Tuple4[Long, Number, Number, Number]]]
    entityDao.search(que) foreach { a =>
      val buffer = wtStatMap.getOrElseUpdate((a(0), a(1)), new ListBuffer[Tuple4[Long, Number, Number, Number]])
      buffer += Tuple4(a(2).asInstanceOf[Long], a(3).asInstanceOf[Number], a(4).asInstanceOf[Number], a(5).asInstanceOf[Number])
    }
    // 问卷得分统计
    val quer = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    quer.where("questionR.result.clazz.semester.id=:semesterId", semesterId)
    quer.where("questionR.result.statType is 1")
    quer.where("questionR.result.department.id in(:depIds)", departmentIds)
    //    quer.where("questionR.result.clazz.course.education.id in(:eduIds)", educationTypeIds)
    quer.where("questionR.question.addition is false")
    quer.select("questionR.result.clazz.course.id,questionR.result.teacher.id,questionR.result.questionnaire.id," + "sum(questionR.score),case when questionR.result.statType =1 then count(questionR.result.id) end," + "count(distinct questionR.result.id),case when questionR.result.statType =1 then sum(questionR.score) end," + "sum(questionR.score)/count(distinct questionR.result.id)")
    quer.groupBy("questionR.result.clazz.course.id,questionR.result.teacher.id,questionR.result.questionnaire.id,questionR.result.statType")
    val wjStat = entityDao.search(quer)

    // 问题类别统计
    val tyquery = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    tyquery.where("questionR.result.clazz.semester.id=:semesterId", semesterId)
    tyquery.where("questionR.result.statType is 1")
    tyquery.where("questionR.result.teacher is not null")
    tyquery.where("questionR.result.department.id in(:depIds)", departmentIds)
    tyquery.where("questionR.question.addition is false")
    //    tyquery.where("questionR.result.clazz.course.education.id in(:eduIds)", educationTypeIds)
    tyquery.select("questionR.result.clazz.course.id,questionR.result.teacher.id,questionR.question.questionType.id,sum(questionR.score)/count(distinct questionR.result.id)")
    tyquery.groupBy("questionR.result.clazz.course.id,questionR.result.teacher.id,questionR.question.questionType.id")

    val typeStatMap = new collection.mutable.HashMap[Tuple2[Any, Any], Buffer[Tuple2[Long, Number]]]
    entityDao.search(tyquery) foreach { a =>
      val buffer = typeStatMap.getOrElseUpdate((a(0), a(1)), new ListBuffer[Tuple2[Long, Number]])
      buffer += Tuple2(a(2).asInstanceOf[Long], a(3).asInstanceOf[Number])
    }
    // 选项统计
    val opQuery = OqlBuilder.from[Array[Any]](classOf[QuestionResult].getName, "questionR")
    opQuery.where("questionR.result.clazz.semester.id=:semesterId", semesterId)
    opQuery.where("questionR.result.statType is 1")
    opQuery.where("questionR.result.department.id in(:depIds)", departmentIds)
    opQuery.where("questionR.question.addition is false")
    //    opQuery.where("questionR.result.clazz.course.education.id in(:eduIds)", educationTypeIds)
    opQuery.select("questionR.result.clazz.course.id," + "questionR.result.teacher.id,questionR.question.id,questionR.option.id,count(questionR.id)")
    opQuery.groupBy("questionR.result.clazz.course.id,questionR.result.teacher.id,questionR.question.id,questionR.option.id")
    val optionStatMap = new collection.mutable.HashMap[Tuple3[Any, Any, Any], Buffer[Tuple2[Long, Number]]]
    entityDao.search(opQuery) foreach { a =>
      val buffer = optionStatMap.getOrElseUpdate((a(0), a(1), a(2)), new ListBuffer[Tuple2[Long, Number]])
      buffer += Tuple2(a(3).asInstanceOf[Long], a(4).asInstanceOf[Number])
    }

    val questionMap = entityDao.getAll(classOf[Question]).map(o => (o.id, o)).toMap
    val questiontyMap = entityDao.getAll(classOf[QuestionType]).map(o => (o.id, o)).toMap
    val optionMap = entityDao.getAll(classOf[Option]).map(o => (o.id, o)).toMap

    // 任务
    val lquery = OqlBuilder.from(classOf[Clazz], "le")
    lquery.where("le.semester.id=:seId", semesterId)
    //  lquery.where("le.course.education.id in(:eduIds)", educationTypeIds)
    lquery.where("le.teachDepart.id in(:depIds)", departmentIds)
    val clazzList = entityDao.search(lquery)
    //任务问卷得分统计
    wjStat foreach { evaObject =>
      val questionS = new CourseEvalStat
      questionS.teacher = new Teacher()
      questionS.teacher.id = evaObject(1).asInstanceOf[Long]
      questionS.semester = semester
      questionS.statAt = Instant.now
      questionS.course = new Course()
      questionS.course.id = evaObject(0).asInstanceOf[Long]
      questionS.totalScore = evaObject(7).toString().toFloat
      questionS.tickets = Integer.valueOf(evaObject(4).toString())
      //      questionS.avgScore = questionS.totalScore / questionS.tickets
      questionS.totalTickets = Integer.valueOf(evaObject(5).toString())
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
          detailStat.totalScore = wt._2.toString().toFloat
          detailStat.avgScore = wt._3.toString().toFloat
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
      //           添加问题类别统计
      val questionTypeStats = Collections.newBuffer[QuestionTypeStat]
      typeStatMap.get((questionS.course.id, questionS.teacher.id)) foreach { buffer =>
        buffer foreach { os =>
          val questionTs = new CourseQuestionTypeStat
          questionTs.totalScore = os._2.toString().toFloat
          questionTs.evalStat = questionS
          questionTs.questionType = questiontyMap(os._1)
          questionTypeStats += questionTs
        }
      }
      questionS.questionTypeStats = questionTypeStats
      entityDao.saveOrUpdate(questionS)
    }

    val avgQuery = OqlBuilder.from[Array[Any]](classOf[CourseEvalStat].getName, "les")
    avgQuery.select("les.course.id , avg(les.totalScore)").groupBy("les.course.id")
    val avgList = entityDao.search(avgQuery)
    avgList.foreach(avgValue => {
      //      val courseEvalStats = entityDao.findBy(classOf[CourseEvalStat], "course_id", avgValue(0).asInstanceOf[Long])
      val courseEvalStats = entityDao.search(OqlBuilder.from(classOf[CourseEvalStat], "ces").where("ces.course.id =:id ", avgValue(0).asInstanceOf[Long]))
      courseEvalStats.foreach(courseEvalStat => {
        courseEvalStat.avgScore = avgValue(1).toString().toFloat
      })
      entityDao.saveOrUpdate(courseEvalStats)
    })
    //        排名
    val rankQuery = OqlBuilder.from(classOf[CourseEvalStat], "les")
    rankQuery.where("les.semester.id=:semesterId", semesterId)
    val evals = entityDao.search(rankQuery)
    Ranker.over(evals) { (x, r) =>
      x.schoolRank = r
    }
    val departEvalMaps = evals.groupBy(x => x.course.department)
    departEvalMaps.values foreach { departEvals =>
      Ranker.over(departEvals) { (x, r) =>
        x.departRank = r
      }
    }
    entityDao.saveOrUpdate(evals)

    redirect("index", "info.action.success")
  }
}
