[@b.head/]
[@b.toolbar title="期末课程评教"]
  bar.addBack();
[/@]
<div class="container">
  [#list stats?keys?sort_by("beginOn")?reverse as semester]
     [#assign semesterStats= stats.get(semester)/]
     [#assign firstStat= semesterStats?first/]
       <div class="card card-info card-primary card-outline">
         <div class="card-header" style="padding: .5rem;">
            ${firstStat.semester.schoolYear}学年 ${firstStat.semester.name} 学期
            <span class="badge badge-primary">${semesterStats?size}门</span>
         </div>
         <div class="card-body" style="padding-top: 0px;">
          [@b.grid items=semesterStats var="stat" theme="mini" style="table-layout:fixed;" class="table-sm"]
            [@b.row]
              [@b.col property="crn" title="课程序号" width="7%"/]
              [@b.col property="course.code" title="课程代码" width="9%"/]
              [@b.col property="course.name" title="课程名称"]
                [@b.a href="!info?id=" +stat.id target="_blank"]${stat.course.name}[/@]
              [/@]
              [@b.col property="teachDepart.name" title="开课院系" width="10%"]
                ${stat.teachDepart.shortName!stat.teachDepart.name}
              [/@]
              [@b.col property="category.name" title="课程分类" width="11%"]
                [#if stat.category.name?length>8]
                <span style="font-size:0.7em">${stat.category.name}</span>
                [#else]
                ${stat.category.name}
                [/#if]
              [/@]
              [@b.col title="文字评教" width="7%"]
                [#if stat.crn?? && feedbackCounts[stat.semester.id?string+"_"+stat.crn]??]
                  [@b.a href="!comments?stat.id=" +stat.id target="_blank"]查看<span class="badge badge-light">${feedbackCounts[stat.semester.id?string+"_"+stat.crn]}</span>[/@]
                [/#if]
              [/@]
              [@b.col property="grade.name" title="档次" width="5%"/]
              [@b.col property="departRank" title="院系排名" width="7%"/]
              [@b.col property="schoolRank" title="全校排名" width="7%"/]
              [@b.col property="tickets" title="参评人数" width="7%"/]
            [/@]
          [/@]
       </div>
     </div>
    [/#list]
  </div>

[@b.foot/]
