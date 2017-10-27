/**
 * Plugin to create a property validation
 */
Ext.define('Ext.ux.plugin.button.GridValidateProperty', {
    extend : 'Ext.plugin.Abstract',
    alias : 'plugin.grid-validate-property',
    
    /**
     * @cfg {String} message
     * The message
     */
    message: '',
    /**
     * @cfg {String} url
     * The url
     */
    url: '',
    /**
     * @cfg {String} idColumn
     * The column id
     */
    idColumn:'',
    /**
     * @cfg {String} column
     * The column
     */
	column: '',
	/**
     * @cfg {String} values
     * The values
     */
	values: '',

    constructor: function(config) {
    	var me = this;

        config = config || {};

        me.callParent([config]);
    },

    init: function(button) {
    	var me = this;
    	
    	button.on('click',function(button){
    		var
    			values = this.values.split(';'),
    			messages = [],
    			baseMessage = Ext.ux.Bundle.instance.getMsg(this.message),
    			idColLabel  = null,
    			grid = button.up('gridpanel'),
    			store = grid.getStore(),
    			selection = grid.getView().getSelectionModel().getSelection();

    		if(this.idColumn)
    			idColLabel = Ext.ux.Bundle.instance.getMsg(pagename+"."+this.idColumn);

    		for(i = 0; i < selection.length; i++) {
    			var element = selection[i],
    				value=element.get(this.column);

    			if(!Ext.Array.contains(values, value)) {
    				var message = baseMessage;
    				if(this.idColumn)
    					message += (" ("+idColLabel+" "+element.get(this.idColumn)+")");
    				Ext.Array.push(messages, message);
    			}
    		}


    		if(messages.length != 0) {
    			var id = me.findMessagePluginId(button);    		
    			Ext.ux.plugin.messages.MessageManager.processIndividualMessages(messages, null, null, id);
    		}

    		return (messages.length == 0);

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