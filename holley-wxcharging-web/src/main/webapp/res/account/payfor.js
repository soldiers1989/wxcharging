 $(function() {

     FastClick.attach(document.body);
     var storage= window.localStorage;
     storage = storage ? storage : [];
     var pindex=1;//页码

     var isOver=false;//是否没有数据
     var listDiv='.more-list-content';//对象
     var infobj='.pullContent';
     var loading=false;//加载更多
     var pullrefresh=false;//下拉刷新
     var vKey=storage.getItem('accountKey');//密钥
     var flag='- -';
     var isFinish=false;
     var isCharge;
     var timer1,timer2;

     var testTime1;
     const interMi=5000;
   
     
     if(GetQueryString('isCharging')==='true'){//充电支付
    	 isCharge=parseInt(GetQueryString('tradeType'));
     }else{
    	 $('.payfor-finish').removeClass('hidden')
    	 isCharge=2;
     }

     // 判断当前环境是否为微信内置浏览器
     var ua = navigator.userAgent.toLowerCase();
     var isWeixin = ua.indexOf('micromessenger') != -1;
     
     

     
     if(GetQueryString('isCharging')==='true'){//充电支付
		 var time=maxTimeOut;
	     timer1=setInterval(function(){
			  setTime(time,maxTimeOut,function(i){
					clearInterval(timer1);
					clearInterval(timer2);
					$.hideLoading();
				    $.toast("付款超时","cancel",function(toast) {
				    	window.location.href='charging/chargingProcess.action';
				    });
			   },function(i){//开启定时器
				    time--;
				    console.log(i);
			   })
	     },perMi);
	     timer2=setInterval(function(){
	   	    getPages(//用于查询
			            'pay/pay_queryPayResult.action',
			            {
			            	accountKey:GetQueryString('accountKey'),
			  	           	tradeType:isCharge,//自行车充电或充值
			  	           	tradeNo:GetQueryString('tradeNo')
			            },function (res) {

			 				 if(res.ret===0){
			 					  if(parseInt(res.data.status)===(3||4)){//交易成功
			 						    $.hideLoading();
		 						        if(parseInt(res.data.status)===3){
		 						        	 $.toast("支付成功",800,function(toast) {
		 						        		
		 						        		if(GetQueryString('tradeType')==='1'){//自行车充电
		 						        			$.showLoading('正在启动充电中');
		 						        			contIsStart();
		 						        		}else{
		 						        			window.location.href='record/chargingRecord.action';//订单管理
		 						        		}
		 						        		
		 								    });
		 						        }else{
		 						        	 $.toast("交易失败","cancel",function(toast) {
		 						        		 window.location.href='charging/chargingProcess.action';
			 								});
		 						        }
		 						        clearInterval(timer1);
		 						        clearInterval(timer2);
//		 						        window.location.href='account/accountRemain.action';

			 					  }
			 				 }else{
			 					 $.hideLoading();
			 					 $.toast(res.msg,"cancel",function(toast) {
			 						 clearInterval(timer1);
		 						     clearInterval(timer2);
			 					 });
			 				
			 				 }
			   
			            },function (msg){
			            	$.hideLoading();
					    	$.toast(msg,"cancel",function(toast) {
					    		 clearInterval(timer1);
							     clearInterval(timer2);
					    	});
			   
			            },function(){
			            });
		  },getDataInterval);
     }
     if(isWeixin) {
     	// 微信浏览器，显示‘在浏览器中打开’
//     	if(ua.indexOf('iphone') != -1 || ua.indexOf('ipad') != -1 || ua.indexOf('ipod') != -1) {
//     		// iOS
//     		document.getElementById("ios").className = '';
//     	} else {
//     		// Android
//     		document.getElementById("android").className = '';
//     	}
    	 document.getElementById("payContent").className ='pullContent';
    	 
     } else {
     	// 非微信内置浏览器，说明已经在浏览器中打开了，执行这段逻辑
    	 
    	if(GetQueryString('isCharging')==='true'){//充电支付
    		chargePay();
    	}else{
    		pay();//充值
    	}
     	
     }
     function chargePay(){
    	
    	   if(GetQueryString('tradeType')==='1'){//自行车充电支付
    		   getPages(//发送请求获取支付宝链接
        	           'pay/pay_queryBikePayment.action',
        	           {
        	           	accountKey:GetQueryString('accountKey'),
        	           	openId:GetQueryString('openId'),
        	           	tradeNo:GetQueryString('tradeNo')
        	           },function (res) {
        				console.log(res)
        					 if(res.ret===0){
        						if(res.data.failReason===0){//成功
        								document.body.innerHTML =res.data.form;
        					    		// 通过innerHTML添加的script代码不会自动执行，需要我们手动来执行
        					    		var scripts = document.body.getElementsByTagName("script");
        					    		for(var i = 0; i < scripts.length; i++) { // 一段一段执行script
        					    			eval(scripts[i].innerHTML);
        					    		}
        					    		$.hideLoading();
        					
        						}else{//失败	
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
    		   
    	   }else{//汽车充电支付
    		
    		   getPages(//发送请求获取支付宝链接
        	           'pay/pay_queryPayment.action',
        	           {
        	           	accountKey:GetQueryString('accountKey'),
        	           	openId:GetQueryString('openId'),
        	           	tradeNo:GetQueryString('tradeNo'),
        	           	payWay:2//支付宝
        	           },function (res) {
        	        	   	console.log(res)
        					 if(res.ret===0){
        						if(res.data.failReason===0){//成功
        								document.body.innerHTML =res.data.form;
        					    		// 通过innerHTML添加的script代码不会自动执行，需要我们手动来执行
        					    		var scripts = document.body.getElementsByTagName("script");
        					    		for(var i = 0; i < scripts.length; i++) { // 一段一段执行script
        					    			eval(scripts[i].innerHTML);
        					    		}
        					    		$.hideLoading();
        					
        						}else{//失败	
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
    	   }
    	 
     }
     
     function pay(){//充值

          getPages(//发送请求获取支付宝链接
           'pay/pay_queryAccountRecharge.action',
           {
           	accountKey:GetQueryString('accountKey'),
           	openId:GetQueryString('openId'),
           	tradeNo:GetQueryString('tradeNo')
           },function (res) {
        	   console.log(res)
				 if(res.ret===0){
					if(res.data.failReason===0){//成功
							document.body.innerHTML =res.data.form;
				    		// 通过innerHTML添加的script代码不会自动执行，需要我们手动来执行
				    		var scripts = document.body.getElementsByTagName("script");
				    		for(var i = 0; i < scripts.length; i++) { // 一段一段执行script
				    			eval(scripts[i].innerHTML);
				    		}
				
					}else{//失败	
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
//         }
//    	 

    	  
     }
     
     function getResult(success){
    	 getPages(//发送请求获取支付宝链接
  	           'pay/pay_queryPayResult.action',
  	           {
  	           	accountKey:GetQueryString('accountKey'),
  	           	tradeType:isCharge,//自行车充电或充值
  	           	tradeNo:GetQueryString('tradeNo')
  	           },function (res) {
  	        	   console.log(res)
  					 if(res.ret===0){	
  						success(res)
  					 }else{
  						 $.hideLoading();
  						 $.toast(res.msg,"cancel",function(toast) {});
  					 }
  	  
  	           },function (msg){
  	        	   $.hideLoading();
  			    	$.toast(msg,"cancel",function(toast) {});
  	  
  	           },function(){
  	        	   isFinish=false;
  	     	    
  	           });
     }
     
     $('.isPay').on('click',function(){//充值
    	 if(isFinish) return;
		   isFinish=true;
		   $.showLoading('Loading');
  
		   getResult(function(res){
						if(res.data.status===2){//支付成功
  							 $.hideLoading();
  							 $.toast('支付成功',800,function() {
  								 window.location.href='account/accountRemain.action';
  							 });
  						}else{//失败	
  							if(res.data.status===1){
  								 $.hideLoading();
  	    						 $.toast('未支付',"cancel",function() {});
  							}else{
  								 $.hideLoading();
  	    						 $.toast('交易关闭/已退款',"cancel",function() {});
  							}
  						  
  						}

		   });
    	 
     })
   




 })