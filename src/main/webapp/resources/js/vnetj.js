YUI.add('vnetj', function(Y) {
	Y.namespace('vnetj');
	
	Y.vnetj.getData = function(url, fnCall) {
		var onComplete = function (id, o, args) {
	        fnCall(o.responseText);
	    };
        Y.on('io:complete', onComplete, Y, []);
        Y.io(url);
	};
	
	Y.vnetj.getPanel = function(panelId, width, height, title, content, ctx) {
	    if (!Y.vnetj.panelRegister) {
	    	Y.vnetj.panelRegister = new Array();
	    }

	    Y.vnetj.panelZIndex++;
	    
	    var reg = Y.vnetj.panelRegister;
	    
	    if (reg[panelId]) {
	        var panel = reg[panelId];
	        if (ctx) {
	        	panel.set("x", ctx.getX());
	        	panel.set("y", ctx.getY());
	            //panel.cfg.setProperty("context", [ctx, "tl", "bl", ["beforeShow", "windowResize"] ]);
	        }
	        panel.set("zIndex", Y.vnetj.panelZIndex);
	    }
	    else {
	        //var elementId = panelId + "-" + Math.floor(Math.random()*100001);
	        var panel = new Y.Panel({ 
	            width:width, 
	            visible:false,
	            zIndex: Y.vnetj.panelZIndex,
	            centered: true,
	            render: false
	        } );
	        if (width) {
	            panel.set("width", width);
	        }
	        if (height) {
	            panel.set("height", height);
	        }	        

	        if (ctx) {
	        	panel.set("x", ctx.getX());
	        	panel.set("y", ctx.getY());
	            //panel.cfg.setProperty("context", [ctx, "tl", "bl", ["beforeShow", "windowResize"] ]);
	        }

	        if (title) {
	        	panel.set("headerContent", title);
	        }
	        if (content) {
	        	panel.set("bodyContent", content);
	        }
	        reg[panelId] = panel;
	        panel.render("#newPanel");
	    }

	    return reg[panelId];
	};
	
}, '0.0.1', { requires: [ 'node' ] });