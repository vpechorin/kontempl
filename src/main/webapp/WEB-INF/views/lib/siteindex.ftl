<#macro siteindex>
    <ul>
        <li class=rootPage><a class=rootLink href="${appConfig.appPath}/" id="home">Home</a></li>
        <#list pageTree.findPageNode("home").children as el>
        <li class=rootPage>
            <#if el.data.placeholder>${el.data.title}<#else>
            <a  class=rootLink href="${appConfig.appPath}/pv/${el.data.name}">${el.data.title}</a></#if>
            <#if el.hasChildren()><@siteindexBranch thisBranch=el/></#if>
        </li>
        </#list>
    </ul>
    <div class=cf></div>
</#macro>
<#macro siteindexBranch thisBranch>
        <ul>
        <#list thisBranch.children as el>
            <li><#if el.data.placeholder>${el.data.title}<#else><a  href="${appConfig.appPath}/pv/${el.data.name}">${el.data.title}</a></#if>
                <#if el.hasChildren()><@siteindexBranch thisBranch=el/></#if>
            </li>
         </#list>
        </ul>
</#macro>