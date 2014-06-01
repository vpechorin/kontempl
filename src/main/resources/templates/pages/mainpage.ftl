<#macro page title="" css=[] js=[] other=[] opts...>
<#import "/spring.ftl" as spring/>
<#import "/lib/cmslib.ftl" as cmslib/>
<#include "/pages/pagenav.ftl"/>
<#include "/lib/dropdownSubmenu.ftl"/>
<#include "/lib/gallery.ftl"/>
<#include "/lib/orbit.ftl"/>
<#include "/lib/breadcrumbs.ftl"/>
<!DOCTYPE html>
<#if txt??><#else><#if homePage??><#assign txt=homePage></#if></#if>
<#if pageName??><#else><#assign pageName="genericpage"></#if>
<html lang="en">
<head>
    <title>${title}</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${appConfig.appPath}/resources/img/favicon.ico" type="image/x-icon">    
    <#if opts.canonical??><link rel="canonical" href="${opts.canonical}"></#if>
    <#if opts.metaDescription??><meta name="description" content="${opts.metaDescription}"></#if>
    <#if opts.metaKeywords??><meta name="keywords" content="${opts.metaKeywords}"></#if>
    <#if opts.ogproperties??>
    <#if opts.ogproperties.sitename??><meta property="og:site_name" content="${opts.ogproperties.sitename?chop_linebreak?xhtml}"></#if> 
    <#if opts.ogproperties.title??><meta property="og:title" content="${opts.ogproperties.title?chop_linebreak?xhtml}"></#if>
    <#if opts.ogproperties.description??><meta property="og:description" content="${opts.ogproperties.description?chop_linebreak?xhtml}"></#if>
    <#if opts.ogproperties.iconUrl??><meta property="og:image" content="${opts.ogproperties.iconUrl}"></#if>
    </#if>
 
    <!-- Le styles -->
    <link href="${appConfig.appPath}/resources/libs/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="${appConfig.appPath}/resources/libs/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
    <link rel="stylesheet" href="${appConfig.appPath}/resources/libs/magnific-popup/magnific-popup.css">
     <link rel="stylesheet" href="${appConfig.appPath}/resources/libs/galleryview/css/jquery.galleryview-3.0-dev.css">
     <link rel="stylesheet" href="${appConfig.appPath}/resources/libs/swipebox/swipebox.css">
     <link rel="stylesheet" href="${appConfig.appPath}/resources/libs/swiper/idangerous.swiper.css">
    
    <!--[if lt IE 9]>
    <script src="${appConfig.appPath}/resources/libs/html5shiv/html5shiv.js"></script>
    <![endif]-->
    <link rel="stylesheet" type="text/css" href="${appConfig.appPath}/resources/css/main.css">
    
    <#list css as file>   		
    	<style type="text/css" media="all">@import ${path}${file};</style>
    </#list>
    <!-- script src="http://yui.yahooapis.com/3.10.1/build/yui/yui-min.js"></script -->
    <script src="${appConfig.appPath}/resources/libs/jquery/jquery-1.10.2.min.js"></script>
    <script src="${appConfig.appPath}/resources/libs/jquery-ui-custom/js/jquery-ui-1.10.3.custom.min.js"></script>
    <script src="${appConfig.appPath}/resources/libs/jquery/modernizr-2.0.6.min.js"></script>
    <script src="${appConfig.appPath}/resources/libs/bootstrap/js/bootstrap.min.js"></script>
    <script src="${appConfig.appPath}/resources/libs/bootstrap/js/twitter-bootstrap-hover-dropdown.min.js"></script>

    <script src="${appConfig.appPath}/resources/libs/magnific-popup/jquery.magnific-popup.min.js"></script>
    <script src="${appConfig.appPath}/resources/libs/swipebox/jquery.swipebox.min.js"></script>
    <script defer src="${appConfig.appPath}/resources/libs/swiper/idangerous.swiper-2.0.min.js"></script>
    
    <script type="text/javascript" src="${appConfig.appPath}/resources/libs/galleryview/js/jquery.galleryview-3.0-dev.js"></script>
    <script type="text/javascript" src="${appConfig.appPath}/resources/libs/galleryview/js/jquery.easing.1.3.js"></script>
    <script type="text/javascript" src="${appConfig.appPath}/resources/libs/galleryview/js/jquery.timers-1.2.js"></script>
            
    <#list js as file>   		
	<script type="text/javascript" src="${path}${file}"></script>
    </#list>

    <#list other as passthrough>   		
        ${passthrough}
    </#list>

    <#if appConfig.lessCompiler == "on">
    <script type="text/javascript">
        less = {
            env: "development", // or "production"
            async: false,       // load imports async
            fileAsync: false,   // load imports async when in a page under
                                // a file protocol
            poll: 1000,         // when in watch mode, time in ms between polls
            functions: {},      // user functions, keyed by name
            dumpLineNumbers: "comments", // or "mediaQuery" or "all"
            relativeUrls: false,// whether to adjust url's to be relative
                                // if false, url's are already relative to the
                                // entry less file
            rootpath: ":/unknown/"// a path to add on to the start of every url resource
        };
    </script>
    <script src="${appConfig.appPath}/resources/libs/less/less-1.3.3.min.js"></script>
    </#if>

    <@cmslib.insertPageElement thePage=txt peKey="html-header"/>
</head>

<body>
    
    <@cmslib.insertPageElement thePage=txt peKey="html-body-start"/>
    
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          	
          	<#if user??>
          	
            <div class="btn-group pull-right">
            
            <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
              <i class="icon-user"></i> ${user.name}
              <span class="caret"></span>
            </a>
            
            <ul class="dropdown-menu">
              <li><a href="${appConfig.appPath}/profile/edit">Profile</a></li>
              <li class="divider"></li>
              <li><a href="${appConfig.appPath}/do/signoff">Sign Out</a></li>

              <li class="divider"></li>
              <#if opts.pageedit??><li><a href="${opts.pageedit}"><i class="icon-edit"> </i> Edit this page</a></li></#if>
              <li><a href="${appConfig.appPath}/page/tree"><i class="icon-list"> </i> Index</a></li>

              <li><a href="${appConfig.appPath}/user/control"><i class="icon-user"> </i> User accounts</a></li>
             </ul>
            </div>
			<#else>

            <div class="nav-collapse pull-right">
                <ul class="nav">
                    <li><a href="${appConfig.appPath}/do/signon">Login</a></li>
                </ul>
            </div>
            </#if>
            
            <#assign pageEditLink="">
            <#if txt??><#assign pageEditLink=appConfig.appPath + "/page/" + txt.id?c + "/edit"></#if>
            <@pagenav pagetree=pageTree pageName=pageName pageedit=pageEditLink />
            
        </div>
      </div>
    </div>
    
    
    <#if pageName == "home">
    <@cmslib.insertPageElement thePage=txt peKey="social-container"/>
    </#if>
       
    <div class=container>
        <#nested>
    </div>  
    
    <footer class="navbar navbar-fixed-bottom">
        <div class="navbar-inner">
            <div class="footerContainer">
                <@cmslib.insertPageElement thePage=txt peKey="footer"/>
                 <div class="srcAuthor pull-right">
                    <i class="icon-certificate"></i>
                    <span class=poweredBy title="Kontempl build. [ ${appConfig["application.build"]} ]">Kontempl</span> 
                 </div>
            </div>
        </div>
    </footer>
</body>
</html>
</#macro>


