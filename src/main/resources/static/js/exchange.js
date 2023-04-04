var uc = 'USDT';
var ec = 'USDT';
function dispUc(sel) {
  uc = sel.options[sel.selectedIndex].text;
  console.log(uc);
}
function dispEc(sel) {
  ec = sel.options[sel.selectedIndex].text;
  console.log(ec);
}
function send() {
  let object = {
'userCoin': uc,
'coinToExchange': ec
};
let json = JSON.stringify(object);
let xhr = new XMLHttpRequest();
xhr.open('POST', '/menu/exchange/set-pair', false);
xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
xhr.send(json);
if (xhr.status == 200) {
alert("coins mapped!");
document.getElementById("submitnext").submit();
  } else {
  alert("something wrong!");
  }
}