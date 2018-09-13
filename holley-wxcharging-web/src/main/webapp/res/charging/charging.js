 $(function() {

//     FastClick.attach(document.body);
     var storage= window.localStorage;
     storage = storage ? storage : [];
     var flag='- -';
     var maxTime=6;
     var timer;
     var timer1;
     var isClick=false;
     var isInput=false;
     
     var vHeight=$('body').innerHeight()-$('.calc-height').innerHeight()-55;
     $('.charge-box-content').css({"height":vHeight+"px"});
     function getInit(){
    	  getPages(
		             'stat/stat_queryStatChargingInfo.action',
		             {
		            	 accountKey:storage.getItem('accountKey')
		             },function (res) {
		 				 if(res.ret===0){ 
		 					 $('.charge-box-account').text(parseFloat(res.data.totalChaMoney).toFixed(2))//总充电金额
		 					 $('.total-time').text((res.data.totalChaLen===null)?flag:res.data.totalChaLen)//总时长(分)
		 					 $('.total-degree').text((res.data.totalChaPower===null)?flag:res.data.totalChaPower)//总充电量(度)
		 					 $('.total-index').text((res.data.totalChaCount===null)?flag:res.data.totalChaCount)//充电次数(次)
		 					$.hideLoading();
		 				 }else{ 
	 							 $.hideLoading();
			 					 $.toast(res.msg,"cancel",function(toast) {		 
			 					 });
	 					
		 				 }
		  
		           },function (msg){
		          	 	$.hideLoading();
		 				$.toast(msg,"cancel",function(toast) {});
		  
		           },function(){
		            
		           });
    	  
     }
     function isTradeNo(){
    	 if(!(storage.getItem('tradeNo')===null)){//有订单编号
	    	 $.showLoading('Loading');
	    	 getPages(
	    		       'charging/charging_queryIsStartCharging.action',
	    		       {
	    		      	 accountKey:storage.getItem('accountKey'),
	    		      	 pileCode:storage.getItem('pileCode'),//桩编号
	    		   		 tradeNo:storage.getItem('tradeNo')
	    		       },function (res) {
	    		       	  	 console.log(res)
	    					 if(res.ret===0){ 
	    						if(parseInt(res.data.isStartChargingStatus)===1){//有在充电订单
	    							window.location.href='charging/chargingProcessData.action';
	    							$.hideLoading();
	    							return;
	    						}else{
	    							
	    							if(parseInt(res.data.isStartChargingStatus)===2){//没有未充电订单
	    								getInit();
	    								return;
	    							}else{//正在充电中
	    								  var time=res.data.delayTime<=0?stopTimeOut:res.data.delayTime;
	    								  timer1=setInterval(function(){
	    									  setTime(time,stopTimeOut,function(i){
	    										    $.hideLoading()
	    			 								clearInterval(timer1);
	    			 								clearInterval(timer);
	    			 							    $.toast("连接超时","cancel",function(toast) {});
	    			 							},function(i){//开启定时器
	    			 							    time--;
	    			 							})
	    								  },1000);
	    								  timer=setInterval(function(){
	    					 		        	    getPages(//用于查询用户是否有正在充电的订单
	    					 				            'charging/charging_queryIsStartCharging.action',
	    					 				            {
	    					 				            	accountKey:storage.getItem('accountKey'),
	    					 				            	pileCode:storage.getItem('pileCode'),//桩编号
	    					 				            	tradeNo:storage.getItem('tradeNo')
	    					 				            },function (res) {
	    					 								
	    					 				 				 if(res.ret===0){//
	    					 				 					  if(parseInt(res.data.isStartChargingStatus)===1){
	    					 				 						window.location.href='charging/chargingProcessData.action';
	    					 				 						$.hideLoading();
	    					 				 						clearInterval(timer);
	    					 				 					  }else{
	    					 				 						  if(parseInt(res.data.isStartChargingStatus)===2){
	    					 				 							getInit();
	    					 				 							clearInterval(timer);
	    					 				 						  }
	    					 				 					  }

	    					 				 				 }else{

	    					 				 				 }
	    					 				   
	    					 				            },function (msg){
	    					 				   
	    					 				            },function(){
	    					 				            });
	    				 							
	    			 						},getDataInterval);
	    								
	    							}
	    							
	    						}
	    						
	    					 }else{
	    						 $.hideLoading();
	    						 $.toast(res.msg,"cancel",function(toast) {});
	    					 }

	    		     },function (msg){
	    		    	 	$.hideLoading();
	    					$.toast(msg,"cancel",function(toast) {});

	    		     },function(){
	    		      
	    		     });
	     }
     }
     
     $.showLoading('Loading');
     if(storage.getItem('accountKey')===null){
	    	getPages(
	    				'frame/frame_queryIsLogin.action',
	    		       {
	    					openId:storage.getItem('openid')
	    		       },function (res) {
	    		       	  	 console.log(res)
	    					 if(res.ret===0){ 
	    						 if(res.data.failReason===0){
	          						 $.hideLoading();
	          						 if(res.data.loginStatus===1){
	          							storage.setItem('accountKey',res.data.accountKey);
	          							getInit();
	          							isTradeNo();
	          						 }else{
	          							window.location.href="frame/login.action?tartHref=charging/charging.action";
	          						 }
	          						
	          					 }else{
	          						 $.hideLoading();
	          						 $.toast(res.data.failReasonDesc,"cancel",function() {
	          			
	          						 });
	          					 }
	    					 }else{
	    						 $.hideLoading();
	    						 $.toast(res.msg,"cancel",function(toast) {});
	    					 }

	    		     },function (msg){
	    		    	 	$.hideLoading();
	    					$.toast(msg,"cancel",function(toast) {});

	    		     },function(){
	    		      
	    		     });
  }else{
	  getInit();
	  isTradeNo();
  }
	 

     
     $('.charging-btn').on('click',function(e){//扫码充电
    	 if(isClick)return;
    	 isClick=true;
    	 getPages(//用于查询当前用户是否存在未支付订单
		            'charging/charging_queryUnpaidFee.action',
		            {
		            	accountKey:storage.getItem('accountKey')
		            },function (res) {
						 console.log(res)
		 				 if(res.ret===0){
		 					if(res.data.hasUnpaidFee===0){//
		 						
		 						 getPages(//调用微信扫码接口
		 			    	            'wechat/wechat_wxConfig.action',
		 			    	            {
		 			    	            	url:window.location.href
		 			    	            },function (res) {
		 			    	            	
		 			    	 				 if(res.ret===0){
		 			    	 					if(res.data.failReason===0){
		 			    	 						 storage.setItem('appId',res.data.appId);
		 			    	 						 storage.setItem('timestamp',res.data.timestamp);
		 			    	 						 storage.setItem('nonceStr',res.data.nonceStr);
		 			    	 						 storage.setItem('signature',res.data.signature);
		 			    	 						wx.config({
		 			    	 						    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
		 			    	 						    appId: storage.getItem('appId'), // 必填，公众号的唯一标识
		 			    	 						    timestamp:storage.getItem('timestamp'), // 必填，生成签名的时间戳
		 			    	 						    nonceStr: storage.getItem('nonceStr'), // 必填，生成签名的随机串
		 			    	 						    signature: storage.getItem('signature'),// 必填，签名
		 			    	 						    jsApiList: ['getLocation','scanQRCode'] // 必填，需要使用的JS接口列表
		 			    	 						});
		 			    	 						wx.ready(function(){
		 			    	 						
		 			    	 						    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
		 			    	 				    		 wx.scanQRCode({
		 			    	 				    			 needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
		 			    	 				    			 scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
		 			    	 				    			 success: function (res) {
		 			    	 				    				 var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果	
		 			    	 				    			
		 			    	 				    				getPages(
		 							    		    	            'charging/charging_queryPileAuth.action',
		 							    		    	            {
		 							    		    	            	accountKey:storage.getItem('accountKey'),
		 							    		    	            	pileCode:result,//桩编号
		 							    		    	            },function (res) { 
		 							    		    	 				 if(res.ret===0){
		 							    		    	 					if(res.data.failReason===0){//设备验证成功
		 							    		    	 						storage.setItem('pileCode',res.data.pileCode);	//缓存桩编号
		 							    		    	 						storage.setItem('pileToType',res.data.pileToType);	//缓存数据桩类型
		 							    		    	 						storage.setItem('pileType',res.data.pileType);	//缓存电流类型
		 							    		    	 						window.location.href="charging/chargingProcess.action";
		 							    		    	 					}else{//设备验证失败	
		 							    		    	 						$.hideLoading();
		 							    		    	 					    $.toast(res.data.failReasonDesc,"cancel",function() {});
		 							    		    	 					}
		 							    		    	 				 }else{
		 							    		    	 					 $.hideLoading();
		 			 				    		    	 					 $.toast(res.msg,"cancel",function(toast) {});
		 							    		    	 				 }
		 							    		    	   
		 							    		    	            },function (msg){
		 							    		    	            	$.hideLoading();
		 							    						    	$.toast(msg,"cancel",function(toast) {});
		 							    		    	   
		 							    		    	            },function(){
		 							    		    	           
		 							    		    	      	    
		 							    		    	            });

		 			    	 				    		    	 
		 			    	 				    			 },
		 			    	 				    			 fail:function(msg){
		 			    	 				    				$.hideLoading();
		 			    	 				    				$.toast(msg,"cancel",function(toast) {});
		 			    	 				    			 }
		 			    	 				    		});
		 			    		 						   
		 			    	    
		 			    	 						});
		 			    	 					}else{
		 			    	 					   $.hideLoading();
		 			    	 					   $.toast(res.data.failReasonDesc,"cancel",function() {});
		 			    	 					}
		 			    	 				 }else{
		 			    	 					 $.hideLoading();
		 			    	 					 $.toast(res.msg,"cancel",function(toast) {});
		 			    	 				 }
		 			    	   
		 			    	            },function (msg){
		 			    	           	 	$.hideLoading();
		 			    	 				$.toast(msg,"cancel",function(toast) {});
		 			    	   
		 			    	            },function(){
		 			    	            	isClick=false;
		 			    	            });
		 						
		 					}else{//有未支付订单
		 					  $.hideLoading();
		 					  $.toast("存在未支付订单请先去支付","cancel",function() {
		 						  isClick=false;
		 						  window.location.href='record/chargingRecord.action';
		 					  });
		 					}
		 				 }else{
		 					 $.hideLoading();
		 					 $.toast(res.msg,"cancel",function(toast) {
		 						isClick=false;
		 					 });
		 				 }
		   
		            },function (msg){
		            	$.hideLoading();
				    	$.toast(msg,"cancel",function(toast) {
				    		isClick=false;
				    	});
		   
		            },function(){
		           
		      	    
		            });
    	 
    
    	

     })
     
     
     $('.input-code').on('click',function(){
    	 $.prompt({
             text: "",
             title: "输入设备编号",
             onOK: function(text) {
            	 var oVal=text;
           
            	 getPages(//用于查询当前用户是否存在未支付订单
     		            'charging/charging_queryUnpaidFee.action',
     		            {
     		            	accountKey:storage.getItem('accountKey')
     		            },function (res) {
     						 console.log(res)
     		 				 if(res.ret===0){
     		 					if(res.data.hasUnpaidFee===0){//
     				    				getPages(
     		    		    	            'charging/charging_queryPileAuth.action',
     		    		    	            {
     		    		    	            	accountKey:storage.getItem('accountKey'),
     		    		    	            	pileCode:text,//桩编号
     		    		    	            },function (res) { 
     		    		    	 				 if(res.ret===0){
     		    		    	 					if(res.data.failReason===0){//设备验证成功
     		    		    	 						storage.setItem('pileCode',res.data.pileCode);	//缓存桩编号
     		    		    	 						storage.setItem('pileToType',res.data.pileToType);	//缓存数据桩类型
     		    		    	 						storage.setItem('pileType',res.data.pileType);	//缓存电流类型
     		    		    	 						window.location.href="charging/chargingProcess.action";
     		    		    	 					}else{//设备验证失败	
     		    		    	 						$.hideLoading();
     		    		    	 					    $.toast(res.data.failReasonDesc,"cancel",function() {});
     		    		    	 					}
     		    		    	 				 }else{
     		    		    	 					 $.hideLoading();
     		    		    	 					 $.toast(res.msg,"cancel",function(toast) {});
     		    		    	 				 }
     		    		    	   
     		    		    	            },function (msg){
     		    		    	            	$.hideLoading();
     		    						    	$.toast(msg,"cancel",function(toast) {});
     		    		    	   
     		    		    	            },function(){
     		    		    	            	isInput=false;
     		    		    	            });

     		 
     		 					}else{//有未支付订单
     		 					  $.hideLoading();
     		 					  $.toast("存在未支付订单请先去支付","cancel",function() {
     		 						  isInput=false;
     		 						  window.location.href='record/chargingRecord.action';
     		 					  });
     		 					}
     		 				 }else{
     		 					 $.hideLoading();
     		 					 $.toast(res.msg,"cancel",function(toast) {
     		 						isInput=false;
     		 					 });
     		 				 }
     		   
     		            },function (msg){
     		            	$.hideLoading();
     				    	$.toast(msg,"cancel",function(toast) {
     				    		isInput=false;
     				    	});
     		   
     		            },function(){
     		           
     		      	    
     		            });
             },
             onCancel: function() {
               isInput=false;
             }
           });
     })
      
     
     $('.btn-int').on('click',function(e){//输入设备编号
    	 if(isInput)return;
    	 isInput=true;
    	 var oVal=$('.input-code input').val().trim();
    	 if(oVal.length===0){
    		 $.toast("设备编号不能为空", "text",function(){	 
    		 });
    		 isInput=false;
    		 return;
    	 }
    	 /*else{
    		 if(oVal.length<10){
    			 $.toast("请输入正确位数的设备编号", "text",function(){	 
        		 });
        		 isInput=false;
        		 return;
    		 }
    	 }*/
     })

 })