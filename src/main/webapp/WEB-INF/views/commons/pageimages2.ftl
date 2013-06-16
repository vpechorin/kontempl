<ul class="thumbnails">
<#if pagenodeImages??><#list pagenodeImages as img>
    <li class="span2">   
        <div class="thumbnail">
        <a target="_blank" href="${appConfig.fileStorageUrl}${img.getAbsolutePath()}" class="thumbnail">
        <#if img.thumb??>
        <img src="${appConfig.fileStorageUrl}${img.thumb.getAbsolutePath()}" alt="${img.thumb.name}">
        </#if>
        </a>
        <div class="caption">
            <p class="text-center">
            <#if img.mainImage><i class="icon-star"></i><#else><a href="#" onClick="onSetMainImgClick(${img.id});" title="Mark image as the MAIN image"><i class="icon-star-empty"></i></a></#if>
             <small>${img.name}</small> 
             <small>${img.getHFileSize()} [${img.width?c}x${img.height?c}]</small></p>   
            <div class="btn-group">
             <a class="btn btn-mini" href="#" onClick="moveImage(${img.id},'UP');" title="Move image higher"><i class="icon-arrow-up"></i></a>
             <a class="btn btn-mini" href="#" onClick="moveImage(${img.id},'DOWN');"  title="Move image lower"><i class="icon-arrow-down"></i></a>
             <a class="btn btn-inverse btn-mini" href="#" onClick="onDeleteBtnClick(${img.id});"  title="Delete image"><i class="icon-trash icon-white"></i></a>
            </div>
            <div class="clearfix"></div>
        </div>
        </div>
    </li>
</#list>
<#else>
<li><p>No images uploaded</p></li>
</#if>
</ul>