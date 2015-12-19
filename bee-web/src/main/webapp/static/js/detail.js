function isInvstor(){
    ckeckPinAndAlert();
    var url = "/funding/checkInvestor.action";
    var value = false;
    $.ajax({url:url,type:'post', cache:false,async:false,dataType:'json',
        success:function(data) {
            if(data != null && data != undefined && data != ""){
                value = data.result;
            }
        },
        error: function(error){
            reminder("系统繁忙！");
        }
    });
    return value;
}
function isInvstorForBooking(callback){
    var value = false;
    ckeckPinAndAlertForBooking(function(){
        var url = "/funding/checkInvestor.action";
        $.ajax({url:url,type:'post', cache:false,async:false,dataType:'json',
            success:function(data) {
                if(data != null && data != undefined && data != ""){
                    value = data.result;
                }
            },
            error: function(error){
                reminder("系统繁忙！");
            }
        });
        callback(value);
    });
}

function isInvstorLeader(callback){
    var value = 0;
    ckeckPinAndAlertForBooking(function(){
        var url = "/funding/checkInvestorAjax.action";
        $.ajax({url:url,type:'post', cache:false,async:false,dataType:'json',
            success:function(data) {
                if(data.code != null){
                    value = data.code;
                }
            },
            error: function(error){
                reminder("系统繁忙！");
            }
        });
        callback(value);
    });
}

function slideFunction(id){
    var winHeight = document.documentElement.clientHeight;
    setTimeout(function () {
        var objH = $("#"+id).height();
        $("html,body").animate({scrollTop: $("#"+id).offset().top + "px"}, 1000);
    }, 100);
}

function slideInvestorList(id){
    $("#projectDetailList").css('display','none');
    $("#projectTopicList").css('display','none');
    $("#projectKownList").css('display','none');
    $("#projectProInvestorList").css('display','block');
    $("#projectKownTop").removeClass();
    $("#projectTopicTop").removeClass();
    $("#projectDetailTop").removeClass();
    $("#projectInvestorTop").addClass('cur');

    investorListFirst();
    var winHeight = document.documentElement.clientHeight;
    setTimeout(function () {
        var objH = $("#"+id).height();
        $("html,body").animate({scrollTop: $("#"+id).offset().top-120 + "px"}, 500);
    }, 80);
}

$("#a_prais").click(function(){
    var projectId = $("#projectId").val();
    var key=$.cookie('Praise_DJ_Project_Id_'+projectId);
    var praise = 0;
    if (key==null || key==undefined || key!="true"){
        $.getJSON("http://sq.jr.jd.com/cm/praise?key=4000&systemId="+projectId+"&callback=?", function(data,e) {
            changeFocusOrPraise(data.praise,"a_prais");
            $.cookie('Praise_DJ_Project_Id_'+projectId,'true');
        });
    }else{
        reminder('东家温馨提示：您已点赞该项目。');
    }
});

function a_focusFunction(){
    $("#user_pin").val('');
    getCurrentUserForDetail();
    var name = $("#user_pin").val();
    if(name==null || name == "" || name == undefined){
        
    }else{
        if("J_ui_prize cur" != $("#a_focus").attr("class")){
            doFocus();
        }else{
            reminder("东家温馨提示：您已关注该项目。")
        }
    }
}

function show_num(f){
    if(f>=10000){
        return parseInt(f/10000) +"万";
    }else if(f<10000 && f>=1000){
        return parseInt(f/1000) +"千";
    }else{
        return f;
    }
}

function getFocusAndPraiseCount(){
    var projectId = $("#projectId").val();
    var url = "http://sq.jr.jd.com/cm_focus/isFocus?key=4000&systemId="+projectId+"&callback=?";
    jQuery.ajax({url:url, dataType: "jsonp",async:false,scriptCharset: "utf-8", success: function (datas,e) {
        if(datas == null || datas["data"] == null || datas["data"] == undefined){
            $("#a_prais").children("span").html("0");
            $("#a_focus").children("span").html("0");
        }else{
            $("#a_prais").children("span").html(show_num(datas.data.praise));
            $("#a_focus").children("span").html(show_num(datas.data.focus));
            getIsPraised();
            getIsFocused(datas)
        }
    },
        error:function(a){
            // reminder("系统繁忙！");
        }
    });

}

function getIsPraised(){
    var projectId = $("#projectId").val();
    var key=$.cookie('Praise_DJ_Project_Id_'+projectId);
    var praise = 0;
    if (key==null || key==undefined || key!="true"){
        return;
    }else{
        $("#a_prais").addClass("cur");
    }
}

