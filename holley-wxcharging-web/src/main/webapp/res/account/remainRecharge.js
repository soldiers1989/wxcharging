 $(function() {

    /* FastClick.attach(document.body);*/
     var storage= window.localStorage;
     storage = storage ? storage : [];
     var pindex=1;//页码

     var isOver=false;//是否没有数据
     var listDiv='.more-list-content';//对象
     var infobj='.pullContent';
     var loading=false;//加载更多
     var pullrefresh=false;//下拉刷新
     var vKey=storage.getItem('accountKey');//密钥
     var isNext=false;
     console.log(storage);
     $.showLoading('Loading');
     $('.charge-box-account').text(storage.getItem('usableMoney'));
     $.hideLoading();
     
     $('.balance-sum-flex').on('click','.balance-sum-item',function(){
    	 $(this).closest('.balance-sum-flex').find('.balance-sum-item').removeClass('balance-sum-item-active');
    	 $(this).addClass('balance-sum-item-active');
    	 
    	 $('.input-val').closest('.balance-title').addClass('bg-gray');//add style
//    	 $('.input-val').attr({'disabled':'disabled'});
     })
      $('.input-val').on('focus',function(){
    	  $('.balance-sum-flex').find('.balance-sum-item').removeClass('balance-sum-item-active');
    	  $(this).closest('.balance-title').removeClass('bg-gray');
      })
      $('.input-val').on('blur',function(){
          var temp=$(this).val();
          if((temp.trim().length===0)||(parseFloat(temp).toFixed(2)==='0.00')){
        	  $(this).closest('.balance-title').addClass('bg-gray');
        	  $('.balance-sum-flex').find('.balance-sum-item').eq(0).addClass('balance-sum-item-active');
          }
      })
     

     
     getWXPay(window.location.href);

     $('.weui-btn_primary').on('click',function(e){
    	 
    	 if(isNext) return;
    	 isNext=true;
    	 var payWay=$('.weui-cells_radio .weui-check:checked').closest('.weui-check__label').find('.payfor-name').text();
    	 var sumVal;//充值的金额
    	 if(($('.input-val').val().trim().length===0)||(parseFloat($('.input-val').val()).toFixed(2)==='0.00')){
    		 sumVal=$('.balance-sum-item.balance-sum-item-active').data('role');
    	 }else{
    		 sumVal=$('.input-val').val().trim();
    	 }
    	 payWay=(payWay==='支付宝')?2:3;
    	 getPages(//获取订单编号
	 	            'pay/pay_queryTradeNo.action',
	 	            {
	 	            	accountKey:storage.getItem('accountKey'),
	 	            	payWay:payWay,
	 	            	tradeType:2,//充值
	 	            	money:parseFloat(sumVal).toFixed(2)
	 	            },function (res) {
						 console.log(res)
	 	 				 if(res.ret===0){
	 	 					if(res.data.failReason===0){//获取订单号成功
	 	 					   	if(payWay===2){//支付宝
	 	 					   	    isNext=false;
	 	 			        		window.location.href='account/payfor.action?accountKey='+storage.getItem('accountKey')
	 	 			    				+'&money='+parseFloat(sumVal).toFixed(2)+'&openId='+storage.getItem('openid')
	 	 			    				+'&tradeNo='+res.data.tradeNo+'&isCharging=false';
	 	 			        	}else{//微信
	 	 			        		getPages(//自行车充值
	 	 			    	 	            'pay/pay_queryAccountRecharge.action',
	 	 			    	 	            {
	 	 			    	 	            	accountKey:storage.getItem('accountKey'),
	 	 			    	 	            	tradeNo:res.data.tradeNo,
	 	 			    	 	            	openId:storage.getItem('openid')
	 	 			    	 	            },function (res) {
	 	 			    						 console.log(res)
	 	 			    	 	 				 if(res.ret===0){
	 	 			    	 	 					if(res.data.failReason===0){//成功
		 	 			    	 	 					//微信
		 	 			    	 	 					if (res.data.wxData.result_code == 'SUCCESS') {
		 	 			    	 							wx.chooseWXPay({
		 	 			    	 								appId : res.data.wxData.appid,
		 	 			    	 								timestamp : res.data.wxData.timeStamp,
		 	 			    	 								nonceStr : res.data.wxData.nonce_str,
		 	 			    	 								package : res.data.wxData.packageStr,
		 	 			    	 								signType : res.data.wxData.signType,
		 	 			    	 								paySign : res.data.wxData.sign,
		 	 			    	 								success : function(res) {//微信充值成功
		 	 			    	 									window.location.href='account/accountRemain.action';
		 	 			    	 								},
		 	 			    	 								fail:function(){
		 	 			    	 								   $.hideLoading();
				 	 			    	 	 					   $.toast('充值失败',"cancel",function() {});
		 	 			    	 								},
		 	 			    	 								complete : function() {
		 	 			    	 								}
		 	 			    	 							});
		 	 			    	 						} else {
		 	 			    	 						   $.hideLoading();
		 	 			    	 	 					   $.toast('失败',"cancel",function() {});	
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
	 	 			    	 	            	isNext=false;		    	 	      	    
	 	 			    	 	            });
	 	 			        	}
	 	 						
	 	 					}else{//获取订单号失败	
	 	 					  $.hideLoading();
	 	 					  $.toast(res.data.failReasonDesc,"cancel",function() {
	 	 						isNext=false;
	 	 					  });
	 	 					}
	 	 				 }else{
	 	 					 $.hideLoading();
	 	 					 $.toast(res.msg,"cancel",function(toast) {
	 	 						isNext=false;
	 	 					 });
	 	 				 }
	 	   
	 	            },function (msg){
	 	            	$.hideLoading();
	 			    	$.toast(msg,"cancel",function(toast) {
	 			    		isNext=false;
	 			    	});
	 	            },function(){
	 	            	
	 	            });
    	 
     })


 })