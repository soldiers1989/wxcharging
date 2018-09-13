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
    
    /* 默认日期为当前年月 */
    function getNowDate(xDate,isDay){
        var oMonth=xDate.getMonth()+1;
        if(oMonth<10){
            oMonth='0'+oMonth;
        }
        if(isDay){
        	var vday=(xDate.getDate()<10)?('0'+xDate.getDate()):xDate.getDate();
        	timestr=xDate.getFullYear()+'-'+(oMonth)+'-'+(vday)+' ';
            return timestr;
        }else{
        	timestr=xDate.getFullYear()+'-'+(oMonth);
            return timestr;
        }  
    }
    
    $('.charge-datetime').val(getNowDate(new Date(),false)+' ');//月
    var oldTime=getNowDate(new Date(),false);//月
    $('.charge-type').val("电动汽车");
    var oldType="电动汽车";
     
    $('.charge-datetime').datetimePicker({//月
    	title: '日期选择',
        monthSplit:'',
        noDay:true,
        times: function () {
          return ;
        },
        onChange: function (picker, values, displayValues) {
         
        },
        onClose:function(vals){
           var str;
           str=vals.value.join('-');//选中的日期
           if(oldTime!=str){
        	   $.showLoading('Loading');
        	   initPage(false)

           	   oldTime=str;
           	}
       },
        onOpen:function(){
    	 
          }
      });
    $(".charge-type").picker({
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
    
    

    function getListItem(vlist,vTart,callback) {
        var listHtml='';
        for(var i in vlist){
        	var xtime=(vlist[i].dateTime===null)?flag:vlist[i].dateTime;
         /*   var finalTime=xtime.substring(0,xtime.indexOf(' '));*/
		          listHtml+='<a href="stat/chargingRecordMore.action" class="weui-media-box weui-media-box_appmsg wui-cell-nopadding-top padding-5 weui-cell_access a-href-storage" data-id="'+((vlist[i].chargingRecordId===null)?flag:vlist[i].chargingRecordId)+'">'+''
		        	  +'<div class="weui-media-box__hd weui-media-box-line"></div><div class="weui-media-box__bd"><h4 class="weui-media-box__title">'+((vlist[i].stationName===null)?flag:vlist[i].stationName)
		        	  +'<span class="weui-media-box__title-after text-red font-16 box-more-icon">￥'+((vlist[i].chaMoney===null)?flag:vlist[i].chaMoney)+'</span>'+''
		        	  +'</h4><p class=" flex-just-between deep-light-gray"><span>'+((vlist[i].tradeNo===null)?flag:vlist[i].tradeNo)+'</span><span class="text-light-gray text-right">'+((vlist[i].pileName===null)?flag:vlist[i].pileName)
		        	  +'</span></p><p class=" flex-just-between deep-light-gray"><span class="'+((vlist[i].payStatus===3)?'text-green':'text-yellow')+'">'+((vlist[i].payStatusDesc===null)?flag:vlist[i].payStatusDesc)+'</span>'
		        	  +xtime+'</p></div></a>';
        }
        $(vTart).append(listHtml);
        $('.a-href-storage').on('click',function(){
        	storage.setItem('chargingRecordId',$(this).data('id'));//缓存accountKey
        })
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
            'record/record_queryChargingRecord.action',
            {
                accountKey:storage.getItem('accountKey'),
                pageIndex:pindex,
                dateTime:$('.charge-datetime').val().trim(),
                chargeType:(($('.charge-type').val()=='电动汽车')?1:2),
                keyWord:''
            },function (res) {
                console.log(res);
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
            });
    }
    
    $.showLoading('Loading');
    initPage(false);
    
    
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
                		'record/record_queryChargingRecord.action',
                        {
                            accountKey:storage.getItem('accountKey'),
                            pageIndex:pindex,
                            dateTime:$('.charge-datetime').val().trim(),
                            chargeType:(($('.charge-type').val()=='电动汽车')?1:2),
                            keyWord:''
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