// JavaScript Document
$(document).ready(function() {


    $('#BTN1').click(function(){
        var plane_ca = $("#s1").val(); //机型
        var plane_num = $("#s2").val();//机号
        var parame = $("#s4").val();//参数
        var start_date = $("#start_date").val();//开始日期
        var startdateStr = start_date.replace(/\-/g, ""); //去除‘-’
        var end_date = $("#end_date").val();//截止日期
        var enddateStr =  end_date.replace(/\-/g, "");
        // alert (start_date)
        // alert(startdateStr)
        $.post("query_sensor",
            {

                plane_number:plane_num[0],
                start_date: startdateStr,
                end_date: enddateStr,
                field_key1: parame[0],
                field_key2: parame[1],
                field_key3: parame[2],
                field_key4: parame[3]



                // plane_num: plane_num[0],

            },
            function(data,status){
                var title = {
                    text:  plane_num
                };
                var subtitle = {
                    text: 'aipt'
                };
                var xAxis = {
                    categories: ['1','2','3','4','5']
                };
                var yAxis = {
                    title: {
                        text: 'Temperature (\xB0C)'
                    },
                    plotLines: [{
                        value: 0,
                        width: 1,
                        color: '#808080'
                    }]
                };

                var tooltip = {
                    valueSuffix: '\xB0C'
                }

                var legend = {
                    layout: 'vertical',
                    align: 'right',
                    verticalAlign: 'middle',
                    borderWidth: 0
                };
                var Results = data.results[0].series[0].values;
                var T471 = [];
                var T472 = [];
                var T474 = [];
                var T475 = [];
                for (var i=0;i < Results.length;i++)
                {
                    T471.push(Results[i][1])
                    T474.push(Results[i][2])
                    T472.push(Results[i][3])
                    T475.push(Results[i][4])
                }

                var series =  [
                    {
                        name: parame[0],
                        data: T471
                    },
                    {
                        name: parame[1],
                        data: T474
                    },
                    {
                        name: parame[2],
                        data: T472
                    },
                    {
                        name: parame[3],
                        data: T475
                    }
                ];

                var json = {};

                json.title = title;
                json.subtitle = subtitle;
                json.xAxis = xAxis;
                json.yAxis = yAxis;
                json.tooltip = tooltip;
                json.legend = legend;
                json.series = series;

                $('#right').highcharts(json);
                alert("数据: \n" + data + "\n状态: " + status);
            });







    });



});
