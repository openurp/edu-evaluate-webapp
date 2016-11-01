package org.openurp.edu.evaluation.department.action

import org.beangle.data.model.Entity
import org.beangle.data.transfer.TransferResult
import org.beangle.data.transfer.importer.{ EntityImporter, IllegalImportFormatException, ImporterFactory }
import org.beangle.data.transfer.importer.listener.ImporterForeignerListener
import org.beangle.data.transfer.io.TransferFormat
import org.beangle.webmvc.api.context.ActionContext
import org.beangle.webmvc.entity.helper.PopulateHelper
import javax.servlet.http.Part
import org.beangle.data.transfer.TransferListener
import org.beangle.webmvc.entity.action.RestfulAction

/**
 * @author xinzhou
 */
trait ImportDataSupport[T <: Entity[_]] {
  self: RestfulAction[T] =>
  def importForm: String = {
    forward("/components/importData/form")
  }
  /**
   * 构建实体导入者
   */
  protected def buildEntityImporter(): EntityImporter = {
    buildEntityImporter(entityMetaData.getType(entityType.getName).get.entityClass, "importFile")
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
    val format = TransferFormat.withName("Xls")
    val importer = ImporterFactory.getEntityImporter(format, is, clazz, null)
    importer.entityMetadata = this.entityMetaData
    importer.populator = PopulateHelper.populator
    importer
  }

  /**
   * 导入信息
   */
  def importData(): String = {
    val tr = new TransferResult();
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
      case e: IllegalImportFormatException =>
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

  protected def importerListeners: List[_ <: TransferListener] = {
    List(new ImporterForeignerListener(entityDao))
  }
}
