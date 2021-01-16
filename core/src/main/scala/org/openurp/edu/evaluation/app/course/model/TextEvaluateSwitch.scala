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
package org.openurp.edu.evaluation.app.course.model

import org.beangle.data.model.IntId
import org.beangle.data.model.pojo.InstantRange
import org.openurp.base.edu.model.{Project, Semester}

class TextEvaluateSwitch extends IntId with InstantRange {

  var semester: Semester = _
  var opened: Boolean = false
  var project: Project =_
  /** 是否开放(教师查询)*/
  var openedTeacher:Boolean=_
  /** 是否开放(学生文字评教)*/
  var textEvaluateOpened:Boolean=_

}
