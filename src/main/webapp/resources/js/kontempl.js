function DeletePageConfirm() {
    var agree = confirm("This will delete this page. Are you sure?");
    if (agree) {
        return true;
    }
    else {
        return false;
    }
}

function onPageDeleteBtnClick(id) {
    var agree = confirm("This will delete this page. Are you sure?");
    if (agree) {
        $.ajax({
            url: "/page/" + id + "/asyncdelete",
            cache: false,
            type: "GET",
            dataType : "text",
            success: function( txt ) {
            	window.location.replace("/page/tree");
            }, 
            error: function( xhr, status ) {
                alert( "Sorry, there was a problem! " + status );
            }
        });
    }
    else {
        return false;
    }
}
