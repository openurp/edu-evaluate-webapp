[#ftl]
[@b.head/]
[@b.form name="questionnaireStatSearchForm" action="!search" target="contentDiv"]
    [@b.grid items=lessonEvalStats var="questionnaireStat" sortable="true"]
        [@b.gridbar title="院系教师评教详细信息"]
            bar.addItem("更改教师","updateTeacher()");
            //bar.addItem("${b.text('action.delete')}",action.remove());
            bar.addItem("${b.text('action.export')}","exportData()");
        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col property="lesson.no" title="课程序号"/]
            [@b.col property="lesson.course.name" title="课程名称"/]
            [@b.col property="lesson.course.code" title="课程代码"/]
            [@b.col property="lesson.teachDepart.name" title="开课院系"/]
            [@b.col property="teacher.user.name" title="任课教师"/]
            [#if questionnaireStat?exists]
            [#list questionnaireStat.questionTypeStats as questionType]
            [@b.col property="" title="${questionType.questionType.name}"]${questionType.score!}[/@]
            [/#list]
            [/#if]
            [@b.col property="validTickets" title="有效票数"/]
            [@b.col property="score" title="总评"]${questionnaireStat.score}[/@]
        [/@]
    [/@]
[/@]
<script type="text/javaScript">
    var searchForm = document.questionnaireStatSearchForm;
    $(".gridempty").html("");

    function exportData(){
        alert("有待完善!");
    }
    function updateTeacher(){
        bg.form.submit(searchForm, "${b.url('!modifyTeacher')}");
    }
</script>
[@b.foot/]
