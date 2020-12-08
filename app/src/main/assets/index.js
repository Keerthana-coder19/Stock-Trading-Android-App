
function init(ticker) {
    const url = 'https://stock-search-nodejs-be.wl.r.appspot.com/historicalchart/' + ticker;
    var stkprice = [];
    var volume = [];
    fetch(url).then((response) => {
          response.json().then((data) => {

            for (var i = 0; i < data.length; i++) {
              let d = new Date(data[i].date);
              let UTCdate = Date.UTC(
                d.getFullYear(),
                d.getMonth(),
                d.getDate(),
                d.getHours(),
                d.getMinutes(),
                d.getSeconds()
              );
              stkprice[i] = [
                UTCdate,
                data[i].open,
                data[i].high,
                data[i].low,
                data[i].close,
              ];
              volume[i] = [UTCdate, data[i].volume];

            }

            var chart1 = new Highcharts.stockChart({
                      chart:{
                        renderTo: 'chart_1',
                        height:300
                        },
                      navigator: {
                        enabled: true,
                      },
                      rangeSelector: { selected: 2 },
                      yAxis: [
                        {
                          startOnTick: false,
                          endOnTick: false,
                          labels: {
                            align: 'right',
                            x: -3,
                          },
                          title: {
                            text: 'OHLC',
                          },
                          height: '60%',
                          lineWidth: 2,
                          resize: {
                            enabled: true,
                          },
                        },
                        {
                          labels: {
                            align: 'right',
                            x: -3,
                          },
                          title: {
                            text: 'Volume',
                          },
                          top: '65%',
                          height: '35%',
                          offset: 0,
                          lineWidth: 2,
                        },
                      ],

                      tooltip: {
                        split: true,
                      },

                      plotOptions: {
                        series: {
                          dataGrouping: {
                            units: [
                                       ['week', [1]],
                                       ['month', [1, 2, 3, 4, 6]],
                             ],
                          },
                        },
                      },

                      series: [
                        {
                          type: 'candlestick',
                          name: ticker,
                          id: ticker,
                          zIndex: 2,
                          data: stkprice,
                        },
                        {
                          type: 'column',
                          name: 'Volume',
                          id: 'volume',
                          data: volume,
                          yAxis: 1,

                        },
                        {
                          type: 'vbp',
                          linkedTo: ticker,
                          params: {
                            volumeSeriesID: 'volume',
                          },
                          dataLabels: {
                            enabled: false,
                          },
                          zoneLines: {
                            enabled: false,
                          },
                        },
                        {
                          type: 'sma',
                          linkedTo: ticker,
                          zIndex: 1,
                          marker: {
                            enabled: false,
                          },
                        },
                      ]});
                });
            });

}

