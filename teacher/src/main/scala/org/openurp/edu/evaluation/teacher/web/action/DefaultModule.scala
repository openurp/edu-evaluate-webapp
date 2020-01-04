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
package org.openurp.edu.evaluation.teacher.web.action

import org.beangle.cdi.bind.BindModule

class DefaultModule extends BindModule {

  override def binding(): Unit = {
    // *****教师菜单  评教查询->文字评教
    //  教师个人查看自己的评教回收率
    bind(classOf[EvaluateStatusTeacherAction])
    //  教师个人查看自己的评教结果
    bind(classOf[QuestionnaireStatTeacherAction])
    //  教师回复学生文字评教、发布公告
    bind(classOf[TextEvaluationTeacherAction])
  }
}
