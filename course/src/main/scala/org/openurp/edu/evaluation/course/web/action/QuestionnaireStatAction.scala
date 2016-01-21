package org.openurp.edu.evaluation.course.web.action

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.action.ServletSupport
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.base.model.Semester
import org.openurp.edu.base.code.model.Education
import org.openurp.edu.base.code.model.StdType
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.openurp.edu.evaluation.lesson.stat.model.LessonEvalStat
import org.openurp.edu.evaluation.lesson.stat.model.OptionStat
import org.openurp.edu.evaluation.lesson.stat.model.QuestionTypeStat
import org.openurp.edu.evaluation.lesson.stat.model.QuestionTypeStat
import org.openurp.edu.evaluation.lesson.stat.model.QuestionTypeStat
import org.openurp.edu.evaluation.lesson.stat.model.QuestionTypeStat
import org.openurp.edu.evaluation.model.EvaluationCriteria
import org.openurp.edu.evaluation.model.EvaluationCriteriaItem
import org.openurp.edu.evaluation.model.QuestionType
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.hr.base.model.Staff
import org.openurp.hr.base.model.Staff
import org.openurp.hr.base.model.Staff
import org.openurp.hr.base.model.Staff

class QuestionnaireStatAction extends RestfulAction[LessonEvalStat] with ServletSupport {
  
  override def  index():String= {
    val stdType = entityDao.get(classOf[StdType],5)
    put("stdTypeList", stdType)
    val department = entityDao.get(classOf[Department],20)
    put("departmentList", department)

    var searchFormFlag = get("searchFormFlag").orNull
    if (searchFormFlag==null) {
      searchFormFlag = "beenStat"
    }
    put("searchFormFlag", searchFormFlag)
//    put("educations", getEducations())
    put("departments", entityDao.getAll(classOf[Department]))
    val query= OqlBuilder.from(classOf[Questionnaire], "questionnaire").where("questionnaire.state =:state",true)
    put("questionnaires", entityDao.search(query))
    val semesterId = 20141
      put("semester",entityDao.get(classOf[Semester], semesterId))
    put("evaluationCriterias",entityDao.getAll(classOf[EvaluationCriteria]))
    put("questionTypes", entityDao.getAll(classOf[QuestionType]))
     forward()
  }
 
  /**
   * 更新(教师)
   * 
   * @return
   */
  def  modifyTeacher():String= {
    put("questionnaireStat", entityDao.get(classOf[LessonEvalStat],getLong("questionnaireStat.id").get));
    forward();
  }

  /**
   * 查询 (获得教师)
   * 
   * @throws IOException
   */
  def searchTeacher() {
// throws IOException {
    val code = get("teacherCode").get;
    val teachers = entityDao.search(OqlBuilder.from(classOf[Staff],"sf").where("sf.code=:code", code))
    if (!teachers.isEmpty) {
      val teacher = teachers(0)
      response.setCharacterEncoding("utf-8");
      response.getWriter().print(
          teacher.id + "_" + teacher.person.name.formatedName + "_" + teacher.state.department.name);
    } else {
      response.getWriter().print("");
    }
  }

