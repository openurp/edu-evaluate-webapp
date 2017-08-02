package org.openurp.edu.evaluation.questionnaire.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.model.EvaluationCriteria
import org.beangle.data.dao.OqlBuilder
import org.beangle.commons.collection.Order
import org.beangle.webmvc.api.context.Params
import org.openurp.base.model.Department
import java.io.FileOutputStream
import org.beangle.commons.lang.Strings
import com.sun.xml.internal.ws.api.message.Attachment
import org.beangle.commons.io.IOs
import org.beangle.webmvc.api.view.View
import org.openurp.edu.evaluation.model.EvaluationCriteriaItem

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
      evalutionCriteria.name = (evalutionCriteria.name.replaceAll("<", "&#60;").replaceAll(">", "&#62;"));
      entityDao.saveOrUpdate(evalutionCriteria);
      return redirect("search", "info.save.success");
    } catch {
      case e: Exception =>
        logger.info("saveAndForwad failure", e);
        return redirect("search", "info.save.failure");
    }
  }

  /**
   * 不能删除默认对照标准
   */
  override def remove(): View = {
    val evaluationCriteriaIds = longIds("evaluationCriteria")
    if (evaluationCriteriaIds.contains(1L)) {
      return redirect("search", "info.delete.failure");
    } else {
      return super.remove();
    }
  }

  protected override def getQueryBuilder(): OqlBuilder[EvaluationCriteria] = {

    val builder: OqlBuilder[EvaluationCriteria] = OqlBuilder.from(entityName, simpleEntityName)
    populateConditions(builder)
    builder.where("evalutionCriteria.depart.id in (:departIds)", get("evalutionCriteria.depart.id"))
    builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
  }

}