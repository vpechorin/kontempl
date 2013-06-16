        <table id="uploaded-files" class="table table-condensed">
            <tr>
                <th>Main Image</th>
                <th>Up/Down</th>
                <th>File Name</th>
                <th>File Size</th>
                <th>File Type</th>
                <th>Dimensions</th>
                <th>Preview</th>
                <th>Delete</th>
            </tr>
            <#if pagenodeImages??>
            <#list pagenodeImages as img>
                <tr>
                <td align=center>
                    <#if img.mainImage>
                        <i class="icon-star"></i>
                    <#else>
                        <a href="#" onClick="onSetMainImgClick(${img.id});"><i class="icon-star-empty"></i></a>
                    </#if>
                </td>
                <td align=center>
                    <a href="#" onClick="moveImage(${img.id},'UP');"><i class="icon-arrow-up"></i></a>&nbsp;&nbsp;
                    <a href="#" onClick="moveImage(${img.id},'DOWN');"><i class="icon-arrow-down"></i></a>
                </td>
                <td>${img.name}</td>
                <td>${img.getHFileSize()}</td>
                <td>${img.contentType}</td>
                <td>${img.width?c}x${img.height?c}</td>
                <td>
                    <#if img.thumb??>
                        <a target="_blank" href="${appConfig.fileStorageUrl}${img.getAbsolutePath()}"><img width=${img.thumb.width} height=${img.thumb.height} src="${appConfig.fileStorageUrl}${img.thumb.getAbsolutePath()}"></a>
                    </#if>
                </td>
                <td>
                    <a class="btn btn-inverse btn-small" href="#" onClick="onDeleteBtnClick(${img.id});"><i class="icon-trash icon-white"></i> Delete</a>
                </td>
                </tr>
            </#list>
            <#else>
            <tr><td colspan=6>No images uploaded</td></tr>
            </#if>
        </table>
