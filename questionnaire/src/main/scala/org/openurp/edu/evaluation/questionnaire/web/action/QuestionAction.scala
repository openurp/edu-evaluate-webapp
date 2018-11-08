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

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.model.Question
import org.beangle.data.dao.OqlBuilder
import org.beangle.commons.collection.Order
import org.beangle.commons.lang.Strings
import org.openurp.edu.evaluation.model.Question
import java.sql.Timestamp
import org.beangle.webmvc.api.view.View
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.evaluation.model.Question
import java.sql.Date
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.evaluation.model.OptionGroup
import org.openurp.base.model.Department
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.edu.evaluation.questionnaire.service.QuestionTypeService
import org.openurp.edu.base.model.Project
import java.time.Instant
import java.time.LocalDate

/**
 * 问题维护响应类
 *
 * @author chaostone
 */
class QuestionAction extends RestfulAction[Question] {

  var questionTypeService: QuestionTypeService = _

  override def search(): View = {
    val builder = OqlBuilder.from(classOf[Question], "question")
    populateConditions(builder)
    builder.orderBy(get(Order.OrderStr).getOrElse("question.state desc")).limit(getPageLimit)
    builder.where("question.state=true")
    val questions = entityDao.search(builder)

    put("questions", questions)
    forward()
  }

  protected override def editSetting(entity: Question): Unit = {
    val optionGroups = entityDao.getAll(classOf[OptionGroup])
    put("optionGroups", optionGroups);
    val departmentList = entityDao.getAll(classOf[Department])
    val questionTypes = questionTypeService.getQuestionTypes()
    put("questionTypes", questionTypes);
    put("departmentList", departmentList);
  }

  protected override def saveAndRedirect(entity: Question): View = {
    try {
      val question = entity.asInstanceOf[Question]
      val projects = entityDao.findBy(classOf[Project], "code", get("project"));
      question.project = projects.head
      question.updatedAt = Instant.now
      val remark = question.remark.orNull
      val content = question.content
      question.beginOn = LocalDate.parse(get("question.beginOn").get)
      question.endOn = get("question.endOn").filter(Strings.isNotEmpty(_)).map(LocalDate.parse(_))
      if (remark != null) {
        question.remark = Some(remark.replaceAll("<", "&#60;").replaceAll(">", "&#62;"))
      }
      question.content = content.replaceAll("<", "&#60;").replaceAll(">", "&#62;")
      if (!question.persisted) {
        if (question.state) {
          question.beginOn = LocalDate.now
        }
      } else {
        question.updatedAt = Instant.now
        val questionOld = entityDao.get(classOf[Question], question.id);
        if (questionOld.state != question.state) {
          if (question.state) {
            question.beginOn = LocalDate.now
          } else {
            question.endOn = Some(LocalDate.now)
          }
        }

      }
      entityDao.saveOrUpdate(question);
      return redirect("search", "info.save.success");
    } catch {
      case e: Exception =>
        logger.info("saveAndForwad failure", e);
        return redirect("search", "info.save.failure");
    }
  }

  override def remove(): View = {
    val questionIds = longId("question");
    val questions = entityDao.get(classOf[Question], questionIds);

    val query = OqlBuilder.from(classOf[Questionnaire], "questionnaire");
    query.join("questionnaire.questions", "question");
    query.where("question in (:questions)", questions);
    val questionnaires = entityDao.search(query);
    if (!questionnaires.isEmpty) { redirect("search", "删除失败,选择的数据中已有被评教问卷引用"); }
    entityDao.remove(questions);
    redirect("search", "删除成功");
  }

}
