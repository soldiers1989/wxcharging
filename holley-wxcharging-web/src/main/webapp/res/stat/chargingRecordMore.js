 $(function() {

//	    FastClick.attach(document.body);
	    var storage= window.localStorage;
	    storage = storage ? storage : [];

	    var listDiv='.more-list-content';//对象
	    var isStart=false;
	    var flag='- -';
        var tradeNumber='';//订单编号
        var payMoney=0;
     // $.showLoading();
//    	storage.setItem('chargingRecordId',23);//缓存chargingRecordId
    	getWXPay(window.location.href);
        function getPayFor(payWay,oMoney){//获取支付方式

   	 	 					   	if(payWay===2){//支付宝
   	 	 					   	    isStart=false;
   	 	 			        		window.location.href='account/payfor.action?accountKey='+storage.getItem('accountKey')
   	 	 			    				+'&money='+parseFloat(oMoney).toFixed(2)+'&openId='+storage.getItem('openid')
   	 	 			    				+'&tradeNo='+tradeNumber+'&isCharging=true'+'&tradeType=3';//汽车充电支付
   	 	 			        	}else{//微信或账户余额
   	 	 			        		getPages(//汽车充电支付
   	 	 			    	 	            'pay/pay_queryPayment.action',
   	 	 			    	 	            {
   	 	 			    	 	            	accountKey:storage.getItem('accountKey'),
   		 	 			    	 	         	tradeNo:tradeNumber,
   		 	 			    	 	         	payWay:payWay,
   	 	 			    	 	            	openId:storage.getItem('openid')
   	 	 			    	 	            },function (res) {
   	 	 			    						 console.log(res)
   	 	 			    	 	 				 if(res.ret===0){
   		 	 			    	 	 					if(res.data.failReason===0){//成功
   			 	 			    	 	 				    if(payWay===3){//微信
   	   			 	 			    	 	 					if (res.data.wxData.result_code == 'SUCCESS') {
	   	   			 	 			    	 							wx.chooseWXPay({
	   	   			 	 			    	 								appId : res.data.wxData.appid,
	   	   			 	 			    	 								timestamp : res.data.wxData.timeStamp,
	   	   			 	 			    	 								nonceStr : res.data.wxData.nonce_str,
	   	   			 	 			    	 								package : res.data.wxData.packageStr,
	   	   			 	 			    	 								signType : res.data.wxData.signType,
	   	   			 	 			    	 								paySign : res.data.wxData.sign,
	   	   			 	 			    	 								success : function(res) {//微信支付成功
//   	   			 	 			    	 									window.location.href='account/accountRemain.action';
//	   	   			 	 			    	 									$.showLoading();
//	   	   			 	 			    	 									contIsStart();
	   	   			 	 			    	 									$.toast('支付成功',800,function() {
	   	   			 	 			    	 										$('.tabbar_bar').removeClass('tabbar_bar_over').addClass('bg-green').text('缴费成功');
	   	   			 	 			    	 									});
	   	   			 	 			    	 								},
	   	   			 	 			    	 								fail:function(){
	   	   				 	 			    	 								   $.hideLoading();
	   	   						 	 			    	 	 					   $.toast('支付失败',"cancel",function() {});
	   	   				 	 			    	 							},
	   	   			 	 			    	 								complete : function() {
	   	   			 	 			    	 									isStart=false;
	   	   			 	 			    	 								}
	   	   			 	 			    	 								
	   	   			 	 			    	 							});
   	   			 	 			    	 						} else {
   	   			 	 			    	 						   $.hideLoading();
   	   			 	 			    	 	 					   $.toast('失败',"cancel",function() {
   	   			 	 			    	 	 						   isStart=false;
   	   			 	 			    	 	 					   });	
   	   			 	 			    	 						}
   			 	 			    	 	 				    }else{//账户余额
   			 	 			    	 	 				       $.hideLoading();
			 	 			    	 	 					   $.toast('支付成功',800,function() {
			 	 			    	 	 						   $('.tabbar_bar').removeClass('tabbar_bar_over').addClass('bg-green').text('缴费成功');
			 	 			    	 	 						   isStart=false;
			 	 			    	 	 					   });
   			 	 			    	 	 				    }
   			 	 			    	 	 					
   		 	 			    	 	 					}else{//失败	
   		 	 			    	 	 					  $.hideLoading();
   		 	 			    	 	 					  $.toast(res.data.failReasonDesc,"cancel",function() {
   		 	 			    	 	 						  isStart=false;
   		 	 			    	 	 					  });
   		 	 			    	 	 					}
   	 	 			    	 	 					}else{//失败	
   	 	 			    	 	 					  $.hideLoading();
   	 	 			    	 	 					  $.toast(res.data.failReasonDesc,"cancel",function() {
   	 	 			    	 	 						  isStart=false;
   	 	 			    	 	 					  });
   	 	 			    	 	 					}
   	 	 			    	 	 				
   	 	 			    	 	            },function (msg){
   	 	 			    	 	            	$.hideLoading();
   	 	 			    	 			    	$.toast(msg,"cancel",function(toast) {
   	 	 			    	 			    		isStart=false;
   	 	 			    	 			    	});
   	 	 			    	 	   
   	 	 			    	 	            },function(){
   	 	 			    	 	               
   	 	 			    	 	            });
   	 	 			        	}

        }

    function getListItem(vlist,vTart,callback) {
        var listHtml='';
        var oChaMoney=((vlist.chaMoney===null)?flag:vlist.chaMoney);
        oChaMoney=parseFloat(oChaMoney).toFixed(2);
		listHtml+='<div class="weui-media-box weui-media-box_appmsg wui-cell-nopadding-top padding-10">'+''
		  +'<div class="weui-media-box__bd"><div class="nearby-list-item flex-just-between">'+
				''+'<div class=""><p class="weui-media-box__title text-primary" style="font-size:18px;">'+((vlist.stationName===null)?flag:vlist.stationName)
				+'</p><span class="deep-light-gray">'+((vlist.tradeNo===null)?flag:vlist.tradeNo)+'</span><p class="deep-light-gray">桩名称:'+((vlist.pileName===null)?flag:vlist.pileName)
				+'</p></div><div class="padding-5"><h2 class="text-red font-wt-500">￥'+oChaMoney+'</h2></div></div></div></div>';
    
        $(vTart).append(listHtml);
      
        if(typeof(eval('callback'))=="function"){
        	callback();
        }
    }
        $.showLoading('Loading');
        getPages(
             'record/record_queryChargingRecordDetail.action',
             {
                 accountKey:storage.getItem('accountKey'),
                 chargingRecordId:storage.getItem('chargingRecordId')
             },function (res) {
            	 //total-charge
                 console.log(res)
  				 if(res.ret===0){
				 		 tradeNumber=res.data.tradeNo;//保存订单编号
				 		 payMoney=res.data.chaMoney;//支付金额
				 		 if(res.data.pileToType===2){
				 			 $('.total-charge').removeClass('hidden');
				 		 }
				    	 if(!(parseInt(res.data.payStatus)===3)){
				    		
				    		$('.tabbar_bar').addClass('tabbar_bar_over');
				    		$('.tabbar_bar.tabbar_bar_over').on('click',function(e){
				    	    	 if(isStart)return;
				    	    	 isStart=true;
				    	    	 $.showLoading('支付中');
				    	    	 getPages(//用于查询是否充电支付成功
				    			            'pay/pay_queryPayResult.action',
				    			            {
				    			            	accountKey:storage.getItem('accountKey'),
				    			  	           	tradeType:3,//电动汽车充电支付
				    			  	           	tradeNo:res.data.tradeNo
				    			            },function (res) {
				    			 				 if(res.ret===0){
				    			 						    $.hideLoading();
				    		 						        if(parseInt(res.data.status)===3){
				    		 						        	 $.toast("支付成功",800,function(toast) {
				    		 						        		 $('.tabbar_bar').removeClass('tabbar_bar_over').addClass('bg-green').text('缴费成功');
				    		 								    });
				    		 						        }else{
				    		 						        	$.hideLoading();
	  					    		 						   	$.actions({
	  					    		 				                title: "",
	  					    		 				                onClose: function() {
	  					    		 				                	isStart=false;
	  					    		 				                },
	  					    		 				                actions: [
	  					    		 				                	 {
	  					    		 				                         text: "账户余额",
	  					    		 				                         className: "",
	  					    		 				                         onClick: function() {
	  					    		 				                        	$.modal({
	  					    		 				                             title: "您确定选择<span class='text-yellow'>账户余额</span>完成支付？",
	  					    		 				                             text: "",
	  					    		 				                             buttons: [
	  					    		 				                               { text: "取消", className:"font-16 default",onClick:function(){
	  					    		 				                            	   isStart=false;
	  					    		 				                               }},
	  					    		 				                               { text: "确定", className:"font-16 text-primary",onClick: function(){ 
	  					    		 				                            	    getPayFor(1,payMoney);
	  					    		 		
	  					    		 				                               } }
	  					    		 				                             ]
	  					    		 				                           });
	  					    		 				                  
	  					    		 				                         }
	  					    		 				                     },
	  					    		 				                    {
	  					    		 				                        text: "支付宝",
	  					    		 				                        className: "",
	  					    		 				                        onClick: function() {
	  					    		 				                        	$.modal({
		  					    		 				                             title: "您确定选择<span class='text-yellow'>支付宝</span>完成支付？",
		  					    		 				                             text: "",
		  					    		 				                             buttons: [
		  					    		 				                               { text: "取消", className:"font-16 default",onClick:function(){
		  					    		 				                            	   isStart=false;
		  					    		 				                               }},
		  					    		 				                               { text: "确定", className:"font-16 text-primary",onClick: function(){ 
		  					    		 				                            	    getPayFor(2,payMoney);
		  					    		 		
		  					    		 				                               } }
		  					    		 				                             ]
		  					    		 				                           });
	  					    		 				                        	
	  					    		 				                   
	  					    		 				                        }
	  					    		 				                    },
	  					    		 				                    {
	  					    		 				                        text: "微信",
	  					    		 				                        className: "",
	  					    		 				                        onClick: function() {
	  					    		 				                        	$.modal({
		  					    		 				                             title: "您确定选择<span class='text-yellow'>微信</span>完成支付？",
		  					    		 				                             text: "",
		  					    		 				                             buttons: [
		  					    		 				                               { text: "取消", className:"font-16 default",onClick:function(){
		  					    		 				                            	   isStart=false;
		  					    		 				                               }},
		  					    		 				                               { text: "确定", className:"font-16 text-primary",onClick: function(){ 
		  					    		 				                            	   getPayFor(3,payMoney);
		  					    		 		
		  					    		 				                               } }
		  					    		 				                             ]
		  					    		 				                           });
	  					    		 				                     
	  					    		 				                        }
	  					    		 				                    }
	  					    		 				                ]
	  					    		 				            });
	  					    		 				    		  $('.weui-actions_mask').on('click',function(){
	  					    		 				    			  isStart=false;
	  					    		 				    		  })

				    		 						        }

				    			 				 }else{
				    			 					 $.hideLoading();
				    			 					 $.toast(res.msg,"cancel",function(toast) {
				    			 					 });
				    			 				
				    			 				 }
				    			   
				    			            },function (msg){
				    			            	$.hideLoading();
				    					    	$.toast(msg,"cancel",function(toast) {
				    					    	});
				    			   
				    			            },function(){
				    			            });
				    	 })	 
				     }else{
				    	$('.tabbar_bar').addClass('bg-green');
				     }
  				
				    $('.tabbar_bar').text(res.data.payStatusDesc);
                     getListItem(res.data,listDiv,function(){
                    	 if(res.data.pileToType===2){
                    		 $('.total-time').text((res.data.bikeChaLen===null)?flag:res.data.bikeChaLen)//test 需要修改
				 		 }
                    	
                    	 $('.charge-time').text((res.data.chaLen===null)?flag:res.data.chaLen);
                    	 $('.charge-payWay').text((res.data.payWayDesc===null)?flag:res.data.payWayDesc);//支付方式描述
                    	 
                    	 
                    	 $('.charge-starttime').text((res.data.chaStartTime===null)?flag:res.data.chaStartTime)
                    	 $('.charge-endtime').text((res.data.chaEndTime===null)?flag:res.data.chaEndTime)
                    	 $.hideLoading();
                     });      	                    
  				 }else{
  					 $.hideLoading();
  					 $.toast(res.msg,"cancel",function(toast) {});
  				 }
    
             },function (msg){
            	 	$.hideLoading();
            	 	$.toast(msg,"cancel",function(toast) {});
    
             },function(){
             
             });


 })