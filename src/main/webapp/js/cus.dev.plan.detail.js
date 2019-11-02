/**
 * 初始化页面
 */
$(function(){
    //获取开发状态
    var devResult = $("#devResult").val();
    //开发成功和开发失败移除可操作栏
    if(devResult==2||devResult==3){
        $("#toolbar").remove();
    }
    //加载数据表格中的内容
    $("#dg").edatagrid({
        url:ctx+"/cus_dev_plan/queryCusDevPlans?saleChanceId="+$("#saleChanceId").val(),
        saveUrl:ctx+"/cus_dev_plan/insert?saleChanceId="+$("#saleChanceId").val(),
        updateUrl:ctx+"/cus_dev_plan/update?saleChanceId="+$("#saleChanceId").val()
    })
});

/**
 * 给保存计划按钮添加点击事件
 */
function saveCusDevPlan() {
    //保存
    $("#dg").edatagrid("saveRow");
    //刷新数据表格
    $("#dg").edatagrid("load");
}

/**
 * 给更新计划按钮绑定点击事件
 */
function updateCusDevPlan() {
    $("#dg").edatagrid("saveRow");
}

/**
 * 给删除计划按钮绑定点击事件
 */
function delCusDevPlan() {
    //获取选中的记录
    var row = $("#dg").datagrid("getSelected");
    //判断是否选中记录
    if(row==null){
        $.messager.alert("来自crm","请选中一条记录","info");
        return;
    }
    //提示用户是否要删除选中的数据
    $.messager.confirm("来自crm","你确定要删除这条记录吗？",function(r){
        if(r){
            $.ajax({
                type:'post',
                data:"id="+row.id,
                dataType:"json",
                url:ctx+"/cus_dev_plan/delete",
                success:function(data){
                    if(data.code==200){
                        $.messager.alert("来自crm",data.msg,"info");
                        //刷新数据表格
                        $("#dg").edatagrid("load");
                    }else{
                        $.messager.alert("来自crm",data.msg,"error");
                    }
                }
            })
        }
    })
}

/**
 * 给开发成功，终止开发按钮绑定点击事件
 */
function updateSaleChanceDevResult(devResult) {
    //提示用户是否执行改操作
    $.messager.confirm("来自crm","你确定要执行该操作吗？",function (r) {
        if(r){
            $.ajax({
                type:"post",
                url:ctx+"/sale_chance/updateSaleChanceDevResult",
                data:"devResult="+devResult + "&saleChanceId=" + $("#saleChanceId").val(),
                dataType:"json",
                success:function (data) {
                    $.messager.alert("来自crm",data.msg,"info");
                    if(data.code==200){
                        $("#toolbar").remove();
                    }
                }
            })
        }
    })
}