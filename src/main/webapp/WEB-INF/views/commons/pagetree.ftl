<#include "/lib/simplepage.ftl"/>
<#import "/spring.ftl" as spring />
<#include "/lib/pageTreeBranch.ftl"/>
<#include "/lib/pageelementsleaves.ftl"/>
<#include "/lib/treeTableBranch.ftl"/>
<@page
    title="Index"
>
<h3>index</h3>

<a class="btn btn-mini btn-primary" href="${appConfig.appPath}/page/add?parentId=0">Add a new root page</a>

<div id=pageTreeContainer>
<table id=treeTab>
<#list pagetree.children as rootPage>
    <#assign ttClassname="file">
    <#if rootPage.hasChildren()><#assign ttClassname="folder"></#if>
    <tr data-tt-id="${rootPage.data.id?c}">
        <td><span class="${ttClassname}"><a href="${appConfig.appPath}/page/edit/${rootPage.data.id?c}">${rootPage.data.title}</a></span></td>
        <td>
            <a href="${appConfig.appPath}/page/move/${rootPage.data.id?c}?direction=up" title="Move page up"><i class="icon-level-up"></i></a>
            <#if rootPage.data.publicPage>Public<#else>Hidden</#if>
            <a href="${appConfig.appPath}/page/move/${rootPage.data.id?c}?direction=down"  title="Move page down"><i class="icon-level-down"></i></a>
        </td>
        <td><#if rootPage.data.placeholder>Placeholder<#else><a href="/pv/${rootPage.data.name}">/pv/${rootPage.data.name}</a></#if></td>
        <td><a href="${appConfig.appPath}/page/add?parentId=${rootPage.data.id?c}" class="btn btn-mini btn-inverse"><i class="icon-plus icon-white"></i>add sub-page</a></td>
    </tr>
    <#if rootPage.hasChildren()>
        <@treeTableBranch depth=10 parent=rootPage children=rootPage.children/>
    </#if>
</#list>
</table>
</div>

<script>
    $("#treeTab").treetable({ expandable: true, initialState: "expanded" });
</script>

</@page>