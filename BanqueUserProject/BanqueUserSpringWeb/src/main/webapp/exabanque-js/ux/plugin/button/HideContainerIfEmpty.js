/**
 * Plugin to hide a container if it's empty
 */
Ext.define('Ext.ux.plugin.button.HideContainerIfEmpty', {
    extend : 'Ext.plugin.Abstract',
    alias : 'plugin.hide-if-empty',

    /**
     * @cfg {String} messageTitle
     * The title of the message
     */
    messageTitle: '',
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

    constructor: function(config) {
    	var me = this;

        config = config || {};

        me.callParent([config]);
    },

    init: function(cmp) {
    	this.setCmp(cmp);
    	cmp.on('afterrender', function(btn,opts) {
        	var panel = btn.getViewModel().getParent()
    		    view = panel.getView();
    		for(var i = 0; i < view.items.getCount(); i++) {
    			if(!view.items.get(i).hidden) {
    	    		view.hidden=false;
    				return;
    			}
    		}
    		view.hidden=true;
    	});
    }

});