[#ftl]
[@b.head/]
[@b.form name="questionnaireStatSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=courseEvalStats var="courseEvalStat" sortable="true"]
        [@b.gridbar title="院系教师评教详细信息"]
            var detailMenu = bar.addMenu("查看详情", "info()");
            detailMenu.addItem("教师历史评教", "evaluateTeachHistory()", "info.png");
            //detailMenu.addItem("查看大类统计", "doing()", "info.png");
            //detailMenu.addItem("详细选项统计", "teachQuestionDetailStat()", "info.png");
            //detailMenu.addItem("学院分项汇总", "doing()", "info.png");
            //detailMenu.addItem("全校分类评教", "doing()", "info.png");
            bar.addItem("${b.text('action.delete')}","remove()");
            bar.addItem("${b.text('action.export')}","exportData()");

        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="course.code" title="课程代码"/]
            [@b.col property="course.name" title="课程名称"/]
            [@b.col property="course.department.name" title="开课院系"/]
            [@b.col property="teacher.user.code" title="教师工号"/]
            [@b.col property="teacher.user.name" title="教师姓名"/]
            [@b.col property="totalScore" title="教师课程得分"]${courseEvalStat.totalScore!}[/@]
        [/@]

    [/@]
[/@]
<script type="text/javaScript">
var form = document.questionnaireStatSearchForm;
    function doing(){
            var form = document.questionnaireStatSearchForm;
            var clazzIds = bg.input.getCheckBoxValues("courseEvalStat.id");
                alert(clazzIds);
    }

    function remove(){
        var questionnaireStatIds = bg.input.getCheckBoxValues("courseEvalStat.id");
        bg.form.addInput(form,"courseEvalStat.id",questionnaireStatIds);
        bg.form.submit(form,"${b.url('!remove')}");
    }
    function evaluateTeachHistory(){
        var questionnaireStatIds = bg.input.getCheckBoxValues("courseEvalStat.id");
        if(questionnaireStatIds == "" || questionnaireStatIds.split(",").length !=1){
                alert("请选择一个进行操作！");
                return false;
        }
        bg.form.addInput(form,"courseEvalStat.id",questionnaireStatIds);
        bg.form.submit(form,"${b.url('!evaluateTeachHistory')}");
    }

    function info(){
        var questionnaireStatIds = bg.input.getCheckBoxValues("courseEvalStat.id");
        if(questionnaireStatIds == "" || questionnaireStatIds.split(",").length !=1){
                alert("请选择一个进行操作！");
                return false;
        }
        bg.form.addInput(form,"courseEvalStat.id",questionnaireStatIds);
        var url="${b.url('course-eval-search!info?id=xxxx')}";
        url = url.replace('xxxx',questionnaireStatIds);
        bg.form.submit(form,url);
    }
    var searchForm = document.questionnaireStatSearchForm;

    $(".gridempty").html("");

    function exportData(){
        alert("有待完善!");
    }
</script>
[@b.foot/]
