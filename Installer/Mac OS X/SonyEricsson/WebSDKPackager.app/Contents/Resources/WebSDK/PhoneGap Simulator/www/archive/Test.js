startXHR();

function handler() {
alert(this.responseText);
return
}

function startXHR() {
var client = new XMLHttpRequest();
client.onreadystatechange = handler;
client.open("GET", "file:///Users/yoheishimomae/Documents/Flex%20Builder%203/PhoneGap/src/www/index.html");
client.send();
//client.statusText
}

