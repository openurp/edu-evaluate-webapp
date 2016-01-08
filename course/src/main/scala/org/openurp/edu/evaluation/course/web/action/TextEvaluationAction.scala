package org.openurp.edu.evaluation.course.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Semester
import org.openurp.edu.base.model.Project
import org.openurp.edu.evaluation.course.model.TextEvaluation

class TextEvaluationAction extends RestfulAction[TextEvaluation]{


  
  /**
   * 修改(是否确认)
   * 
   * @return
   */
  def  updateAffirm():View= {
    val semesterId=20141
    val projectId=1
    put ("semester",entityDao.get(classOf[Semester],semesterId))
    put ("project",entityDao.get(classOf[Project],projectId))
    val ids = longIds(simpleEntityName)
    val state = getBoolean("state").get

    val textEvaluations = entityDao.find(classOf[TextEvaluation], ids)
    textEvaluations foreach {textEvaluation =>
    textEvaluation.state=state
    }
//    for (TextEvaluation textEvaluation : textEvaluations) {
//      textEvaluation.setIsAffirm(isAffirm);
//    }
    entityDao.saveOrUpdate(textEvaluations);
    redirect("search", "info.action.success")
  }
}