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
package org.openurp.qos.evaluation.clazz.model

import org.openurp.base.model.Department
import org.openurp.base.edu.model.Semester

class EvaluateSearchManager {

  var  semester:Semester=_

  var  department:Department=_
  /*
   * 院系总评人次
   */
  var  countAll:Long=_
//  总评人数
  var  stdAll:Long=_
  /*
   * 院系实评人次
   */
  var  haveFinish:Long=_
//  实评人数
  var stdFinish:Long=_
  /*
   * 完成率
   */
  var  finishRate:String=_
  var  stdRate:String=_
}
