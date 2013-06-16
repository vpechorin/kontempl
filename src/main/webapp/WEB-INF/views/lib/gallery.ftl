<#macro gallery panelWidth=486 panelHeight=292 thumbWidth=90 thumbHeight=54 frameGap=2>
<#if pagenodeImages??>
<#assign imgList=pagenodeImages>
    <ul id="gvGallery">
<#list imgList as img>
        <li><img src="${appConfig.fileStorageUrl}${img.getAbsolutePath()}" data-frame="${appConfig.fileStorageUrl}${img.thumb.getAbsolutePath()}"></li>
</#list>
    </ul>
<script>
$(document).ready(function () {
    $('#gvGallery').galleryView({
        panel_width: ${panelWidth},
        panel_height: ${panelHeight},
        frame_width: ${thumbWidth},
        frame_height: ${thumbHeight},
        panel_scale: 'fit',
        transition_speed: 250,
        easing: 'swing',
        panel_animation: 'slide',
        filmstrip_position: "right",
        frame_scale: "fit",
        frame_gap: ${frameGap},
        show_infobar: true,
        infobar_opacity: 0.5
    });   
});
</script>
</#if>
</#macro>