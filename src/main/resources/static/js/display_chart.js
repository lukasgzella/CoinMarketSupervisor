const log = console.log;

const chartProperties = {
  width: 1100,
    height: 600,
	layout: {
	    background: '#302525',
		textColor: 'rgba(255, 255, 255, 0.9)',
	},
	grid: {
		vertLines: {
			visible: false,
		},
		horzLines: {
			visible: false,
		},
	},
	crosshair: {
		mode: LightweightCharts.CrosshairMode.Normal,
	},
	rightPriceScale: {
		borderColor: 'rgba(197, 203, 206, 0.8)',
	},
	timeScale: {
		borderColor: 'rgba(197, 203, 206, 0.8)',
	}
}

const domElement = document.getElementById('tvchart');
const chart = LightweightCharts.createChart(domElement,chartProperties);
const candleSeries = chart.addCandlestickSeries({
  upColor: '#26a69a',
  downColor: '#ef5350',
  borderDownColor: '#ef5350',
  borderUpColor: '#26a69a',
  wickDownColor: '#ef5350',
  wickUpColor: '#26a69a',
});

const BinanceFullURL = 'https://api.binance.com/api/v3/klines?symbol=' + symbol + 'USDT&interval=' + interval + '&limit=1000';
fetch(BinanceFullURL)
  .then(res => res.json())
  .then(data => {
    const cdata = data.map(d => {
      return {time:d[0]/1000,open:parseFloat(d[1]),high:parseFloat(d[2]),low:parseFloat(d[3]),close:parseFloat(d[4])}
    });
    candleSeries.setData(cdata);
  })
  .catch(err => log(err))

//Dynamic Chart
const socket = io.connect('http://127.0.0.1:4000');
socket.on('KLINE',(pl)=>{
  log(pl);
  let obj = JSON.parse(pl);
  obj.time = obj.time/1000;
  log(obj);
  candleSeries.update(obj);
});