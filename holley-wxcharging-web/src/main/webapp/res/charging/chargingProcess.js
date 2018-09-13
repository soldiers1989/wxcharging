 $(function() {

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
     var modeType,remainSum;
   
     var isStart=false;

//   
//     storage.setItem('pileCode','010203040506070802');//电动汽车
//     storage.setItem('pileCode','1111212101');//电自行车
     getWXPay(window.location.href);
     function getInit(){
    	  getPages(
		             'stat/stat_queryStatChargingInfo.action',
		             {
		            	 accountKey:storage.getItem('accountKey')
		             },function (res) {
		             	
		 				 if(res.ret===0){ 
		 					 $('.charge-box-account').text(res.data.totalChaMoney)//总充电金额
		 					 $('.total-time').text(res.data.totalChaLen)//总时长(分)
		 					 $('.total-degree').text(res.data.totalChaPower)//总充电量(度)
		 					 $('.total-index').text(res.data.totalChaCount)//充电次数(次)
		 					$.hideLoading();
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
     
     function getTradeNo(payWay,oMoney){//获取订单编号
    	 getPages(//获取订单编号
	 	            'pay/pay_queryTradeNo.action',
	 	            {
	 	            	accountKey:storage.getItem('accountKey'),
	 	            	payWay:payWay,
	 	            	tradeType:1,//充电支付
	 	            	money:oMoney,
	 	            	pileCode:storage.getItem('pileCode')
	 	            },function (res) {
						 console.log(res)
	 	 				 if(res.ret===0){
	 	 					if(res.data.failReason===0){//获取订单号成功
	 	 						 storage.setItem('tradeNo',res.data.tradeNo);//缓存订单编号
	 	 						alert(storage.getItem('tradeNo'))
	 	 					   	if(payWay===2){//支付宝
	 	 					   	    isStart=false;
	 	 			        		window.location.href='account/payfor.action?accountKey='+storage.getItem('accountKey')
	 	 			    				+'&money='+parseFloat(oMoney).toFixed(2)+'&openId='+storage.getItem('openid')
	 	 			    				+'&tradeNo='+res.data.tradeNo+'&isCharging=true'+'&tradeType=1';
	 	 			        	}else{//微信
	 	 			        		getPages(//自行车充电支付
	 	 			    	 	            'pay/pay_queryBikePayment.action',
	 	 			    	 	            {
	 	 			    	 	            	accountKey:storage.getItem('accountKey'),
		 	 			    	 	         	tradeNo:res.data.tradeNo,
	 	 			    	 	            	openId:storage.getItem('openid')
	 	 			    	 	            },function (res) {
	 	 			    						 console.log(res)
	 	 			    	 	 				 if(res.ret===0){
		 	 			    	 	 					if(res.data.failReason===0){//成功
			 	 			    	 	 					if(payWay===3){//微信
				 	 			    	 	 					//微信
				 	 			    	 	 					if (res.data.wxData.result_code == 'SUCCESS') {
				 	 			    	 							wx.chooseWXPay({
				 	 			    	 								appId : res.data.wxData.appid,
				 	 			    	 								timestamp : res.data.wxData.timeStamp,
				 	 			    	 								nonceStr : res.data.wxData.nonce_str,
				 	 			    	 								package : res.data.wxData.packageStr,
				 	 			    	 								signType : res.data.wxData.signType,
				 	 			    	 								paySign : res.data.wxData.sign,
				 	 			    	 								success : function(res) {//微信支付成功
	//			 	 			    	 									window.location.href='account/accountRemain.action';
				 	 			    	 									$.showLoading('正在启动充电');
				 	 			    	 									contIsStart()
				 	 			    	 								},
				 	 			    	 								fail:function(){
					 	 			    	 								   $.hideLoading();
							 	 			    	 	 					   $.toast('微信支付失败',"cancel",function() {});
					 	 			    	 							},
				 	 			    	 								complete : function() {
				 	 			    	 									isStart=false;
				 	 			    	 								}
				 	 			    	 								
				 	 			    	 							});
				 	 			    	 						} else {
				 	 			    	 						   $.hideLoading();
				 	 			    	 	 					   $.toast('微信支付失败',"cancel",function() {
				 	 			    	 	 						  isStart=false;
				 	 			    	 	 					   });	
				 	 			    	 						}
			 	 			    	 	 					 }else{//账户余额
	   			 	 			    	 	 				       $.hideLoading();
				 	 			    	 	 					   $.toast('账户支付成功',800,function() {
				 	 			    	 	 						   $.showLoading('正在启动充电')
				 	 			    	 	 						   contIsStart()
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
	 	 						
	 	 					}else{//获取订单号失败	
	 	 					  $.hideLoading();
	 	 					  $.toast(res.data.failReasonDesc,"cancel",function() {
	 	 						isStart=false;
	 	 					  });
	 	 					}
	 	 				 }else{
	 	 					 $.hideLoading();
	 	 					 $.toast(res.msg,"cancel",function(toast) {
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

    
     getPages(//获取界面数据
	            'charging/charging_queryPileAuth.action',
	            {
	            	accountKey:storage.getItem('accountKey'),
	            	pileCode:storage.getItem('pileCode'),//桩编号
	            },function (res) {
						console.log(res)
	 				 if(res.ret===0){
	 					if(res.data.failReason===0){//设备验证成功
	 						$('.charge-stationName').text((res.data.stationName===null)?flag:res.data.stationName);
	 						$('.charge-pileName').text((res.data.pileName===null)?flag:res.data.pileName);
	 						
	 						$('.charge-pileCode').text((res.data.pileCode===null)?flag:res.data.pileCode);
	 						var type=(res.data.pileToType===null)?flag:res.data.pileToType;
	 						$('.charge-pileToType').text((parseInt(type)===1)?'电动汽车':'电自行车');
	 						modeType=parseInt(type);//parseInt(type)
	 						$('.remain-sum').text(parseFloat(res.data.usableMoney).toFixed(2));
	 						remainSum=parseFloat(res.data.usableMoney).toFixed(2);//res.data.可用余额
	 						if(modeType===1){//电动汽车
	 							$('.charge-mode-car').removeClass('hidden');
	 						}else{//电自行车
	 							$('.charge-mode-bic').removeClass('hidden');
	 							var valRange='';
	 							for(var i in res.data.chargeModes){
	 								valRange+="<div class='deep-light-gray charge-mode-range "+(i==0?"charge-mode-active":'')+"' data-role='"+res.data.chargeModes[i].value+"'>"+res.data.chargeModes[i].name+"</div>"
	 							}
	 							$(".charge-mode-bic .charge-mode-content").append(valRange);
	 							$('.charge-mode-bic').on('click','.charge-mode-range',function(){
	 								$(this).closest('.charge-mode-bic').find('.charge-mode-range').removeClass('charge-mode-active');
	 								$(this).addClass('charge-mode-active');
	 							})
	 							
	 						}

	 					}else{//设备验证失败	
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
     
     
     
     function getQueryStart(mode,vals){
    	 $.showLoading('启动充电模式')
    	 getPages(//点启动
		            'charging/charging_queryStartCharging.action',
		            {
		            	accountKey:storage.getItem('accountKey'),
		            	pileCode:storage.getItem('pileCode'),//桩编号
		            	chargeMode:mode,//充电模式
		            	value:vals
		            },function (res) {
						 console.log(res)
		 				 if(res.ret===0){
		 					if(res.data.failReason===0){//启动成功delayTime,平时为0
		 						   storage.setItem('tradeNo',res.data.tradeNo); 
		 						   contIsStart(true);
		 					}else{//启动失败
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
		            	isStart=false;
		            });
     }
     
    
     $('.charge-mode-item').on('click',function(e){//带金额的
    	 var $oPar=$(this).closest('.charge-mode');
     	if( $oPar.hasClass('charge-mode-car')){//当是电动汽车
     		$oPar.find('.userName').removeAttr('disabled');
     	}else{//电动自行车
     		$('.charge-mode-content>.charge-mode-range').eq(0).addClass('charge-mode-active');
			$('.charge-mode-content').on('click','.charge-mode-range',function(e){
				$(this).closest('.charge-mode-content').children('.charge-mode-range').removeClass('charge-mode-active');
				$(this).addClass('charge-mode-active');	
			})
     	}
    	$('.charge-mode-content').addClass('bg-gray');
    	$(this).next('.charge-mode-content').removeClass('bg-gray');
     })
     $('.charge-mode-auto').on('click',function(e){//自动
    	 var $oPar=$(this).closest('.charge-mode');
      	if( $oPar.hasClass('charge-mode-car')){//当是电动汽车
      		$oPar.find('.userName').attr('disabled','true');
    	}else{//电动自行车
    		$('.charge-mode-content>.charge-mode-range').removeClass('charge-mode-active');
    		$('.charge-mode-content').off('click','.charge-mode-range')
    	}
     	$('.charge-mode-content').addClass('bg-gray');
      })
  
      $('.tabbar_btn').on('click',function(e){//启动按钮
    	  if(isStart) return;
    	  isStart=true;
    	  
    	  if(modeType===1){//电动汽车
    		  var isAuto=$('.charge-mode-car .charge-mode-content').hasClass('bg-gray');
    		  if(!isAuto){//金额
    			  if($('.userName').val()>remainSum){
    				  $.toast("输入的金额不能大于可用余额", "text");
    				  isStart=false;
    				  return;
    			  }
    			  if(!($('.userName').val().length>0)){
    				  $.toast("输入的金额不能为空", "text");
    				  isStart=false;
    				  return;
    			  }
    		  }
    		  $.modal({
                    title: "您确定&nbsp;<span class='text-yellow'>启动充电</span>&nbsp;？",
                    text: "",
                    buttons: [
                      { text: "取消", className:"font-16 default",onClick:function(){
                    	    isStart=false;
                      }},
                      { text: "确定", className:"font-16 text-primary",onClick: function(){ 
                   	    	getQueryStart(isAuto?0:3,$('.userName').val())
                      } }
                    ]
                  });
    		  
 
    	  }else{//电自行车
    		  var oMode=$('.charge-mode-range.charge-mode-active').data('role');//充电模式
    		 //按时收费
    		  
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
		                            	   getTradeNo(1,oMode);

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
		                            	   getTradeNo(2,oMode);

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
		                            	   getTradeNo(3,oMode);

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
      })


 })