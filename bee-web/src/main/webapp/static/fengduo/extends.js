function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return decodeURI(r[2]); return null;
}

//修改url，去除后面的参数
function modifyUrl2DelParams(){
	var stateObject = {};
	var title = '';
	var url = window.location.pathname;
	history.pushState(stateObject,title,url);
}
$(function(){

jQuery.extend({
	//页面tips提示
	 tips:function(msg,node,millisecond){
		 layer.open({
			    type: 4,
			    content: [msg, node],//数组第二项即吸附元素选择器或者DOM
			    time:millisecond,
			    closeBtn: false
			}); 
	 },
	 
 	//页面弹出提示框
	message:function(title,content){
		layer.open({
		    type: 0, 
		    title:title,
		    content: content
		});
	},
	
	//页面弹出提示框传入回调方法
	message:function(title,content,Func){
		layer.open({
		    type: 0, 
		    title:title,
		    content: content,
		    yes: function(index){
		        layer.close(index); 
		        Func();
		    }
		});
	},
	 
	//操作询问框
	 FDConfirm:function(title,Func){
		var index = layer.confirm(title, {
			    btn: ['确定','取消'], //按钮
			    shade: false //不显示遮罩
			}, function(){
				layer.close(index);
				Func();
			}, function(){
				layer.close(index)
			});
	 },
	 
	//html页面弹出层
	reqHtml:function(url,params,width){
		$.post(url, params, function(res){
		    layer.open({
		        type: 1,
		        area: width+'px',
		        skin: 'layui-layer-lan',
		        content: res 
		    });
		});
	},
	//html页面弹出层
	html:function(params){
	    layer.open({
	        type: 1,
	        area: ['500px', '250px'],
	        title: '回复',
	        shade: 0.6, //遮罩透明度
	        skin: 'layui-layer-lan',
	        content: params 
	    });
	},
	//html页面弹出层
	html:function(params,width,height){
	    layer.open({
	        type: 1,
	        area: [width, height],
	        title: '头像上传',
	        shade: 0.6, //遮罩透明度
	        skin: 'layui-layer-lan',
	        content: params 
	    });
	}
});
})