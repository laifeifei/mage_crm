function searchRoles() {
    $("#dg").datagrid("load",{
        roleName:$("#roleName").val()
    })
}

function openAddRoleDialog(){
    $("#dlg").dialog("open");
}

function closeDialog() {
    $("#dlg").dialog("close");
}
function saveOrUpdateRole(){
    var id=$("#id").val();
    var url=ctx+"/role/insert";
    if(!isEmpty(id)){
        url=ctx+"/role/update";
    }
    $("#fm").form("submit",  {
        url:url,
        onSubmit:function () {
            return $("#fm").form("validate");
        },
        success:function (data) {
            data = JSON.parse(data);
            if(data.code==200){
                $.messager.alert("来自crm系统",data.msg,"info")
                $("#fm").form("clear");
                closeDialog();
                searchRoles();
            }else{
                $.messager.alert("来自crm系统",data.msg,"info")
            }
        }
    });
}
function openModifyRoleDialog() {
    var rows= $("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选中待更新记录!","info");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","只能选择一条记录执行更新!","info");
        return;
    }
    $("#fm").form("load",rows[0]);
    $("#dlg").dialog("open").dialog("setTitle","修改用户记录");
}

/**
 * 删除角色
 */
function deleteRole() {
    var rows=$("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选中待删除记录!","info");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","不能同时删除多条记录!","info");
        return;
    }
    $.messager.confirm("来自crm","确定删除选中的记录吗?",function(r){
        if(r){
            $.ajax({
                type:"post",
                url:ctx+"/role/delete",
                data:"id=" + rows[0].id,
                dataType:"json",
                success:function(data){
                    $.messager.alert("来自crm",data.msg,"info");
                    if(data.code==200){
                        closeDialog();
                        searchRoles();
                    }
                }
            });
        }
    });
}

/*------------------------关联权限-------------------------*/
/*
打开关联权限对话框
 */
function openRelatePermissionDlg(){
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选择角色进行授权","error");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","只能选择一个角色进行授权","error");
        return;
    }
    $("#rid").val(rows[0].id);
    //加载ztree
    loadModuleData();
    //打开对话框
    $("#dlg02").dialog("open");
}

/**
 * 给关闭按钮绑定点击事件
 */
function closeDialog02() {
    $("#dlg02").dialog("close");
}

/**
 * 加载ztree
 */
var zTreeObj;
function loadModuleData() {
    //提交数据
    $.ajax({
        type:"post",
        url:ctx+"/module/queryAllsModuleDtos",
        data:"rid="+$("#rid").val(),
        dataType:"json",
        success:function (data) {
            // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
            var setting = {
                check: {
                    enable: true
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                callback: {
                    onCheck: zTreeOnCheck
                }
            };
            // zTree 的数据属性，深入使用请参考 API 文档（zTreeNode 节点数据详解）
            var zNodes = data;
            zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        }
    });
}
function zTreeOnCheck() {
    var znodes=zTreeObj.getCheckedNodes(true);
    var moduleIds="moduleIds=";
    if(znodes.length>0){
        for(var i=0;i<znodes.length;i++){
            if(i<=znodes.length-2){
                moduleIds=moduleIds+znodes[i].id+"&moduleIds=";
            }else{
                moduleIds=moduleIds+znodes[i].id;
            }
        }
    }
    $("#moduleIds").val(moduleIds);
}
/*
给授权按钮绑定点击事件
 */
function addPermission() {
    $.ajax({
        type:"post",
        url:ctx+"/role/addPermission",
        data:"rid="+$("#rid").val()+"&"+$("#moduleIds").val(), //将角色id和模块id传给后台
        dataType:"json",
        success:function(data){
            if(data.code==200){
                $.messager.alert("来自crm",data.msg,"info");
                $("#moduleIds").val("");
                $("#rid").val("");
                closeDialog02();
            }else{
                $.messager.alert("来自crm",data.msg,"info");
            }
        }
    });
}

