// 显示实时时间
function showTime() {
    let time = new Date();
    let year = time.getFullYear();
    let month = (time.getMonth() + 1 + '').padStart(2, '0');
    let day = (time.getDate() + '').padStart(2, '0');
    let hour = (time.getHours() + '').padStart(2, '0');
    let minute = (time.getMinutes() + '').padStart(2, '0');
    let second = (time.getSeconds() + '').padStart(2, '0');

    let content = `${year}年${month}月${day}日 ${hour}:${minute}:${second}`;

    $('#title .time').text(content);
}
showTime();
setInterval(showTime, 1000);

  function getData() {
      $.ajax({
          url: 'https://api.inews.qq.com/newsqa/v1/query/inner/publish/modules/list?modules=statisGradeCityDetail,diseaseh5Shelf',
          dataType: 'json',
          success: (res) => {
              let data = res.data.diseaseh5Shelf;
               upcenter(data);
               bottomCenter(data);
               upright(data);
               bottomRight(data);
          }
      });

      $.ajax({
          url: 'https://api.inews.qq.com/newsqa/v1/query/inner/publish/modules/list',
          type: 'post',
          data: {
              modules: 'chinaDayList,chinaDayAddList,nowConfirmStatis,provinceCompare'
          },
          dataType: 'json',
          success: (res) => {
              upleft(res.data);
              bootmLeft(res.data);
          }
      })
  }
getData();

function upcenter(data) {
    $(".confirm").text(data.chinaTotal.confirm);
    $(".heal").text(data.chinaTotal.heal);
    $(".dead").text(data.chinaTotal.dead);
    $(".localConfirm").text(data.chinaTotal.localConfirm);
    $(".noInfect").text(data.chinaTotal.noInfect);
    $(".importedCase").text(data.chinaTotal.importedCase);
}

function bottomCenter(data) {

    let myChart = echarts.init(document.getElementById('bottom-center'), 'dark');

    let option = {
        title: {
            text: ''
        },
        tooltip: {
            trigger: 'item'
        },
        visualMap: {
            show: true,
            x: 'left',
            y: 'bottom',
            textStyle: {
                fontSize: 8
            },
            // 左侧小导航图标
            splitList: [
                {start: 1, end: 9},
                {start: 10, end: 99},
                {start: 100, end: 999},
                {start: 1000, end: 9999},
                {start: 10000}
            ],
            color: ['#8A3310', '#C64918', '#E55B25', '#F2AD92', '#F9DCD1']
        },
        series: [
            {
                name: '累计确诊人数',
                type: 'map',
                mapType: 'china',
                // 禁用拖动和缩放
                roam: false,
                // 图形样式
                itemStyle: {
                    normal: {
                        // 区域边框高度
                        borderWidth: 0.5,
                        // 区域边框颜色
                        borderColor: '#009fe8',
                        // 区域颜色
                        araeaColor: '#ffefd5'
                    },
                    // 鼠标滑过地图高亮的设置
                    emphasis: {
                         borderWidth: 0.5,
                         borderColor: '#4b0082',
                         araeaColor: '#fff'
                    }
                },
                // 图形上的文本标签
                label: {
                    normal: {
                        show: true,
                        fontSize: 8
                    },
                    emphasis: {
                        show: true,
                        fontSize: 8
                    }
                },
                data:[]
            }
        ]
    };

    let provinces = data.areaTree[0].children;
    for(let province of provinces) {
        let obj = {};
        obj.name = province.name;
        obj.value = province.total.confirm;
        option.series[0].data.push(obj);
    }
    myChart.setOption(option);
}

function upright(data) {
    let myChart = echarts.init(document.getElementById('upright'), 'dark');

    let option = {
        title: {
            text: '全国确诊省市TOP10',
            textStyle: {
                color: 'white'
            },
            left: 'left'
        },
        color: ['#3398DB'],
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        xAxis: {
            type: 'category',
            data: []
        },
        yAxis: {
            type: 'value',
            axisLabel: {
                show: true,
                color: 'white',
                fontSize: 12,
                formatter: function(value) {
                    if(value >= 1000) {
                        value = value / 1000 + 'k';
                    }
                    return value;
                }
            },
        },
        series: [
            {
                data: [],
                type: 'bar',
                barMaxWidth: "50%"
            }
        ]
    };

    let topData = [];
    let provinces = data.areaTree[0].children;
    for(let province of provinces) {
        let obj = {};
        obj.name = province.name;
        obj.value = province.total.confirm;
        topData.push(obj);
        
    }
    topData.sort((a, b) => {
        return b.value - a.value;
    });

    topData = topData.slice(0, 10);
    for(let province of topData) {
        option.xAxis.data.push(province.name);
        option.series[0].data.push(province.value);
    }
    // topData.length = 10;
    console.log(topData)
    myChart.setOption(option);
   
}