  /**
   * 保存(更新教师)
   * 
   * @return
   */
  def  saveTeacher():View= {
    val questionnaireStat = entityDao.get(classOf[LessonEvalStat],getLong("questionnaireStat.id").get);
    questionnaireStat.staff=entityDao.get(classOf[Staff], getLong("teacher.id").get)
    entityDao.saveOrUpdate(questionnaireStat);
    redirect("search", "info.update.success");
  }

  
   override def  remove():View= {
    val idStr = get("questionnaireStat.ids").get
    val Ids = idStr.split(",");
    val questionSIds = Array[Long]()
    for (i <- 0 to  Ids.length) {
      questionSIds(i) = Ids(i).toLong
    }
    val query = OqlBuilder.from(classOf[LessonEvalStat], "questionS");
    query.where("questionS.id in(:ids)", questionSIds);
    val li = entityDao.search(query);
    try {
      li foreach {questionnaireStat =>
        
        if (questionnaireStat.questionStats.size > 0) {
          val questionStats = questionnaireStat.questionStats
          
          questionStats foreach {questionstat =>
            
            val options = entityDao.search(OqlBuilder.from(classOf[OptionStat],"op").where("op.questionStat.id=:id", questionstat.id))
            val optionStats = Collections.newBuffer[OptionStat]
            
            options foreach {optionStat =>
              
              questionstat.optionStats -=optionStat
              optionStats +=optionStat
              // entityDao.remove(optionStat);
            }
//            for (OptionStat optionStat : options) {
//              questionstat.getOptionStats().remove(optionStat);
//              optionStats.add(optionStat);
//              // entityDao.remove(optionStat);
//            }
            if (optionStats.size > 0) {
              entityDao.remove(optionStats);
            }
//            if (optionStats.size > 0) {
//              entityDao.remove(optionStats);
//            }
            questionnaireStat.questionStats -=questionstat
            entityDao.remove(questionstat);
          
          }
//          for (QuestionDetailStat questionstat : questionStats) {
//            List<OptionStat> options = entityDao.get(OptionStat.class, "state.id", questionstat.getId());
//            List<OptionStat> optionStats = new ArrayList<OptionStat>();
//            for (OptionStat optionStat : options) {
//              questionstat.getOptionStats().remove(optionStat);
//              optionStats.add(optionStat);
//              // entityDao.remove(optionStat);
//            }
//            if (optionStats.size() > 0) {
//              entityDao.remove(optionStats);
//            }
//            if (optionStats.size() > 0) {
//              entityDao.remove(optionStats);
//            }
//            questionnaireStat.getQuestionStats().remove(questionstat);
//            entityDao.remove(questionstat);
//          }

        }
        if (questionnaireStat.questionTypeStats.size > 0) {
          val questionTS = questionnaireStat.questionTypeStats
          questionnaireStat.questionTypeStats --=questionTS
          entityDao.remove(questionTS);
        }
        entityDao.remove(questionnaireStat);
      
      }
//      for (QuestionnaireStat questionnaireStat : li) {
//        if (questionnaireStat.getQuestionStats().size() > 0) {
//          List<QuestionDetailStat> questionStats = new ArrayList<QuestionDetailStat>(
//              questionnaireStat.getQuestionStats());
//          for (QuestionDetailStat questionstat : questionStats) {
//            List<OptionStat> options = entityDao.get(OptionStat.class, "state.id", questionstat.getId());
//            List<OptionStat> optionStats = new ArrayList<OptionStat>();
//            for (OptionStat optionStat : options) {
//              questionstat.getOptionStats().remove(optionStat);
//              optionStats.add(optionStat);
//              // entityDao.remove(optionStat);
//            }
//            if (optionStats.size() > 0) {
//              entityDao.remove(optionStats);
//            }
//            if (optionStats.size() > 0) {
//              entityDao.remove(optionStats);
//            }
//            questionnaireStat.getQuestionStats().remove(questionstat);
//            entityDao.remove(questionstat);
//          }
//
//        }
//        if (questionnaireStat.getQuestionTypeStats().size() > 0) {
//          List<QuestionTypeStat> questionTS = new ArrayList<QuestionTypeStat>(
//              questionnaireStat.getQuestionTypeStats());
//          questionnaireStat.getQuestionTypeStats().removeAll(questionTS);
//          entityDao.remove(questionTS);
//        }
//        entityDao.remove(questionnaireStat);
//      }
    } catch {
      case e: Exception =>
      // TODO: handle exception
      redirect("search", "删除失败！");
    }
    redirect("search", "info.delete.success");
  }

  /**
   * 跳转(统计首页面)
   */
  def  statHome():String= {
    put("stdTypeList", entityDao.getAll(classOf[StdType]));
    put("departmentList", entityDao.search(OqlBuilder.from(classOf[Department],"de").where("de.teaching=:tea",true)));
    put("semester",20141)
    put("educations", entityDao.getAll(classOf[Education]))
    put("departments",entityDao.search(OqlBuilder.from(classOf[Department],"de").where("de.teaching=:tea",true)));
    forward();
  }

  /**
   * 跳转(初始有效值页面)
   */
  def  initValidHome():String= {
    put("stdTypeList", entityDao.getAll(classOf[StdType]));
     put("departmentList", entityDao.search(OqlBuilder.from(classOf[Department],"de").where("de.teaching=:tea",true)));
     put("semester",20141)
     forward();
  }

