<#include "/template/head.ftl"/>
 <body>
 <table id="gpBar"></table>
 <script>
  var bar = new ToolBar("gpBar","统计结果",null,true,true);
  bar.setMessage('<@getMessage/>');
  bar.addItem("<@text name="action.print"/>","print()");
  bar.addBack("<@text name="action.back"/>");
 </script>
<@cewolf.chart  id="line"  title=" ${semester?if_exists.schoolYear?if_exists}学年度 ${semester?if_exists.name?if_exists}学期 汇总情况"  type="line"  
  xaxislabel="Page"  yaxislabel="Views" showlegend=true
  backgroundimagealpha=0.5>   
 <@cewolf.data>
    <@cewolf.producer id="pageViews"/>    
 </@cewolf.data>
 </@cewolf.chart>
 <p style="text-align:center">
 <@cewolf.img chartid="line" renderer="cewolf" width=650 height=487.5/>
 </p>
 <body>
 <#include "/template/foot.ftl"/>