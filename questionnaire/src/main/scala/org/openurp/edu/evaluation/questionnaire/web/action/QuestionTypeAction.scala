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

import java.util.Date
import java.sql.Date
import org.beangle.commons.collection.Order
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.model.QuestionType
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.base.model.Project
import java.time.Instant
import org.springframework.cglib.core.Local
import java.time.LocalDate

class QuestionTypeAction extends RestfulAction[QuestionType] {

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
      val projects = entityDao.findBy(classOf[Project], "code", List(get("project").get));
      questionType.project = projects.head
      questionType.updatedAt = Instant.now

      val name = questionType.name;
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
      if (!questionType.persisted) {
        if (questionType.state) {
          questionType.beginOn = LocalDate.now
        }
      } else {
        questionType.updatedAt = Instant.now
        val questionTypeOld = entityDao.get(classOf[QuestionType], questionType.id);
        if (questionTypeOld.state != questionType.state) {
          if (questionType.state) {
            questionType.beginOn = LocalDate.now
          } else {
            questionType.endOn = Some(LocalDate.now)
          }
        }
      }
      entityDao.saveOrUpdate(questionType);
      return redirect("search", "info.save.success");
    } catch {
      case e: Exception =>
        logger.info("saveAndForwad failure", e);
        return redirect("search", "info.save.failure");
    }
  }

  override def remove(): View = {
    val questionTypeIds = longIds("questionType")
    val query1 = OqlBuilder.from(classOf[QuestionType], "questionType")
    query1.where("questionType.id in (:questionTypeIds)", questionTypeIds)
    val questionTypes = entityDao.search(query1);

    val query = OqlBuilder.from(classOf[Question], "question");
    query.where("question.questionType in (:questionTypes)", questionTypes);
    val questions = entityDao.search(query);
    if (!questions.isEmpty) { return redirect("search", "删除失败,选择的数据中已有被评教问题引用"); }

    entityDao.remove(questionTypes);
    return redirect("search", "info.remove.success");
  }

}
