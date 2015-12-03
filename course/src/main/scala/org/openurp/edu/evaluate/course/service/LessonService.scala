package org.openurp.edu.evaluate.course.service

import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.lesson.model.Lesson
import org.openurp.base.model.Semester
import org.beangle.data.dao.EntityDao
import org.openurp.edu.lesson.model.Lesson
import org.openurp.edu.lesson.model.Lesson
import org.openurp.edu.lesson.model.CourseTake

class LessonService (entityDao: EntityDao) {
  
//  def  getStdLessons(stdId:Long, semesters:List[Semester]):List[Lesson]= {
////    if (stdId!=null && !semesters.isEmpty) {
////      val prefix = "select distinct lesson.id from org.openurp.edu.teach.lesson.model.Lesson as lesson ";
////      val postfix = " and lesson.semester in (:semesters) ";
//      val query = OqlBuilder.from(classOf[CourseTake], "courseTake")
//      query.select("distinct courseTake.lesson.id ")
//      query.where("courseTake.std.id=:stdId",stdId);
//      query.where("courseTake.semester.id in (:semesters)",semesters);
////      hql(prefix + strategy.getFilterString() + postfix);
////      query.param("id", id);
////      query.param("semesters", semesters);
//     val lessonIds = entityDao.search(query);
//
//      var stdLessons:Seq[Lesson] =Seq()
////      val rs:List[Lesson] = CollectUtils.newArrayList();
//      if (!lessonIds.isEmpty) {
//        val entityquery=OqlBuilder.from(classOf[Lesson], "lesson").where("lesson.id in (:lessonIds)",lessonIds)
//         stdLessons = entityDao.search(entityquery)
//      }
//       stdLessons
//    }
//  }
  
}