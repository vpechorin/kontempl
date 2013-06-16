<#macro orbit timer_speed=10000 pause_on_start=true>
<#if pagenodeImages??>
<div class="slideshow-wrapper">
    <div class="preloader"></div>
    <ul id="slidesContainer" data-orbit data-options="timer_speed:${timer_speed?c}; bullets: <#if (pagenodeImages?size > 1)>true<#else>false</#if>;">
        <#list pagenodeImages as img>
        <li><a class="swipebox" href="${appConfig.fileStorageUrl}${img.getAbsolutePath()}"><img src="${appConfig.fileStorageUrl}${img.getAbsolutePath()}"></a></li>
        </#list>
    </ul>
</div>
<script>
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
<#if (pagenodeImages?size < 2)>
$(document).ready(function() {
    $("ul.orbit-slides-container").each(function() {
        $(this).siblings("a.orbit-prev, a.orbit-next").hide(); //Hides controls on load
    });
    $(".orbit-slide-number").hide();
    $(".orbit-progress").hide();
});
</#if>
</script>    
</#if>
</#macro>