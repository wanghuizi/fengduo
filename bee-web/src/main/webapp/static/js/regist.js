$(document).ready(function(){
$("#login-button").click(function(){
var d = dialog({
title: '会员登录',
width: "320px",
content: '<html><head><title></title><style type="text/css"> button.c2 {width:200px;} input.c1 {width:200px;}</style></head><body><div class="container"><div class="row"><div class="col-md-1"><label class="vertical-centers">账号</label></div><div class="col-md-4"><input type="text" name="nickname" class="regist-textarea c1"></div></div><div class="row"><div class="col-md-1"><label class="vertical-center">密码</label></div><div class="col-md-4"><input type="password" name="password" class="regist-textarea c1"></div></div><div class="row"><div class="col-md-1"></div><div class="col-md-4"><button class="btn btn-success c2" type="submit">登录</button></div></div></div></body></html>'
});
d.showModal();
});
});