<#include "/lib/simplepage.ftl"/>
<@page
    title= "User accounts"
>

<div  class="yui3-skin-sam">
    <p><a class="btn btn-success" href="${appConfig.appPath}/user/add"><i class="icon-user icon-white"></i>Add user</a></p>
    <div id=usersContainer></div>
</div>

<script>
YUI({ debug: true }).use("datatable-mutable","panel","dd-plugin", "datasource-io", "datasource-jsonschema", "datatable-datasource", function (Y) {

    var dataSource = new Y.DataSource.IO({ source: "${appConfig.appPath}/user/list" });

    Y.log("dataSource created");
    
    dataSource.plug(Y.Plugin.DataSourceJSONSchema, {
        schema: {
           resultListLocator: "resultList",
           resultFields: [ {key:"id"}, {key:"name"}, {key:"locked"}, {key:"active"},{key:"createdDate"},{key:"userroles"}  ]
        }
    });
    
    var editCellFormatter = function(o) {
        var id = o.data.id;
        var rowIdx = o.rowIndex;
        var s = "<a class='btn btn-small btn-info' href='${appConfig.appPath}/user/" + id + "/edit'><i class='icon-user icon-white'> </i>Edit</a>";
        return s;
    };
    
    var table = new Y.DataTable({
        columns: [
            { key: "id", label: "Id", sortable: true},
            { key: "createdDate", label: "Created",sortable: true },
            { key: "name", label: "Name",sortable: true},
            { key: "userroles", label: "Role" },
            { key: "locked", label: "Locked",sortable: false },
            { key: "active", label: "Active",sortable: false },
            { label: "Edit",sortable: false, formatter: editCellFormatter, allowHTML: true  }
        ],
        summary: "User accounts",
        caption: "User accounts"
    });
    
    Y.log("dataTable created");
    
    table.plug(Y.Plugin.DataTableDataSource, { datasource: dataSource });
    
    table.render("#usersContainer");
    
    table.datasource.load();    
    Y.log("dataSource loaded - Done");

});
</script>


</@page>