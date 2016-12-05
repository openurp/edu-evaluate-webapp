/*
 * OpenURP, Agile University Resource Planning Solution
 *
 * Copyright (c) 2014-2015, OpenURP Software.
 *
 * OpenURP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenURP is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with OpenURP.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.edu.evaluation.course.model

import scala.reflect.runtime.universe

import org.beangle.data.model.bind.Mapping

class DefaultMapping extends Mapping {

  def binding(): Unit = {
    defaultIdGenerator("auto_increment")
    defaultCache("openurp.edu.evaluation", "read-write")

    bind[StdEvaluateSwitch].on(e => declare(
      e.endAt & e.beginAt & e.opened & e.semester & e.project are notnull))
   
    bind[TextEvaluation].on(e => declare(
      e.student & e.lesson & e.evaluateByTeacher & e.evaluateAt are notnull ,
      e.teacherRemessages is (depends("textEvaluation"),table("text_evaluation_remsgs"))
      ))
        
    bind[TextEvaluateSwitch].on(e => declare(
      e.endAt & e.beginAt & e.opened & e.semester & e.project are notnull))
      
    bind[TeacherRemessage].on(e => declare(
     e.textEvaluation & e.visible are notnull))
      

}
  
}
