[#ftl]
[@b.toolbar title="教师个人的历史评级结果"]
  bar.addBack();
[/@]

[@b.grid items=stats var="courseEvalStat" sortable="true"]
    [@b.row]
        [@b.col title="序号" width="5%"]${courseEvalStat_index+1}[/@]
        [@b.col property="semester.beginOn" title="学年学期" width="10%"]
            ${courseEvalStat.semester.schoolYear} ${courseEvalStat.semester.name}
        [/@]
        [@b.col property="crn" title="课程序号" width="7%"/]
        [@b.col property="course.code" title="课程代码" width="9%"/]
        [@b.col property="course.name" title="课程名称" width="20%"/]
        [@b.col property="teachDepart.name" title="开课院系" width="8%"]
            ${courseEvalStat.teachDepart.shortName!courseEvalStat.teachDepart.name}
        [/@]
        [@b.col property="category.name" title="课程分类" width="12%"]
            [#if courseEvalStat.category.name?length>8]
                <span style="font-size:0.7em">${courseEvalStat.category.name}</span>
            [#else]
                ${courseEvalStat.category.name}
            [/#if]
        [/@]
        [@b.col property="score" title="总得分" width="6%"]
            [@b.a href="!info?id=" +courseEvalStat.id target="_blank"]${courseEvalStat.score?string("#.00")}[/@]
        [/@]
        [@b.col property="grade.name" title="档次" width="5%"/]
        [@b.col property="departRank" title="院系排名" width="6%"/]
        [@b.col property="schoolRank" title="全校排名" width="6%"/]
        [@b.col property="tickets" title="参评人数" width="6%"/]
    [/@]
[/@]
[@b.foot/]
