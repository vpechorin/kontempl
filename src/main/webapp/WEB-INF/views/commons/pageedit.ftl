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
<#if  pagedata?? && pagedata.pageElements??><#assign elementsNum = pagedata.pageElements?keys?size></#if>

<ul class="nav nav-tabs">
    <#if pagenode.id??>
    <li class="active"><a href="#edit" data-toggle="tab" ><i class="icon-pencil"></i> Edit</a></li>
    <li><a href="#elements" data-toggle="tab"><i class="icon-wrench"></i> Elements (${elementsNum})</a></li>
    <li><a href="#images" data-toggle="tab"><i class="icon-camera"></i> Images (<#if imagesNum??>${imagesNum}</#if>)</a></li>
    <#else>
    <li class="active"><a href="#edit" data-toggle="tab" ><i class="icon-pencil"></i> Edit</a></li>
    </#if>
</ul>

<div class="tab-content">
    <div class="tab-pane active" id="edit">
    
    <form class="form-horizontal" method="POST" action="">
    <div class="row">
        <div class="span8">
            <div class="control-group">
                <label class="control-label" for="title">Title</label>
                <div class="controls">
                <@spring.formInput "pagenode.title" "class='span12' onKeyUp='onTitleChange();' onBlur='onTitleChange();'"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="body">Body</label>
                <div class="controls">
                <@spring.formTextarea "pagenode.body" "class='span12 markItUp' style='height: 500px;'"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="description">Description</label>
                <div class="controls">
                <@spring.formTextarea "pagenode.description" "class='span12'"/>
                </div>
            </div>            
            <div class="control-group">
                <label class="control-label" for="tags">Tags</label>
                <div class="controls">
                <@spring.formInput "pagenode.tags" "class='span12'"/>
                </div>
            </div>                           
        </div><!-- end of first col -->
        <div class="span4" style="background-color: #EEE;">
            <div class="control-group">
                <label class="control-label" for="publicPage">Public</label>
                <div class="controls">
                <@spring.formCheckbox "pagenode.publicPage"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="placeHolder">Placeholder</label>
                <div class="controls">
                <@spring.formCheckbox "pagenode.placeholder"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="sortindex">Index</label>
                <div class="controls">
                <@spring.formInput "pagenode.sortindex" "class='span12'"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="hideTitle">Hide the title</label>
                <div class="controls">
                <@spring.formCheckbox "pagenode.hideTitle"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="autoName">Automatic naming</label>
                <div class="controls">
                <@spring.formCheckbox "pagenode.autoName" "onClick='onAutonameClick();'"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="name">Pagename</label>
                <div class="controls">
                <@spring.formInput "pagenode.name" "class='span12'"/>
                </div>
            </div>
            
            <div class="control-group">
                <label class="control-label" for="parentId">Parent</label>
                <div class="controls">
                <@spring.formSingleSelect "pagenode.parentId" parents/>
                </div>
            </div>
                  
            <div class="control-group">
                <div class="controls">
                    <button type="submit" class="btn btn-primary">Save</button>
                </div>
            </div>
        
        </div><!-- end of second col -->
        
        
    </div><!-- end form row -->            
    </form>
    
    <!-- end of edit tab content -->
    </div>
    <#if pagenode.id??>
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
            <#if pagedata.pageElements??>
                <#if (pagedata.pageElements?keys?size > 0)>
                    <#assign elementNames = pagedata.pageElements?keys>
                    <#list elementNames as n>
                        <tr>
                            <td>${pagedata.pageElements[n].id}</td>
                            <td>${pagedata.pageElements[n].name!""?html}</td>
                            <td><pre class="prettyprint">${pagedata.pageElements[n].body?html}</pre></td>
                            <td><a class="btn btn-primary" href="${appConfig.appPath}/page/${pagedata.id?c}/edit-element?id=${pagedata.pageElements[n].id}"><i class="icon-pencil icon-white"></i> edit</a>
                            <a class="btn btn-inverse" href="${appConfig.appPath}/page/${pagedata.id?c}/delete-element?id=${pagedata.pageElements[n].id}"><i class="icon-trash icon-white"></i> delete</a></td>
                        </tr>
                    </#list>
                </#if>
            </#if>
        </table>    
    
    <!-- end of elements tab content -->
    </div>
    </#if>
    <#if pagenode.id??>
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
    </#if>
</div>

    
    <script>
    <#if pagenode.id??>
    var thisPageId=${pagenode.id?c};
    <#else>
    var thisPageId=0;
    </#if>
    
    $(document).ready(function () {
        prettyPrint();
        onAutonameClick();
        // init markItUp textareas
        $(".markItUp").markItUp(mySettings);
    });
    
    function onAutonameClick() {
        if ($('#autoName').is(':checked')) {
            console.log( "set disabled attr" );
            $('#name').attr('disabled', true);
        }
        else {
            console.log( "remove disabled attr" );
            $('#name').removeAttr('disabled');
        }
    }
    
    function onTitleChange() {
        var title = $('#title').val();
        var url = "/page/titleToName?title=" + encodeURIComponent(title);
        if ($('#autoName').is(':checked')) {
            $.ajax({
                url: url,
                cache: false,
                type: "GET",
                dataType : "text",
                success: function( txt ) {
                    $("#name").val(txt);
                }, 
                error: function( xhr, status ) {
                    console.log( "Error: " + status );
                    alert( "Sorry, there was a problem! " + status );
                }
            });
        }
    }
    
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