  /**
   * 设置有效记录
   **/
//  def  setValid():View= {
//    redirect(new Action(classOf[EvaluateResultStatAction], "search"), "更新成功");
//
//    // FIXEME 死方法
//    // Integer semesterId = getInt("semester.id");
//    // String percent = get("percent");
//    // questionnairStatService.setValidResult(semesterId, new Integer(percent));
//  }

  /**
   * 统计(学生评教结果)
   * 
   * @return
   */
//  def  stat():View= {
//    redirect(new Action(classOf[EvaluateResultStatAction], "search"), "更新成功");
//
//    // FIXEME 死方法
//    // setSemesterDataRealm(hasStdTypeTeachDepart);
//    // String departIdSeq = get("departIdSeq");
//    // String educationIdSeq = get("educationIdSeq");
//    // String semesterIdSeq = get("semester.id");
//    // // if (Strings.isBlank(departIdSeq)) {
//    // // departIdSeq = getDepartmentIdSeq();
//    // // }
//    // // if (Strings.isBlank(educationIdSeq)) {
//    // // educationIdSeq =getStdTypeIdSeq();
//    // // }
//    // User user = entityDao.get(User.class, getUserId());
//    // questionnairStatService.saveStatEvaluateResult(educationIdSeq, departIdSeq,
//    // semesterIdSeq, user);
//    // questionnairStatService.setDepartmentStat(educationIdSeq, semesterIdSeq);
//    // questionnairStatService.setCollegeStat(educationIdSeq, semesterIdSeq);
//    // return redirect(new Action(EvaluateResultStatAction.class, "search"), "更新成功");
//  }

  /**
   * 跳转(院系评教比较,根据所属部门)
   * 
   * @return
   */
//  @SuppressWarnings({ "unchecked", "rawtypes" })
  def  departDistributeStat():String= {
    val semesterId = getInt("semester.id").get
    val lis = entityDao.search(OqlBuilder.from (classOf[EvaluationCriteriaItem], "criteriaItem").where("criteriaItem.criteria.id =:id",1L)) 
    if (lis.size < 1) { redirect("search", "未找到评价标准！"); }
    put("criterias", lis);
    put("departments", entityDao.search(OqlBuilder.from(classOf[Department],"dep").where("dep.teaching=:tea",true)))
    put("semester", entityDao.get(classOf[Semester], semesterId));
    val que = OqlBuilder.from[Float](classOf[EvaluateResult].getName + " evaluateResult,"+ classOf[QuestionResult].getName + " questionResult");
    que.select("sum(questionResult.score)/count(distinct evaluateResult.id)");
    que.where("evaluateResult.id=questionResult.result.id");
    que.where("evaluateResult.lesson.semester.id=" + semesterId);
    val lit = entityDao.search(que);
    var fl = 0f;
    if (lit.size > 0) {
      if (lit(0) != 0f) {
        fl = lit(0)
      }
    }
    put("evaluateResults", fl);
    val hql = "select evaluateR.lesson.teachDepart.id,count( evaluateR.teacher.id) from" +
         " org.openurp.edu.teach.evaluate.course.QuestionnaireStat evaluateR " +
         " where evaluateR.lesson.semester.id=" + semesterId + " " +
         "group by evaluateR.lesson.teachDepart.id,evaluateR.lesson.semester.id ";
    put("questionNums", entityDao.search(hql));
    val maps = Collections.newMap[String, Seq[Array[Any]]]
    lis foreach {evaluationCriteriaItem =>
      val query = OqlBuilder.from[Array[Any]](classOf[LessonEvalStat].getName, "questionnaireStat");
      query.where("questionnaireStat.semester.id=:semesterId", semesterId);
      query.select("questionnaireStat.lesson.teachDepart.id,count(questionnaireStat.teacher.id)");
      query.where("questionnaireStat.score>=" + evaluationCriteriaItem.min
          + " and questionnaireStat.score<" + evaluationCriteriaItem.max);
      query.groupBy("questionnaireStat.lesson.teachDepart.id");
      maps.put(evaluationCriteriaItem.id.toString(), entityDao.search(query));
    
    }
//    for (EvaluationCriteriaItem evaluationCriteriaItem : lis) {
//      OqlBuilder<?> query = OqlBuilder.from(QuestionnaireStat.class, "questionnaireStat");
//      query.where("questionnaireStat.semester.id=:semesterId", semesterId);
//      query.select("questionnaireStat.lesson.teachDepart.id,count(questionnaireStat.teacher.id)");
//      query.where("questionnaireStat.score>=" + evaluationCriteriaItem.getMin()
//          + " and questionnaireStat.score<" + evaluationCriteriaItem.getMax());
//      query.groupBy("questionnaireStat.lesson.teachDepart.id");
//      maps.put(evaluationCriteriaItem.getId().toString(), entityDao.search(query));
//    }
    put("questionDeps", maps);
    forward();
  }

