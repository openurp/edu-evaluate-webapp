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
package org.beangle.data.transfer.importer.listener

import org.beangle.commons.lang.Strings
import org.beangle.commons.model.Entity
import org.beangle.data.transfer.TransferResult
import org.beangle.data.transfer.importer.MultiEntityImporter
import org.beangle.commons.dao.EntityDao
import org.beangle.commons.bean.Properties

object ImporterForeignerListener {
  val CACHE_SIZE = 500;
}
/**
 * 导入数据外键监听器<br>
 * 这里尽量使用entityDao，因为在使用entityService加载其他代码时，jpa会保存还未修改外的"半成对象"<br>
 * 从而造成有些外键是空对象的错误<br>
 * 如果外键不存在，则目标中的外键会置成null；<br>
 * 如果外键是空的，那么目标的外键取决于importer.isIgnoreNull取值
 *
 * @author chaostone
 */
class ImporterForeignerListener(entityDao: EntityDao) extends ItemImporterListener {

  import ImporterForeignerListener._
  protected val foreigersMap = new collection.mutable.HashMap[String, collection.mutable.HashMap[String, Object]]

  private val foreigerKeys = new collection.mutable.ListBuffer[String]
  foreigerKeys += "code"

  private var multiEntity = false;

  override def onStart(tr: TransferResult) {
    if (importer.getClass().equals(classOf[MultiEntityImporter])) {
      multiEntity = true;
    }
    super.onStart(tr);
  }

  override def onItemFinish(tr: TransferResult) {
    // 过滤所有外键
    importer.attrs foreach { attri => // getAttrs()得到属性,即表的第二行
      val processed = importer.processAttr(attri);
      val foreigerKeyIndex = 0
      val isforeiger = foreigerKeys exists { fk =>
        (processed.endsWith("." + fk))
      }
      val value = importer.curData.get(attri).orNull
      if (isforeiger && null != value) {
        val codeValue = value.toString()
        var foreiger: Object = null;
        // 外键的代码是空的
        if (Strings.isNotEmpty(codeValue)) {
          var entity: Object = null;
          if (multiEntity) {
            entity = importer.asInstanceOf[MultiEntityImporter].getCurrent(attri).asInstanceOf[AnyRef]
          } else {
            entity = importer.current;
          }

          val attr = importer.processAttr(attri)
          val nestedForeigner = Properties.get[Object](entity, Strings.substring(attr, 0, attr.lastIndexOf(".")));
          nestedForeigner match {
            case nestf: Entity[_] =>
              val className = nestedForeigner.getClass.getName
              val foreignerMap = foreigersMap.getOrElseUpdate(className, new collection.mutable.HashMap[String, Object])
              if (foreignerMap.size > CACHE_SIZE) foreignerMap.clear();
              foreiger = foreignerMap.get(codeValue).orNull
              if (foreiger == null) {
                val clazz = nestedForeigner.getClass.asInstanceOf[Class[Entity[_]]]
                val foreigners = entityDao.findBy(clazz, foreigerKeys(foreigerKeyIndex), List(codeValue));
                if (!foreigners.isEmpty) {
                  foreiger = foreigners.head
                  foreignerMap.put(codeValue, foreiger);
                } else {
                  tr.addFailure("代码不存在", codeValue);
                }
              }
            case _ =>
          }
          val parentAttr = Strings.substring(attr, 0, attr.lastIndexOf("."));
          val entityImporter = importer.asInstanceOf[MultiEntityImporter]
          entityImporter.populator.populate(entity.asInstanceOf[Entity[_]], entityImporter.entityMetadata.getType(entity.getClass).get, parentAttr, foreiger);
        }
      }
    }
  }

  def addForeigerKey(key: String) {
    this.foreigerKeys += key
  }
}
