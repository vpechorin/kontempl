<#include "/pages/mainpage.ftl"/>
<#import "/spring.ftl" as spring />
<#import "/lib/cmslib.ftl" as cmslib/>

<@page
    title="Internal Server Error"
    metaDescription=""
>

<div class=genericPageContainer>

<div style="padding:30px;">
    <p><h1 style="font-size: 20px;">There was a problem serving the requested page</h1></p>
    <p><b>Now you're wondering, &quot;What do i do now!?!&quot;, Well...</b>
    <p>
    <ul style="list-style-type:circle;margin-left:0em; padding-left:0.2em; margin-bottom:1em;">
        <li style="list-style-type: circle">you can try refreshing the page, the problem may be temporary</li>
        <li style="list-style-type: circle">if you entered the URL in by hand, double check that it is correct</li>
        <li style="list-style-type: circle">we've been notified of the problem and will do our best to make sure it doesn't happen again!</li>
    </ul>
    </p>
 </div>
   
</div>

</@page>