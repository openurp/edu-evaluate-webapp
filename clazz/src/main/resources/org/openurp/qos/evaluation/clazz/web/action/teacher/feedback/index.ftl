[#ftl]
[@b.head/]
[@b.toolbar title="过程评教查询"]
  bar.addBack();
[/@]
[@urp_base.semester_bar value=currentSemester/]
<div class="container-fluid">
  <div class="row">
     <div class="col-3" id="accordion">

       <div class="card card-info card-primary card-outline">
         <div class="card-header" id="stat_header_2">
              <button class="btn btn-link" data-toggle="collapse" data-target="#stat_body_2" aria-expanded="true" aria-controls="stat_body_2" style="padding: 0;">
                课程名称（课程序号）
              </button>
              [#if feedbacks?size>0]
              <span class="badge badge-primary">${feedbacks?size}门课程</span>
              <span class="badge badge-success">${stdCount}名同学</span>
              [/#if]
         </div>
         <div id="stat_body_2" class="collapse show" aria-labelledby="stat_header_2" data-parent="#accordion">
           <div class="card-body" style="padding: 0px;">
           <ul class="list-group">
              [#list feedbacks as fd]
                <li class="list-group-item">
                 <a href="#" onclick="displayFeedBack('${fd.course.id}_${fd.crn}')">${fd.course.name} ${fd.crn}</a> <span class="badge  badge-primary" style="float:right">${fd.feedbacks?size}</span></li>
              [/#list]
           </ul>
           </div>
         </div>
       </div>
     </div><!--end col-3-->
     <div  class="col-9">

          <div class="card card-info card-primary card-outline">
              <div class="card-header">
                <h3 class="card-title" id="feedback_title">[#if feedbacks?size>0]${feedbacks?first.course.name} ${feedbacks?first.crn} <small>学生反馈</small>[/#if]</h3>
              </div>
              <div class="card-body">
              [#list feedbacks as fd]
                 <div  id="feedback_${fd.course.id}_${fd.crn}" [#if fd_index=0]style="display:block"[#else]style="display:none"[/#if]>
                   <table class="table">
                     <tr>
                       <th>序号</th>
                       <th>综合感受</th>
                       <th>内容</th>
                       <th>时间</th>
                     </tr>
                   [#list fd.feedbacks as f]
                        <tr>
                         <td>${f_index+1}</td>
                         <td>${f.grade!}</td>
                         <td>${f.contents}</td>
                         <td><small>${f.updatedAt?string("yyyy-MM-dd HH:mm")}</small></td>
                        </tr>
                   [/#list]
                   </table>
                 </div>
              [/#list]
              </div>
          </div>

     </div>
  </div>
</div>
<script>
   var clazzes={}
   [#list feedbacks as fd]
     clazzes['${fd.course.id}_${fd.crn}']='${fd.course.name} ${fd.crn}';
   [/#list]
   function displayFeedBack(id){
     jQuery("#feedback_title").html(clazzes[id]+" <small>学生反馈</small>");
     for( k in clazzes){
        jQuery("#feedback_"+k).hide();
     }
     jQuery("#feedback_"+id).show();
   }
</script>
[@b.foot/]
