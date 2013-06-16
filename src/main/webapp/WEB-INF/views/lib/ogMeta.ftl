<#macro ogMeta ogproperties>
<#if ogproperties??>
    <#if ogproperties.sitename?has_content><meta property="og:site_name" content="${ogproperties.sitename?chop_linebreak?xhtml}"></#if>
    <#if ogproperties.title?has_content><meta property="og:title" content="${ogproperties.title?chop_linebreak?xhtml}"></#if>
    <#if ogproperties.description?has_content><meta property="og:description" content="${ogproperties.description?chop_linebreak?xhtml}"></#if>
    <#if ogproperties.iconUrl??><meta property="og:image" content="${ogproperties.iconUrl}"></#if>
</#if>
</#macro>