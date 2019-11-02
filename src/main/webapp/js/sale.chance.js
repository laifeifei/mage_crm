/**
 * 设置分配状态
 */
function formatterState(val){
    if(val==0){
        return "未分配";
    }else if(val==1){
        return "已分配";
    }else{
        return "未定义";
    }
}



/**
 * 查询数据
 */
function searchSaleChances() {
    //获取查询条件
    $("#dg").datagrid("load",{
        createMan:$("#createMan").val(),
        customerName:$("#customerName").val(),
        createDate:$("#createDate").datebox("getValue"),
        state:$("#state").combobox("getValue")
    })
}

/**
 * 给添加按钮绑定点击事件
 */
function openAddAccountDialog(){
    //获取选项框内容
    $("#dlg").dialog("setTitle","添加营销机会记录");
    //清空表单中的内容
    $("#fm").form("clear");
    //打开添加框
    $("#dlg").dialog("open");
}

/**
 * 给取消按钮绑定点击事件
 */
function closeDialog(){
    $("#dlg").dialog("close");
}

/**
 * 给保存按钮绑定点击事件
 */
function saveAccount(){
    //根据id的值是否为空来判断是添加还是修改 id=null 添加，id!=null 修改
    //获取id的值
    var id = $("#id").val();
    //设置url的默认值，修改
    var url = ctx+"/sale_chance/update";
    if(isEmpty(id)){//id为空，添加
        url = ctx+"/sale_chance/insert";
    }
    $("#fm").form("submit",{
        url:url,
        onSubmit:function(params){
            //将当前用户设置为创建人
             params.createMan = $.cookie("trueName");
             return $("#fm").form("validate");
        },
        //回调函数
        success:function (data) {
            data = JSON.parse(data);
            //成功
            if(data.code == 200){
                //弹出提示框
                $.messager.alert("来自crm系统",data.msg,"info");
                //关闭
                closeDialog();
                //刷新数据表格
                searchSaleChances();
            }else{
                //失败
                //弹出提示框
                $.messager.alert("来自crm系统",data.msg,"error");
            }
        }
    })
}

/**
 * 给修改按钮绑定点击事件
 */
function openModifyAccountDialog(){
    //获取勾选的内容
    var rows = $("#dg").datagrid("getSelections");
    //判断是否有选中记录
    if(rows.length==0){
        $.messager.alert("来自crm","请选中一条记录","error");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","最多选中一条记录","error");
        return;
    }
    //获取选中的表单中的数据
    $("#fm").form('load',rows[0]);
    //更改修改框的标题
    $("#dlg").dialog("open").dialog("setTitle","修改营销机会记录");
}

/**
 * 给删除按钮绑定点击事件
 */
function deleteAccount() {
    //获取选中内容
    var rows = $("#dg").datagrid("getSelections");
    //判断是否未选中
    if(rows.length==0){
        //提示用户至少选中一条记录
        $.messager.alert("来自crm","至少选中一条要删除的记录","error");
        return;
    }
    //将获取到的id拼接
    var params = "id=";
    for(var i=0;i<rows.length;i++){
        if(i<rows.length-1){
            params += rows[i].id+"&id=";
        }else{
            params += rows[i].id;
        }
    }
    //提示是否要删除所选中的记录
    $.messager.confirm("来自crm系统","你确定要删除所选中的记录吗？",function (r) {
        if(r){
            //ajax提交数据
            $.ajax({
                url:ctx+"/sale_chance/delete",
                data:params,
                //回调函数
                success:function (data) {
                    if(data.code == 200){
                        //成功
                        $.messager.alert("来自crm",data.msg,"info");
                        //查询数据
                        searchSaleChances();
                    }else{
                        //失败
                        $.messager.alert("来自crm",data.msg,"error");
                    }
                }
            })
        }
    })
}































