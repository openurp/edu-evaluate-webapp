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
package org.openurp.qos.evaluation.questionnaire.web.action

import org.beangle.commons.collection.Order
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.qos.evaluation.model.{EvaluationCriteria, EvaluationCriteriaItem}

class EvaluationCriteriaAction extends RestfulAction[EvaluationCriteria] {

  override def search(): View = {
    val builder = OqlBuilder.from(classOf[EvaluationCriteria], "criteria")
    populateConditions(builder)
    builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    val evalutionCriterias = entityDao.search(builder)
    put("evalutionCriterias", evalutionCriterias)
    forward()
  }

  override def editSetting(entity: EvaluationCriteria): Unit = {
    val departmentList = entityDao.getAll(classOf[Department])
    put("departmentList", departmentList)
    super.editSetting(entity)
  }

  protected override def saveAndRedirect(entity: EvaluationCriteria): View = {
    try {
      val evalutionCriteria = entity.asInstanceOf[EvaluationCriteria]
      evalutionCriteria.criteriaItems.clear()
      val criteriaCount = getInt("criteriaItemCount", 0)
      (0 until criteriaCount) foreach { i =>
        get("criteriaItem" + i + ".name") foreach { criteriaItemName =>
          val item = populateEntity(classOf[EvaluationCriteriaItem], "criteriaItem" + i)
          item.criteria = evalutionCriteria
          evalutionCriteria.criteriaItems += item
        }
      }
      evalutionCriteria.name = (evalutionCriteria.name.replaceAll("<", "&#60;").replaceAll(">", "&#62;"))
      entityDao.saveOrUpdate(evalutionCriteria)
      return redirect("search", "info.save.success")
    } catch {
      case e: Exception =>
        logger.info("saveAndForwad failure", e)
        return redirect("search", "info.save.failure")
    }
  }

  /**
   * 不能删除默认对照标准
   */
  override def remove(): View = {
    val evaluationCriteriaIds = longIds("evaluationCriteria")
    if (evaluationCriteriaIds.contains(1L)) {
      return redirect("search", "info.delete.failure")
    } else {
      return super.remove()
    }
  }

  protected override def getQueryBuilder: OqlBuilder[EvaluationCriteria] = {

    val builder: OqlBuilder[EvaluationCriteria] = OqlBuilder.from(entityName, simpleEntityName)
    populateConditions(builder)
    builder.where("evalutionCriteria.depart.id in (:departIds)", get("evalutionCriteria.depart.id"))
    builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
  }

}
