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
package org.beangle.data.transfer

import org.beangle.data.transfer.Transfer

/**
 * 基于行的转换器
 *
 * @author chaostone
 */
trait ItemTransfer extends Transfer {

  /**
   * 返回要转换的各个属性的名称
   */
  def attrs: List[String]

  /**
   * 将当前正在转换的数据转换成map[attr,value]
   */
  def curData: collection.mutable.Map[String, Any]
}
