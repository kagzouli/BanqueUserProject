/**
 * Plugin to create a popup template
 */
Ext.define('Ext.ux.plugin.button.PopupTemplate', {
    extend : 'Ext.plugin.Abstract',
    alias : 'plugin.popup-template',
	
	requires: ['PBLinkComponent.view.container.PblinkInnerPageContainer'],

	/**
     * The width of the container
     */
	width: undefined,
	/**
     * The height of the container
     */
	height: undefined,
	/**
     * The title of the container
     */
	title:undefined,
	/**
	 * If true, the list of selected masterkey's is added to the templateConfig object
	 */
	selectedMasterkeyList:false,
	
	templateConfig:{},
    
    constructor: function(config) {
    	var me = this;
        
        config = config || {};

        me.callParent([config]);
    },
    
    init: function(button) {

		var me = this;

    	button.on('click',function(button){

    		// if there is a selection add the masterkey to the templateConfig 
    		var grid = button.up('gridpanel');
    		
    		if (grid != null) {
    			var selection = grid.getView().getSelectionModel().getSelection();
    			
    			if (selection != null && selection.length > 0) {
    				me.templateConfig.masterkey = selection[0].get('masterKey');
    				
    				if (me.selectedMasterkeyList) {
        				var list = [];
        				Ext.Array.each(selection, function(record, index, array) {
        					list.push(record.get('masterKey'));
        				});
        				me.templateConfig.selectedMasterkeyList = list;
        			}
    			}
    		}
    		    		
    		Ext.create("Ext.window.Window",{
				modal:true,
				layout:'fit',
				title:me.title,
				width: me.width,
				height: me.height,
				originCmp: this.getCmp(),
				items:[{
					xtype:'pblinkinnerpagecontainer',
					layout:{
						type:'vbox',
						align:'stretch'
					},
					padding:5,
					viewModel:{
						data: me.templateConfig
					}
				}]
			});
    		
    		return false;
    		
    	}, this, {priority:1});
    	
    	
    }
    
});