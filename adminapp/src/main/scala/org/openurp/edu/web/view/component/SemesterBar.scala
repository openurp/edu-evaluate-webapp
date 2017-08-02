/*
 * OpenURP, Agile Development Scaffold and Toolkit
 *
 * Copyright (c) 2005-2015, OpenURP Software.
 *
 * OpenURP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenURP is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenURP.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.edu.web.view.component;

import org.beangle.commons.lang.Strings
import org.beangle.webmvc.view.tag.ClosingUIBean
import org.beangle.webmvc.view.tag.ComponentContext
/**
 * @author chaostone
 * @version $beangle 2.4.2 2011-8-14 下午11:32:37$
 */
class SemesterBar(context: ComponentContext) extends ClosingUIBean(context) {

  var formName: String = _
  var action: String = _
  var target: String = _
  var semesterName: String = _
  var name: String = _
  var onChange: String = _
  var onSemesterChange: String = _
  var initCallback: String = _
  var label: String = _
  var submitValue: String = _
  var value: String = _
  var divId: String = _

  var submit: Object = _

  var semesterEmpty: Object = _

  var empty: Object = _

  var semesters: Object = _
  
  var semesterValue: Object = _

  override def evaluateParams() {
    if (Strings.isEmpty(this.id)) {
      generateIdIfEmpty();
    }
  }

}
