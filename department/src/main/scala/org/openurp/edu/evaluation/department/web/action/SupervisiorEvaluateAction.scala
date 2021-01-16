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
package org.openurp.edu.evaluation.department.web.action

import java.time.Instant

import org.beangle.commons.collection.{Collections, Order}
import org.beangle.commons.lang.ClassLoaders
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.transfer.importer.ImportSetting
import org.beangle.data.transfer.importer.listener.ForeignerListener
import org.beangle.webmvc.api.view.{Stream, View}
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.edu.model.{Semester, Teacher}
import org.openurp.base.model.Department
import org.openurp.boot.edu.helper.ProjectSupport
import org.openurp.edu.clazz.model.Clazz
import org.openurp.edu.evaluation.app.department.model.EvaluateSwitch
import org.openurp.edu.evaluation.department.helper.ImportSupervisiorListener
import org.openurp.edu.evaluation.department.model.{SupervisiorEvaluate, SupervisiorQuestion}
import org.openurp.edu.evaluation.model.{Question, QuestionType, Questionnaire}

import scala.collection.mutable.Buffer

/**
 * @author xinzhou
 */
class SupervisiorEvaluateAction extends RestfulAction[SupervisiorEvaluate] with ProjectSupport {

  override def indexSetting(): Unit = {
    put("departments", findInSchool(classOf[Department]))
    put("semesters", entityDao.getAll(classOf[Semester]))
    put("currentSemester", getCurrentSemester)
  }

  def importTeachers(): View = {
    val builder = OqlBuilder.from[Array[Any]](classOf[Clazz].getName, "clazz")
    getInt("supervisiorEvaluate.semester.id") foreach { semesterId => builder.where("clazz.semester.id=:id", semesterId) }
    builder.join("clazz.teachers", "teacher")
    builder.select("distinct teacher.id , clazz.teachDepart.id , clazz.semester.id")
    builder.where("not exists (from " + classOf[SupervisiorEvaluate].getName + " se where se.semester = clazz.semester and se.teacher = teacher and se.department = clazz.teachDepart)")
    val datas = entityDao.search(builder)
    val supervisiorEvaluates = Collections.newBuffer[SupervisiorEvaluate]
    datas foreach { data =>
      val supervisiorEvaluate = new SupervisiorEvaluate
      supervisiorEvaluate.teacher = new Teacher
      supervisiorEvaluate.teacher.id = data(0).asInstanceOf[Long]
      supervisiorEvaluate.department = new Department
      supervisiorEvaluate.department.id = data(1).asInstanceOf[Int]
      supervisiorEvaluate.semester = new Semester
      supervisiorEvaluate.semester.id = data(2).asInstanceOf[Int]
      supervisiorEvaluate.evaluateAt = Instant.now
      supervisiorEvaluate.questionnaire = entityDao.get(classOf[Questionnaire], 322L)
      supervisiorEvaluates += supervisiorEvaluate
    }
    entityDao.saveOrUpdate(supervisiorEvaluates)
    val semesterId = get("supervisiorEvaluate.semester.id").orNull
    redirect("search", s"orderBy=supervisiorEvaluate.teacher.user.code asc&supervisiorEvaluate.semester.id=$semesterId", "导入完成")
  }

  override protected def getQueryBuilder(): OqlBuilder[SupervisiorEvaluate] = {
    val query = OqlBuilder.from(classOf[SupervisiorEvaluate], "supervisiorEvaluate")
    getBoolean("passed") match {
      case Some(true) => query.where("supervisiorEvaluate.totalScore is not null")
      case Some(false) => query.where("supervisiorEvaluate.totalScore is null")
      case None =>
    }
    populateConditions(query)
    query.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
  }

  override def editSetting(supervisiorEvaluate: SupervisiorEvaluate): Unit = {
    val semesterId = intId("supervisiorEvaluate.semester")
    put("semester", entityDao.get(classOf[Semester], semesterId))

    val esbuilder = OqlBuilder.from(classOf[EvaluateSwitch], "es")
    esbuilder.where("es.questionnaire.id =:quId", 322L)
    esbuilder.where("es.semester.id = :semesterId", semesterId)
    esbuilder.where("es.opened = :opened", true)
    val evaluateSwitches = entityDao.search(esbuilder)
    put("evaluateSwitches", evaluateSwitches)

    if (evaluateSwitches.nonEmpty) {
      val questionnaire = evaluateSwitches.head.questionnaire
      put("questionnaire", questionnaire)

      val questionTree = Collections.newMap[QuestionType, Buffer[Question]]
      questionnaire.questions foreach { question =>
        val key = question.questionType
        var questions: Buffer[Question] = questionTree.get(key).orNull
        if (null == questions) {
          questions = Collections.newBuffer
        }
        questions += question
        questions.sortWith((x, y) => x.priority < y.priority)
        questionTree.put(key, questions)
      }
      put("questionTree", questionTree)

    }
    val resultMap = Collections.newMap[Question, Float]
    supervisiorEvaluate.questionResults foreach { qr =>
      resultMap.put(qr.question, qr.score)
    }
    put("resultMap", resultMap)
    super.editSetting(supervisiorEvaluate)
  }

  override def saveAndRedirect(supervisiorEvaluate: SupervisiorEvaluate): View = {
    val questionnaire = entityDao.get(classOf[Questionnaire], 322L)
    val resultMap = Collections.newMap[Question, SupervisiorQuestion]
    supervisiorEvaluate.questionResults foreach { qr =>
      resultMap.put(qr.question, qr)
    }
    questionnaire.questions foreach { question =>
      resultMap.get(question) match {
        case Some(qr) => qr.score = getFloat(s"${question.id}_score").get
        case None =>
          val qr = new SupervisiorQuestion
          qr.question = question
          qr.result = supervisiorEvaluate
          qr.score = getFloat(s"${question.id}_score").get
          supervisiorEvaluate.questionResults += qr
      }
    }
    supervisiorEvaluate.calTotalScore()
    super.saveAndRedirect(supervisiorEvaluate)
  }

  def importTemplate: View = {
    Stream(ClassLoaders.getResourceAsStream("supervisiorEvaluate.xls").get, "application/vnd.ms-excel", "评教结果.xls")
  }

  protected override def configImport(setting: ImportSetting): Unit = {
    setting.listeners = List(new ForeignerListener(entityDao), new ImportSupervisiorListener(entityDao))
  }
}
