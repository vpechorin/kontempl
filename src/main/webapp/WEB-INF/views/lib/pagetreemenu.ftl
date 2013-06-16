<#macro pagetreemenu pagetree selectedPageId=0 >
            <#list pagetree.children as rootPage>
            <li <#if selectedPageId == rootPage.data.id>class=active</#if>>
                <a  href="${appConfig.appPath}/pv/${rootPage.data.name}">${rootPage.data.title}</a>
                <#if selectedPageId != 0>
                    <#if pagetree.isTheSameBranch(selectedPageId, rootPage.data.id)>
                        <@pagebranch depth=9 children=rootPage.children selectedPageId=selectedPageId />
                    </#if>
                <#else> 
                    <#if (rootPage.getNumberOfChildren() > 0)>
                    <@pagebranch depth=1 children=rootPage.children />
                    </#if>
                </#if>
            </li>
            </#list>
</#macro>