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

package org.openurp.qos.evaluation.clazz.web.helper

import org.beangle.commons.bean.Properties
import org.beangle.commons.lang.Strings
import org.beangle.data.transfer.exporter.DefaultPropertyExtractor
import org.openurp.qos.evaluation.clazz.model.CourseEvalStat

class StatCoursePropertyExtractor extends DefaultPropertyExtractor {

  override def getPropertyValue(target: Object, property: String): Any = {
    if (property.startsWith("indicator_score_")) {
      val stat = target.asInstanceOf[CourseEvalStat]
      val indicatorCode = Strings.substringAfter(property, "indicator_score_")
      val i = stat.indicatorStats find { x => x.indicator.code == indicatorCode }
      i.map(_.score).getOrElse("")
    } else if (property.startsWith("indicator_rank_")) {
      val stat = target.asInstanceOf[CourseEvalStat]
      val indicatorCode = Strings.substringAfter(property, "indicator_rank_")
      val i = stat.indicatorStats find { x => x.indicator.code == indicatorCode }
      i.map(_.categoryRank).getOrElse("")
    } else {
      Properties.get[Any](target, property)
    }
  }
}
