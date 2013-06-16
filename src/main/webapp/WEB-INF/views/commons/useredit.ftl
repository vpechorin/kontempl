<#include "/lib/simplepage.ftl"/>
<#import "/spring.ftl" as spring />

<@page
    title="Add/Edit user account"
>
<#assign htmlEscape = true in spring>

<div id="formContainer">
    <form id="userForm" class="cmxform" method="POST" action="">
    <fieldset>
        <legend>Edit user #${u.id}</legend>
        <p>Mandatory fields marked<em>*</em></p>        
        <@spring.bind "userform.*"/>
        <#if spring.status.error>
        <div class="formErrors">
            <span class=errorHdr>The following must be corrected before continuing:</span>
            <#list spring.status.errorMessages as error>
                <li>${error}</li>
            </#list>
        </div>
        </#if>
        <ol>
            <li>
                <label for=name>Name<em>*</em></label>
                <@spring.formInput "userform.name", "required"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li>
            <li>
                <label for=active>Active</label>
                <@spring.formCheckbox "userform.active"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li>
            <li>
                <label for=active>Locked</label>
                <@spring.formCheckbox "userform.locked"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li>            
            <li>
                <button type="submit">Save</button>
            </li>
        </ol>
    </fieldset>
    </form>
</div>

<div  class="yui3-skin-sam">
        <div id=credContainer></div>
</div>

<script>
YUI({ debug: true }).use("datatable-mutable","panel","dd-plugin", "datasource-io", "datasource-jsonschema", "datatable-datasource", function (Y) {

    var dataSource = new Y.DataSource.IO({ source: "${appConfig.appPath}/user/credlist?userId=${u.id}" });

    Y.log("dataSource created");
    
    dataSource.plug(Y.Plugin.DataSourceJSONSchema, {
        schema: {
           resultListLocator: "resultList",
           resultFields: [ {key:"id"}, {key:"userId"}, {key:"email"}, {key:"verified"}, {key:"active"},{ key: "createdDate"},{ key: "updatedDate"},
           {key: "authServiceType"},{key: "link"},{key: "uid"},{key: "username"},{key: "optData"} ]
        }
    });
    
    var editCellFormatter = function(o) {
        var id = o.data.id;
        var userId = o.data.userId;
        var rowIdx = o.rowIndex;
        var s = "<a class='btn btn-small btn-info' href='${appConfig.appPath}/user/" + userId + "/crededit?id=" + id + "'><i class='icon-wrench icon-white'></i> Edit</a>";
        return s;
    };
    
    var deleteCellFormatter = function(o) {
        var id = o.data.id;
        var userId = o.data.userId;
        var rowIdx = o.rowIndex;
        var s = "<a class='btn btn-small btn-danger' href='${appConfig.appPath}/user/" + userId + "/creddelete?id=" + id + "'><i class='icon-trash icon-white'></i> Delete</a>";
        return s;
    };
    
    var table = new Y.DataTable({
        columns: [
            { key: "id", label: "Id", sortable: true},
            { key: "createdDate", label: "Created",sortable: true },
            { key: "updatedDate", label: "Updated",sortable: true },
            { key: "authServiceType", label: "AuthType",sortable: true},
            { key: "uid", label: "UID",sortable: true},
            { key: "email", label: "Email" },
            { key: "verified", label: "Verified",sortable: false },
            { key: "active", label: "Active",sortable: false },
            { label: "Edit",sortable: false, formatter: editCellFormatter, allowHTML: true  },
            { label: "Delete",sortable: false, formatter: deleteCellFormatter, allowHTML: true  }
        ],
        summary: "User credentials",
        caption: "User credentials"
    });
    
    Y.log("dataTable created");
    
    table.plug(Y.Plugin.DataTableDataSource, { datasource: dataSource });
    
    table.render("#credContainer");
    
    table.datasource.load();    
    Y.log("dataSource loaded - Done");
 
});
</script>

</@page>