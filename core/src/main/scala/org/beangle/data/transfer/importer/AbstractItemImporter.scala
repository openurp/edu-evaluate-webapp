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

import org.beangle.data.transfer.ItemTransfer
import org.beangle.commons.lang.Strings

/**
 * 线性导入实现
 *
 * @author chaostone
 */
abstract class AbstractItemImporter extends AbstractImporter with Importer with ItemTransfer {

  this.prepare = new DescriptionAttrPrepare()
  /** 属性说明[attr,description] */
  protected val descriptions = new collection.mutable.HashMap[String, String]

  /** 导入属性 */
  var attrs: List[String] = _

  /**
   * 改变现有某个属性的值
   */
  def changeCurValue(attr: String, value: Any): Unit = {
    this.curData.put(attr, value)
  }

  final def read(): Boolean = {
    val data = reader.read().asInstanceOf[Array[_]]
    if (null == data) {
      this.current = null
      this.curData = null
      return false;
    } else {
      curData = new collection.mutable.HashMap[String, Any]
      (0 until data.length) foreach { i =>
        this.curData.put(attrs(i), data(i));
      }
      return true;
    }
  }

  def isDataValid: Boolean = {
    this.curData.values exists { v =>
      v match {
        case tt: String => Strings.isNotBlank(tt)
        case _ => null != v
      }
    }
  }

  def setAttrs(attrs: List[String], descs: List[String]): Unit = {
    val max = Math.min(attrs.length, descs.length)
    (0 until max) foreach { i =>
      descriptions.put(attrs(i), descs(i))
    }
    this.attrs = attrs
  }

  def processAttr(attr: String): String = {
    attr
  }
}
