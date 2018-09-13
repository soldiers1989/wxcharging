$(function() {
    var storage= window.localStorage;
    storage = storage ? storage : [];
    $.showLoading('Loading');
    
    getPages("account/account_queryAccountInfo.action",
			{
      		    accountKey:storage.getItem('accountKey')
			},function(res){
				 console.log(res);
				
				 if(res.ret===0){//如果成功
					 $('.show-remain').text(parseFloat(res.data.usableMoney).toFixed(2));
					 storage.setItem('usableMoney',parseFloat(res.data.usableMoney).toFixed(2));
					 $.hideLoading();
				 }else{
					 $.hideLoading();
                     $.toast(res.msg,"cancel",function(toast) {});
				 }
				
			},function(msg){
				 $.hideLoading();
				 $.toast(msg,"cancel",function(toast) {
                 });
			},function(){
				
			});
    
    

});