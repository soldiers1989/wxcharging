 $(function() {

//        FastClick.attach(document.body);
     var storage= window.localStorage;
     storage = storage ? storage : [];

     var listDiv='.more-list-content';//对象
     var vKey=storage.getItem('accountKey');//密钥
     

     function getListItem(olist,vTart,callback) {
         var listHtml='',appendHtml='';
         var vlist=olist.datas;
         appendHtml+='<div class="weui-panel_access text-white"><div class="" style="padding:20px 10px;"><div class="">'+''+
			'<div class="flex-just-between"><div><p class="" style="padding:0 0 10px;font-size:16px;">桩名称：'+((olist.stationName===null)?flag:olist.stationName)+
			'</p><p class="font-r1"><span class="font-14">'+((olist.address===null)?flag:olist.address)+'</span><span>'+''+
		      '</span></p></div><div class="hidden">'+((olist.phone===null)?flag:olist.phone)+
				'</div></div><p class="font-14 flex-just-between"><span>经度:'+((olist.longtitude===null)?flag:olist.longtitude)+'</span>'
				+'<span>纬度:'+((olist.latitude===null)?flag:olist.latitude)+'</span></p></div></div>'+''+
	'<div class="charge-data nearby-more-nav padding-5 padding-15"><div class="charge-data-item">'+
			'<a href="javascript:void(0)" class="link-a-function get-location text-white"><img src="res/images/gps.png" />'+'导航'+'</a></div><div class="charge-data-item"><a href="tel:'+((olist.phone===null)?flag:olist.phone)+'" class="link-a-function text-white"><img src="res/images/contact.png" />'+'联系'+'</a></div></div></div>'+''+
			'<div class="nearby-more-box bg-init font-12 margin-10" style="padding:30px 15px;">';
         for(var i in vlist){
         	var xtime=(vlist[i].dateTime===null)?flag:vlist[i].dateTime;
         	//桩状态颜色更换
         	var status;
         	/*if(vlist[i].statusText==='充电'){
         		status=' nearby-charging';
         	}else{
         		status=((vlist[i].statusText==='空闲')?' nearby-more-item-out':'')
         	}*/
         	switch (vlist[i].status)
         	{
         	case 1:
     		  status=" nearby-free";
         	  break;
         	case 2:
         	  status=" nearby-busy";
         	  break;
         	case 3:
         	  status=" nearby-charging";
         	  break;
         	case 4:
         	  status=" nearby-busy";
         	  break;
         	case 5:
           	  status=" nearby-busy";
           	  break;
         	default:
     		  status="";
       	      break;
         		  
         	}
         	
         	if(i%4===0){
         		listHtml+='<div class="">';
         	}
         	listHtml+='<div class="nearby-more-list"><div class="nearby-more-box-item'+status+'">'+
				'<p class="nearby-ab nearby-ab-top text-ellipsis">'+(parseInt(i)+1)+'</p>'+
				'<p class="text-ellipsis" style="max-width:80px;">'+((vlist[i].pileName===null)?flag:vlist[i].pileName)+'</p>'+
				'<p>'+((vlist[i].typeText===null)?flag:vlist[i].typeText)+'</p>'+
				'<p class="nearby-ab nearby-ab-bt text-ellipsis">'+((vlist[i].statusText===null)?flag:vlist[i].statusText)+'</p>'+
				'</div></div>';
         	if(i%4===3){
         		listHtml+='</div>';
         	}
         }
         listHtml+=
        	 '</div>';
         $(vTart).append((appendHtml+listHtml));
        
        
         if(typeof(eval('callback'))=="function"){
         	callback();
         }
         
         $('.get-location').on('click',function(){
		         
		    	 getPages(
		    	            'wechat/wechat_wxConfig.action',
		    	            {
		    	            	url:window.location.href
		    	            },function (res) {
		    	            	console.log(res)
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
		    	 						    jsApiList: ['openLocation'] // 必填，需要使用的JS接口列表
		    	 						});
		    	 						wx.ready(function(){
		    	 							wx.openLocation({
		    	 								latitude: Number(olist.latitude), // 纬度，浮点数，范围为90 ~ -90
		    	 								longitude: Number(olist.longtitude), // 经度，浮点数，范围为180 ~ -180。
		    	 								name: olist.stationName, // 位置名
		    	 								address: olist.address, // 地址详情说明
		    	 								scale: 1, // 地图缩放级别,整形值,范围从1~28。默认为最大
		    	 								infoUrl: '' // 在查看位置界面底部显示的超链接,可点击跳转
		    	 							});
		    		 						   
		    	 						});
		    	 						wx.error(function(res){
		    	 							console.log(res)
		    	 						    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
		    	 						});
		    	 					}else{
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
		    	    
		     })
     }
     
     
     
     function initPage(isFresh,isInput){ //默认更新
         

 	    if(!isInput){
 	    	inputLoading=false;
 	    }
 	    getPages(
 			  'common/common_queryStationDetail.action',
            {
           	     stationId:storage.getItem('id')
//           	     stationId:17
     			},function(res){
     				console.log(res)
     				 if(res.ret===0){//如果成功
     					 if(res.data.failReason===0){
     						var olist=res.data;
     						getListItem(olist,listDiv,function(){
    	                    	 $.hideLoading();
    	                     });
     					 }else{
     						 $.hideLoading();
     						 $.toast(res.data.failReasonDesc,"cancel",function() {});
     					 }
                         
     				 }else{
                         $.toast(res.msg,"cancel",function(toast) {});
     				 }
     			},function(msg){
     				 $.hideLoading();
     				 $.toast(msg,"cancel",function(toast) {
                          
                     });
     			},function(){
     				
     			});
     }
     
     $.showLoading('Loading');
     initPage(false);
     $('.tabbar_bar').on('click',function(e){
    	 window.location.href='charging/charging.action';
     })
    

   
 })