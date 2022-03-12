[#ftl]
[@b.head/]
[@b.form name="questionnaireStatSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=courseEvalStats var="courseEvalStat" sortable="true"]
        [@b.gridbar title="院系教师评教详细信息"]
            var detailMenu = bar.addMenu("查看详情", "info()");
            detailMenu.addItem("教师历史评教", "evaluateTeachHistory()", "info.png");
            bar.addItem("${b.text('action.delete')}","remove()");
            bar.addItem("${b.text('action.export')}","exportData()");

        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="crn" title="课程序号" width="7%"/]
            [@b.col property="course.code" title="课程代码" width="10%"/]
            [@b.col property="course.name" title="课程名称" width="20%"/]
            [@b.col property="teachDepart.shortName" title="开课院系" width="10%"/]
            [@b.col property="teacher.user.code" title="教师工号" width="10%"/]
            [@b.col property="teacher.user.name" title="教师姓名" width="10%"/]
            [@b.col property="score" title="分数" width="7%"/]
            [@b.col property="schoolRank" title="全校排名" width="7%"/]
            [@b.col property="departRank" title="院系排名" width="7%"/]
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
        var url= "${b.url('course-eval-search!info?id=aaa')}"
        url=url.replace('aaa',questionnaireStatIds);
        bg.form.submit(form,url);
    }
    var searchForm = document.questionnaireStatSearchForm;

    $(".gridempty").html("");

    function exportData(){
        alert("有待完善!");
    }
</script>
[@b.foot/]
