package org.openurp.edu.evaluation.course.web.action

import scala.collection.mutable.Buffer

import org.beangle.commons.dao.OqlBuilder
import org.beangle.commons.model.Entity
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Semester
import org.openurp.edu.base.model.Project

abstract class ProjectRestfulAction[T <: Entity[_]] extends RestfulAction[T] with ProjectSupport {
  
  def getSemesters():Seq[Semester]={
    val builder = OqlBuilder.from(classOf[Semester], "semester")
    builder.where("semester.calendar.school=:school",currentProject.school)
     entityDao.search(builder)
  }
//
//  override def getQueryBuilder(): OqlBuilder[T] = {
//    val builder: OqlBuilder[T] = OqlBuilder.from(entityName, simpleEntityName)
//    populateConditions(builder)
//    builder.where(simpleEntityName + ".project = :project", currentProject)
//    builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
//  }
//
//  override protected def saveAndRedirect(entity: T): View = {
//    Properties.set(entity, "project", currentProject)
//    super.saveAndRedirect(entity)
//  }
}

trait ProjectSupport { this: RestfulAction[_] =>

  def findItems[T <: Entity[_]](clazz: Class[T]): Buffer[T] = {
    val query = OqlBuilder.from(clazz)
    entityDao.search(query).toBuffer
  }

  def findItemsBySchool[T <: Entity[_]](clazz: Class[T]): Buffer[T] = {
    val query = OqlBuilder.from(clazz, "aa")
    query.where("aa.school=:school", currentProject.school)
    query.orderBy("code")
    entityDao.search(query).toBuffer
  }
  def findItemsByProject[T <: Entity[_]](clazz: Class[T], orderBy: String = "code"): Buffer[T] = {
    val query = OqlBuilder.from(clazz, "aa")
    query.where("aa.project=:project", currentProject)
    query.orderBy(orderBy)
    entityDao.search(query).toBuffer
  }

  def currentProject: Project = {
    get("project") match {
      case Some(code) =>
        val projects = entityDao.search(OqlBuilder.from(classOf[Project], "p").where("p.code= :code", code).cacheable())
        if (projects.size != 1) throw new RuntimeException("Cannot find unique project according to " + code)
        projects.head
      case None => throw new RuntimeException("Cannot find project parameter")
    }
  }

}