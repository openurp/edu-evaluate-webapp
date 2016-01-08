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
package org.beangle.data.transfer.importer;

import org.beangle.data.transfer.Transfer;
import org.beangle.data.transfer.io.Reader;

/**
 * 数据转换接口
 *
 * @author chaostone
 */
trait Importer extends Transfer {
  /**
   * 是否忽略空值
   */
  def ignoreNull: Boolean

  var reader: Reader = _

  var curData: collection.mutable.Map[String, Any] = _

  var prepare: ImportPrepare = _
  /**
   * 读取数据，设置内部步进状态等
   */
  def read(): Boolean

  /**
   * 当前读入的数据是否有效
   */
  def isDataValid: Boolean

  /**
   * 设置当前正在转换的对象
   */
  def current_=(obj: AnyRef);
}
