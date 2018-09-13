const stopPageInterval=60000;//当前界面刷新时间间隔
const getDataInterval=5000;//间隔几秒获取时间
const stopTimeOut=120;//超时时间
const maxTimeOut=60;//最大超时时间
const startTimeOut=120;//启动充电时间
const perMi=1000;//每隔一秒触发
const sendCodeTime=60;//发送短信时间
var appId="wx30c09c08b1313c7b";
var redirectUri="http%3a%2f%2fcharging.crxnytech.com%2fwxcharging%2f";

function getkey(event,id){
        event = event || window.event;
        if(event.keyCode==13){
            event.returnValue = false;
            document.getElementById(id).blur();
            return false;
        }
}

function getStorage(url){
	var storage= window.localStorage;
	var vkey=storage.getItem('accountKey');

	if((vkey===null)||(vkey.trim()=='')){
		storage.setItem('vlocation',url);
		window.location.href='frame/initLoginPage.action';
		return;
	}
}
function GetQueryString(name) { 
	  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	  var r = window.location.search.substr(1).match(reg); //获取url中"?"符后的字符串并正则匹配
	  var context = ""; 
	  if (r != null) 
	     context = r[2]; 
	  reg = null; 
	  r = null; 
	  return context == null || context == "" || context == "undefined" ? "" : context; 
	}
function loadscript(url) {
    var script = document.createElement("script");
    script.type = "text/javacript";
    script.src = url;
    document.body.appendChild(script);
}
function getPages(ourl,objdata,successBack,errorBack,completeBack,method){  //获取数据
    $.ajax({
        url : ourl,
        type : (method==null)?"POST":method,
        data :objdata,
        dataType : "json",
        success : function(res) {
            if(typeof(eval('successBack'))=="function"){
            	 console.log(objdata);
            	 successBack(res);
            }
           
        },
        error:function(msg){
        	if(typeof(eval('errorBack'))=="function"){
        		errorBack(msg)
            }
        },
        complete:function(){
        	if(typeof(eval('completeBack'))=="function"){
        		completeBack();
        	}
        }
    })
}
var x_PI = 3.14159265358979324 * 3000.0 / 180.0;//经纬度转换
function gcj02tobd09(lng, lat) {
    var z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * x_PI);
    var theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * x_PI);
    var bd_lng = z * Math.cos(theta) + 0.0065;
    var bd_lat = z * Math.sin(theta) + 0.006;
    return [bd_lng, bd_lat]
}

function setTime(time,max,failBack,successBack){//定时
    if (time == 0) {    
        if(typeof(eval('failBack'))=="function"){
        	time=max;
       	    failBack(time);
        }
        return;
    } else {
        if(typeof(eval('successBack'))=="function"){
       	    successBack(time);     
        }
          
    }
}
var storage= window.localStorage;

function getQueryIsStartCharging(successBack,failBack,failMsg){//queryIsStartCharging
	getPages(//用于查询用户是否有正在充电的订单
	            'charging/charging_queryIsStartCharging.action',
	            {
	            	accountKey:storage.getItem('accountKey'),
	            	pileCode:storage.getItem('pileCode'),//桩编号
	            	tradeNo:storage.getItem('tradeNo')
	            },function (res) {
					 console.log(res);
	 				 if(res.ret===0){//  
	 					 successBack(res)  
	 				 }else{
	 					 failBack(res)  
	 				 }
	   
	            },function (msg){
	            	failMsg(msg);
	            },function(){
	            });
}
function contIsStart(flag){//继续判断是否启动成功
	var timer;
    var timer1;
    var perMi=1000;
    
	 var time=startTimeOut;
	   timer=setInterval(function(){
		  setTime(time,startTimeOut,function(i){
			    clearInterval(timer);
			    clearInterval(timer1);
				$.hideLoading();
				if(flag){//汽车
					$.toast("启动失败","cancel",function(toast) {});
				}else{
					$.toast("启动失败,金额将于10分钟内返还","cancel",function(toast) {});
				}
			    
			},function(i){//开启定时器
				time--;
			    console.log(i);
			})
	   },perMi);
	   
	   timer1=setInterval(function(){
		   getQueryIsStartCharging(function(res){
					if(parseInt(res.data.isStartChargingStatus)===1){//有正在充电订单
					    $.hideLoading();
						clearInterval(timer);
						clearInterval(timer1);
						window.location.href='charging/chargingProcessData.action';	
				    }  
			   
		   },function(res){
					$.hideLoading();
					$.toast(res.msg,"cancel",function(toast) {});
					clearInterval(timer);
					clearInterval(timer1);
		   },function(msg){
					$.hideLoading();
			    	$.toast(msg,"cancel",function(toast) {});
			    	clearInterval(timer);
			    	clearInterval(timer1);
		   });
	   },getDataInterval);

}


function getWXPay(ohref){
	 getPages(
             'wechat/wechat_wxConfig.action',
             {
             	url:ohref
             },function (res) {
             	 console.log(res)
  				 if(res.ret===0){
  					if(res.data.failReason===0){
  						wx.config({
  						    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
  						    appId: res.data.appId, // 必填，公众号的唯一标识
  						    timestamp:res.data.timestamp, // 必填，生成签名的时间戳
  						    nonceStr: res.data.nonceStr, // 必填，生成签名的随机串
  						    signature: res.data.signature,// 必填，签名
  						    jsApiList: ['chooseWXPay'] // 必填，需要使用的JS接口列表
  						});
  					}else{
  					  $.toast(res.data.failReasonDesc,"cancel",function() {});
  					}
  				 }else{
  					 $.hideLoading();
  					 $.toast(res.msg,"cancel",function(toast) {});
  				 }
    
       });
}