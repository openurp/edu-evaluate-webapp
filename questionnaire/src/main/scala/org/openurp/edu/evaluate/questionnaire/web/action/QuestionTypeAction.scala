package org.openurp.edu.evaluate.questionnaire.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.model.QuestionType
import org.beangle.data.dao.OqlBuilder
import org.beangle.commons.collection.Order
import org.openurp.edu.evaluation.model.QuestionType
import org.beangle.commons.lang.Strings
import java.sql.Timestamp
import java.sql.Date
import org.beangle.webmvc.api.view.View

class QuestionTypeAction extends RestfulAction[QuestionType] {

      override def search(): String = {
      val builder = OqlBuilder.from(classOf[QuestionType], "questionType")
      populateConditions(builder)
      builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
      val questionTypes = entityDao.search(builder)
      put("questionTypes", questionTypes)
      forward()
    }
      
  
  protected  def saveAndForward(entity: QuestionType): View = {
    try {
      val questionType = entity.asInstanceOf[QuestionType]
      val name = questionType.name;
      val enName = questionType.enName;
      val remark = questionType.remark;
      questionType.beginOn=  getDate("questionType.beginOn").get
      val invalidat = getDate("questionType.endOn").get;
      if (!"".equals(invalidat) && invalidat != null) {
        questionType.endOn =invalidat
      }
      if (remark!=null) {
        questionType.remark=remark.replaceAll("<", "&#60;").replaceAll(">", "&#62;")
      }
      if (enName!=null) {
        questionType.enName=enName.replaceAll("<", "&#60;").replaceAll(">", "&#62;")
      }
      questionType.name=name.replaceAll("<", "&#60;").replaceAll(">", "&#62;")
      if (!questionType.persisted) {
        if (questionType.state) {
          questionType.beginOn=(new Date(System.currentTimeMillis()));
        }
      } else {
        questionType.updatedAt=(new Date(System.currentTimeMillis()))
        val questionTypeOld = entityDao.get(classOf[QuestionType], questionType.id);
        if (questionTypeOld.state != questionType.state) {
          if (questionType.state) {
            questionType.beginOn= (new Date(System.currentTimeMillis()));
          } else {
            questionType.endOn = (new Date(System.currentTimeMillis()));
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
  
}
