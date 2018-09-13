$(function() {

    FastClick.attach(document.body);
    var storage= window.localStorage;
    storage = storage ? storage : [];
    var pindex=1;//页码
    var listDiv='.more-list-content';//对象
    var mainDiv='.pullContent';
    var pullrefresh;
    var loading;
    var isOver;//是否没有数据
    var flag='- -';
    console.log(storage);

     function getListItem(vlist,vTart,callback) {
         var listHtml='';
         for(var i in vlist){
             var xtime=((vlist[i].dataTime===null)?flag:vlist[i].dataTime);
             listHtml+='<div class="weui-panel weui-panel_access"><a href="javascript:void(0);" class="weui-media-box weui-media-box_appmsg wui-cell-nopadding-top padding-5"><div class="weui-media-box__hd weui-media-box-line"></div>'+
            	 ''+'<div class="weui-media-box__bd"><h4 class="weui-media-box__title">'+((vlist[i].tradeNo===null)?flag:vlist[i].tradeNo)
			+'<span class="weui-media-box__title-after text-red font-16">￥'+((vlist[i].rechargeMoney===null)?flag:vlist[i].rechargeMoney)+
			'</span></h4><p class="font-14 text-light-gray">'+((vlist[i].payWayDesc===null)?flag:vlist[i].payWayDesc)+'</p><p class="flex-just-between deep-light-gray">'
					+((vlist[i].rechargeTime===null)?flag:vlist[i].rechargeTime)+'<span class="text-light-gray text-green text-right hidden">'
					+((vlist[i].rechargeStatusDesc===null)?flag:vlist[i].rechargeStatusDesc)+'</span></p></div></a></div>';
         }
         $(vTart).append(listHtml);
         if(typeof(eval('callback'))=="function"){
        	 callback();
         }
     }

     function initPage(isFresh){ //默认更新
    
		if(!isFresh){
			 loading = false;//重置加载更多标记
	         pullrefresh=false;//重置下拉刷新标记
		}
		 isOver=false;//重置有无数据标记
	     pindex=1;//重置页码
    
         getPages(
             'record/record_queryRechargeRecord.action',
             {
                 accountKey:storage.getItem('accountKey'),
                 pageIndex:pindex
             },function (res) {
                  console.log(res)
  				 if(res.ret===0){
	                 var olist=res.data.datas;
	                 var oindex=res.data.pageIndex;//页码
	                 var tproperty=res.data.totalProperty;//总记录数
	                 var tpage=res.data.totalPage;//总页数
	                 $('.accout-all').text('￥'+parseFloat(res.data.totalMoney).toFixed(2))//统计总金额
	    
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
             });
     }
     $.showLoading('Loading');
     initPage(false);

    $(mainDiv).pullToRefresh({
        distance:55
    }).on("pull-to-refresh", function() {//下拉刷新
    	 if(loading) return;//如果在加载更多
         if(pullrefresh) return;//如果在刷新
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
                         'record/record_queryRechargeRecord.action',
                         {
                             accountKey:storage.getItem('accountKey'),
                             pageIndex:pindex
                         },function (res) {
        	                 $('.accout-all').text('￥'+parseFloat(res.data.totalMoney).toFixed(2));//统计总金额
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