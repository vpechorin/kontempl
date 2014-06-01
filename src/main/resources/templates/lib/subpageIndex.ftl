<#macro subpageIndex gridColumns=2 mainClass="subpageIndex">
<#assign branch=pageTree.findPageNode(txt.id)>
<#if branch.hasChildren()>
<#assign rowIdx = 1>
<div class="${mainClass}"><!-- subpageIndex start -->
<#list branch.children?chunk(gridColumns) as row>
<ul class="large-block-grid-${gridColumns}"><!-- row ${rowIdx} start -->
    <#list row as cell><#assign cat=cell>
    <li>
        <span class="categoryPageHdr"><a href="${appConfig.appPath}/pv/${cat.data.name}"><i class="icon-folder-open"></i> ${cat.data.title?upper_case}</a></span>
        <#if cat.hasChildren()>
        <ul>
            <#list cat.children as el>
            <li  class="itemPageHdr"><a href="${appConfig.appPath}/pv/${el.data.name}">${el.data.title}</a></li>
            </#list>
        </ul>
        </#if>
    </li>
    </#list>
</ul><!-- row ${rowIdx} end -->   

<#assign rowIdx = rowIdx + 1>
</#list>
</div><!-- subpageIndex end -->
</#if>
</#macro>