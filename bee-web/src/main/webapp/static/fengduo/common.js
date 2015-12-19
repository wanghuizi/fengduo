function login(){
	$.reqHtml('/ajaxLogin',"",500);
}	

function redirctTipsMsg(){
    var errMsg = getQueryString('errMsg');
    if(errMsg != null){
		modifyUrl2DelParams();
		document.body.scrollTop =  document.body.scrollHeight - 150;
    	jQuery.tips(errMsg,'.btn_submit_cls',10000);
    }
}

$(function(){
	//登录提交
	$(".a-login-btn").click(function () {
	    var username = $("input[name='username']").val(),
	        password = $("input[name='password']").val(),
	        verify = $("input[name='captcha']").val();
	    if(username==""){
	        $.tips("请输入手机号",$("input[name='username']"),2000);
	        return;
	    }else if(!(/^1[3|4|5|7|8][0-9]\d{8}$/.test(username))){
	        $.tips("请输入正确的手机号",$("input[name='username']"),2000);
	        return;
	    } 
	    if(password==""){
	        $.tips("请输入您的密码",$("input[name='password']"),2000);
	        return;
	    }
	    if(verify==""){
	        $.tips("请输入图片验证码",$("input[name='captcha']"),2000);
	        return;
	    }       
	    $("form[name='loginForm']").get(0).submit();
	});
})