function getIsFocused(datas){
    var name =   $("#user_pin").val();
    if(name != null && name != undefined && name != ""){
        if(datas["flag"]==true){
            $("#a_focus").addClass("cur");
        }
    }
}

function changeFocusOrPraise(count , id){
    var t = parseInt($("#"+id).position().left) + 10;
    var i = parseInt($("#"+id).position().top) - 10;
    var n = $("#"+id);
    $("#"+id).append('<div class="add"><b>+1</b></div>');
    $(".add").css({
        position: "absolute",
        "z-index": "100",
        left: t + 20 + "px",
        top: i - 20 + "px"
    });
    $(".add").animate({left: t - 10, top: i + 10, opacity: 0}, 1500, function () {
        $(".add").fadeOut(5500).remove();
        n.find("span").text(show_num(count))
    });
    $("#"+id).addClass("cur");
}

function doFocus(){
    var projectId = $("#projectId").val();
    var url = "http://sq.jr.jd.com/cm_focus/focus?key=4000&systemId="+projectId+"&callback=?";
    jQuery.ajax({url:url, dataType: "jsonp",async:false,scriptCharset: "utf-8", success: function (data) {
        var focus = data.focus;
        var praise = data.praise;
        changeFocusOrPraise(focus,"a_focus");
    },
        error:function(a){
            reminder("系统繁忙");
        }
    });
}

function reminder(content){
    var btns = '<div class="submit t-center">' +
        '<input type="button" id="sendbtnReminder" class="subbtn confirm" value="确定"/>' +
        '</div>';
    var json = {
        "text":content,
        "btns":btns
    };
    openWin(json);
    $("#sendbtnReminder").click(function(){
        creatMask.hide();
        $(".popwin").remove();
    });
}

function urlReminderLeader(content){
    var btns = '<div class="submit t-center">' +
        '<input type="button" id="sendbtnReminderUrl" class="subbtn confirm" value="立即申请"/>' +
        '</div>';
    var json = {
        "text":content,
        "btns":btns
    };
    openWin(json);
    $("#sendbtnReminderUrl").click(function(){
        creatMask.hide();
        leaderStatus();
        $(".popwin").remove();
    });
}

function urlReminder(content,url){
    var btns = '<div class="submit t-center">' +
        '<input type="button" id="sendbtnReminderUrl" class="subbtn confirm" clstag="jr|keycount|gq_xmy|gq_sqtzr" value="去申请"/>' +
        '</div>';
    var json = {
        "text":content,
        "btns":btns
    };
    openWin(json);
    $("#sendbtnReminderUrl").click(function(){
        creatMask.hide();
        window.open(url);
        $(".popwin").remove();
    });
}

function ckeckPinAndAlert(){
    var name =   $("#user_pin").val('');
    getCurrentUserForDetail();
    var name =   $("#user_pin").val();
    if(name==null || name == "" || name == undefined){
       
    }
}

function ckeckPinAndAlertForBooking(callback){
    var name =   $("#user_pin").val('');
    getCurrentUserForDetail();
    var name =   $("#user_pin").val();
    if(name==null || name == "" || name == undefined){
        
    }else{
        callback();
    }
}

function getCurrentUserForDetail() {
    if ($("#user_pin").val() == "" || $("#user_pin").val() == undefined || $("#user_pin").val() == null || $("#user_pin").val() == "{}") {
        jQuery.ajax({url: "/login",dataType: "text",scriptCharset: "utf-8",async:false,
            success: function (msg) {
                $("#user_pin").val(msg);
            }, error: function (e) {
                $("#user_pin").val("");
                // reminder("系统繁忙！");
            }
        });
    }
}

function isRealname(){
    var url = "/funding/validRealName.action";
    var realname = false;
    $.ajax({url:url,type:'post', cache:false,async:false,dataType:'json',
        success:function(data) {
            if(data.code == 1){
                realname = true;
            }
        },
        error: function(error){
            reminder("系统繁忙！");
        }
    });
    return realname;
}

function checkLength(content,length,id){
    if(content.length > length){
        $('#'+id).html("<font>字数超出范围.</font>");
        $('#'+id).css("display","block");
        return false;
    }else if(content.length==0){
        $('#'+id).html("<font>内容不能为空.</font>");
        $('#'+id).css("display","block");
        return false;
    }
    else{
        $('#'+id).css("display","none");
        return true;
    }
}

function submintBookingLeader(url,json){
    var flag = false ;
    jQuery.ajax({url:url, dataType: "json",async:false,scriptCharset: "utf-8", data:json,
        success: function (data) {
            alert(data.message);
            if(data!=null || data != undefined){
                flag = data.flag;
            }else{
                flag = 0 ;
            }
        },
        error:function(a){
            //reminder("系统繁忙");
        }
    });
    return flag;
}