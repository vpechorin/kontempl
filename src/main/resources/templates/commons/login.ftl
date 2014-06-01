<#include "/lib/simplepage.ftl"/>
<#import "/spring.ftl" as spring />

<@page
    title="Signon page"
>

<div class="genericPageContainer">
    <#if RequestParameters.error??>
    <#if RequestParameters.error == "auth_fail">
    <div style="padding: 10px 10px;">
    <span style="color: #F00; font-weight: bold;">
        <#if SPRING_SECURITY_LAST_EXCEPTION??>
            ${SPRING_SECURITY_LAST_EXCEPTION.message}
        <#else>
            Account authentication failed based on the information provided.
            <br>Please verify and try again. 
            <br><i>(Note: for your added security passwords are case-sensitive.)</i> 
        </#if>
    </span>
    </div>
    </#if>
    </#if>

<form action="${appConfig.appPath}/do/login" method=POST>
    <div>
        <label for="email">Email</label><input type="text" id="email" name="email" required><br/>
        <label for="password">Password</label><input type="password" id="password" name="password" required><br/>
        <input type="submit" value="Login!">
    </div>
    <div style="margin-top: 2px;">
        <input type=checkbox  name="_spring_security_remember_me" id="_spring_security_remember_me" value="1" tabindex=3> Keep me logged in on this computer.
    </div>
</form>
</div>
</@page>