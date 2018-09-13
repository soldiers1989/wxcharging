<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/top.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
</head>
<body>
<p id="messages"></p>
<button id='startBtn'>start</button>
<button id='sendBtn'>send</button>
<button id='stopBtn'>stop</button>
<%@include file="../common/js.jsp" %>
</body>
<script type="text/javascript">
var webSocket;
var isOpen=false;
$("#sendBtn").on("click",function(){
		send();	
});

$("#startBtn").on("click",function(){
		initWebSocket();
});
$("#stopBtn").on("click",function(){
		stop();
});
 
function initWebSocket(){
	if(!isOpen){
		if ('WebSocket' in window) {
	    	webSocket = new WebSocket("ws://127.0.0.1:8081/wxcharging/ws/websocket");  
		} else if ('MozWebSocket' in window) {  
	    	webSocket = new MozWebSocket("ws://127.0.0.1:8081/wxcharging/ws/websocket");  
		} else {
		    alert("js");  
		    webSocket = new SockJS("ws://127.0.0.1:8081/wxcharging/ws/websocket");  
		} 
	
		if(webSocket){
				webSocket.onerror = function(event) {  
				  	onError(event)  
				};  
				
				webSocket.onopen = function(event) { 
				  	onOpen(event);  
				};  
				
				webSocket.onmessage = function(event) {  
				  	onMessage(event)  
				};  
				
				webSocket.onclose = function(){
					onClose();
				}
		}
  	}
} 
 
function onMessage(event) {  
	writeContent(event.data);
}  
function writeContent(content){
	$("#messages").append(content);
/* 	document.getElementById('messages').innerHTML  
    += '<br />' + content;   */
}
function onOpen(event) {  
	isOpen = true; 
	writeContent("open");
}  
function onClose(){
	isOpen=false;
	writeContent("close");
}
function onError(event) {  
	isOpen=false;
  	writeContent("error>>"+event.data); 
} 
//发送信息
function send() {
	if(isOpen){
		 webSocket.send(new Date()); 
	}
} 
//关闭连接
function stop(){
	if(isOpen){
		webSocket.close();	
	}
}
</script>
</html>

