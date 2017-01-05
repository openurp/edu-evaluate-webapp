package org.openurp.edu.evaluation.questionnaire.web.action

import java.util.Date
import java.sql.Date
import org.beangle.commons.collection.Order
import org.beangle.commons.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.model.QuestionType
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.base.model.Project

class QuestionTypeAction extends RestfulAction[QuestionType] {

      override def search(): String = {
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
      questionType.updatedAt = new java.util.Date()
      
      val name = questionType.name;
      val enName = questionType.enName.orNull
      val remark = questionType.remark.orNull
      questionType.beginOn=  getDate("questionType.beginOn").get
      questionType.endOn =getDate("questionType.endOn")
      if (remark!=null) {
        questionType.remark=Some(remark.replaceAll("<", "&#60;").replaceAll(">", "&#62;"))
      }
      if (enName!=null) {
        questionType.enName=Some(enName.replaceAll("<", "&#60;").replaceAll(">", "&#62;"))
      }
      questionType.name=name.replaceAll("<", "&#60;").replaceAll(">", "&#62;")
      if (!questionType.persisted) {
        if (questionType.state) {
          questionType.beginOn=new java.sql.Date(System.currentTimeMillis())
        }
      } else {
        questionType.updatedAt= new java.util.Date
        val questionTypeOld = entityDao.get(classOf[QuestionType], questionType.id);
        if (questionTypeOld.state != questionType.state) {
          if (questionType.state) {
            questionType.beginOn=(new java.sql.Date(System.currentTimeMillis()))
          } else {
            questionType.endOn = Some(new java.sql.Date(System.currentTimeMillis()))
          }
        }
      }
      entityDao.saveOrUpdate(questionType);
      return redirect("search", "info.save.success");
    } catch {
      case e:Exception=>
      logger.info("saveAndForwad failure", e);
      return redirect("search", "info.save.failure");
    }
  }
  
    override def remove() : View = {
    val questionTypeIds = longIds("questionType")
    val query1=OqlBuilder.from(classOf[QuestionType],"questionType")
    query1.where("questionType.id in (:questionTypeIds)",questionTypeIds)
    val questionTypes = entityDao.search(query1);

    val query = OqlBuilder.from(classOf[Question], "question");
    query.where("question.questionType in (:questionTypes)", questionTypes);
    val questions = entityDao.search(query);
    if (!questions.isEmpty) { return redirect("search", "删除失败,选择的数据中已有被评教问题引用"); }

    entityDao.remove(questionTypes);
    return redirect("search", "info.remove.success");
  }
  
}
