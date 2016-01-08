package org.openurp.edu.evaluation.course.web.action

import org.openurp.base.model.Semester
import org.beangle.webmvc.entity.action.RestfulAction
import org.hibernate.tool.hbm2ddl.Exporter
import org.beangle.webmvc.api.annotation.response
import javax.servlet.http.HttpServletResponse
import org.openurp.edu.evaluation.lesson.model.DepartEvaluation
import org.beangle.data.dao.OqlBuilder
import java.io.IOException

class DepartEvaluationAction  extends RestfulAction[DepartEvaluation] {
//
//
//  @Override
//  public String getEntityName() {
//    return DepartEvaluation.class.getName();
//  }
//
//  public String index() {
//    put("stdTypeList", getStdTypes());
//    put("departmentList", getColleges());
//
//    put("departments", getDeparts());
//    return forward();
//  }
//
//  public String search() {
//    String code = get("departEvaluation.teacher.staff.code");
//    String name = get("departEvaluation.teacher.staff.person.name");
//    Long departId = getLong("departEvaluation.teacher.staff.department.id");
//    Integer semesterId = getInt("departmentEvaluation.semester.id");
//    if (getBoolean("passed")) {
//      OqlBuilder<DepartEvaluation> query = getQueryBuilder();
//      put("departEvaluations", entityDao.search(query));
//    } else {
//      OqlBuilder<Teacher> quer = OqlBuilder.from(Teacher.class, "teacher");
//      if (code != null && !"".equals(code)) {
//        quer.where("teacher.staff.code like '%" + code + "%'");
//      }
//      if (name != null && !"".equals(name)) {
//        quer.where("teacher.staff.person.name like '%" + name + "%'");
//      }
//      if (departId != null) {
//        quer.where("teacher.staff.department.id=" + departId);
//        quer.where("not exists (select departE.id from org.openurp.edu.teach.evaluate.course.model.DepartEvaluationBean"
//            + " departE where departE.semester.id="
//            + semesterId.toString()
//            + " and "
//            + "departE.teacher.staff.department.id=" + departId + " and departE.teacher.id = teacher.id)");
//      } else {
//        quer.where("teacher.staff.department in(:departs)", getDeparts());
//        quer.where(
//            "not exists (select departE.id from org.openurp.edu.teach.evaluate.course.model.DepartEvaluationBean"
//                + " departE where departE.semester.id=" + semesterId.toString() + " and "
//                + "departE.teacher.staff.department in (:deps) and departE.teacher.id = teacher.id)", getDeparts());
//      }
//      quer.limit(getPageLimit());
//      quer.orderBy(Order.parse(get("orderBy")));
//      put("teachers", entityDao.search(quer));
//      return forward("teacherList");
//    }
//    return forward();
//  }
//
//  protected <T extends Entity<?>> OqlBuilder<T> getQueryBuilder() {
//    OqlBuilder<T> query = OqlBuilder.from(getEntityName(), "departEvaluation");
//    populateConditions(query);
//    query.limit(getPageLimit());
//    query.orderBy(Order.parse(get("orderBy")));
//    return query;
//  }

//  public String addTeaEvaluate() {
//    String idStr = get("teacher.ids");
//    OqlBuilder<Teacher> query = OqlBuilder.from(Teacher.class, "teacher");
//    query.where("teacher.id in (" + idStr + ")");
//    put("teachers", entityDao.search(query));
//    put("semester", entityDao.get(Semester.class, getInt("semester.id")));
//    return forward();
//  }

//  public String saveTeaEvaluate() {
//    String[] teaIds = (get("teacher.ids") + ",").split(",");
//    Integer semesterId = getInt("semester.id");
//    Semester semester = entityDao.get(Semester.class, semesterId);
//    OqlBuilder<Staff> query = OqlBuilder.from(Staff.class, "teacher");
//    query.where("teacher.id in (" + get("teacher.ids") + ")");
//    List<Staff> teachers = entityDao.search(query);
//    List<DepartEvaluation> list = new ArrayList<DepartEvaluation>();
//    Date date = new Date();
//    try {
//      for (String id : teaIds) {
//        DepartEvaluation departE = Model.newInstance(DepartEvaluation.class);
//        departE.setScore(getFloat(id + "_score"));
//        departE.setSemester(semester);
//        departE.setUpdateAt(date);
//        departE.setUserName(getUsername());
//        for (Staff teacher : teachers) {
//          if (teacher.getId().toString().equals(id)) {
//            departE.setStaff(teacher);
//          }
//        }
//        list.add(departE);
//      }
//      entityDao.saveOrUpdate(list);
//    } catch (Exception e) {
//      // TODO: handle exception
//      return redirect("search", "info.save.failure", "passed=0&departmentEvaluation.semester.id="
//          + semesterId);
//    }
//    return redirect("search", "info.save.success", "passed=1&departmentEvaluation.semester.id=" + semesterId);
//  }

//  public String edit() {
//    String teaIdStr = get("departEvaluation.ids");
//    OqlBuilder<DepartEvaluation> quer = OqlBuilder.from(DepartEvaluation.class, "departE");
//    quer.where("departE.id in (" + teaIdStr + ")");
//    put("departEvaluations", entityDao.search(quer));
//    put("semester", entityDao.get(Semester.class, getInt("semester.id")));
//    return forward();
//  }

//  public String save() {
//    Integer semesterId = getInt("semester.id");
//    OqlBuilder<DepartEvaluation> query = OqlBuilder.from(DepartEvaluation.class, "departEvaluation");
//    query.where("departEvaluation.id in (" + get("departEvaluation.ids") + ")");
//    List<DepartEvaluation> departEvaluations = entityDao.search(query);
//    List<DepartEvaluation> list = new ArrayList<DepartEvaluation>();
//    Date date = new Date();
//    try {
//      for (DepartEvaluation departEvaluation : departEvaluations) {
//        departEvaluation.setScore(getFloat(departEvaluation.getId() + "_score"));
//        departEvaluation.setUpdateAt(date);
//        list.add(departEvaluation);
//      }
//      entityDao.saveOrUpdate(list);
//    } catch (Exception e) {
//      // TODO: handle exception
//      return redirect("search", "info.save.failure", "passed=0&departmentEvaluation.semester.id="
//          + semesterId);
//    }
//    return redirect("search", "info.save.success", "passed=1&departmentEvaluation.semester.id=" + semesterId);
//  }

//  public String remove() {
//    Long[] ids = getLongIds("departEvaluation");
//    try {
//      OqlBuilder<DepartEvaluation> query = OqlBuilder.from(DepartEvaluation.class, "departmentEvaluation");
//      query.where("departmentEvaluation.id in(:ids)", ids);
//      entityDao.remove(entityDao.search(query));
//    } catch (Exception e) {
//      // TODO: handle exception
//      return redirect("search", "info.delete.failure");
//    }
//    return redirect("search", "info.delete.success");
//  }

//  @Override
//  public String importForm() {
//    return forward();
//  }

//  @Override
//  protected List<TransferListener> getImporterListeners() {
//    List<TransferListener> listeners = CollectUtils.newArrayList();
//    listeners.add(new ImporterForeignerListener(entityDao));
//    listeners.add(new DepEvaluationImportListener(entityDao));
//    return listeners;
//  }

//  @Override
//  public String importData() {
//    TransferResult tr = new TransferResult();
//    EntityImporter importer = buildEntityImporter(DepartEvaluation.class);
//    if (importer == null) { return redirect("importForm", "请上传一个正确的XLS文件!"); }
//    configImporter(importer);
//    try {
//      importer.transfer(tr);
//    } catch (RuntimeException e) {
//      // 模板错误
//      put("importResultState", "importFileError");
//      tr.addFailure("<font style='color:red;font-size:14px'>您使用的模版不正确!</font>", "");
//    } catch (Exception e) {
//      // 数据错误
//      e.printStackTrace();
//    }
//    put("importResult", tr);
//    put("importer", importer);
//    put("downUrl", "departEvaluation!downDepEvaluateTemp.action");
//    return tr.hasErrors() ? forward("/components/importData/error")
//        : forward("/components/importData/result");
//
//  }

  /**
   * 下载 (支付信息,导入模板)
   * 
   * @return
   * @throws IOException
   */
//  public String downDepEvaluateTemp() throws IOException {
//    Context context = new Context();
//    context.put("format", TransferFormat.Xls);
//    Exporter exporter = new TemplateExporter();
//    HttpServletResponse response = ServletActionContext.getResponse();
//    // 设置 (下载信息)
//    TemplateWriter templateWriter = new ExcelTemplateWriter();
//    templateWriter.setTemplate(getResource("template/excel/部门评教结果导入模版.xls"));
//    templateWriter.setOutputStream(response.getOutputStream());
//    templateWriter.setContext(context);
//    exporter.setWriter(templateWriter);
//    response.setContentType("application/vnd.ms-excel;charset=GBK");
//    String oldFileName = "部门评教结果导入模版.xls";
//    String fileName = RequestUtils.encodeAttachName(ServletActionContext.getRequest(), oldFileName);
//    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//    // 进行输出
//    exporter.setContext(context);
//    exporter.transfer(new TransferResult());
//    return null;
//  }


}