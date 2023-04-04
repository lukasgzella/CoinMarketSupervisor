var amountUcToExchange;
var amountCteAfterExchange;

var slider = document.getElementById("range");
var output = document.getElementById("amount");
var inUsdt = document.getElementById("inUsdt");
var afterExchange = document.getElementById("after");

slider.setAttribute("max", maxAmount);
output.innerHTML = slider.value;
inUsdt.innerHTML = slider.value * ucPrice;
afterExchange.innerHTML = (slider.value * ucPrice) / ctePrice;

slider.oninput = function() {
  output.innerHTML = this.value;
  amountUcToExchange = this.value;
  inUsdt.innerHTML = this.value * ucPrice;
  afterExchange.innerHTML = (this.value * ucPrice) / ctePrice;
  amountCteAfterExchange = (this.value * ucPrice) / ctePrice;
}

async function displayCurrentPrice(coinSymbol) {
  const BINANCE_PRICE_URI = "https://api.binance.com/api/v3/ticker/price?symbol=" + coinSymbol + "USDT";
  let res = await fetch(BINANCE_PRICE_URI, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
            },
        })
        .then(response => response.json());
  console.log(res.price);
  return res.price;
}

async function exchange() {
  sendTransactionDetails();
  let updatedTransaction = await getUpdatedTransactionDetails();
  ucPrice = updatedTransaction.ucPrice;
  ctePrice = updatedTransaction.ctePrice;
  amountCteAfterExchange = updatedTransaction.amountCteAfterExchange;
  let message = amountUcToExchange + userCoin + " will be exchanged to " + amountCteAfterExchange  + coinToExchange;
  return message;
}
async function showConfirmation() {
  var window = document.getElementById('hid');
  var message = document.getElementById('message');
  message.innerHTML = await exchange();
  window.style.display = 'block';
  setTimeout(function() {
    window.style.display = 'none';
  }, 5000);
}

function confirmTransaction() {
  let transaction = {
    'userCoin': userCoin,
    'ucPrice': ucPrice,
    'amountUcToExchange': amountUcToExchange,
    'coinToExchange': coinToExchange,
    'ctePrice': ctePrice,
    'amountCteAfterExchange': amountCteAfterExchange
  };
  let json = JSON.stringify(transaction);
  let xhr = new XMLHttpRequest();
  xhr.open('POST', '/menu/exchange/confirm', false);
  xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
  xhr.send(json);
  if (xhr.status == 202) {
    alert("Transaction successful!");
    var goToTransactionPage = document.getElementById('next_page');
    goToTransactionPage.submit();
    } else {
      alert("Something went wrong please try again!");
    }
}
function sendTransactionDetails() {
  let transaction = {
    'userCoin': userCoin,
    'amountUcToExchange': amountUcToExchange,
    'coinToExchange': coinToExchange,
  };
  let json = JSON.stringify(transaction);
  let xhr = new XMLHttpRequest();
  xhr.open('POST', '/menu/exchange/set-details', false);
  xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
  xhr.send(json);
  if (xhr.status == 202) {
    alert("details send to server!");
  } else {
    alert("Something wrong!");
  }
}
async function getUpdatedTransactionDetails() {
  const SERVER_PRICE_URI = "http://localhost:28852/menu/exchange/get-details";
  let transaction = await fetch(SERVER_PRICE_URI, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
            },
        })
        .then(response => response.json());
  console.log(transaction);
  return transaction;
}