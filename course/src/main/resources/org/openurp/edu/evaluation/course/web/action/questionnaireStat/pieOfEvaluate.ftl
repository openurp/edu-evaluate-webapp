<#include "/template/head.ftl"/>
 <body>
 <table id="gpBar"></table>
 <script>
  var bar = new ToolBar("gpBar","统计结果",null,true,true);
  bar.setMessage('<@getMessage/>');
  bar.addItem("<@text name="action.print"/>","print()");
  bar.addBack("<@text name="action.back"/>");
 </script>
<@cewolf.chart  id="line"  title="${department?if_exists.name?if_exists} ${semester?if_exists.schoolYear?if_exists}学年度 第${semester?if_exists.name?if_exists}学期 评教汇总"   type="pie"
  xaxislabel="Page"  yaxislabel="Views" showlegend=false
  backgroundimagealpha=0.5>
 <@cewolf.data>
    <@cewolf.producer id="pageViews"/>
 </@cewolf.data>
 </@cewolf.chart>
 <p>
 <@cewolf.img chartid="line" renderer="cewolf" width=400 height=300/>
 <body>
 <#include "/template/foot.ftl"/>
