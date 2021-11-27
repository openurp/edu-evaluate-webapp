[@b.head/]
[@urp_base.semester_bar value=currentSemester/]
<div style="border:0.5px solid #006CB2">
[@b.grid items=stats var="stat" sortable="false"]
    [@b.row]
        [@b.col property="crn" title="课程序号" width="7%"/]
        [@b.col property="course.code" title="课程代码" width="9%"/]
        [@b.col property="course.name" title="课程名称" width="31%"]
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
          [#if stat.crn?? && feedbackCounts[stat.crn]??]
            [@b.a href="!comments?stat.id=" +stat.id target="_blank"]查看<span class="badge badge-light">${feedbackCounts[stat.crn]}</span>[/@]
          [/#if]
        [/@]
        [@b.col property="grade.name" title="档次" width="5%"/]
        [@b.col property="departRank" title="院系排名" width="7%"/]
        [@b.col property="schoolRank" title="全校排名" width="7%"/]
        [@b.col property="tickets" title="参评人数" width="7%"/]
    [/@]
[/@]


[@b.foot/]
