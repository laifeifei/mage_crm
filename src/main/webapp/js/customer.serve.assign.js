function openAssignDlg() {
    //获取表格中的数据
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("crm","请选择一条记录进行分配","error");
        return ;
    }
    if(rows.length>1){
        $.messager.alert("crm","不能同时分配多条记录","error");
        return ;
    }
    //将表格中的数据填充到数据框中
    $("#fm").form("load",rows[0]);
    $("#dlg").dialog("open");
}

function closeCustomerServeDialog() {
    $("#dlg").dialog("close");

}

function addCustomerServeAssign() {
    $("#fm").form("submit",{
        url:ctx + "/customer_serve/update",
        onSubmit:function (params) {
            params.state = 2;
            return $("#fm").form("validate");
        },
        success:function (data) {
            data=JSON.parse(data);
            $.messager.alert("crm",data.msg,"info")
            if(data.code==200){
                closeCustomerServeDialog();
                $("#dg").datagrid("load");
            }
        }
    })
}