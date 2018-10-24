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
package org.openurp.edu.evaluation.student.web

import org.beangle.cdi.bind.BindModule
import org.openurp.edu.evaluation.app.lesson.service.StdEvaluateSwitchService
import org.openurp.edu.evaluation.student.web.index.IndexAction
import org.openurp.edu.evaluation.student.web.action.ClazzAction
import org.openurp.edu.evaluation.student.web.action.TextAction

class DefaultModule extends BindModule {

  override def binding() {
    bind(classOf[ClazzAction])
    bind(classOf[TextAction])
    bind(classOf[IndexAction])

  }
}
