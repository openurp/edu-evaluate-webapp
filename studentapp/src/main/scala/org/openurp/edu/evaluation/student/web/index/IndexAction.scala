/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.edu.evaluation.student.web.index

import java.time.LocalDate

import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.security.realm.cas.{ Cas, CasConfig }
import org.beangle.webmvc.api.action.{ ActionSupport, ServletSupport }
import org.beangle.webmvc.api.annotation.{ mapping, param }
import org.beangle.webmvc.api.context.ActionContext
import org.beangle.webmvc.api.view.View
import org.openurp.app.web.NavContext
import org.openurp.edu.base.model.Project

/**
 * @author xinzhou
 */
class IndexAction extends ActionSupport with ServletSupport {
  var entityDao: EntityDao = _
  var casConfig: CasConfig = _

  @mapping("{project}")
  def project(@param("project") project: String): View = {
    put("nav", NavContext.get(request))
    val projects = entityDao.findBy(classOf[Project], "code", List(project))
    val currentProject = projects.head
    put("currentProject", currentProject)
    put("projects", entityDao.getAll(classOf[Project]))
    forward()
  }

  def index(): View = {
    val now = LocalDate.now
    val builder = OqlBuilder.from(classOf[Project], "p").where("p.beginOn <= :now and( p.endOn is null or p.endOn >= :now)", now).orderBy("p.code").cacheable()
    val projects = entityDao.search(builder)
    if (projects.isEmpty) throw new RuntimeException("Cannot find any valid projects")

    redirect("project", "&project=" + projects.head.code, null)
  }

  def logout(): View = {
    redirect(to(Cas.cleanup(casConfig, ActionContext.current.request, ActionContext.current.response)), null)
  }
}
