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

import scala.annotation.elidable.FINE
import org.beangle.commons.logging.Logging
import org.beangle.data.transfer.{ TransferListener, TransferResult }
import scala.collection.mutable.ListBuffer
import org.beangle.data.transfer.io.TransferFormat
import java.util.Locale
import org.beangle.data.transfer.Transfer

abstract class AbstractImporter extends Importer with Logging {
  /**
   * 转换结果
   */
  protected var transferResult: TransferResult = _

  /**
   * 监听器列表
   */
  protected val listeners = new ListBuffer[TransferListener]

  /**
   * 成功记录数
   */
  var success = 0;

  /**
   * 失败记录数
   */
  var fail = 0;

  /**
   * 下一个要读取的位置
   */
  protected var index = 0;

  /**
   * 进行转换
   */
  def transfer(tr: TransferResult): Unit = {
    this.transferResult = tr;
    this.transferResult.transfer = this;
    val transferStartAt = System.currentTimeMillis();
    try {
      prepare.prepare(this);
    } catch {
      // 预导入发生位置错误，错误信息已经记录在tr了
      case e: Throwable => e.printStackTrace(); return ;
    }
    listeners.foreach(l => l.onStart(tr))
    while (read()) {
      val transferItemStart = System.currentTimeMillis();
      index += 1;
      beforeImportItem()
      if (isDataValid) {
        val errors = tr.errors;
        // 实体转换开始
        listeners.foreach(l => l.onItemStart(tr))
        // 如果转换前已经存在错误,则不进行转换
        if (tr.errors == errors) {
          // 进行转换
          transferItem();
          // 实体转换结束
          listeners.foreach(l => l.onItemFinish(tr))
          // 如果导入过程中没有错误，将成功记录数增一
          if (tr.errors == errors) this.success += 1;
          else this.fail += 1;

          if (logger.isDebugEnabled) {
            logger.debug(s"importer item:$tranferIndex take time: " + (System.currentTimeMillis() - transferItemStart));
          }
        }
      }
    }
    listeners.foreach(l => l.onFinish(tr))
    reader.close();
    logger.debug("importer elapse: " + (System.currentTimeMillis() - transferStartAt));
  }

  def ignoreNull: Boolean = {
    true
  }

  def locale: Locale = {
    Locale.getDefault();
  }

  def format: TransferFormat.Value = {
    reader.format
  }

  def tranferIndex: Int = {
    index;
  }

  def addListener(listener: TransferListener): Transfer = {
    listeners += listener
    listener.transfer = this
    this
  }

  protected def beforeImportItem(): Unit = {
  }
}
