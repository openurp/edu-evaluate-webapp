/*
 * Copyright (C) 2005, The OpenURP Software.
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

package org.openurp.qos.evaluation.app.course.model

import org.openurp.base.edu.model.{Semester, Student}
import org.openurp.edu.clazz.model.Clazz

class EvaluateSearchAdminclass {
  var semester: Semester = _

  var clazz: Clazz = _

  var student: Student = _
  /*
   * 学生需要评教的总人次
   */
  var countAll: Integer = _

  /*
   * 学生已经评教的人次
   */
  var haveFinish: Integer = _
  /*
   * 完成率
   */
  var finishRate: Float = _
}
