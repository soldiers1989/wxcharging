$(function() {
    var storage= window.localStorage;
    storage = storage ? storage : [];
    var text=$('.text-show');

    var otel;
    var time=60;
    var ocode=$('.code');
    var ocodeVal;
    var isClick=false;
    
    function repEmpty(obj) {
        if(obj.val().trim().length===0){
            return true;
        }else{
            return false;
        }
    }
    var regPhone=/^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0-9])|(17[0-9]))\d{8}$/;

    $('.exper-user').on('click',function(e){//点击确认修改密码
        if(isClick)return;
        isClick=true;
        text.text('');
        $('.weui-cells_form .weui-input').each(function (i) {
              var isEpVal=$(this);
              var rpLabel=isEpVal.closest('.weui-cell').find('.weui-label');
              var oRole= parseInt(isEpVal.data('role'));
              if(repEmpty(isEpVal)){
                  text.text(rpLabel.text()+'不能为空');
                  isClick=false;
                  return false;
              }else{
                  if(oRole===2){
                      if(!(regPhone.test(isEpVal.val()))){
                          text.text(rpLabel.text()+'格式错误');
                          isClick=false;
                          return false;
                      }
                  }else{
                      if(oRole===5){
                          if(!($('#password').val()===isEpVal.val())){
                              text.text('密码输入不一致');
                              isClick=false;
                              return false;
                          }else{
                        	 $.showLoading('Loading');
                            //修改密码成功
                          	getPages("account/account_queryModifyPassword.action",
                        			{
	                          		    accountKey:storage.getItem('accountKey'),
	                          		    oldPassword:$('#oldPassword').val(),
	                          		    newPassword:$('#password').val(),
	                          		    confirmNewPassword:$('#confirmPassword').val()
                        			},function(res){
                        				 
                        				 if(res.ret===0){//如果成功
                        					 if(res.data.failReason===0){
                        						 $.hideLoading();
                        						 $.toast('密码修改成功',800,function() {
                        							 window.location.href="account/userInfo.action";
                                                 });
                        					 }else{
                        						 $.hideLoading();
                        						 $.toast(res.data.failReasonDesc,"cancel",function() {});
                        					 }
                                            
                        				 }else{
                        					$.hideLoading();
                                            $.toast(res.msg,"cancel",function(toast) {});
                        				 }
                        			},function(msg){
                        				 $.hideLoading();
                        				 $.toast(msg,"cancel",function(toast) {});
                        			},function(){
                        				isClick=false;
                        			});
                          }
                      }
                  }
              }
        })

    })
});