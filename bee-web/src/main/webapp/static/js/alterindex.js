/* @update: 2015-3-19 15:34:34 */ 
function renderHtmlindex(i,e,n){var t='<div class="popwinindex"><div class="mesg"><p class="tit mb20">'+e+'</p><p class="btns mb10"><a href="'+i+'" id="linkto" target="_blank">'+n+'</a></p><p class="tips">\u6ce8\uff1a\u53ea\u6709\u5b9e\u540d\u8ba4\u8bc1\u7684\u4e1c\u5bb6\u624d\u53ef\u4ee5\u67e5\u770b\u9879\u76ee\u4fe1\u606f\uff01</p></div><a href="javascript:;" class="close"></a></div>';return t}function setPosition(i){var e=$(document).scrollTop(),n=$(window).height(),s=$(window).width(),o=$(i).outerWidth(),a=$(i).outerHeight(),d=(s-o)/2;t=e+(n-a)/2,$(i).css({position:"absolute",left:d,top:t})}function openWinindex(i,e,n){creatMaskindex.show();var t=$(renderHtmlindex(i,e,n));t.appendTo($("body")),setPosition(t)}var creatMaskindex={create:function(){var i=$(document).height(),e=$(document).width();this.mask=$("<div class='mask'></div>").css({height:i,width:e,display:"none"}).appendTo($("body"))},show:function(){this.mask||this.create(),this.mask.show()},hide:function(){this.mask.hide()}};$(".popwinindex .close").live("click",function(){creatMaskindex.hide(),$(this).parents(".popwinindex").remove()}),$(".popwinindex #linkto").live("click",function(){creatMaskindex.hide(),$(".popwinindex").remove()}),$(".mask").live("click",function(){creatMaskindex.hide(),$(".popwinindex").remove()});