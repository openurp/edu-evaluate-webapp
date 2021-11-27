/*
 * Copyright (C) 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openurp.qos.evaluation.questionnaire.service

import java.time.LocalDate

import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.openurp.qos.evaluation.model.{Question, Indicator}

class IndicatorService(entityDao: EntityDao) {

  def getIndicators(): Seq[Indicator] = {
    val query = OqlBuilder.from(classOf[Indicator], "type")
    query.where("type.beginOn <= :now and (type.endOn is null or type.endOn >= :now)", LocalDate.now)
    entityDao.search(query)
  }

  def getIndicator(indicatorId: String): Indicator = {
    if (indicatorId == null) { return null; }
    entityDao.get(classOf[Indicator], indicatorId.toLong)
  }

  def getIndicator(indicatorId: Long): Indicator = {
    entityDao.get(classOf[Indicator], indicatorId)
  }

  def getIndicatorsScore(): collection.Map[Long, Number] = {
    val query = OqlBuilder.from[Array[Any]](classOf[Question].getName, "question")
    query.groupBy("question.indicator.id").select("question.indicator.id,sum(question.score)")
    entityDao.search(query).map(obj => (obj(0).asInstanceOf[Number].longValue, obj(1).asInstanceOf[Number])).toMap
  }

  def getIndicatorMap(): collection.Map[Long, Indicator] = {
    entityDao.getAll(classOf[Indicator]).map(x => (x.id, x)).toMap
  }
}
