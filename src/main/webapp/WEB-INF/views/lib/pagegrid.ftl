<#macro pagegrid gridColumns=3 mainClass="pagegrid">
<#assign branch=pageTree.findPageNode(txt.id)>
<#if branch.hasChildren()>
<#assign rowIdx = 1>
<div class="${mainClass}"><!-- grid start -->
<#list branch.children?chunk(gridColumns) as row>
<ul class="small-block-grid-${gridColumns} large-block-grid-${gridColumns}"><!-- row ${rowIdx} start -->
    <#list row as cell>
    <li><div class="cellImg">
        <#if cell.data.mainImage??>
            <#assign img = cell.data.mainImage>
            <a href="${appConfig.appPath}/pv/${cell.data.name}" class="th">
            <img src="${appConfig.fileStorageUrl}${img.thumb.getAbsolutePath()}">
            </a>
        <#else>
            <a href="${appConfig.appPath}/pv/${cell.data.name}" class="th">
            <img src="${appConfig.appPath}/resources/img/placeholder.png">
            </a>
        </#if>
        <div>
        <div class="cellHdr"><a href="${appConfig.appPath}/pv/${cell.data.name}">${cell.data.title?html}</a></div>
    </li>
    </#list>
</ul><!-- row ${rowIdx} end -->   

<#assign rowIdx = rowIdx + 1>
</#list>
</div><!-- grid end -->
</#if>
</#macro>