package org.openurp.edu.evaluation.questionnaire.web.action

import org.beangle.commons.collection.Order
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.model.OptionGroup
import org.openurp.edu.evaluation.model.Option
import org.beangle.webmvc.api.view.View
import org.openurp.base.model.Department
import org.openurp.edu.evaluation.model.Question

class OptionGroupAction extends RestfulAction[OptionGroup] {
    
  override def search(): String = {
      val builder = OqlBuilder.from(classOf[OptionGroup], "optionGroup")
      populateConditions(builder)
      builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
      val optionGroups = entityDao.search(builder)
      
      put("optionGroups", optionGroups)
      forward()
  }
  
  
override def editSetting(entity: OptionGroup): Unit = {
    val departmentList = entityDao.getAll(classOf[Department])
    put("departmentList", departmentList)
    super.editSetting(entity)
  }

override def saveAndRedirect(entity: OptionGroup): View = {
    try {
      val optionGroup = entity.asInstanceOf[OptionGroup];
      optionGroup.options.clear();
      val optionCount = getInt("optionCount", 0);
        (0 until optionCount) foreach { i =>
          get("option" + i + ".name") foreach { optionName=>
          val option = populateEntity(classOf[Option], "option" + i)
          option.optionGroup = optionGroup
          optionGroup.options += option
          }
        }
      optionGroup.name=optionGroup.name.replaceAll("<", "&#60;").replaceAll(">", "&#62;")
      entityDao.saveOrUpdate(optionGroup);
      return redirect("search", "info.save.success");
    } catch{
      case e:Exception=>
      logger.info("saveAndForwad failure", e);
      return redirect("search", "info.save.failure");
    }

  }

      
    protected override def getQueryBuilder(): OqlBuilder[OptionGroup] = {
    
    val builder: OqlBuilder[OptionGroup] = OqlBuilder.from(entityName, simpleEntityName)
    populateConditions(builder)
    builder.where("optionGroup.depart.id in (:departIds)")
    builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
  }
    
override  def remove(): View = {
    val optionGroupIds = longIds("optionGroup")
    val query1=OqlBuilder.from(classOf[OptionGroup],"optionGroup")
    query1.where("optionGroup.id in (:optionGroupIds)",optionGroupIds)
    val optionGroups = entityDao.search(query1);

    val query = OqlBuilder.from(classOf[Question], "question");
    query.where("question.optionGroup.id in (:optionGroups)", optionGroupIds);
    val questions = entityDao.search(query);
    if (!questions.isEmpty )  return redirect("search", "删除失败,选择的数据中已有被评教问题引用")
    else entityDao.remove(optionGroups);
    return redirect("search", "info.remove.success");
  }

   
    
}