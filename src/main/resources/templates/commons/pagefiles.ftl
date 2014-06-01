<ul class="docFiles">
<#if pagenodeFiles??><#list pagenodeFiles as doc>
    <li class="span2">   
        <div class="thumbnail">
        <a target="_blank" href="${appConfig.fileStorageUrl}${doc.getAbsolutePath()}" class="thumbnail">
        ${doc.name}
        </a>
        <div class="caption">
            <p class="text-center">
             <small>${doc.name}</small> 
             <small>${doc.getHFileSize()}</small></p>   
            <div class="btn-group">
             <a class="btn btn-mini" href="#" onClick="moveFile(${doc.id?c},'UP');" title="Move file higher"><i class="icon-arrow-up"></i></a>
             <a class="btn btn-mini" href="#" onClick="moveFile(${doc.id?c},'DOWN');"  title="Move file lower"><i class="icon-arrow-down"></i></a>
             <a class="btn btn-inverse btn-mini" href="#" onClick="onFileDeleteBtnClick(${doc.id?c});"  title="Delete"><i class="icon-trash icon-white"></i></a>
            </div>
            <div class="clearfix"></div>
        </div>
        </div>
    </li>
</#list>
<#else>
<li><p>No files uploaded</p></li>
</#if>
</ul>