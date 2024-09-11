[@b.head/]
<div class="container">
<div class="page-header">
  <h4>学生反馈<small>${courseEvalStat.crn} ${courseEvalStat.course.name} ${courseEvalStat.teacher.name}</small></h4>
</div>
<ul class="list-group">
 [#list comments as comment]
      <li class="list-group-item"> ${comment_index+1} ${comment.contents} <small>${comment.updatedAt?string("yyyy-MM-dd HH:mm")}</small></li>
 [/#list]
 </ul>
 </div>
[@b.foot/]
