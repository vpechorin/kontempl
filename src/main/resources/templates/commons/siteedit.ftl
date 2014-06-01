<#include "/lib/simplepage.ftl"/>
<#import "/spring.ftl" as spring />

<@page
    title="Edit site"
>
<#assign htmlEscape = true in spring>

<div id="formContainer">
    <form id="siteForm" class="cmxform" method="POST" action="">
    <fieldset>
        <legend>Edit site</legend>
        <p>You can edit your email/password here. Mandatory fields marked<em>*</em></p>        
        <@spring.bind "siteform.*"/>
        <#if spring.status.error>
        <div class="formErrors">
            <span class=errorHdr>The following must be corrected before continuing:</span>
            <#list spring.status.errorMessages as error>
                <li>${error}</li>
            </#list>
        </div>
        </#if>
        <ol>
            <li>
                <label for=name>Site name<em>*</em></label>
                <@spring.formInput "siteform.name", "required"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li>
            <li>
                <label for=title>Title<em>*</em></label>
                <@spring.formInput "siteform.title", "required"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li>
            <li>
                <label for=domain>Domain<em>*</em></label>
                <@spring.formInput "siteform.domain", "required"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li>
            <li>
                <button type="submit">Submit</button>
            </li>
        </ol>
    </fieldset>
    </form>
</div>

</@page>