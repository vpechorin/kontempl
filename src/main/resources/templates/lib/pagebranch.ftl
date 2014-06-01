<#macro pagebranch depth=5 children=[] selectedPageId=0 opts...>
<ul class="nav nav-list">
<#list children as child>
    <li <#if selectedPageId == child.data.id>class="active"</#if>><a  href="${appConfig.appPath}/pv/${child.data.name}.html">${child.data.title}</a></span>
    <#if ((depth-1) > 0)>
        <#assign k = child.getNumberOfChildren()>
        <#if (k > 0)>
            <@pagebranch depth=depth-1 children=child.children selectedPageId=selectedPageId/>
        </#if>
    </#if>
    </li>
</#list>
</ul>
</#macro>


