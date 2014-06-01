<#macro page title="" css=[] js=[] other=[] opts...>
<#import "/spring.ftl" as spring/>
<#import "/lib/cmslib.ftl" as cmslib/>
<#include "/pages/pagenav.ftl"/>
<#include "/lib/pagetreemenu.ftl"/>
<#include "/lib/pagebranch.ftl"/>
<#include "/lib/dropdownSubmenu.ftl"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>${title}</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    
    <link href="${appConfig.appPath}/resources/vendor/css/normalize.css" rel="stylesheet">
	
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
	<link rel="stylesheet" href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css">

    <link href="//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="${appConfig.appPath}/resources/libs/jquery-treetable/stylesheets/jquery.treetable.css" rel="stylesheet"/>
    <link href="${appConfig.appPath}/resources/libs/jquery-treetable/stylesheets/jquery.treetable.theme.default.css" rel="stylesheet"/>
    <link href="${appConfig.appPath}/resources/libs/markitup/markitup/skins/markitup/style.css" rel="stylesheet"/>
    <link href="${appConfig.appPath}/resources/libs/markitup/markitup/sets/html/style.css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${appConfig.appPath}/resources/css/main.css">
    
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    <script src="//code.jquery.com/ui/1.10.3/jquery-ui.min.js"></script>
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
    <script src="${appConfig.appPath}/resources/libs/jquery-fileupload/js/vendor/jquery.ui.widget.js"></script>
    <script src="${appConfig.appPath}/resources/libs/jquery-fileupload/js/jquery.iframe-transport.js"></script>
    <script src="${appConfig.appPath}/resources/libs/jquery-fileupload/js/jquery.fileupload.js"></script>
    <script src="${appConfig.appPath}/resources/libs/jquery-treetable/javascripts/src/jquery.treetable.js"></script>
    <script src="${appConfig.appPath}/resources/libs/markitup/markitup/jquery.markitup.js"></script>
    <script src="${appConfig.appPath}/resources/libs/markitup/markitup/sets/html/set.js"></script>
    <script src="${appConfig.appPath}/resources/js/kontempl.js"></script>
</head>

<body>
	<#if user??>
    <nav class="navbar navbar-inverse navbar-static-top role=navigation">
    	<div class="container">
	    	<div class="navbar-header">
	    		<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#topmenu-navbar-collapse-1">
	       			<span class="sr-only">Toggle navigation</span>
	        		<span class="icon-bar"></span>
	        		<span class="icon-bar"></span>
	        		<span class="icon-bar"></span>
	      		</button>
	      		<a class="navbar-brand" href="#">kontempl</a>
	    	</div>
	    	<div class="collapse navbar-collapse" id="topmenu-navbar-collapse-1">
	    		<ul class="nav navbar-nav">
	    			<li><a href="${appConfig.appPath}/page/tree"><i class="fa fa-list"></i> Index</a></li>
	    			<li><a href="${appConfig.appPath}/user/control"><i class="fa fa-users"></i> User accounts</a></li>
	    			<li><a href="${appConfig.appPath}/profile/edit">Profile</a></li>
	    			<li class="dropdown">
	          			<a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-sitemap"></i> Sitemap <b class="caret"></b></a>
	          			<ul class="dropdown-menu">
	          				<li><a href="${appConfig.appPath}/do/sitemap/update"><i class="fa fa-refresh"></i> Update sitemap</a></li>
	          				<li><a href="${appConfig.appPath}/do/sitemap/submit"><i class="fa fa-upload"></i> Re-Submit sitemap</a></li>
	          			</ul>
	          		</li>
	          		<li><span class="glyphicon glyphicon-user"></span> ${user.name}</li>
	    			 <li><a href="${appConfig.appPath}/do/signoff"><i class="fa fa-sign-out"></i> Log Out</a></li>
	    		</ul>
	    	</div>
    	</div>
    </nav>
	<#else>
    <nav class="navbar navbar-default navbar-fixed-top role=navigation">
    	<div class="container">
	    	<div class="navbar-header">
	    		<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#topmenu-navbar-collapse-1">
	       			<span class="sr-only">Toggle navigation</span>
	        		<span class="icon-bar"></span>
	        		<span class="icon-bar"></span>
	        		<span class="icon-bar"></span>
	      		</button>
	      		<a class="navbar-brand" href="#">kontempl</a>
	    	</div>
	    	<div class="collapse navbar-collapse" id="topmenu-navbar-collapse-1">
	    		<ul class="nav navbar-nav">
	    			<li><a href="${appConfig.appPath}/do/signon"><i class="fa fa-sign-in"></i> Login</a></li>
	    		</ul>
	    	</div>
    	</div>
    </nav>
	</#if> 

    <div class="container">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="span12">
            <#nested>
            </div>
        </div>
    </div>

    <footer class="navbar navbar-default navbar-fixed-bottom">
    	<div class=container>
            <div class="footerContainer">
                 <div class="srcAuthor pull-right">
                    <i class="fa fa-bolt"></i>
                    <span class=poweredBy title="${appConfig["application.build"]}">Kontempl</span>
                 </div>
            </div>
        </div>
    </footer>
    
    <script src="${appConfig.appPath}/resources/libs/google-code-prettify/prettify.js"></script>
    
</body>
</html>
</#macro>


