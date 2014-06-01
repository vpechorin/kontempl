<#macro dropdownSubmenu branch>
 <li class="dropdown-submenu">
        <a href="${appConfig.appPath}/pv/${branch.data.name}" data-toggle="dropdown" data-target="#">${branch.data.title}</a>
        <ul class="dropdown-menu">
            <#list branch.children as childPage>
                <#if childPage.hasChildren()>
                    <@dropdownSubmenu branch=childPage/>
                <#else>
                <li><a  tabindex="-1" href="${appConfig.appPath}/pv/${childPage.data.name}">${childPage.data.title}</a></li>
                </#if>
            </#list>
        </ul>
 </li>
</#macro>