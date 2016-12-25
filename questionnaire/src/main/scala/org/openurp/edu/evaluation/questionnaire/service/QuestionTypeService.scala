package org.openurp.edu.evaluation.questionnaire.service

import org.openurp.edu.evaluation.model.Question
import org.beangle.commons.dao.EntityDao
import org.beangle.commons.dao.OqlBuilder
import org.openurp.edu.evaluation.model.QuestionType
import java.util.Date
import org.openurp.edu.evaluation.model.Question
import org.openurp.edu.evaluation.model.Question
class QuestionTypeService(entityDao: EntityDao) {

  def getQuestionTypes():Seq[QuestionType] = {
    val query = OqlBuilder.from(classOf[QuestionType], "type");
    query.where("type.state=true");
    query.where("type.beginOn <= :now and (type.endOn is null or type.endOn >= :now)", new Date());
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
