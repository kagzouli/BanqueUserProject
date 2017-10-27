/**
 * Plugin to manage all messages of the messages compoment {@link Ext.ux.plugin.messages.MessagesManagement}
 */
Ext.define('Ext.ux.plugin.messages.MessageManager', {
	
	singleton: true,
	
	MESSAGE_MODE_INLINE:0,
	MESSAGE_MODE_POPUP: 1,
	
	LABEL_TITLE_ERRORS: "", // "Error",
	LABEL_TITLE_WARNS: "", // "Warning",
	LABEL_TITLE_INFOS: "", // "Information",
	
	messagePlugin: new Ext.util.HashMap(),
	
	template: Ext.create('Ext.XTemplate', '<ul class="' + Ext.plainListCls + '"><tpl for="."><li><span class="field-name">{.}</span></li></tpl></ul>'),
	
	register: function (plugin) {
		if (this.messagePlugin.get(plugin.itemId)){
				this.unregisterWithItemId(plugin.itemId);
		}
			this.messagePlugin.add(plugin.itemId, plugin);
	},
	
	unregister: function (plugin){
		this.messagePlugin.remove(plugin);
	},
	
	unregisterWithItemId: function (itemId){
		this.messagePlugin.removeAtKey(itemId);
	},
	
	processIndividualMessages: function(errors, warns, infos, itemId, mode, callback, confirmation, labelMessage){
		var action = this.generateActionMessages(errors, warns, infos);
		this.processMessages(action, itemId, mode, callback, confirmation, labelMessage);
	},
	
	processMessages: function (action, itemId, mode, callback, confirmation, labelMessage) {
		if (action.result.success){
			
			if(Ext.isDefined(itemId) && Ext.isDefined(itemId.result) && itemId.result != null){
				this.cleanAllMessages(itemId.result);			
			}			
			
			if (this._hasMessagesToProcess()){
				var sessionStorageData = JSON.parse(sessionStorage.getItem('Pblink_Messages'));
				
				if(Ext.isDefined(itemId) && Ext.isDefined(itemId.result) && itemId.result != null){
					this.processMessageMethod({result:sessionStorageData}, itemId.result, mode, callback, confirmation, labelMessage);
				}else{
					this.processMessageMethod({result:sessionStorageData}, itemId, mode, callback, confirmation, labelMessage);
				}				
				sessionStorage.removeItem('Pblink_Messages');
			}
			if(Ext.isDefined(itemId) && Ext.isDefined(itemId.result) && itemId.result != null){
				this.processMessageMethod(action, itemId.result, mode, callback, confirmation, labelMessage);
			}else{
				this.processMessageMethod(action, itemId, mode, callback, confirmation, labelMessage);
			}			
		}else{
			if(Ext.isDefined(itemId) && Ext.isDefined(itemId.result) && itemId.result != null){
				this._processActionFailure(action, itemId.result, mode, callback, confirmation, labelMessage);
			}else{
				this._processActionFailure(action, itemId, mode, callback, confirmation, labelMessage);
			}

		}
	},
	
	processMessageMethod: function (action, itemId, mode, callback, confirmation, labelMessage){
		if (mode == this.MESSAGE_MODE_INLINE || mode == undefined){
			this._processActionMessagesInline(action, itemId, labelMessage);
		}else if (this.MESSAGE_MODE_POPUP){
			this._processActionMessagesPopup(action, callback, confirmation);
		}
		
	},

	keepMessages: function(data, inSessionStorage){
		if (inSessionStorage){
			sessionStorage.setItem('Pblink_Messages', JSON.stringify(data));
		}
	},

	_hasMessagesToProcess:function(){
		return sessionStorage.getItem('Pblink_Messages');
	},
	
	cleanAllMessages: function (itemId) {
		var pginTarget = this._getPlugin(itemId);
		if (pginTarget) {
			pginTarget.cleanAllMessages();
		}
	},
	
	/**
	 * @param {} itemId [String/Object]
	 * @return {}
	 */
	_getPlugin: function (value){
		var pluginId = (!Ext.isEmpty(value)
							? (value)
							: undefined),
			plugin = this.messagePlugin.get(pluginId);
		return plugin;
	},
	
	_processActionFailure: function(action, itemId, mode, callback, confirmation, labelMessage) {
		if (action.failureType) {
	        switch (action.failureType) {
	        case Ext.form.Action.CLIENT_INVALID:
	            Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
	            break;
	        case Ext.form.Action.CONNECT_FAILURE:
	            Ext.Msg.alert('Failure', 'Ajax communication failed');
	            break;
	        case Ext.form.Action.SERVER_INVALID:
	        	var ex = action.result.exception;
	        	if (ex) {
	        		TryCatch.showException(ex.message, ex.where);
	        	}
	        	else {
	            	this.processMessageMethod(action, itemId, mode, callback, confirmation, labelMessage);
	        	}
	        	break;
	        default:
	            Ext.Msg.alert('Failure', 'Unknown failure');
	        }   
		}
		else {
        	var ex = action.result.exception;
        	if (ex) {
        		TryCatch.showException(ex.message, ex.where);
        	}
        	else {
            	this.processMessageMethod(action, itemId, mode, callback, confirmation, labelMessage);
        	}
		}
	},
	
	_processActionMessagesInline: function(action, itemId, labelMessage){
		var plugin = this._getPlugin(itemId);
		if (plugin){
			plugin.processMessages(action, labelMessage);
		}else{
			this._processActionMessagesPopup(action)
		}
	},
	
	_processActionMessagesPopup: function(action,callback,confirmation){
		
		var errors = action.result.globalerrors;
		var warns = action.result.warnings;
		var infos = action.result.infos;
		
		var me = this;
		
		// callback is not required
    	if(Ext.isEmpty(callback) || !Ext.isFunction(callback)) {
    		callback = Ext.emptyFn;
    	}
		
		var buttons = Ext.Msg.OK;
		if (confirmation){
			buttons = Ext.Msg.OKCANCEL;
		}
	
		if (errors != undefined && errors != null && errors.length > 0){
			Ext.Msg.show({
				title: this.LABEL_TITLE_ERRORS,
				msg: me.template.apply(errors),
				buttons: buttons,
				fn: function(btn, text){
	                if (btn == 'ok'){
	                	
	                }
				},
				icon: Ext.Msg.ERROR
			});
			return;
		}
		
		if (warns != undefined && warns != null && warns.length > 0){
			Ext.Msg.show({
				title: this.LABEL_TITLE_WARNS,
				msg: me.template.apply(warns),
				buttons: buttons,
				fn: function(btn, text){
	                if (btn == 'ok'){
	                	callback();
	                }
				},
				icon: Ext.Msg.WARNING
			});
			return;
		}
		
		if (infos != undefined && infos != null && infos.length > 0){
			Ext.Msg.show({
				title: this.LABEL_TITLE_INFOS,
				msg: me.template.apply(infos),
				buttons: buttons,
				fn: function(btn, text){
	                if (btn == 'ok'){
	                	callback();
	                }
				},
				icon: Ext.Msg.INFO
			});
			return;
		}
		
	},
	
	generateActionMessages: function(errors, warns, infos) {
    	return {
    		result: {
    			globalerrors : Ext.isArray(errors) ? errors : (Ext.isEmpty(errors) ? undefined : [errors] ),
    			warnings: Ext.isArray(warns) ? warns : (Ext.isEmpty(warns) ? undefined : [warns] ),
    			infos: Ext.isArray(infos) ? infos : (Ext.isEmpty(infos) ? undefined: [infos] ),
    			success:true
    		}
    	};
	},
	
	simpleWarning: function (message, callback, confirmation){
		var action = this.generateActionMessages(undefined, message, undefined);
		this._processActionMessagesPopup(action, callback, confirmation);
	},
	
	simpleInfo: function (message, callback, confirmation){
		var action = this.generateActionMessages(undefined, undefined, message);
		this._processActionMessagesInline(action, callback, confirmation);
	},

	simpleError: function (message, callback, confirmation){
		var action = this.generateActionMessages(message, undefined, undefined);
		this._processActionMessagesInline(action, callback, confirmation);
	}
	
});