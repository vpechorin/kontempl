<#macro pageTreeBranch depth=7 children=[]>
<ul>
<#list children as child>
    <li>${child.data.name}
    <#if child.data.publicPage> - [Public] - </#if>
     <a href="${appConfig.appPath}/page/${child.data.id?c}/edit">${child.data.title}</a> (${child.items})
    [ <a href="${appConfig.appPath}/page/add?parentId=${child.data.id?c}">Add a sub-page</a> ]
    [ <a href="${appConfig.appPath}/page/${child.data.id?c}/delete" class="btn btn-mini btn-danger"><i class="icon-trash icon-white"></i>Delete</a> ]
    <@pageElementsLeaves thisNode=child />

    <#if ((depth-1) > 0)>
        <#assign k = child.getNumberOfChildren()>
        <#if (k > 0)><@pageTreeBranch depth=depth-1 children=child.children/></#if>
    </#if>

    </li>
</#list>
</ul>
</#macro>


