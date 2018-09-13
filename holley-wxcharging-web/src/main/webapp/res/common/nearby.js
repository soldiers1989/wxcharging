$(function() {

//    FastClick.attach(document.body);
    var storage= window.localStorage;
    storage = storage ? storage : [];
    var pindex=1;//页码
    var listDiv='.more-list-content';//对象
    var mainDiv='.pullContent';
    var pullrefresh;
    var loading;
    var inputLoading;
    var isOver;//是否没有数据
    var flag='- -';

    console.log(storage)
    var oldType="电动汽车";
    $('.nearby-type').val("电动汽车");
    $(".nearby-type").picker({
        title: "类型选择",
        cols: [
          {
            textAlign: 'center',
            values: ['电动汽车', '电动自行车']
          }
        ],
        onChange: function(p, v, dv) {
//          console.log(p, v, dv);
        },
        onClose: function(p, v, d) {
          var str=p.value;
          if(oldType!=str){
        	  $.showLoading('Loading');
        	  initPage(false)
        	  oldType=str;
        	  console.log(oldType)
          	}
          }
      });

    
    if(storage.getItem('openid')===null){//没有openid
    	if(GetQueryString('code')){
    	 $.showLoading('Loading');
       	 getPages(//没有openid
     	            'wechat/wechat_authorize.action',
     	            {
     	               code:GetQueryString('code')
     	            },function (res) {
     	            	   console.log(res);
     					 if(res.ret===0){
     						 if(res.data.failReason===0){
    	    						  storage.setItem('openid',res.data.openid);
    	    						  getPages(
    	    						            'wechat/wechat_userInfo.action',
    	    						            {
    	    						            	openid:res.data.openid
    	    						            },function (res) {
    	    						            	  console.log(res);
    	    										 if(res.ret===0){
    	    											 if(res.data.failReason===0){
    	    												 storage.setItem('nickname',res.data.nickname)
    	    												 storage.setItem('headimgurl',res.data.headimgurl)
    	    												 storage.setItem('city',res.data.city)
    	    								                 
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
     	             
     	          });
    	}else{
    		var tartUrl="common%2fnearby.action";
            window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+(redirectUri+tartUrl)+"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
    	}
         
    }

    function getListItem(vlist,vTart,callback) {
        var listHtml='';
        for(var i in vlist){
        	var xtime=(vlist[i].dateTime===null)?flag:vlist[i].dateTime;
        	listHtml+='<a href="common/nearbyMore.action" data-id="'+((vlist[i].id===null)?flag:vlist[i].id)
        	+'"  class="weui-media-box weui-media-box_appmsg wui-cell-nopadding-top padding-10 a-list-charge">'+''+
			'<div class="weui-media-box__bd nearby-list"><div class="nearby-list-item">'+''+
				'<p class="font-15">'+((vlist[i].address===null)?flag:vlist[i].address)+
				'</p><p class="flex-just-between flex-init deep-light-gray nearby-badge margin-10">'+''+
					'<span class="text-right flex-just-between margin-right-10"><span class="weui-badge bg-green">闲</span>'+((vlist[i].available===null)?flag:vlist[i].available)+'个</span>'+
					'<span class="text-right flex-just-between margin-right-10"><span class="weui-badge">充</span>'+((vlist[i].busy===null)?flag:vlist[i].busy)+'个</span>'+''+
					'<span class="text-right flex-just-between"><span class="weui-badge bg-orange">共</span>'+((vlist[i].total===null)?flag:vlist[i].total)
					+'个</span></p></div><div class="nearby-list-item text-primary">'+((vlist[i].distance===null)?flag:vlist[i].distance)+'km</div></div></a>';
        }
        $(vTart).append(listHtml);
        $('.a-list-charge').on('click',function(){
        	storage.setItem('id',$(this).data('id'));//缓存accountKey
        	console.log(storage)
        })
        if(typeof(eval('callback'))=="function"){
        	callback();
        }
    }
  
/*    $.showLoading('Loading');*/
    if(storage.getItem('longitude')===null){//没有经纬度
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
	 						    jsApiList: ['getLocation','scanQRCode'] // 必填，需要使用的JS接口列表
	 						});
	 						wx.ready(function(){
	
	 						    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
	 							    wx.getLocation({
										type: 'gcj02', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
										success: function (res) {
											var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
											var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
											var transTude=gcj02tobd09(longitude,latitude);
											storage.setItem('latitude',transTude[1]);
											storage.setItem('longitude',transTude[0]);	
		
					 						initPage(false);
										},
										fail:function(){	
											$.hideLoading();
											$.toast(res.data.errMsg,"cancel",function() {});
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
	           
	      	    
	            });
    }else{//缓存了已有的经纬度
		initPage(false);
    }
    
    
    
function initPage(isFresh,isInput){ //默认更新
        
		if(!isFresh){
			 loading = false;//重置加载更多标记
	         pullrefresh=false;//重置下拉刷新标记
		}
	    isOver=false;//重置有无数据标记
	    pindex=1;//重置页码
	    if(!isInput){
	    	inputLoading=false;
	    }
	    
        getPages(
            'common/common_queryStationNearby.action',
            {
            	longtitude:storage.getItem('longitude'),
            	latitude:storage.getItem('latitude'),
            	keyWord:$('#searchInput').val(),
            	pageIndex:pindex,
            	stationType:(($('.nearby-type').val()=='电动汽车')?1:2)
            },function (res) {
            	 console.log(res)
 				 if(res.ret===0){
	                 var olist=res.data.datas;
	                 var oindex=res.data.pageIndex;//页码
	                 var tproperty=res.data.totalProperty;//总记录数
	                 var tpage=res.data.totalPage;//总页数
	    
	                 if(oindex===tpage){
	                     isOver=true;
	                 }
	                 $(listDiv).children().remove();//清空列表
	                 if((tproperty===0)&&(($(listDiv).children().length)===0)){//没有数据的时候
	                	 
	                     $(listDiv).html('<p class="nodata">没有数据</p>');
	                     $.hideLoading();
	                     return;
	                 }else{
	                	
	                     getListItem(olist,listDiv,function(){
	                    	 $.hideLoading();
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
              if(isFresh){
           	   $(mainDiv).pullToRefreshDone();
                  pullrefresh=false;
                  loading = false;//重置加载更多标记
              }
              $(mainDiv).infinite();
              if(isInput){
      	    	inputLoading=false;
      	      }
      	    
            });
    }
    
   
    
	$(document).on('input','#searchInput',function (e) {         
		$.showLoading('Loading');
		if(inputLoading){return;}
		inputLoading=true;
		initPage(true,true);//搜索出来的结果   
	})
	
	$('#searchClear,#searchCancel').on('click',function(){
		$('#searchInput').val('');
		$.showLoading('Loading');
		initPage(false);//搜索出来的结果  
	})
    
    $(mainDiv).pullToRefresh({
        distance:55
    }).on("pull-to-refresh", function() {//下拉刷新
         if(pullrefresh) return;
         pullrefresh = true;
        setTimeout(function() {
        	initPage(true);
        }, 1000);
    });
   
    $(mainDiv).infinite().on("infinite", function() {
     
        if(pullrefresh) return;
        var self=$(this);
        if(loading) return;
        loading = true;
  
        $('.weui-loadmore').addClass('loadmore-opacity');
        setTimeout(function() {  
            if(isOver){//全部数据已经展示
                $('.weui-loadmore').removeClass('loadmore-opacity');
                $.toast("已加载全部",800,function(toast) {
                    loading = false;
                    self.destroyInfinite();
                });
            }else{
                pindex++;
                getPages(
                		 'common/common_queryStationNearby.action',
                         {
//                         	longtitude:"120.02",
//                         	latitude:"30.25",
                			latitude:storage.getItem('latitude'),
                			longtitudelongtitude:storage.getItem('longitude'),
                         	keyWord:$('#searchInput').val(),
                         	pageIndex:pindex,
                         	stationType:(($('.nearby-type').val()=='电动汽车')?1:2)
                        },function (res) {
 
                            var olist=res.data.datas;
                            var oindex=res.data.pageIndex;//页码
                            var tproperty=res.data.totalProperty;//总记录数
                            var tpage=res.data.totalPage;//总页数
               
                            if(oindex===tpage){
                                isOver=true;
                            }
                            getListItem(olist,listDiv);
          
                        },function (msg){},function(){
	                       	 loading=false;
	                       	 $('.weui-loadmore').removeClass('loadmore-opacity');
                        });
            }
       },500);
    });

})