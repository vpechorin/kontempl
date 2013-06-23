<#macro orbit timer_speed=10000 pause_on_start=true>
<#assign imgAltSuffix =  cmslib.getPageElementContent(txt, "sitename")>
<#if pagenodeImages??>
<#if (pagenodeImages?size < 2)>
<#list pagenodeImages as img>
<a class="swipebox" href="${appConfig.fileStorageUrl}${img.getAbsolutePath()}"><img src="${appConfig.fileStorageUrl}${img.getAbsolutePath()}" width=656 height=437 alt="${txt.title} - ${img.name} - ${imgAltSuffix}"></a>
</#list>
<script>
$(document).ready(function() {
$(".swipebox").swipebox({hideBarsDelay : 0});
});
</script>    
<#else>
<div class="slideshow-wrapper">
<div class="preloader"></div>
<ul id="featured2" data-orbit data-options="timer_speed:${timer_speed?c}; bullets: <#if (pagenodeImages?size > 1)>true<#else>false</#if>;">
    <#list pagenodeImages as img>
    <li><a class="swipebox" href="${appConfig.fileStorageUrl}${img.getAbsolutePath()}"><img src="${appConfig.fileStorageUrl}${img.getAbsolutePath()}" width=656 height=437 alt="${txt.title} - ${img.name} - ${imgAltSuffix}"></a></li>
    </#list>
</ul>
</div>
<script>
$(document).foundation();
$(document).ready(function() {
$(".swipebox").swipebox({hideBarsDelay : 0});
});
<#if pause_on_start>
$(document).ready(function() {
    $("ul.orbit-slides-container").each(function() {
        $(this).siblings('.orbit-timer').click(); //Clicks pause
    });
});
</#if>
</script>    
</#if>
</#if>
</#macro>