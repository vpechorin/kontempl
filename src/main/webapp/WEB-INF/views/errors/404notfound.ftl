<#include "/pages/mainpage.ftl"/>
<#import "/spring.ftl" as spring />
<#import "/lib/cmslib.ftl" as cmslib/>

<@page
    title="Error: Resource not found"
    metaDescription=""
>
<div class=genericPageContainer>

<div style="padding:30px;">
    <p><h1 style="font-size: 40px;">Resource not found</h1></p>
    <p>Oops! Looks like you followed a bad link.
    <br>
    <br>
<p>Sorry, the page you requested was not found.
<p>Please check the URL for proper spelling and capitalization. 
<p>If you're having trouble locating a destination on <a href="/">${appConfig.domainname}</a>, <br>try visiting the <b><a href="/">Home page</a></b>. 
 </div>
   
</div>
</@page>