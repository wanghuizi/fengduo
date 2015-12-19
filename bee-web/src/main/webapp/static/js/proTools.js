/**
 * Created by SDX on 2014/5/22.
 * vision:1.0
 * title: 众筹项目通用工具
 * e-mail:jrshenduxian@jd.com
 */


/**
 *图片中心弹窗
 * @param $obj
 */
function alertPicBox($obj) {
    var mask = JrTools.createMask(0.5);
    var $mask = $(mask);
    var $imgHolder = $("#imgHolder");
    var $picOuter = $("#imgHolder").find('div');
    var $img = $imgHolder.find(".b_pic");
    var $sp = $imgHolder.find("span");

    $obj.bind("click", function () {
        var src = $(this).attr("big-data");
        $mask.show();
        if (src != "") {
            $img.load(function () {
                $imgHolder.show();
                $picOuter.show();
                var $imgW = $img.width();
                var $imgH = $img.height();
                $picOuter.css({"width": $imgW, "height": $imgH});
                JrTools.autoMid($picOuter);
            });
            $img.attr("src", src);
            $img.show();
        }
        else {
            return;
        }

    })

    $mask.bind("click", function () {
        $mask.hide();
        $imgHolder.hide();
        $img.attr("src", "");
    })
    $sp.bind("click", function () {
        $mask.hide();
        $imgHolder.hide();
        $img.attr("src", "");
    })
}


/**
 *选项卡切换
 * @param {
		    $Tag:(object)        		导航标签
		    $conts:(object)       	    内容区
		    checkClass:(string)         选中标签的Class
			type:(string)
		  }
 */
function changeTag($Tag, $conts, checkClass, type, fn) {
    var checkClass = checkClass || ""
    var type = type || "click";
    $Tag.each(function (index, ele) {
        $(this).bind(type, function () {
            $Tag.removeClass(checkClass);
            $conts.hide();
            $(this).addClass(checkClass);
            $conts.eq(index).show();
            fn && fn(index, ele);
        })
    });
}


/**
 * input框placeHolder效果
 */
function placeHolder($obj, holderClassName) {
    if ('placeholder' in document.createElement('input'))return;
    $obj.each(function (index, ele) {
        var holderTxt = $(this).attr("placeholder");
        if (!holderTxt)return;
        $(this).val(holderTxt)
        $(this).focus(function () {
            if ($.trim($(this).val()) == $.trim(holderTxt)) {
                $(this).val("");
                holderClassName && $(this).removeClass(holderClassName)
            }
        })

        $(this).blur(function () {
                if (!$.trim($(this).val())) {
                    $(this).val(holderTxt);
                    holderClassName && $(this).addClass(holderClassName)
                }
            }
        )
    })
}

function imgResize(obj,size){
    $obj = $(obj);
    $obj.width()>$obj.height()?($obj.css("width",size+"px")):($obj.css("height",size+"px"));
}

//表情处理
function EmojiTransform() {
    this.init();
}
EmojiTransform.prototype = {
    init: function() {
        this.emojiMap = {
            "[大哭]": "-3",
            "[害羞]": "-33",
            "[憨笑]": "-63",
            "[奸笑]": "-93",
            "[可爱]": "-123",
            "[玫瑰]": "-153",
            "[难过]": "-183",
            "[太阳]": "-213",
            "[调皮]": "-242",
            "[偷笑]": "-272",
            "[吻]": "-301",
            "[握手]": "-330",
            "[疑问]": "-360",
            "[拥抱]": "-393",
            "[再见]": "-423",
            "[真棒]": "-453"
        }
    },
    change: function(text) {
        var self = this;
        var m = text.replace(/(\[[\u4e00-\u9fa5]*\w*\]){1}/g, function(emoji) {
            var value = self.emojiMap[emoji];
            if (value) {
                if(emoji=='[真棒]'){
                    return '<i class="emoji-text " style="background-position: -4px ' + value + 'px"></i>';
                }else{
                    return '<i class="emoji-text " style="background-position: -3px ' + value + 'px"></i>';
                }
            }
            return emoji;
        });
        return m;
    }
}