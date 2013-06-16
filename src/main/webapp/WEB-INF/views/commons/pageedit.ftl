<#include "/lib/simplepage.ftl"/>
<#import "/spring.ftl" as spring />
<@page
    title="Edit: " + pagenode.title!""
>
<div class="btn-group">
    <a class="btn btn-small" href="${appConfig.appPath}/page/tree"><i class="icon-list"></i> Index</a>
    <#if pagenode.id??>
    <a class="btn btn-small btn-inverse" href="${appConfig.appPath}/page/${pagenode.id?c}/add-child"><i class="icon-plus icon-white"></i> Add a sub-page</a>
    <a class="btn btn-small btn-inverse" href="${appConfig.appPath}/page/${pagenode.id?c}/add-element"><i class="icon-plus-sign icon-white"></i> Add an element</a>
    <a class="btn btn-small" href="${appConfig.appPath}/pv/${pagenode.name}"><i class="icon-external-link"></i> Preview</a>
    <a class="btn btn-small" href="${appConfig.appPath}/page/${pagenode.id?c}/copy"><i class="icon-copy"></i> Copy</a>
    <a class="btn btn-small btn-danger" href="#" onClick="return onPageDeleteBtnClick(${pagenode.id?c});" ><i class="icon-trash icon-white"></i> Delete</a>
    </#if>
</div>

<h3><#if pagenode.id??>edit page #${pagenode.id?c}<#else>add a new page</#if></h3>

<#assign elementsNum = 0>
<#if pagenode.pageElements??><#assign elementsNum = pagenode.pageElements?keys?size></#if>

