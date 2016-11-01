/*
 * Beangle, Agile Development Scaffold and Toolkit
 *
 * Copyright (c) 2005-2014, Beangle Software.
 *
 * Beangle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beangle is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Beangle.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.beangle.data.transfer.importer

import org.beangle.data.transfer.io.ItemReader

/**
 * DescriptionAttrPrepare class.
 *
 * @author chaostone
 */
object EntityPrepare extends ImportPrepare {

  def prepare(importer: Importer) {
    val entityImporter = importer.asInstanceOf[DefaultEntityImporter]
    entityImporter.addEntity(DefaultEntityImporter.alias, entityImporter.entityClass)

    val reader = importer.reader.asInstanceOf[ItemReader]
    importer.asInstanceOf[AbstractItemImporter].setAttrs(reader.readTitle(), reader.readDescription());
  }

}
