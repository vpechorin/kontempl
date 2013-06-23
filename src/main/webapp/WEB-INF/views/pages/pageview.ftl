<#include "/pages/mainpage.ftl"/>
<#import "/spring.ftl" as spring />
<#import "/lib/cmslib.ftl" as cmslib/>
<#assign canonical = "http://${appConfig.domainname}${appConfig.appPath}/pv/${txt.name}">
<#assign pageTitle = cmslib.getPageElementContent(txt, "sitename")>
<#if txt.title?has_content>
<#assign pageTitle = txt.title?xhtml + " - " + cmslib.getPageElementContent(txt, "sitename")>
</#if>
<#if txt.name == "home">
<#assign pageTitle = cmslib.getPageElementContent(txt, "sitename")>
<#assign canonical = "http://${appConfig.domainname}${appConfig.appPath}">
</#if>
<@page
    title=txt.title!"" + " - " + cmslib.getPageElementContent(txt, "sitename")
    metaDescription=txt.getTextDescription(300)!""
    selectedPage = txt
    canonical = canonical
    ogproperties = ogproperties
    pageedit=appConfig.appPath + "/page/" + txt.id?c + "/edit"
>
<@breadcrumbs/>
<#if (txt.hideTitle == false) && !(isError??)>
    <#if txt??><#if txt.title??><h2>${txt.title}</h2></#if></#if>
</#if>
<#if txt.body?has_content><#assign pageBody=txt.body?interpret><@pageBody /></#if>

</@page>