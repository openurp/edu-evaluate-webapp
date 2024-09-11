[#macro echarts id title='' title2='' names=[] values=[] onclick='' xname='' yname='' interval=0 color=true showSeriesLable=true xrotate=-30
 barMinHeight=20 maxAndMin=true series='' height=300 legend='' trigger='item' datas=[] alertIdx=0]

[#if (names?size gt 0) || (datas?size gt 0)]
<div id="${id}" style="height:${height}px;"></div>
[#assign type="bar"/]
<script>
   function init_${id}(echarts) {
            // 基于准备好的dom，初始化echarts图表
            var myChart = echarts.init(document.getElementById('${id}'));
            var option = {
                color: ['#3398DB'],
                title: {
                  text:'${title}',
                  left:'center'
                  [#if title2 != '']
                  , subtext : '${title2}', textAlign:'center'
                  [/#if]},
                //renderAsImage:true,
                [#if legend != '']
                legend: {
                    data:${legend}
                },
                [/#if]

                [#if type != "pie"]
                xAxis : [
                    {
                        [#if xname != '']name : '${xname}',[/#if]
                        type : 'category',
                        start:0,
                        axisLabel:{interval:'${interval}', rotate:${xrotate}},
                        splitLine:{show: true},
                        axisLine:{
                          lineStyle:{
                            color:'#337ab7',
                          }
                        },
                        [#if datas?size gt 0]
                        data : [[#list datas as d][#if d_index gt 0],[/#if]'${d[0]}'[/#list]]
                        [#else]
                        data : [[#list names as d][#if d_index gt 0],[/#if]'${d}'[/#list]]
                        [/#if]
                    }
                ],
                yAxis : [
                    {
                        scale:true,
                        axisLine:{
                          lineStyle:{
                            color:'#337ab7',
                          }
                        },
                        [#if yname != '']name : '${yname}',[/#if]
                        type : 'value',
                        splitLine:{show: true}
                    }
                ],
                tooltip : {
                  trigger: '${trigger}',
                  axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                  },
                  formatter: function (params) {
                    return params['name']+"<br>课程数量："+params['value'];
                  }
                },
                [#else]//pie
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                calculable : true,
                [/#if]
                [#if series == '']
                series : [
                    {
                        type:"${type}",
                        barMinHeight: ${barMinHeight},
                        barWidth:30,
                        smooth:true,
                        [#if type == "pie"]
                          data:[
                          [#list datas as d]
                            {value: ${d[1]},  name:'${d[0]}'}[#if d_has_next],[/#if]
                          [/#list]
                          ],
                        [#else]
                          itemStyle: {
                              normal: {
                                lineStyle:{
                                  color:'#E87C25'
                                },
                                label: {
                                      show: ${showSeriesLable?string},
                                      position: 'top',
                                      formatter: '{c}'
                                },
                                color :function(params){
                                   if(params.dataIndex==${alertIdx}){
                                     return 'rgb(255,149,162)'
                                   }else{
                                     return '#3398DB';
                                   }
                                }

                              }
                          },
                          [#if datas?size gt 0]
                            "data":[[#list datas as d][#if d_index gt 0],[/#if]${d[1]}[/#list]],
                          [#else]
                            "data":[[#list values as d][#if d_index gt 0],[/#if]${d}[/#list]],
                          [/#if]
                        [/#if]
                        [#if maxAndMin]
                        markPoint : {
                            data : [
                                {type : 'max', name: '最大值'},
                                {type : 'min', name: '最小值'}
                            ]
                        },
                        [/#if]
                        markLine : {
                            data : [
                                {type : 'average', name: '平均值'}
                            ]
                        }
                    }
                ]
                [#else]
                series : ${series}
                [/#if]
            };
            // 为echarts对象加载数据
            myChart.setOption(option);
            [#if onclick != '']
            myChart.on('click', function (param){
              if(param.name == '最大值' || param.name == '最小值') return;
              ${onclick}(param)
            });
            [/#if]
        }
</script>
[#else]
<div style="padding:100px; font-size:20px; text-align:center">暂无数据</div>
[/#if]
[/#macro]
