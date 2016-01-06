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
package org.beangle.data.transfer.importer.listener;

import org.beangle.data.transfer.Transfer;
import org.beangle.data.transfer.TransferListener;
import org.beangle.data.transfer.TransferResult;
import org.beangle.data.transfer.importer.AbstractItemImporter;

/**
 * ItemImporterListener class.
 *
 * @author chaostone
 */
class ItemImporterListener extends TransferListener {

  def importer: AbstractItemImporter = {
    transfer.asInstanceOf[AbstractItemImporter]
  }

  def onFinish(tr: TransferResult) {
  }

  def onItemFinish(tr: TransferResult) {
  }

  def onStart(tr: TransferResult) {
  }

  def onItemStart(tr: TransferResult) {
  }

}