function bottomRight(data) {
    let myChart = echarts.init(document.getElementById('bottom-right'), 'dark');

    option = {
        title: {
            text: '境外输入TOP5',
            left: 'center'
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: []
        },
        series: [
            {
                name: '省市名称',
                type: 'pie',
                radius: '50%',
                data: [],
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    let topData = [];
    let provinces = data.areaTree[0].children;
    for(let province of provinces) {
       province.children.forEach((item) => {
        if(item.name == '境外输入') {
            let obj = {};
            obj.name = province.name;
            obj.value = item.total.confirm;
            topData.push(obj);
       }
       })
    }
    topData.sort((a, b) => {
        return b.value - a.value;
    });

    topData.length = 5;
    for(let province of topData) {
        option.legend.data.push(province.name);
        option.series[0].data.push(province);
    }
    myChart.setOption(option);
}

function upleft(data) {
    let myChart = echarts.init(document.getElementById('upleft'), 'dark');

    option = {
        title: {
            text: '全国累计趋势',
            textStyle: {
                color: 'white'
            },
            left: 'left'
        },
        tooltip: {
            trigger: 'axis',
            // 指示器
            axisPointer: {
                type: 'line',
                lineStyle: {
                    color: '#7171C6'
                }
            }
        },
        legend: {
            data: ['累计确诊', '累计治愈', '累计死亡'],
            left: 'right'
        },
        grid: {
            left: '4%',
            right: '6%',
            bottom: '4%',
            top: 50,
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                data: []
            }
        ],
        yAxis: [{
            type: 'value',
            // y轴字体设置
            axisLabel: {
                show: true,
                fontSize: 12,
                color: 'white',
                formatter: function(value) {
                    if(value > 1000) {
                        value = value / 100 + 'k';
                    }
                    return value;
                },
                // y轴线设置
                axisLine: {
                    show: true
                },
                // 与x轴平行的线样式
                splitLine: {
                    show: true,
                    lineStyle: {
                        color: '#17273B',
                        width: 1,
                        type: 'solid'
                    }
                }
            }
        }],
        series: [
            {
                name: '累计确诊',
                type: 'line',
                smooth: true,
                data: []
            },
            {
                name: '累计治愈',
                type: 'line',
                mooth: true,
                data: []
            },
            {
                name: '累计死亡',
                type: 'line',
                mooth: true,
                data: []
            },
        ]
    };
    let chinaDayList = data.chinaDayList;
    for(let day of chinaDayList) {
        option.xAxis[0].data.push(day.date);
        option.series[0].data.push(day.confirm);
        option.series[1].data.push(day.heal);
        option.series[2].data.push(day.dead);
    }

    myChart.setOption(option);
}
function bootmLeft(data) {
    let myChart = echarts.init(document.getElementById('bottom-left'), 'dark');

    option = {
        title: {
            text: '全国新增趋势',
            textStyle: {
                color: 'white'
            },
            left: 'left'
        },
        tooltip: {
            trigger: 'axis',
            // 指示器
            axisPointer: {
                type: 'line',
                lineStyle: {
                    color: '#7171C6'
                }
            }
        },
        legend: {
            data: ['新增确诊', '新增疑似'],
            left: 'right'
        },
        grid: {
            left: '4%',
            right: '6%',
            bottom: '4%',
            top: 50,
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                data: []
            }
        ],
        yAxis: [{
            type: 'value',
            // y轴字体设置
            axisLabel: {
                show: true,
                fontSize: 12,
                color: 'white',
                formatter: function(value) {
                    if(value > 1000) {
                        value = value / 100 + 'k';
                    }
                    return value;
                },
                // y轴线设置
                axisLine: {
                    show: true
                },
                // 与x轴平行的线样式
                splitLine: {
                    show: true,
                    lineStyle: {
                        color: '#17273B',
                        width: 1,
                        type: 'solid'
                    }
                }
            }
        }],
        series: [
            {
                name: '新增确诊',
                type: 'line',
                smooth: true,
                data: []
            },
            {
                name: '新增疑似',
                type: 'line',
                smooth: true,
                data: []
            }
        ]
    };

   
    let chinaDayAddList = data.chinaDayAddList;
    console.log(chinaDayAddList);
    for(let day of chinaDayAddList) {
        option.xAxis[0].data.push(day.date);
        option.series[0].data.push(day.confirm);
        option.series[1].data.push(day.suspect);
    }
    myChart.setOption(option);
}