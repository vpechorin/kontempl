<#include "/lib/simplepage.ftl"/>
<#import "/spring.ftl" as spring />

<@page
    title="Edit user email/password credential"
>
<#assign htmlEscape = true in spring>

<div id="formContainer">
    <form id="userPasswordForm" class="cmxform" method="POST" action="">
    <fieldset>
        <legend>Edit credential #${credential.id}</legend>
        <p>Mandatory fields marked<em>*</em></p>        
        <@spring.bind "credential.*"/>
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
                <label for=email>Email<em>*</em></label>
                <@spring.formInput "credential.email", "required"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li>
            <li>
                <label for=password>Password<em>*</em></label>
                <@spring.formInput "credential.password", "required"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li>
            <li>
                <label for=passwordConfirm>Confirm password<em>*</em></label>
                <@spring.formInput "credential.passwordConfirm", "required"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li>             
            <li>
                <@spring.formHiddenInput "credential.id"/>
                <button type="submit">Submit</button>
            </li>
        </ol>
    </fieldset>
    </form>
</div>

</@page>