/**
 * Plugin to create a message box
 */
Ext.define('Ext.ux.plugin.button.GridMessageBox', {
    extend : 'Ext.plugin.Abstract',
    alias : 'plugin.grid-message-box',

    /**
     * @cfg {String} message
     * The message
     */
    messageTitle: '',
    /**
     * @cfg {String} message
     * Title of the message box
     */
    message: '',
    /**
     * @cfg {String} url
     * The url
     */
    url: '',
    
    constructor: function(config) {
    	var me = this;
        
        config = config || {};

        me.callParent([config]);
    },
    
    init: function(button) {

    	button.on('click',function(button){
    		
    		var gridMessageBoxTitle  = Ext.ux.Bundle.instance.getMsg(this.messageTitle);
    		var gridMessageBoxCore	 = Ext.ux.Bundle.instance.getMsg(this.message);
    		
    		Ext.Msg.show({
			    title: gridMessageBoxTitle,
			    message: gridMessageBoxCore,
			    buttons: Ext.Msg.YESNO,
			    scope: this,
			    fn: function(btn) {
			        if (btn === 'yes') {
			   			button.fireEvent('resumeclick', button);
			        }
			    }
    		});
    		
    		return false;
    		
    	}, this, {priority:1});
    	
    	
    }
    
});