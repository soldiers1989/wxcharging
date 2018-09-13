 $(function() {

//        FastClick.attach(document.body);
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
     var timer1,timer2,timer3;
     var isClick=false;
     var isStop=false;
     var oPileStatus;
//     console.log(storage)
//  
   /*  storage.setItem('tradeNo','05240000000120180503151649000001');*/ 
//     storage.setItem('pileToType',2)//1.汽车 2.自行车
  
    /* if(!isStop){*/
		 
	/* }*/
     function initData(){
     
    	 getPages(
                 'charging/charging_queryChargingInfo.action',
                 {
                	 accountKey:storage.getItem('accountKey'),
                	 pileCode:storage.getItem('pileCode'),//桩编号
    		         tradeNo:storage.getItem('tradeNo')
                 },function (res) {
                 	 console.log(res)
                 	 oPileStatus=res.data.pileStatus;
                 	
     				 if(res.ret===0){ 
     					 var oChaMoney=(res.data.chaMoney===null)?flag:res.data.chaMoney;
     					 oChaMoney=parseFloat(oChaMoney).toFixed(2);
     					 
     					 var oCurrentA=(res.data.currentA===null)?flag:res.data.currentA;//电流
     					 oCurrentA==parseFloat(oCurrentA).toFixed(2);
     					 var oCurrentV=(res.data.currentV===null)?flag:res.data.currentV;//电压
     					 oCurrentV==parseFloat(oCurrentV).toFixed(2);
     					 
     					 $('.pile-code').text((res.data.pileCode===null)?flag:res.data.pileCode)//
 	 					 $('.pile-tradeNo').text((res.data.tradeNo===null)?flag:res.data.tradeNo)//
 	 					 $('.pile-status').text((res.data.pileStatusDesc===null)?flag:res.data.pileStatusDesc)//
 	 					 $('.pile-chaLen').text((res.data.chaLen===null)?flag:res.data.chaLen)//
 	 					 $('.pile-chaMoney').text(oChaMoney)//
 	 					 $('.pile-startTime').text((res.data.startTime===null)?flag:res.data.startTime)//
 	 					 $('.pile-currentA').text(oCurrentA)//电流
 	 					 $('.pile-currentV').text(oCurrentV)//电压
 	 					 
     					 if(parseInt(storage.getItem('pileToType'))===1){//如果是汽车，有电量，有服务费
     	 					 $('.pile-chaPower').text((res.data.chaPower===null)?flag:res.data.chaPower)//电量
     	 					 $('.pile-serviceMoney').text((res.data.serviceMoney===null)?flag:res.data.serviceMoney)//服务费
     	 					 $('.has-service,.pile-chaPower-box').removeClass('hidden');
     	 					
     					 }else{//自行车
     						 $('.none-service,.predict-time').removeClass('hidden');
     						 $('.pile-preLen').text((res.data.bikeChaLen===null)?flag:res.data.bikeChaLen)//预计充电总时长
     					 }
     					 
     					 if(parseInt(storage.getItem('pileType'))===1){//交流电
     						 $('.direct-current').addClass('hidden');
     					 }else{
     						 $('.direct-current').removeClass('hidden');
     						 $('.pile-soc').text((res.data.soc===null)?flag:res.data.soc);
     					 }
 						 
     					 $('.tabbar_bar').on('click',function(e){
     						
  	 						 if(isClick)return;
  	 						 isClick=true;
  	 						 if(oPileStatus===5||oPileStatus===6){
  	 							$.hideLoading();
  	 							$.toast((oPileStatus===5?'设备故障':'设备离线'),"cancel",function(toast) {
  	 								isClick=false;
  	                      		});
  	 						 }else{
  	 							 $.hideLoading();
  	 							 $.modal({
  		                             title: "您确定&nbsp;<span class='text-yellow'>停止充电</span>&nbsp;？",
  		                             text: "",
  		                             buttons: [
  		                               { text: "取消", className:"font-16 default",onClick:function(){
  		                            	   isClick=false;
  		                               }},
  		                               { text: "确定", className:"font-16 text-primary",onClick: function(){ 
  		                            	    $.showLoading('正在停止中');
  		  	  	 						 	getStop();
  		                               } }
  		                             ]
  		                           });
  	 							 
  	 						 }
  	 						
	 					 })
     					 
     					 if(oPileStatus===5||oPileStatus===6){
     						$.hideLoading();
                      		$.toast((oPileStatus===5?'设备故障':'设备离线'),"cancel",function(toast) {
                      		});
                      		return false;
                      	 }
     					  if(oPileStatus===3){//充电中
     						  
     						$.hideLoading();
					    	var tartSoc=(res.data.soc<0)?0:res.data.soc;
					    	tartSoc=(tartSoc>100)?100:tartSoc;
					    	
					    	var tart=$('#main').innerWidth();//目标的高度
					    	var animateBottom=-(tart-tart*(tartSoc)/100);
					    	
					    	$('#wave-top').css({"bottom":animateBottom+'px'})
					    	$('#wave-bottom').css({"bottom":animateBottom+'px'})
     						 
     					 }else{//3以外的状态
     						$.hideLoading();
     						$.showLoading('自动充满停止中');
     						getIsStop();
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
     
     
     
     /* getIsStop start */
     function getIsStop(){
    	isStop=true;
   
    	var time=stopTimeOut;
    	timer3=setInterval(function(){
    		setTime(time,stopTimeOut,function(i){
						$.hideLoading();
						clearInterval(timer3);
						clearInterval(timer2);
					    $.toast("停止充电失败","cancel",function(toast) {//超时
					    	isClick=false;
					    	isStop=false;
					    });
				   },function(i){//开启定时器
					   time--;
					   console.log(i);
				   })
		},1000);
    	 
    	timer2=setInterval(function(){
      	    getPages(//用于查询用户是否停止充电
		            'charging/charging_queryIsStopCharging.action',
		            {
		            	accountKey:storage.getItem('accountKey'),
		            	pileCode:storage.getItem('pileCode'),//桩编号
		            	tradeNo:storage.getItem('tradeNo')
		            },function (res) {
						 console.log(res);
		 				 if(res.ret===0){//
		 					  if(parseInt(res.data.isStopChargingStatus)===1){//停止成功
		 						    $.hideLoading();
	 						        clearInterval(timer1);
	 						        clearInterval(timer2);
	 						        clearInterval(timer3);
	 						       
	 						        var isType=parseInt(storage.getItem('pileToType'))//桩类型
	 						        $.modal({
	 						          title: "充电信息",
	 						          text: "<div class='text-left'><p class='"+((isType===1)?'hidden':'')+"'>预计充电总时长(分钟):&nbsp;"
	 						          +res.data.bikeChaLen+"</p><p>实际充电总时长(分钟):&nbsp;"+
	 						          res.data.chaLen+"</p><p class='"+((isType===1)?'':'hidden')+"'>充电电量(度):&nbsp;"
	 						          +res.data.chaPower+"</p><p>充电金额(元):&nbsp;"
	 						          +res.data.chaMoney+"</p><p>开始时间:&nbsp;"
	 						          +res.data.startTime+"</p><p>结束时间:&nbsp;"
	 						          +res.data.endTime+"</p></div>",
	 						          buttons: [
	 						            { text: isType===1?"去支付":"确定",className:"text-primary", onClick: function(){ 
	 						            	 if(isType===1){//电动汽车
	 						            		storage.setItem('chargingRecordId',res.data.paymentId);//缓存chargingRecordId
	 						            		window.location.href='record/chargingRecord.action';//订单管理
	 						            	 }else{
	 						            		window.location.href='charging/charging.action';
	 						            	 }					   
	 		 						         isClick=false;
	 		 						         isStop=false;
	 						            	
	 						            	}
	 						            }]
	 						        });
	 						        $('.tabbar_bar').off('click');
		 					  }  
		 				 }else{
		 					 $.hideLoading();
		 					 clearInterval(timer1);
						     clearInterval(timer2);
						     clearInterval(timer3);
		 					 $.toast(res.msg,"cancel",function(toast) {
	 						     isClick=false;
	 						     isStop=false;
		 					 });
		 				
		 				 }
		   
		            },function (msg){
		            	$.hideLoading();
				    	$.toast(msg,"cancel",function(toast) {
				    		 clearInterval(timer1);
						     clearInterval(timer2);
						     clearInterval(timer3);
						     isClick=false;
						     isStop=false;
				    	});
		   
		            },function(){
		            	
		            });
				},getDataInterval);
     }
     /* getIsStop end */
     
     /* getStop start */
     function getStop(){
    	   getPages(
   	            'charging/charging_queryStopCharging.action',
   	            {
   	            	accountKey:storage.getItem('accountKey'),
   	            	pileCode:storage.getItem('pileCode'),//桩编号
   	            	tradeNo:storage.getItem('tradeNo')
   	            },function (res) {
   	            	   console.log(res);
   					 if(res.ret===0){
   						 if(res.data.failReason===0){//请求isStoping
   							getIsStop();
   						 }else{
   							 $.hideLoading();
      						 $.toast(res.data.failReasonDesc,"cancel",function() {
      							isClick=false;
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
     }
     /* getStop end */
     
    timer1=setInterval(function(){
    	if(isStop)return;
    	initData();
     },stopPageInterval);
     
     $.showLoading('Loading');
     initData();
     

 })
 