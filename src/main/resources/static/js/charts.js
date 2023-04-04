var coin = 'BTC';
var interval = '4h';
function dispCoin(sel) {
  coin = sel.options[sel.selectedIndex].text;
  console.log(coin);
}
function dispInterval(sel) {
  interval = sel.options[sel.selectedIndex].text;
  console.log(interval);
}
function send() {
  let object = {
'coinSymbol': coin,
'interval': interval
};
let json = JSON.stringify(object);
let xhr = new XMLHttpRequest();
xhr.open('PUT', '/menu/charts/set-details', false);
xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
xhr.send(json);
if (xhr.status == 202) {
alert("chart details mapped!");
document.getElementById("submitnext").submit();
  } else {
  alert("something wrong!");
  }
}