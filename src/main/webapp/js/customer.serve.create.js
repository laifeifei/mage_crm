/*
给保存按钮绑定点击事件
 */
function saveCustomerService() {
    $("#fm").form("submit",{
        url:ctx+"/customer_serve/insert",
        onSubmit:function (params) {
            params.createPeople = $.cookie("trueName");
            return $("#fm").form("validate")
        },
        success:function (data) {
            data = JSON.parse(data);
            if(data.code == 200){
                $.messager.alert("crm",data.msg,"info");
                $("#fm").form("clear");
            }else {
                $.messager.alert("crm",data.msg,"error");
            }
        }
    })
}
function resetValue() {
    $("#fm").form("clear");
}