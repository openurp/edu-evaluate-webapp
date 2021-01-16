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
package org.openurp.edu.evaluation.questionnaire.web.action

import java.time.{Instant, LocalDate}

import org.beangle.commons.collection.Order
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.boot.edu.helper.ProjectSupport
import org.openurp.edu.evaluation.model.{Question, QuestionType}

class QuestionTypeAction extends RestfulAction[QuestionType] with ProjectSupport {

  override def search(): View = {
    val builder = OqlBuilder.from(classOf[QuestionType], "questionType")
    populateConditions(builder)
    builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    val questionTypes = entityDao.search(builder)
    put("questionTypes", questionTypes)
    forward()
  }

  protected override def saveAndRedirect(entity: QuestionType): View = {
    try {
      val questionType = entity.asInstanceOf[QuestionType]
      questionType.project = getProject
      questionType.updatedAt = Instant.now

      val name = questionType.name
      val enName = questionType.enName.orNull
      val remark = questionType.remark.orNull
      questionType.beginOn = LocalDate.parse(get("questionType.beginOn").get)
      questionType.endOn =
        get("questionType.endOn") match {
          case Some(endOn) =>
            if (endOn.isEmpty) None else Some(LocalDate.parse(endOn))
          case None => None
        }
      if (remark != null) {
        questionType.remark = Some(remark.replaceAll("<", "&#60;").replaceAll(">", "&#62;"))
      }
      if (enName != null) {
        questionType.enName = Some(enName.replaceAll("<", "&#60;").replaceAll(">", "&#62;"))
      }
      questionType.name = name.replaceAll("<", "&#60;").replaceAll(">", "&#62;")
      questionType.updatedAt = Instant.now
      entityDao.saveOrUpdate(questionType)
      redirect("search", "info.save.success")
    } catch {
      case e: Exception =>
        logger.info("saveAndForwad failure", e)
        redirect("search", "info.save.failure")
    }
  }

  override def remove(): View = {
    val questionTypeIds = longIds("questionType")
    val query1 = OqlBuilder.from(classOf[QuestionType], "questionType")
    query1.where("questionType.id in (:questionTypeIds)", questionTypeIds)
    val questionTypes = entityDao.search(query1)

    val query = OqlBuilder.from(classOf[Question], "question")
    query.where("question.questionType in (:questionTypes)", questionTypes)
    val questions = entityDao.search(query)
    if (questions.nonEmpty) { return redirect("search", "删除失败,选择的数据中已有被评教问题引用"); }

    entityDao.remove(questionTypes)
    redirect("search", "info.remove.success")
  }

}
