/*
 * Copyright (C) 2014, The OpenURP Software.
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

package org.openurp.qos.evaluation.clazz.web.action.admin

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.support.action.{ExportSupport, RestfulAction}
import org.openurp.base.model.Project
import org.openurp.qos.evaluation.clazz.model.FinalComment
import org.openurp.starter.web.support.ProjectSupport

class FinalCommentAction extends RestfulAction[FinalComment], ExportSupport[FinalComment], ProjectSupport {

  override protected def indexSetting(): Unit = {
    given project: Project = getProject

    put("project", project)
    put("departments", getDeparts)
    put("currentSemester", getSemester)
  }

  override protected def getQueryBuilder: OqlBuilder[FinalComment] = {
    val builder = super.getQueryBuilder
    given project: Project = getProject
    queryByDepart(builder,"finalComment.teachDepart")
  }
}
