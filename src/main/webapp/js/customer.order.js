function formatterState(val) {
    if (val==0){
        return "未支付";
    }
    if (val==1){
        return "已支付";
    }
}
function formatterOp() {
    var href = "javascript:openOrderDetailById()";
    return "<a href=" + href +">查看详情</a>";
}

/**
 * 打开查看详情页面
 */
function openOrderDetailById() {
    //获取选中的行数
    var rows = $("#dg").datagrid("getSelections");
    $.ajax({
        type:'post',
        url:ctx+"/customer_order/queryOrderInfoById",
        data:"orderId="+rows[0].orderNo, //将订单编号传入后台
        dataType:"json",
        success:function (data) {
            if(data.state==1){
                data.state="已支付";
            }else{
                data.state="未支付";
            }
            data.orderDate=new Date(data.orderDate).format("yyyy-MM-dd hh:mm:ss");
            $("#fm").form("load",data);
        }
    });
    //加载数据到客户订单详情对话框
    $("#dg2").datagrid("load",{
        orderId:rows[0].orderNo
    });
    //打开对话框
    $("#dlg").dialog("open");
}
function closeOrderDetailDialog() {
    //关闭对话框
    $("#dlg").dialog("close");
}