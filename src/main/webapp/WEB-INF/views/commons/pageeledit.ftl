<#include "/lib/simplepage.ftl"/>
<#import "/spring.ftl" as spring />
<@page
    title="Edit page element: " + pageelement.name!""
>
    <h2><#if pageelement.id??>Edit<#else>Add</#if> Page Element</h2>
    
    <form class="cmxform" method="POST" action="${appConfig.appPath}/page/${pagenode.id!"0"}/edit-element">
        <fieldset>
        <#if pageelement.id??>
        <input type=hidden name=id value="${pageelement.id!0}">
        </#if>
        <ol>
            <li>
                <label for=title>name</label>
                <span class=textlinesmall>
                    <@spring.formSingleSelect "pageelement.name", types/>
                    <@spring.formInput "pageelement.customName"/>
                </span>
            </li>
            <li>
                <label for=body>content</label>
                <@spring.formTextarea "pageelement.body"/>
            </li>
            <li>
                <button type="submit" class="btn btn-primary">Save</button>
            </li>
        </ol>
        </fieldset>
    </form>
</@page>