<#include "/lib/simplepage.ftl"/>
<#import "/spring.ftl" as spring />

<@page
    title="Edit user email/password profile"
>
<#assign htmlEscape = true in spring>

<div id="formContainer">
    <form id="profileForm" class="cmxform" method="POST" action="">
    <fieldset>
        <legend>Edit profile</legend>
        <p>You can edit your email/password here. Mandatory fields marked<em>*</em></p>        
        <@spring.bind "profile.*"/>
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
                <label for=name>Name<em>*</em></label>
                <@spring.formInput "profile.name", "required"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li>
            <li>
                <label for=email>Email<em>*</em></label>
                <@spring.formInput "profile.email", "required"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li>
            <li>
                <label for=password>Password<em>*</em></label>
                <@spring.formPasswordInput "profile.password", "required"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li>
            <li>
                <label for=passwordConfirm>Confirm password<em>*</em></label>
                <@spring.formPasswordInput "profile.passwordConfirm", "required"/>
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