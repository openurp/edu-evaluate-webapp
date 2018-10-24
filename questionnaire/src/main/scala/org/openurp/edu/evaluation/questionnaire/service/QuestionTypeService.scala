/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.edu.evaluation.questionnaire.service

import org.openurp.edu.evaluation.model.Question
import org.beangle.data.dao.EntityDao
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.evaluation.model.QuestionType
import java.util.Date
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.evaluation.model.Question
import java.time.LocalDate

class QuestionTypeService(entityDao: EntityDao) {

  def getQuestionTypes(): Seq[QuestionType] = {
    val query = OqlBuilder.from(classOf[QuestionType], "type");
    query.where("type.state=true");
    query.where("type.beginOn <= :now and (type.endOn is null or type.endOn >= :now)", LocalDate.now);
    entityDao.search(query)
  }

  def getQuestionType(questionTypeId: String): QuestionType = {
    if (questionTypeId == null) { return null; }
    entityDao.get(classOf[QuestionType], questionTypeId.toLong)
  }

  def getQuestionType(questionTypeId: Long): QuestionType = {
    entityDao.get(classOf[QuestionType], questionTypeId);
  }

  def getQuestionTypesScore(): collection.Map[Long, Number] = {
    val query = OqlBuilder.from[Array[Any]](classOf[Question].getName, "question");
    query.groupBy("question.questionType.id").select("question.questionType.id,sum(question.score)")
    entityDao.search(query).map(obj => (obj(0).asInstanceOf[Number].longValue, obj(1).asInstanceOf[Number])).toMap
  }

  def getQuestionTypeMap(): collection.Map[Long, QuestionType] = {
    entityDao.getAll(classOf[QuestionType]).map(x => (x.id, x)).toMap
  }
}
