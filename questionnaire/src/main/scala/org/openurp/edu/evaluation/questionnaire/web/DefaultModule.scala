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
package org.openurp.edu.evaluation.questionnaire.web

import org.beangle.cdi.bind.BindModule
import org.openurp.edu.evaluation.questionnaire.web.action.QuestionTypeAction
import org.openurp.edu.evaluation.questionnaire.web.action.QuestionnaireAction
import org.openurp.edu.evaluation.questionnaire.web.action.EvaluationCriteriaAction
import org.openurp.edu.evaluation.questionnaire.web.action.EvaluationConfigAction
import org.openurp.edu.evaluation.questionnaire.web.action.OptionGroupAction
import org.openurp.edu.evaluation.questionnaire.web.action.QuestionAction
import org.openurp.edu.evaluation.questionnaire.service.QuestionTypeService

class DefaultModule extends BindModule {

  override def binding() {

    //*******教务处  评教设置——>问卷设置
    bind(classOf[EvaluationConfigAction])
    //  问卷、问题、问题类别
    bind(classOf[QuestionnaireAction], classOf[QuestionAction], classOf[QuestionTypeAction], classOf[QuestionTypeService])
    //  选项组、评价标准
    bind(classOf[OptionGroupAction], classOf[EvaluationCriteriaAction])
  }

}