  // /**
  // * 饼图
  // *
  // * @param mapping
  // * @param form
  // * @param request
  // * @param response
  // * @return
  // */
  // public String pieOfEvaluate() {
  // String departmentId = get("departmentId");
  // if (Strings.isNotEmpty(departmentId)) {
  // Department department = (Department) entityDao.get(Department.class,
  // Long.valueOf(departmentId));
  // put("department", department);
  // }
  // String semesterId = get("semesterId");
  // if (Strings.isNotEmpty(semesterId)) {
  // Semester semester = (Semester) entityDao.get(Semester.class, Long.valueOf(semesterId));
  // put("semester", semester);
  // }
  // // 定义4个变量来接收 优,良,中,差的值.
  // Map optionNameMap = getOptionMap();
  // Map returnMap = questionnairStatService.getDatasOfPieChar(getStdTypeIdSeq(),
  // Long.valueOf(departmentId), Long.valueOf(semesterId), optionNameMap);
  // DefaultPieDataset dataset = new DefaultPieDataset();
  // dataset.setValue("优", returnMap.containsKey("A") ? (Integer) returnMap.get("A") : new
  // Integer(0));
  // dataset.setValue("良", returnMap.containsKey("B") ? (Integer) returnMap.get("B") : new
  // Integer(0));
  // dataset.setValue("中", returnMap.containsKey("C") ? (Integer) returnMap.get("C") : new
  // Integer(0));
  // dataset.setValue("差", returnMap.containsKey("D") ? (Integer) returnMap.get("D") : new
  // Integer(0));
  // put("pageViews", new GeneralDatasetProducer("pieChart", dataset));
  // return forward();
  // }
  //
  // /**
  // * 线图。。。。。
  // *
  // * @param mapping
  // * @param form
  // * @param request
  // * @param response
  // * @return
  // */
  // public String lieOfEvaluate() {
  // String[] level = new String[] { "优", "良", "中", "差" };
  // Integer semesterId = getLong("questionnaireStat.semester.id");
  // Semester semester = (Semester) entityDao.get(Semester.class, semesterId);
  // put("semester", semester);
  // Map optionNameMap = getOptionMap();
  // List collegeList = departmentService.getColleges(getDepartmentIdSeq());
  // List seriesNames = new ArrayList();
  // Map lineChartMap = questionnairStatService.getDataByDepartAndRelated(getStdTypeIdSeq(),
  // getDepartmentIdSeq(), semester.getId(), optionNameMap);
  // DefaultCategoryDataset dataset = new DefaultCategoryDataset();
  // for (int i = 0; i < collegeList.size(); i++) {
  // Department college = (Department) collegeList.get(i);
  // seriesNames.add(college.getName());
  // dataset.setValue(
  // lineChartMap.containsKey(college.getId() + "-A") ? (Integer) lineChartMap.get(college
  // .getId() + "-A") : new Integer(0), college.getName(), level[0]);
  // dataset.setValue(
  // lineChartMap.containsKey(college.getId() + "-B") ? (Integer) lineChartMap.get(college
  // .getId() + "-B") : new Integer(0), college.getName(), level[1]);
  // dataset.setValue(
  // lineChartMap.containsKey(college.getId() + "-C") ? (Integer) lineChartMap.get(college
  // .getId() + "-C") : new Integer(0), college.getName(), level[2]);
  // dataset.setValue(
  // lineChartMap.containsKey(college.getId() + "-D") ? (Integer) lineChartMap.get(college
  // .getId() + "-D") : new Integer(0), college.getName(), level[3]);
  // }
  // // 定义4个变量来接收 优,良,中,差的值.
  // String[] seriesName = new String[seriesNames.size()];
  // for (int i = 0; i < seriesNames.size(); i++) {
  // seriesName[i] = seriesNames.get(i).toString();
  // }
  // put("pageViews", new DefaultCategoryProducer("lieOfEvaluate", dataset, seriesName));
  // return forward();
  // }


}