<ul class="nav nav-tabs">
    <li class="active"><a href="#edit" data-toggle="tab" ><i class="icon-pencil"></i> Edit</a></li>
    <li><a href="#elements" data-toggle="tab"><i class="icon-wrench"></i> Elements (${elementsNum})</a></li>
    <li><a href="#images" data-toggle="tab"><i class="icon-camera"></i> Images (<#if imagesNum??>${imagesNum}</#if>)</a></li>
</ul>

<div class="tab-content">
    <div class="tab-pane active" id="edit">
    
    <form class="cmxform" method="POST" action="">
        <fieldset>
        <#if pagenode.parentId??>
        <input type=hidden name=parentId value="${pagenode.parentId}">
        </#if>
        <ol>
            <li>
                <label for=title>Public</label>
                <@spring.formCheckbox "pagenode.publicPage"/>
            </li>
            <li>
                <label for=hideTitle>Placeholder</label>
                <@spring.formCheckbox "pagenode.placeholder"/>
            </li>
            <li>
                <label for=title>Index</label>
                <@spring.formInput "pagenode.sortindex"/>
            </li>
            <li>
                <label for=title>Title</label>
                <@spring.formInput "pagenode.title"/>
            </li>
            <li>
                <label for=hideTitle>Hide the title</label>
                <@spring.formCheckbox "pagenode.hideTitle"/>
            </li>
            <li>
                <label for=title>Pagename</label>
                <@spring.formInput "pagenode.name"/>
            </li>
            <li>
                <label for=body>Description</label>
                <@spring.formTextarea "pagenode.description"/>
            </li>
            <li>
                <label for=body>Body</label>
                <@spring.formTextarea "pagenode.body"/>
            </li>
            <li>
                <label for=title>Tags</label>
               <@spring.formInput "pagenode.tags"/>
            </li>
            <li>
                <@spring.formHiddenInput "pagenode.parentId"/>
                <button type="submit" class="btn btn-primary">Save</button>
            </li>
        </ol>
        </fieldset>
    </form>
    
    <!-- end of edit tab content -->
    </div>
    <div class="tab-pane" id="elements">
        <div>
            <h3>Page Elements</h3>
        </div>
        <#if pagenode??><#if pagenode.id??>
        <a class="btn btn-inverse" href="${appConfig.appPath}/page/${pagenode.id?c}/add-element"><i class="icon-plus icon-white"></i> Add an element</a>
        </#if></#if>
        
        <table class="table table-condensed">
            <tr>
                <th>#</th>
                <th>Name</th>
                <th>Content</th>
                <th>Actions</th>
            </tr>
            <#if pagenode.pageElements??>
                <#if (pagenode.pageElements?keys?size > 0)>
                    <#assign elementNames = pagenode.pageElements?keys>
                    <#list elementNames as n>
                        <tr>
                            <td>${pagenode.pageElements[n].id}</td>
                            <td>${pagenode.pageElements[n].name!""?html}</td>
                            <td><pre class="prettyprint">${pagenode.pageElements[n].body?html}</pre></td>
                            <td><a class="btn btn-primary" href="${appConfig.appPath}/page/${pagenode.id?c}/edit-element?id=${pagenode.pageElements[n].id}"><i class="icon-pencil icon-white"></i> Edit</a>
                            <a class="btn btn-inverse" href="${appConfig.appPath}/page/${pagenode.id?c}/delete-element?id=${pagenode.pageElements[n].id}"><i class="icon-trash icon-white"></i> Delete</a></td>
                        </tr>
                    </#list>
                </#if>
            </#if>
        </table>    
    
    <!-- end of elements tab content -->
    </div>
    <div class="tab-pane" id="images">
        <div>
            <h3>Images</h3>
        </div>
        
        <div>
        <label>Upload images(s):</label>
        <input class="btn btn-inverse" id="fileupload" type="file" name="files[]" data-url="${appConfig.appPath}/image/${pagenode.id?c}/upload" multiple>
        </div>
        
        <div id="progress" class="progress progress-striped">
            <div class="bar" style="width: 0%;"></div>
        </div>
        
        <div id=imagesContainer>
        <#include "/commons/pageimages2.ftl">
        </div>
    <!-- end of images tab content -->
    </div>
</div>

    
    <script>
    
    var thisPageId=${pagenode.id?c};
    
    $(document).ready(function () {
        prettyPrint()
    });
    
    function onDeleteBtnClick(imageId) {
        $.ajax({
            url: "/images/" + imageId + "/asyncdelete",
            cache: false,
            type: "GET",
            dataType : "text",
            success: function( txt ) {
                reloadImageTable(thisPageId);
            }, 
            error: function( xhr, status ) {
                alert( "Sorry, there was a problem! " + status );
            }
        });
    }
    
    function onSetMainImgClick(imageId) {
        $.ajax({
            url: "/images/" + imageId + "/setMainAsync",
            cache: false,
            type: "GET",
            dataType : "text",
            success: function( txt ) {
                reloadImageTable(thisPageId);
            }, 
            error: function( xhr, status ) {
                alert( "Sorry, there was a problem! " + status );
            }
        });
    }
    
    function moveImage(imageId, direction) {
        $.ajax({
            url: "/images/" + imageId + "/move",
            data: { dir: direction },
            cache: false,
            type: "GET",
            dataType : "text",
            success: function( txt ) {
                reloadImageTable(thisPageId);
            }, 
            error: function( xhr, status ) {
                alert( "Sorry, there was a problem! " + status );
            }
        });
    }
    
    function reloadImageTable(pageId) {
        var url = "/page/" + pageId + "/images";
        console.log( "URL to images: " + url );
        $( "#imagesContainer" ).load( url );
        console.log( "Image reload requested" );
    }
    
    $(function () {
    $('#fileupload').fileupload({
        dataType: 'json',
        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
        maxFileSize: 10000000, // 10 MB
        previewMaxWidth: 100,
        previewMaxHeight: 100,
 
        done: function (e, data) {
            reloadImageTable(thisPageId);
            $('#progress .bar').css('width', '0%' );
        },
 
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#progress .bar').css(
                'width',
                progress + '%'
            );
        },
 
        // dropZone: $('#dropzone')
    });
});
    </script>
</@page>