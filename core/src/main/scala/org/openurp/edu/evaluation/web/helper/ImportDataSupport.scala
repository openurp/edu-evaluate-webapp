package org.openurp.edu.evaluation.web.helper

import org.beangle.data.model.Entity
import org.beangle.data.transfer.importer.listener.ForeignerListener
import org.beangle.webmvc.api.context.ActionContext
import org.beangle.webmvc.entity.helper.PopulateHelper
import javax.servlet.http.Part
import org.beangle.webmvc.entity.action.RestfulAction
import org.beangle.webmvc.api.view.View
import org.beangle.data.transfer.importer.listener.ForeignerListener
import org.beangle.data.transfer.importer.ImporterFactory
import org.beangle.data.transfer.importer.EntityImporter
import org.beangle.data.transfer.importer.ImportResult
import org.beangle.data.transfer.importer.ImportListener
import org.beangle.data.transfer.Format

trait ImportDataSupport[T <: Entity[_]] {
  self: RestfulAction[T] =>

  def importForm: View = {
    forward("/components/importData/form")
  }
  /**
   * 构建实体导入者
   */
  protected def buildEntityImporter(): EntityImporter = {
    buildEntityImporter(this.entityDao.domain.getEntity(this.entityName).get.clazz, "importFile")
  }

  /**
   * 构建实体导入者
   *
   * @param upload
   * @param clazz
   */
  protected def buildEntityImporter(clazz: Class[_], upload: String = "importFile"): EntityImporter = {
    val request = ActionContext.current.request
    val parts = request.getParts
    val partIter = parts.iterator
    var filePart: Part = null
    while (partIter.hasNext() && null == filePart) {
      val part = partIter.next()
      if (part.getName == "importFile") filePart = part
    }
    if (null == filePart) {
      logger.error("cannot get upload file $upload")
      return null;
    }
    //    val fileName = get(upload + "FileName").get;
    val is = filePart.getInputStream
    //    val formatName = Strings.capitalize(Strings.substringAfterLast(fileName, "."));
    val format = Format.withName("Xls")
    val importer = ImporterFactory.getEntityImporter(format, is, clazz, null)
    importer
  }

  /**
   * 导入信息
   */
  def importData(): View = {
    val tr = new ImportResult();
    val importer = buildEntityImporter();
    if (null == importer) { return forward("/components/importData/error"); }
    try {
      configImporter(importer);
      importer.transfer(tr);
      put("importer", importer);
      put("importResult", tr);
      if (tr.hasErrors) {
        return forward("/components/importData/error");
      } else {
        return forward("/components/importData/result");
      }
    } catch {
      case e: Exception =>
        e.printStackTrace();
        tr.addFailure(getText("error.importformat"), e.getMessage());
        put("importResult", tr);
        return forward("/components/importData/error");
    }
  }

  protected def configImporter(importer: EntityImporter): Unit = {
    importerListeners.foreach { l =>
      importer.addListener(l);
    }
  }

  protected def importerListeners: List[_ <: ImportListener] = {
    List(new ForeignerListener(this.entityDao))
  }
}