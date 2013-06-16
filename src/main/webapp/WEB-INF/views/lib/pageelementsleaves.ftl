<#macro pageElementsLeaves thisNode>
<#if thisNode.data.pageElements??>

<#assign pagenode = thisNode.data>
<#assign elementNames = pagenode.pageElements?keys>
<#if (elementNames?size > 0)>
<div class=pageElements>Page Elements:
    <ul>
    <#list elementNames as n>
        <li>[
            <a href="${appConfig.appPath}/page/${thisNode.data.id?c}/edit-element?id=${pagenode.pageElements[n].id}">
                #${pagenode.pageElements[n].id} ${pagenode.pageElements[n].name}</a>
            ]
        </li>
    </#list>
    </ul>
</div>
</#if>

</#if>
</#macro>