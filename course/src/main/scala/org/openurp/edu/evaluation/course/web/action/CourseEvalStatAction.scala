package org.openurp.edu.evaluation.course.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.openurp.hr.base.model.Staff
import org.beangle.webmvc.api.view.View
import org.openurp.edu.evaluation.lesson.model.DepartEvaluation
import org.openurp.edu.evaluation.lesson.stat.model.CourseEvalStat
import org.openurp.base.model.Semester
import java.text.DecimalFormat
import org.springframework.beans.support.PropertyComparator
import org.openurp.edu.evaluation.lesson.model.DepartEvaluation
import java.text.FieldPosition
import org.openurp.edu.evaluation.lesson.model.DepartEvaluation
import java.util.ArrayList
import org.beangle.commons.collection.Collections
import org.openurp.edu.evaluation.lesson.model.DepartEvaluation
import org.openurp.edu.evaluation.lesson.model.DepartEvaluation
import org.openurp.edu.evaluation.lesson.model.DepartEvaluation
import org.openurp.edu.evaluation.department.model.DepartEvaluate
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.base.model.Department

class CourseEvalStatAction extends RestfulAction[CourseEvalStat] {

 override def  index():String = {
//    put("stdTypeList", getStdTypes());
//    put("departmentList", getColleges());
//    put("departments", getDeparts());
   put("departments", entityDao.search(OqlBuilder.from(classOf[Department],"dep").where("dep.teaching =:tea",true)))
    /** 本学期是否评教 */
    val builder = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult");
    val semesters = 20141
//    if (semesters != null) {
      builder.where("evaluateResult.lesson.semester.id=:ids", semesters);
//    }
    builder.select("distinct questionnaire");
    put("questionnaires", entityDao.search(builder));
    forward();
  }

  
  override protected def  getQueryBuilder():OqlBuilder[CourseEvalStat]={
    val semesterId = 20141
    val queryBuilder = OqlBuilder.from(classOf[CourseEvalStat],"courseEvalStat")
    queryBuilder.where("courseEvalStat.semester.id=:semesterId", semesterId);
    populateConditions(queryBuilder);
    queryBuilder.limit(getPageLimit).orderBy("courseEvalStat.rank asc");
  }

  
  override def search():String= {
    put("evaluates", entityDao.search(getQueryBuilder()));
    forward();
  }

  def  stat():View = {
    val semesterId = 20141
//    semesterId = semesterId == null ? getSemester().getId() : semesterId;
    /** 获取本学期所有已使用的问卷 **/
    val builder = OqlBuilder.from[Questionnaire](classOf[EvaluateResult].getName, "evaluateResult");
    builder.where("evaluateResult.lesson.semester.id=:semesterId", semesterId);
    builder.select("distinct questionnaire");
    val questionnaires = entityDao.search(builder);
    /** 统计每份问卷的评教结果 **/
    questionnaires foreach { questionnaire =>
      val questionnaireId = questionnaire.id
      val query = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName + " evaluateResult,"
          + classOf[QuestionResult].getName + " questionResult," + classOf[Staff].getName + " teach");
      query.select("teach.id,sum(questionResult.score)/count(distinct evaluateResult.id)");
      query.join("evaluateResult.lesson.teachers", "tea");
      query.where("tea.id = teach.id");
      query.where("evaluateResult.id=questionResult.result.id ");
      query.where("evaluateResult.lesson.semester.id=" + semesterId);
      query.where("evaluateResult.questionnaire.id=:ids", questionnaireId);
      query.groupBy("teach.id");
      query.orderBy("sum(questionResult.score)/count(distinct evaluateResult.id) desc");
      val li = entityDao.search(query);
      val depEvaluates = entityDao.search(OqlBuilder.from(classOf[DepartEvaluate], "de").where("de.semester.id=:id", semesterId))
      val teaList = entityDao.search(OqlBuilder.from(classOf[Staff], "staff").where("staff.state.id=:id",1L))
      val evaluates =Collections.newBuffer[CourseEvalStat]
      val df = new DecimalFormat("#.00 ");
      teaList foreach { teacher =>
        var fl = 0f;
        val evaluate = new CourseEvalStat
        evaluate.staff=teacher
        evaluate.semester=entityDao.get(classOf[Semester], semesterId)
        evaluate.questionnaire=questionnaire
        li foreach { bo =>
          if (bo(0).toString().equals(teacher.id.toString())) {
            if (bo(1) != null && !"".equals(bo(1))) {
              val d = new StringBuffer(" ");
              df.format(bo(1).toString().toFloat, d, new FieldPosition(2));
              evaluate.stdEvaluate=d.toString().toFloat
              fl += bo(1).toString().toFloat * 0.5f;
            }
          }
        }
        depEvaluates foreach {departEvaluation =>
          
          if (departEvaluation.staff.id.toString().equals(teacher.id.toString())) {
            if (departEvaluation.totalScore!= null) {

              evaluate.depEvaluate=departEvaluation.totalScore

              fl += departEvaluation.totalScore * 0.5f;
            }
          }
        
        }
//        for (DepartEvaluation departEvaluation : depEvaluates) {
//          if (departEvaluation.getStaff().getId().toString().equals(teacher.getId().toString())) {
//            if (departEvaluation.getScore() != null) {
//
//              evaluate.setDepEvaluate(departEvaluation.getScore());
//
//              fl += departEvaluation.getScore() * 0.5f;
//            }
//          }
//        }
        val dd = new StringBuffer(" ");
        df.format(fl, dd, new FieldPosition(2));
        evaluate.score=dd.toString().toFloat
        evaluates += evaluate
      }
//      Collections.sort(evaluates, new PropertyComparator("score desc"));
      
      for (i<-0 to  evaluates.size) {
        val courseEvalStat = evaluates(i)
        courseEvalStat.rank=i + 1
        var num = 0;
        for (j <- 0 to evaluates.size) {
          val courseEvaluate = evaluates(j);
          val t1 = courseEvaluate.staff
          val t2 = courseEvalStat.staff
          if (t1.state.department != null && t2.state.department != null) {
            if (t1.state.department.id.toString().equals(t2.state.department.id.toString())) {
              num += 1;
              if (courseEvaluate.staff.id.toString().equals(courseEvalStat.staff.id.toString())) {
                courseEvalStat.departRank=num
              }
            }
          }

        }
      }
      val list = Collections.newBuffer[CourseEvalStat]
      evaluates foreach {courseEvalStat =>
        
        if ((courseEvalStat.staff.state.department != null)) {
          list +=courseEvalStat
        } else {
          val lists = entityDao.getAll(classOf[Department])
          lists foreach {department =>
            
            val teacher = courseEvalStat.staff
            if (null != teacher.state.department) {
              if (teacher.state.department.id.toString().equals(department.id.toString())) {
                list += courseEvalStat
              }
            }
          
          }
        }
      
      }
      /** 删除本学期某一问卷的统计结果 **/
      val cBuilder = OqlBuilder.from(classOf[CourseEvalStat], "stat")
          .where("stat.semester.id= :semesterId", semesterId)
          .where("stat.questionnaire.id=:questionnaireId", questionnaireId);
      entityDao.remove(entityDao.search(cBuilder));
      saveOrUpdate(list);
    }
    redirect("index", "统计完成");
  }



}