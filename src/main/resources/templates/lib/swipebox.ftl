<#macro swipebox imgList=[]>
<#if imgList??>
<div id="swb">
<#list imgList as img>
    <div class="box">
        <a href="${appConfig.fileStorageUrl}${img.getAbsolutePath()}" class="swipebox"> 
            <img src="${appConfig.fileStorageUrl}${img.thumb.getAbsolutePath()}" width=${img.thumb.width} height=${img.thumb.height}>
        </a>
    </div>
</#list>
</div>
<div class="clearfix"></div>
<script>
    $(".swipebox").swipebox({
        useCSS : true, // false will force the use of jQuery for animations
        hideBarsDelay : 3000 // 0 to always show caption and action bar
    });
</script>
</#if>
</#macro>