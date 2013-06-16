<#include "/lib/simplepage.ftl"/>
<#import "/spring.ftl" as spring />

<@page
    title="Add a new user"
>
<#assign htmlEscape = true in spring>

<div id="formContainer">
    <form id="userForm" class="cmxform" method="POST" action="">
    <fieldset>
        <legend>Add a new user</legend>
        <p>Mandatory fields marked<em>*</em></p>        
        <@spring.bind "userform.*"/>
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
                <@spring.formInput "userform.name", "required"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li>
            <li>
                <label for=email>Email<em>*</em></label>
                <@spring.formInput "userform.email", "required", "email"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li>
            <li>
                <label for=active>Active</label>
                <@spring.formCheckbox "userform.active"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li>
            <li>
                <label for=active>Locked</label>
                <@spring.formCheckbox "userform.locked"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li>            
            <li>
                <label for=password>Password</label>
                <@spring.formInput "userform.password"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li>
            <li>
                <label for=passwordConfirm>Confirm password</label>
                <@spring.formInput "userform.passwordConfirm"/>
                <@spring.showErrors "<br>", "fieldError" />
            </li> 
            <li>
                <label for=suburb>Role<em>*</em></label>
                <@spring.formSingleSelect "userform.role", roleMap, "required"/>
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