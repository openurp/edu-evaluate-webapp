[@b.head/]
[#assign indicatorNames=[]/]
[#assign indicators =categoryStat.indicatorList /]
[#list indicators as i]
  [#assign indicatorNames=indicatorNames+[i.name] /]
[/#list]
[#assign assessGrades=categoryStat.gradeList/]

[#assign scoreRanges=["[0,10)","[10,20)","[20,30)","[30,40)","[40,50)","[50,60)","[60,70)","[70,80)","[80,90)","[90,100]"] /]

[#function getCounts(ranges)]
    [#assign keys=["00","10","20","30","40","50","60","70","80","90"] /]
    [#assign values={}/]
    [#list ranges as r]
       [#assign values=values+{(r.fromScore)?string('00'):r.courseCount}/]
    [/#list]
    [#assign cnts=[]]
    [#list keys as k]
    [#assign cnts=cnts+[values[k]!0]]
    [/#list]
    [#return cnts/]
[/#function]

[#function getAvgScores(statGrades)]
  [#assign values={}/]
   [#list statGrades as g]
       [#assign values=values+{g.indicator.name:g.avgScore}/]
    [/#list]
    [#assign scores=[]]
    [#list indicatorNames as k]
    [#assign scores=scores+[values[k]!0]]
    [/#list]
    [#return scores/]
[/#function]

<script>
    bg.requireCss("${base}/static/css/style.css");
</script>
[#include "../bars.ftl"/]
<div style="width:800px;min-width:400px;margin:auto">
    <div class="header">
        <img class="header-photo" src="${b.static_url('local','images/qos/head-photo.jpg')}">
        <div class="header-title">
            ${stat.semester.schoolYear}学年 ${stat.semester.name}学期<br>
            ${stat.course.project.school.name}课程评教结果分析报告
        </div>
    </div>
    <section class="view" style="background-color: rgb(249, 245, 244);">
        <div class="pj_container">
            <p>
                尊敬的${stat.teacher.user.name}老师，您好：<br>
                本学期您所授的《${stat.course.name}》(课程序号:${stat.crn})共有 ${stat.tickets} 名学生对课程进行了评价，通过统计与分析，课程的教学质量评价报告已经生成，如下内容供您参考。
            </p>
            <div class="zb-title">
                <div class="line-divider"></div>
                ◇ &nbsp;评价指标及排名&nbsp; ◇
                <div class="line-divider"></div>
            </div>
            [#list stat.indicatorStats?sort_by(['indicator','code']) as indicatorDetail]
            <div class="title-1">
                <span>${indicatorDetail.indicator.name!}表现</span> | 通过评教统计指标分析：
            </div>
            <div class="title-2">
                您所授课程在${indicatorDetail.indicator.description!}，在同类课程中所处排位如下图<span class="txt-red">【红色】</span>区间所示：
            </div>
            <div class="chart">
                [#assign ranges=categoryStat.getRanges(indicatorDetail.indicator)/]
                [#--查找我所在的位置--]
                [#assign alertIdx=0/]
                [#list ranges as r]
                  [#if r.contains(indicatorDetail.score)]
                    [#assign alertIdx = (r.fromScore/10)?string('0')/]
                    [#break/]
                  [/#if]
                [/#list]
                [#assign cnts=getCounts(ranges)/]
                [@echarts id="chart_"+indicatorDetail.id  height="360" xrotate=0  xname='分数区间' yname='同类课程数量'  names=scoreRanges values=cnts alertIdx=alertIdx/]
            </div>
            <div class="fenxi">
                分析：您在${indicatorDetail.indicator.name}方面得分为：<b>${indicatorDetail.score?string("#.###")}</b>
                <span style="color: transparent;">空白</span>排名为：<b>${indicatorDetail.categoryRank}/${categoryStat.courseCount}</b>
                <div class="fenxijieguo">
                [#if indicatorDetail.grade.name?contains('A')][#--10~19 属于A级--]
                  <span aria-hidden="true" class="glyphicon glyphicon-thumbs-up">
                  </span>
                [/#if]
                ${indicatorDetail.grade.description}
                </div>
            </div>
            [#if indicatorDetail_has_next]<div class="line-divider"></div>[/#if]
            [/#list]
        </div>
        <!--/.container-->
        <div style="height: 60px;"></div>
        <div class="pj_container">
            <p class="zhpj"> ◇ &nbsp;综合档次评价&nbsp; ◇</p>
            <div class="line-divider"></div>
            <div class="fenxi">
                根据上述六项指标，<br>
                您在同类课程中的综合评价档次为<b>【${stat.grade.name}】</b><br>
                课程在开课院系的排名：<b>${stat.departRank} / ${departEvalStat.courseCount}</b><br>
                课程在所属${categoryStat.category.name}中的排名：<b>${stat.categoryRank}  / ${categoryStat.courseCount}</b><br>
                与各档平均水平得分的对比如下，供参考。
            </div>

            [#assign gradeColors={'A':'#7cb5ec','B':'#434348','C':'#90ed7d','D':'#f7a35c','您的得分':'red'}/]
            <div id="chart-container" class="chart-container" data-highcharts-chart="0">
               <div id="radar_${stat.id}" style="height: 330px;left: 10%;"></div>
                <script type="text/javascript">
                    function init_radar_${stat.id}(echarts){
                  // 基于准备好的容器(这里的容器是id为chart1的div)，初始化echarts实例
                  var chart1 = echarts.init(document.getElementById("radar_${stat.id}"));

                  // 指定图表的配置项和数据
                  var option = {
                      color:[[#list assessGrades as g]'${gradeColors[g.name]}',[/#list]'red'],
                      legend: {                        // 图例组件
                          show: true,
                          icon: 'rect',                   // 图例项的 icon。ECharts 提供的标记类型包括 'circle', 'rect', 'roundRect', 'triangle', 'diamond', 'pin', 'arrow'也可以通过 'image://url' 设置为图片，其中 url 为图片的链接，或者 dataURI。可以通过 'path://' 将图标设置为任意的矢量路径。
                          top : '0%',                    // 图例距离顶部边距
                          //left : '10%',                   // 图例距离左侧边距
                          itemWidth: 10,                  // 图例标记的图形宽度。[ default: 25 ]
                          itemHeight: 10,                 // 图例标记的图形高度。[ default: 14 ]
                          itemGap: 15,                  // 图例每项之间的间隔。[ default: 10 ]横向布局时为水平间隔，纵向布局时为纵向间隔。
                          orient: 'horizontal',             // 图例列表的布局朝向,'horizontal'为横向,''为纵向.
                          textStyle: {                    // 图例的公用文本样式。
                              fontSize: 15,
                              color: '#fff'
                          },
                          data: [
                          [#list assessGrades as g]
                          {                    // 图例的数据数组。数组项通常为一个字符串，每一项代表一个系列的 name（如果是饼图，也可以是饼图单个数据的 name）。图例组件会自动根据对应系列的图形标记（symbol）来绘制自己的颜色和标记，特殊字符串 ''（空字符串）或者 '\n'（换行字符串）用于图例的换行。
                              name: '${g.name}',                 // 图例项的名称，应等于某系列的name值（如果是饼图，也可以是饼图单个数据的 name）。
                              icon: 'rect',               // 图例项的 icon。
                              textStyle: {                // 图例项的文本样式。
                                  color: '${gradeColors[g.name]}',
                                  fontWeight: 'bold'    // 文字字体的粗细，可选'normal'，'bold'，'bolder'，'lighter'
                              }
                          },
                          [/#list]
                          {                    // 图例的数据数组。数组项通常为一个字符串，每一项代表一个系列的 name（如果是饼图，也可以是饼图单个数据的 name）。图例组件会自动根据对应系列的图形标记（symbol）来绘制自己的颜色和标记，特殊字符串 ''（空字符串）或者 '\n'（换行字符串）用于图例的换行。
                                name: '您的得分',                 // 图例项的名称，应等于某系列的name值（如果是饼图，也可以是饼图单个数据的 name）。
                                icon: 'rect',               // 图例项的 icon。
                                textStyle: {                // 图例项的文本样式。
                                    color: '${gradeColors['您的得分']}',
                                    fontWeight: 'bold'    // 文字字体的粗细，可选'normal'，'bold'，'bolder'，'lighter'
                                }
                            }
                          ],
                      },

                      radar: [{                       // 雷达图坐标系组件，只适用于雷达图。
                          center: ['50%', '60%'],             // 圆中心坐标，数组的第一项是横坐标，第二项是纵坐标。[ default: ['50%', '50%'] ]
                          radius: 100,                        // 圆的半径，数组的第一项是内半径，第二项是外半径。
                          startAngle: 90,                     // 坐标系起始角度，也就是第一个指示器轴的角度。[ default: 90 ]
                          name: {                             // (圆外的标签)雷达图每个指示器名称的配置项。
                              formatter: '{value}',
                              textStyle: {
                                  fontSize: 15,
                                  color: '#000'
                              }
                          },
                          nameGap: 15,                        // 指示器名称和指示器轴的距离。[ default: 15 ]
                          splitNumber: 2,                     // (这里是圆的环数)指示器轴的分割段数。[ default: 5 ]
                          shape: 'polygon',                    // 雷达图绘制类型，支持 'polygon'(多边形) 和 'circle'(圆)。[ default: 'polygon' ]
                          axisLine: {                         // (圆内的几条直线)坐标轴轴线相关设置
                              lineStyle: {
                                  color: 'gray',                   // 坐标轴线线的颜色。
                                  width: 1,                         // 坐标轴线线宽。
                                  type: 'solid',                   // 坐标轴线线的类型。
                              }
                          },
                          splitLine: {                        // (这里是指所有圆环)坐标轴在 grid 区域中的分隔线。
                              show:true
                          },
                          splitArea: {                        // 坐标轴在 grid 区域中的分隔区域，默认不显示。
                              show: false
                          },
                          indicator: [
                          [#list categoryStat.getIndicators(assessGrades?first) as i]
                          {               // 雷达图的指示器，用来指定雷达图中的多个变量（维度）,跟data中 value 对应
                              name: '${i.indicator.name}',                           // 指示器名称
                              max: 100,                               // 指示器的最大值，可选，建议设置
                          }[#if i_has_next],[/#if]
                          [/#list]]
                      }],
                      series: [{
                          name: '雷达图',             // 系列名称,用于tooltip的显示，legend 的图例筛选，在 setOption 更新数据和配置项时用于指定对应的系列。
                          type: 'radar',              // 系列类型: 雷达图
                          itemStyle: {                // 折线拐点标志的样式。
                              normal: {                   // 普通状态时的样式
                                  lineStyle: {
                                      width: 2
                                  },
                                  opacity: 0.5
                              },
                              emphasis: {                 // 高亮时的样式
                                  lineStyle: {
                                      width: 5
                                  },
                                  opacity: 1
                              }
                          },
                          data: [
                          [#list assessGrades as grade]
                          {                    // 雷达图的数据是多变量（维度）的
                              name: '${grade.name}',                 // 数据项名称
                              [#assign avg_scores= getAvgScores(categoryStat.getIndicators(grade))/]
                              value: [[#list avg_scores as g]${g?string("#0.00")}[#if g_has_next],[/#if][/#list]],        // 其中的value项数组是具体的数据，每个值跟 radar.indicator 一一对应。
                              symbol: 'circle',                   // 单个数据标记的图形。
                              symbolSize: 5,                      // 单个数据标记的大小，可以设置成诸如 10 这样单一的数字，也可以用数组分开表示宽和高，例如 [20, 10] 表示标记宽为20，高为10。
                              label: {                    // 单个拐点文本的样式设置
                                      normal: {
                                          show: false
                                      }
                                  },
                              itemStyle: {                // 单个拐点标志的样式设置。
                                  normal: {
                                      borderColor: '${gradeColors[grade.name]}',       // 拐点的描边颜色。[ default: '#000' ]
                                      borderWidth: 3,                        // 拐点的描边宽度，默认不描边。[ default: 0 ]
                                  }
                              },
                              lineStyle: {                // 单项线条样式。
                                  normal: {
                                      opacity: 0.5 ,           // 图形透明度
                                      color:"${gradeColors[grade.name]}"
                                  }
                              }
                          },
                         [/#list]
                           {                    // 雷达图的数据是多变量（维度）的
                              name: '您的得分',                 // 数据项名称
                              value: [[#list stat.indicatorStats?sort_by(['indicator','code']) as i]${i.score?string("#.00")}[#if i_has_next],[/#if][/#list]],        // 其中的value项数组是具体的数据，每个值跟 radar.indicator 一一对应。
                              symbol: 'circle',                   // 单个数据标记的图形。
                              symbolSize: 5,                      // 单个数据标记的大小，可以设置成诸如 10 这样单一的数字，也可以用数组分开表示宽和高，例如 [20, 10] 表示标记宽为20，高为10。
                              label: {                    // 单个拐点文本的样式设置
                                      normal: {
                                          show: false
                                      }
                                  },
                              itemStyle: {                // 单个拐点标志的样式设置。
                                  normal: {
                                      borderColor: '${gradeColors['您的得分']}',       // 拐点的描边颜色。[ default: '#000' ]
                                      borderWidth: 3,                        // 拐点的描边宽度，默认不描边。[ default: 0 ]
                                  }
                              },
                              lineStyle: {                // 单项线条样式。
                                    normal: {
                                        type:'dotted',
                                        opacity: 0.5 ,           // 图形透明度
                                        color:"${gradeColors['您的得分']}"
                                    }
                                },
                              areaStyle: {
                                    normal: {
                                        color: '#F6CECE'
                                    }
                                }
                          }
                         ]
                      }, ]
                  };

                  // 使用刚指定的配置项和数据显示图表
                  chart1.setOption(option);
                 }
                </script>
            </div>
        </div>
        <div class="btn-container">
            <a class="btn">${stat.course.project.school.name}课程评教结果分析</a>
        </div>
    </section>
 </div>
 <script>
        function initCharts_${stat.indicatorStats?first.id}(echarts){
        [#list stat.indicatorStats?sort_by(['indicator','code']) as i]
          init_chart_${i.id}(echarts);
        [/#list]
          init_radar_${stat.id}(echarts);
        }
        require(['echarts'],initCharts_${stat.indicatorStats?first.id});
 </script>
[@b.foot/]
