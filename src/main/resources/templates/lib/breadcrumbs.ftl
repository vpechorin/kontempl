<#macro breadcrumbs>
<#if bclist??>
    <nav class="breadcrumbs">
<#list bclist as bc>
    <#assign liClass="">
    <#if txt?? && txt.id??><#if bc.id == txt.id><#assign liClass=" class='current'"></#if></#if>
    <#if (bc.name == appConfig.homePage)>
        <li${liClass}><a href="${appConfig.appPath}${appConfig.startPage}">${bc.title}</a></li>
    <#else>
        <li${liClass}>
            <#if bc.placeholder>${bc.title}<#else><a href="${appConfig.appPath}/pv/${bc.name}">${bc.title}</a></#if>
        </li>
    </#if>    
</#list>
    </nav>
</#if>
</#macro>