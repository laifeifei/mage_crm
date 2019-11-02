$(function () {
    var lossId = $("#lossId").val();
    $("#dg").edatagrid({
        url: ctx + "/customer_repri/customerReprieveByLossId?lossId=" + lossId,
        saveUrl: ctx + "/customer_repri/insertReprive?lossId=" + lossId,
        updateUrl: ctx + "/customer_repri/updateReprive?lossId=" + lossId
    })
    var state = $("#state").val();
    if (state == 1) {
        $("#toolbar").remove();
        $("#dg").edatagrid("disableEditing");
    }
});

/*
保存暂缓措施
 */
function saveCustomerRepri(){
    $("#dg").edatagrid("saveRow");
    $("#dg").edatagrid("load");
}
/*
删除暂缓措施
 */
function delCustomerRepri(){
    var rows = $("#dg").edatagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请至少选择一条要删除的暂缓措施","error");
        return;
    }
    $.messager.confirm("来自crm","你确定要删除这条记录吗？",function (r) {
        if(r){
            $.ajax({
                type:"post",
                url: ctx + "/customer_repri/delete",
                data: "id=" + rows[0].id,
                dataType: "json",
                success: function (data) {
                    if (data.code == 200) {
                        $.messager.alert("来自crm", data.msg, "info");
                        $("#dg").edatagrid("load");
                    }else {
                        $.messager.alert("来自crm", data.msg, "error");
                    }
                }
            });
        }
    })
}
/*
确认流失
 */
function updateCustomerLossState() {
    $.messager.confirm("来自crm","确定该用户已流失吗？",function (r) {
        if(r){
            $.messager.prompt("来自crm","请输入流失原因",function (msg) {
                if(msg){
                    $.ajax({
                        type:"post",
                        url:ctx + "/customer_loss/updateCustomerLossState",
                        data:"lossId=" + $("#lossId").val() + "&lossReason=" + msg,
                        dataType:"json",
                        success:function (data) {
                            if (data.code == 200) {
                                $.messager.alert("来自crm", data.msg, "info");
                                $("#toolbar").remove();
                            }else {
                                $.messager.alert("来自crm", data.msg, "error");
                            }
                        }
                    });
                }else {
                    $.messager.alert("来自crm","流失原因不能为空","error");
                }
            })
        }
    })
}