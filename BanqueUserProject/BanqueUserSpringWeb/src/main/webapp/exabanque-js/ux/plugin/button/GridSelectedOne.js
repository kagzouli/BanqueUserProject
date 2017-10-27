/**
 * Plugin to create a selected one dialog
 */
Ext.define('Ext.ux.plugin.button.GridSelectedOne', {
    extend : 'Ext.plugin.Abstract',
    alias : 'plugin.grid-selected-one',
    
    /**
     * @cfg {String} message
     * The message
     */
    message: '$CommonGridSelectedOne',
    
    constructor: function(config) {
    	var me = this;
        
        config = config || {};

        me.callParent([config]);
    },
    
    init: function(button) {
    	var me = this;
    	
    	button.on('click',function(button){
    		
    		var grid = button.up('gridpanel'),
    			store = grid.getStore();
			
			if (!grid.getView().getSelectionModel().getSelection().length ||
				 grid.getView().getSelectionModel().getSelection().length > 1){
				
				var id = me.findMessagePluginId(button);
				Ext.ux.plugin.messages.MessageManager.cleanAllMessages(id);
    			Ext.ux.plugin.messages.MessageManager.processIndividualMessages(me.message, null, null, id);
    			
				return false;
			}
    		
    	}, this, {priority:1});
    	
    	
    },
    
    findMessagePluginId:function(view) {
    	// find messagecontainer
		var container = view.up('pblinkinnerpagecontainer');
		if (container == null) {
			container = view.up('pagecontainer');
			if (container == null) {
				container = view;
			}
		}
		// return uniqueId
		return container.down('pblinkmessagecontainer').getUniqueId();
    }
    
});