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
package org.openurp.edu.evaluation.course.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.openurp.edu.evaluation.app.course.model.TextEvaluateSwitch

class TextEvaluateSwitchAction extends ProjectRestfulAction[TextEvaluateSwitch] {

  protected override def indexSetting(): Unit = {
    put("currentSemester", getCurrentSemester)
  }

  override def search(): View = {
    val opened = getBoolean("opened")
    val semesterId = getInt("semester.id")
    val textEvaluationSwitchs = OqlBuilder.from(classOf[TextEvaluateSwitch], "textEvaluateSwitch")
    semesterId.foreach { semesterId => textEvaluationSwitchs.where("textEvaluateSwitch.semester.id=:semesterId", semesterId) }
    opened.foreach { opened => textEvaluationSwitchs.where("textEvaluateSwitch.opened=:opened", opened) }
    textEvaluationSwitchs.where("textEvaluateSwitch.project=:project", getProject)
    put("textEvaluationSwitchs", entityDao.search(textEvaluationSwitchs))
    forward()
  }

  override def saveAndRedirect(evaluateSwitch: TextEvaluateSwitch): View = {
    if (!evaluateSwitch.persisted) {
      val query = OqlBuilder.from(classOf[TextEvaluateSwitch], "textEvaluateSwitch")
      query.where("textEvaluateSwitch.semester.id =:semesterId", evaluateSwitch.semester.id)
      val textEvaluateSwitchs = entityDao.search(query)
      if (!textEvaluateSwitchs.isEmpty) {
        return redirect("search", "&textEvaluateSwitch.semester.id=" + evaluateSwitch.semester.id, "该学期评教开关已存在,请删除后再新增!")
      }
    }
    try {
      saveOrUpdate(evaluateSwitch)
      redirect("search", "success ,&evaluateSwitch.semester.id=" + evaluateSwitch.semester.id, "info.save.success")
    } catch {
      case e: Exception =>
        e.printStackTrace()
        redirect("search", "failure,&evaluateSwitch.semester.id=" + evaluateSwitch.semester.id, "info.save.failure")
    }
  }

}
