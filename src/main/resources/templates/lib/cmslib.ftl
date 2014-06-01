<#function searchUri q  prefix="/reviews/">
<#return prefix + "search.html?q=" + q?url>
</#function>

<#macro pageselector totalItems=0 offset=0 pagesize=20 link="">
<#assign pages = (totalItems/pagesize)?ceiling>
<#assign thisPage = (offset/pagesize+1)?ceiling>
<#assign pagesJoinChar="?">
<#if link?contains("?")><#assign pagesJoinChar="&"></#if>
<#if (pages > 1)>
    <div id=pagination>
    pages: 
    <#if (thisPage > 1)>
        <#assign prevPageOffset=offset-pagesize>
        <#assign offsetParam="">
        <#if (prevPageOffset > 0)><#assign offsetParam=pagesJoinChar + "offset="+prevPageOffset?c></#if>
        <a class=prevNextActiveBtn href="${link}${offsetParam}">&lt;&lt; Prev</a>
    </#if>
    <#list 1..pages as p>
        <#assign pageOffset = (p - 1) * pagesize>
        <#if p == thisPage>
            <span class=activePage>${p}</span>
        <#else>
            <#assign offsetParam="">
            <#if (pageOffset > 0)><#assign offsetParam=pagesJoinChar + "offset="+pageOffset?c></#if>
            <a href="${link}${offsetParam}">${p}</a>
        </#if>
    </#list>
    <#if (thisPage < pages)>
        <a class=prevNextActiveBtn href="${link}${pagesJoinChar}offset=${offset+pagesize}">Next &gt;&gt;</a>
    </#if>
    </div>
</#if>
</#macro>

<#function w3ctime dt>
<#assign d = dt?datetime?string("yyyy-MM-dd'T'HH:mm:ss")>
<#assign z = dt?datetime?string("Z")>
<#assign zh = z?substring(0,3)>
<#assign zm = z?substring(3,5)>
<#assign zn = zh + ":" + zm >
<#return d + zn>
</#function>

<#function nowYear>
<#assign d = .now>
<#assign y = d?string("yyyy")>
<#return y>
</#function>

<#function categoryTrace thisCat catParents=[] divider="/">
    <#assign path="">
    <#assign lastParentName="">
    <#list catParents as cat>
        <#assign lastParentName = cat.name>
    </#list>
    <#if (lastParentName?length > 0)>
    <#assign path = divider + lastParentName>
    </#if>
    <#assign path = path + divider + thisCat.name + divider>
    <#return path>
</#function>

<#function getPageElementContent thePage={} peKey="default">
    <#assign content="">
    <#if (peKey?length > 0)>
        <#if thePage.pageElements??>
            <#if thePage.pageElements[peKey]??>
                <#attempt>
                    <#assign content=thePage.pageElements[peKey].body!""/>
                <#recover>
                </#attempt>
             </#if>
          </#if>
    </#if>
    <#return content>
</#function>

<#macro insertPageElement thePage={} peKey="defaultKey">
<#if (peKey?length > 0)>
<#if thePage.pageElements??>
<#if thePage.pageElements[peKey]??>
<#attempt>
<#assign templateSource = thePage.pageElements[peKey].body!"">
<#assign inlineTemplate = templateSource?interpret>
<@inlineTemplate />
<#recover>
<#if thePage??>
${thePage.pageElements[peKey].body!""}
</#if>
</#attempt>
</#if>
</#if>
</#if>
</#macro>

<#macro includePageElement thePage={} peKey="defaultKey" attr="body">
<#if (peKey?length > 0)><#if thePage??><#if thePage.pageElements??><#if thePage.pageElements[peKey]??>
<#assign el = thePage.pageElements[peKey]>
${el[attr]!""}
</#if></#if></#if></#if>
</#macro>