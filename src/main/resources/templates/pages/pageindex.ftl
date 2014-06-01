<#include "/pages/mainpage.ftl"/>
<#import "/spring.ftl" as spring />
<#import "/lib/cmslib.ftl" as cmslib/>
<#include "/lib/siteindex.ftl"/>
<@page
    title="Site map" + " - " + cmslib.getPageElementContent(txt, "sitename")
    canonical = "http://" + appConfig.domainname + appConfig.appPath + "/do/index"
>
    <nav class="breadcrumbs">
        <li><a href="${appConfig.appPath}${appConfig.startPage}">Home</a></li>
        <li class=current><a href="${appConfig.appPath}/do/index">Site map</a></li>
    </nav>

<div class="pageTitle">
    <h2>Site map</h2>
</div>

<div id=siteindex>
<@siteindex />
</div>

</@page>