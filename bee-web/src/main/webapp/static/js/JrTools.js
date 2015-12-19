/**
 * Created by SDX on 2014/5/22.
 * vision:1.0
 * title:金融项目通用工具j
 * e-mail:jrshenduxian@jd.com
 */

/**
 *  ie console bug修复
 */

if (!window.console) {
    console = {
        log: function () {
        }, info: function () {
        }
    }
}

var JrTools = JrTools || {};

/**  
 * 浏览器版本检测
 */
JrTools.ie6Check = function () {
    var isIe6 = !-[1, ] && !window.XMLHttpRequest;
    return isIe6;
}


/**
 * 用户登录弹窗
 * @param fn
 */
JrTools.alertLogInBox = function (fn) {
    jdModelCallCenter.settings = {
        'fn': function () {
            helloService();
            fn && fn();
        }
    };
    jdModelCallCenter.login();

    function helloService() {
        $.ajax({
            url: ("https:" == document.location.protocol ? "https://" : "http://") + "passport." + pageConfig.FN_getDomain() + "/new/helloService.ashx?m=ls",
            dataType: "jsonp",
            scriptCharset: "gb2312",
            success: function (a) {
                a && a.info && $("#loginbar").html(a.info),
                    a && a.sso && $.each(a.sso,
                    function () {
                        $.getJSON(this);
                    });
            }
        });
    }
}


/**
 ***获取url参数**
 * @param name
 */

JrTools.getString = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURIComponent(r[2]);
    return null;
}

/**
 * cookie存储
 * @param c_name
 * @param value
 * @param expiredays
 */
JrTools.setCookie = function (c_name, value, expiredays) {
    var exdate = new Date()
    exdate.setDate(exdate.getDate() + expiredays)
    document.cookie = c_name + "=" + encodeURI(value) +
        ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString())
}


JrTools.getCookie = function (c_name) {
    if (document.cookie.length > 0) {
        c_start = document.cookie.indexOf(c_name + "=")
        if (c_start != -1) {
            c_start = c_start + c_name.length + 1
            c_end = document.cookie.indexOf(";", c_start)
            if (c_end == -1) c_end = document.cookie.length
            return decodeURI(document.cookie.substring(c_start, c_end))
        }
    }
    return ""
}





/**
 *  *  平滑滚动  *
 * @param obj
 * @param time
 */

JrTools.navScroll = function (obj, time) {
    if (!obj[0])return false;
    var n = 0;
    var stepTime = 20;
    var start = $(window).scrollTop();
    var currentTarget = start;
    var target = obj.offset().top;
    var totalNum = time / stepTime;
    var dis = target - currentTarget;
    var timer = setInterval(function () {
        n++;
        var a = 1 - n / totalNum;
        currentTarget = start + dis * (1 - a * a * a) | 0;
        $(window).scrollTop(currentTarget);
        if (n == totalNum) {
            $(window).scrollTop(target);
            clearInterval(timer)
        }
    }, stepTime)
}
/**
 *自动垂直居中
 * @param obj
 */
JrTools.autoMid = function (obj) {
    if (!obj[0])return false;
   //var winHeight = $(window).height();
    var winHeight = document.documentElement.clientHeight;
    var winWidth = document.documentElement.clientWidth;
//    var winHeight = window.screen.height;
    var winScrollTop = $(window).scrollTop();
    var objH = obj.height();
    var objW = obj.width();
    if(objW>782){
        objW = 782;
    }
    obj.css({"top": ( winScrollTop + Math.max(0,(winHeight - objH )/ 2))+ "px", "margin-left": ((winWidth-objW) / 2)+ "px"})
}


/**
 * 创建全屏遮罩
 * @param alpha
 * @param zIndex
 */
JrTools.createMask = function (alpha, zIndex) {
    var alpha = alpha || 0.5;
    var zIndex = zIndex || 1000;
    var mask = document.getElementById("Jr_Mask");
    if (!mask) {
        mask = document.createElement("div");
        mask.id = "Jr_Mask"
        if (JrTools.ie6Check()) {
            var _height = Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight);
            mask.style.cssText = "width:100%;display:none;position:absolute;left:0;top:0;background:#000;height:" + _height + "px;filter:alpha(opacity:" + alpha * 100 + ");z-index:" + zIndex;
        } else {
            mask.style.cssText = "width:100%;display:none;height:100%;position:fixed;left:0;top:0;background:#000;opacity:" + alpha + ";filter:alpha(opacity:" + alpha*100 + ");z-index:" + zIndex;
        }
        document.body.appendChild(mask);

    }
    return mask;
}




<!-- 弹出框 -->
function popAlert(obj,ibtn){
    var mask = JrTools.createMask(0.5);
    var $mask = $(mask);
    var ibtn = $(ibtn);
    var obj = $(obj);
    $(ibtn).click(function(){
        JrTools.autoMid($(obj));
        $mask.show();
        $(obj).show();
    })
    $mask.bind("click", function () {
        $mask.hide();
        $(obj).hide();
    });
    $("#sendMessContentDiv").bind('click',function(event) {
        $mask.hide();
        $(obj).hide();
    });
    $(obj).find("a").bind('click',function(event) {
        $mask.hide();
        $(obj).hide();
    });
}
popAlert(".microLetter",".weixin");



