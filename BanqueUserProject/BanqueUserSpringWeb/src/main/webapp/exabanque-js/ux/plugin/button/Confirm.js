/**
 * Plugin to create a confirm dialog
 */
Ext.define('Ext.ux.plugin.button.Confirm', {
    extend : 'Ext.plugin.Abstract',
    alias : 'plugin.button-confirm',
    
    /**
     * @cfg {String} message
     * The message
     */
    message: '$common-button-confirm-message',
    
    constructor: function(config) {
    	var me = this;
        
        config = config || {};

        me.callParent([config]);
    },
    
    init: function(button) {
    	
    	button.on('click',function(button){
    		
    		var buttonConfirmTitle 		= Ext.ux.Bundle.instance.getMsg(this.message);
    		
			var r = confirm(buttonConfirmTitle);
			if (r == false) {
			    return false;
			}
    		
    	}, this, {priority:1});
    	
    	
    }
    
});