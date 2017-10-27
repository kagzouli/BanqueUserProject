/**
 * Plugin to create a select at least one dialog
 */
Ext.define('Ext.ux.plugin.button.GridSelectedAtLeastOne', {
    extend : 'Ext.plugin.Abstract',
    alias : 'plugin.grid-selected-atleast-one',
    
    /**
     * @cfg {String} message
     * The message
     */
    message: '$common-gridSelectedAtLeastOne',
    
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
			
			if (!grid.getView().getSelectionModel().getSelection().length){
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