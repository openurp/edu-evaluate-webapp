[#ftl]
[@b.head/]
[@b.form name="textEvaluationSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=lessons var="lesson" sortable="true"]
        [@b.gridbar title="教学任务列表"]
            bar.addItem("查看文字评教","infoText()","info.png");
        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="no" title="课程序号" width="10%"/]
            [@b.col property="course.code" title="课程代码"/]
            [@b.col property="course.name" title="课程名称"/]
            [@b.col property="teachclass.name" title="教学班" width="45%"/]
            [#--[@b.col property="semester" title="学年学期" width="18%"]
                ${(lesson.semester.schoolYear)!}学年${(lesson.semester.name)!}学期
            [/@]--]
        [/@]
    [/@]
[/@]
<script type="text/javaScript">
    var searchForm = document.textEvaluationSearchForm;

    function infoText(){
        var id = bg.input.getCheckBoxValues("lesson.id");
        if(id == "" || null == id || id.split(",").length != 1){
            alert("请选择一项!");
            return false;
        }
        bg.form.submit(searchForm,"${b.url('!searchTextEvaluation')}");
    }
</script>
[@b.foot/]
