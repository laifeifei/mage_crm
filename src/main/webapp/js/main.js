function openTab(text, url, iconCls){
    if($("#tabs").tabs("exists",text)){
        $("#tabs").tabs("select",text);
    }else{
        var content="<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='" + url + "'></iframe>";
        $("#tabs").tabs("add",{
            title:text,
            iconCls:iconCls,
            closable:true,
            content:content
        });
    }
}

/**
 * 安全退出
 */
function logout(){
    $.messager.confirm("来自Crm","你确定要退出系统吗？",function(r){
        if(r){
            $.messager.alert("来自Crm系统","一秒后退出系统","info");
            setTimeout(function (){
                //将cookie清除
                $.removeCookie("id");
                $.removeCookie("userName");
                $.removeCookie("trueName");
                //跳转到登录页面
                window.location.href="index";
            },1000)
        }
    })
}

/**
 * 打开密码修改框
 */
function openPasswordModifyDialog() {
    $("#dlg").dialog("open");
}
/*
关闭密码修改框
 */
function closePasswordModifyDialog() {
    $("#dlg").dialog("close");
}

/**
 * 给保存按钮绑定点击事件
 */
function modifyPassword() {
    $("#fm").form("submit",{
        url:ctx+"/user/updatePwd",
        onsubmit:function () {
            return $("#fm").form("validate");
        },
        success:function (data) {
            data=JSON.parse(data);//将返回值转换成为json串
            if(data.code==200){
                $.messager.alert("来自crm系统","修改密码成功，两秒后退出系统","info");
                setTimeout(function () {
                    //移除cookie，重新登录
                    $.removeCookie("userName");
                    $.removeCookie("trueName");
                    $.removeCookie("userId");
                    window.location.href="index";
                },2000)
            }else {
                $.messager.alert("来自Crm",data.msg,"info");
            }
        }
    })
}
