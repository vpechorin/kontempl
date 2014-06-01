<#macro treeTableBranch depth=7 parent=[] children=[]>
<#list children as rootPage>
    <#assign ttClassname="file">
    <#if rootPage.hasChildren()><#assign ttClassname="folder"></#if>
    <tr data-tt-id="${rootPage.data.id?c}" data-tt-parent-id="${parent.data.id?c}">
        <td><span class="${ttClassname}"><a href="${appConfig.appPath}/page/${rootPage.data.id?c}/edit">${rootPage.data.title}</a></span></td>
        <td>
            <a href="${appConfig.appPath}/page/${rootPage.data.id?c}/move?direction=up"  title="Move page up"><i class="icon-level-up"></i></a>
            <#if rootPage.data.publicPage>Public<#else>Hidden</#if>
            <a href="${appConfig.appPath}/page/${rootPage.data.id?c}/move?direction=down"  title="Move page down"><i class="icon-level-down"></i></a>
        </td>
        <td><#if rootPage.data.placeholder>Placeholder<#else><a href="/pv/${rootPage.data.name}">/pv/${rootPage.data.name}</a></#if></td>
        <td><a href="${appConfig.appPath}/page/add?parentId=${rootPage.data.id?c}" class="btn btn-mini btn-inverse"><i class="icon-plus icon-white"></i> add sub-page</a></td>
    </tr>
    <#if rootPage.hasChildren()>
        <@treeTableBranch depth=10 parent=rootPage children=rootPage.children/>
    </#if>
</#list>
</#macro>


