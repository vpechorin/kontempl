<#macro pagenav pagetree={} pageName="home" opts...>
    <div class="nav-collapse collapse">
        <ul class="nav">
            <li <#if pageName == "home">class=active</#if>><a  href="${appConfig.appPath}/">Home</a></li>
        <#if pagetree.children??>    
        <#list pagetree.children as rootPage>
            <#assign elementClass="">
            <#if pageName == rootPage.data.name><#assign elementClass="active"></#if>
            <#if rootPage.hasChildren()>
            <li class="dropdown">
                <a href="${appConfig.appPath}/pv/${rootPage.data.name}" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-target="#">${rootPage.data.title}<b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <#list rootPage.children as childPage>
                        <#if childPage.hasChildren()>
                        <@dropdownSubmenu branch=childPage/>
                        <#else>
                        <li><a  href="${appConfig.appPath}/pv/${childPage.data.name}">${childPage.data.title}</a></li>
                        </#if>
                    </#list>
                </ul>
            </li>    
            <#else>
            <li class="${elementClass}">
                <a  href="${appConfig.appPath}/pv/${rootPage.data.name}">${rootPage.data.title}</a>
            </li>
            </#if>
        </#list>
        </#if>
        <#if user??>
            <#if opts.pageedit??><li><a href="${opts.pageedit}"><i class="icon-edit icon-white"> </i> Edit this page</a></li></#if>
        </#if>
        </ul>
    </div>
</#macro>