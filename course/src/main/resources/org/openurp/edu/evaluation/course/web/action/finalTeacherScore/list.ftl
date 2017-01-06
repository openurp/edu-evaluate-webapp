[#ftl]
[@b.head/]
[@b.form name="finalTeacherScoreSearchForm" action="!search" target="contentDiv"]    <table id="bar" width="100%"></table>
    <input type="hidden" name="semester.id" value="${semesterId!}">
    [@b.grid items=finalTeacherScores var="finalTeacherScore" sortable="true"]    
        [@b.gridbar title="教师评教最终结果"]
        bar.addItem("导出","exportData()");
        [/@]
        [@b.row]
            [@b.boxcol/]
            [@b.col  property="teacher.user.department.name" title="教师所属部门" width="20%"/]
            [@b.col  property="teacher.user.code" title="教师工号"/]
            [@b.col  property="teacher.user.name" title="教师姓名"/]
            [@b.col  property="stdScore" title="学生评教"]${finalTeacherScore.stdScore}[/@]
            [@b.col  property="supviScore" title="督导评教"]${finalTeacherScore.supviScore}[/@]        
            [@b.col  property="departScore" title="院系评教"]${finalTeacherScore.departScore}[/@]        
            [@b.col  property="score" title="最后得分"]${finalTeacherScore.score}[/@]     
            [@b.col property="rank" title="全校排名"/]
            [@b.col property="departRank" title="院系排名"/]
        [/@]
    [/@]
[/@]
<script>
 var form = document.finalTeacherScoreSearchForm;
 function exportData(){
      bg.form.addInput(form,"semester.id",document.finalTeacherScoreSearchForm['semester.id'].value);
      bg.form.addInput(form,"keys","teacher.user.code,teacher.name,teacher.state.department.name,semester.code,stdScore,supviScore,departScore,score,rank");
      bg.form.addInput(form,"titles","教师工号,教师姓名,教师所属院系，学年学期,学生评教,督导评教,院系评教,最后得分,全校排名");
      bg.form.addInput(form,"fileName","finalTeacherScore");
      bg.form.submit(form, "${b.url('!export')}", '_blank');
  }
  </script>
[@b.foot/]