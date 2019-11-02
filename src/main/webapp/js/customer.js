/**
 * 给搜索按钮绑定点击事件
 */
function searchCustomer() {
    $("#dg").datagrid("load",{
        khno:$("#s_khno").val(),
        name:$("#s_name").val()
    })
}

/**
 * 给添加按钮绑定点击事件
 */
function openCustomerAddDialog() {
    //清空添加表单中的内容
   $("#fm").form("clear");
   //打开添加表单
    $("#dlg").dialog("open").dialog("setTitle","添加客户信息");
}

/**
 * 给关闭按钮绑定点击事件
 */
function closeCustomerDialog() {
    //关闭添加表单
    $("#dlg").dialog("close");
}

/**
 * 给保存按钮绑定点击事件
 */
function saveOrUpdateCustomer() {
    //获取表单编号，判断是添加还是修改
    var id = $("#id").val();
    //默认为修改
    var url = ctx+"/customer/update";
    if(isEmpty(id)){//为空，则为添加
        url = ctx+"/customer/insert";
    }
    //提交数据
    $("#fm").form("submit",{
        url:url,
        onSubmit:function () {
            return $("#fm").form("validate");
        },
        //回调函数
        success:function (data) {
            data = JSON.parse(data);
            if(data.code==200){
                //提示信息
                $.messager.alert("来自crm",data.msg,"info");
                //关闭表单框
                closeCustomerDialog();
                //刷新数据
                searchCustomer();
            }else {
                //提示错误
                $.messager.alert("来自crm",data.msg,"error");
            }
        }
    })
}

/**
 * 给修改按钮绑定点击事件
 */
function openCustomerModifyDialog() {
    //获取选中的数据
    var row = $("#dg").datagrid("getSelections");
    //判断是否只选中一条
    if(row.length==0){
        $.messager.alert("来自crm","请选中一条记录","error");
        return;
    }
    if(row.length>1){
        $.messager.alert("来自crm","只能选中一条记录","error");
        return;
    }
    //将数据填充到修改数据表格中
    $("#fm").form("load",row[0]);
    //设置表格标题
    $("#dlg").dialog("open").dialog("setTitle","修改客户信息");
}

/**
 * 给删除按钮绑定点击事件
 */
function deleteCustomer() {
    //判断是否有选中要修改的记录
    var row = $("#dg").datagrid("getSelections");
    //判断是否只选中一条
    if(row.length==0){
        $.messager.alert("来自crm","请至少选中一条记录","error");
        return;
    }
    //将选中的记录的id拼接
    var params="id=";
    for(var i=0;i<row.length;i++){
        if(i<row.length-1){
            params += row[i].id+"&id=";
        }else{
            params += row[i].id;
        }
    }
    //提示用户是否要删除所选中的记录
    $.messager.confirm("来自crm","你确定要删除所选中的内容吗？",function (r) {
        if(r){
            $.ajax({
                url:ctx+"/customer/delete",
                data:params,
                dataType:"json",
                success:function (data) {
                    if(data.code==200){
                        $.messager.alert("来自crm",data.msg,"info");
                        //刷新数据
                        searchCustomer()
                    }else{
                        $.messager.alert("来自crm",data.msg,"error");
                    }
                }
            });
        }
    })
}
function openCustomerOtherInfo(title,type) {
    //判断是否有选中要修改的记录
    var row = $("#dg").datagrid("getSelections");
    //判断是否只选中一条
    if(row.length==0){
        $.messager.alert("来自crm","请选中一个客户","error");
        return;
    }
    if(row.length>1){
        $.messager.alert("来自crm","只能选中一个客户","error");
        return;
    }
    //打开相应的视图
    window.parent.openTab(title,ctx+"/customer/openCustomerOtherInfo/"+type+"/"+row[0].id)
}