<#include "/lib/simplepage.ftl"/>
<#import "/spring.ftl" as spring />
<@page
    title="Edit page element: " + pageelement.name!""
>
    <h2><#if pageelement.id??>Edit<#else>Add</#if> Page Element</h2>
    
    <form class="form-horizontal" method="POST" action="${appConfig.appPath}/page/${pagenode.id!"0"}/edit-element">
        <div class="control-group">
                <label class="control-label" for="name">Element Name</label>
                <div class="controls">
                <@spring.formSingleSelect "pageelement.name", types/>
                </div>
         </div>
        <div class="control-group">
                <label class="control-label" for="customName">or Element Custom Name</label>
                <div class="controls">
                <@spring.formInput "pageelement.customName" "class='span8'"/>
                </div>
         </div>
        <div class="control-group">
            <label class="control-label" for="body">Content</label>
            <div class="controls">
            <@spring.formTextarea "pageelement.body" "class='span12'  style='height: 500px;'"/>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <button type="submit" class="btn btn-primary">Save</button>
            </div>
        </div>
        <#if pageelement.id??>
        <input type=hidden name=id value="${pageelement.id!0}">
        </#if>
    </form>
</@page>