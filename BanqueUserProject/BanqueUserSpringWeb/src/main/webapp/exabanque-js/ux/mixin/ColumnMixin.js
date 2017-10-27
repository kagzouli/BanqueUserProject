/**
 * The column mixin provides a pblink columns a way to add some common behaviors
 */
Ext.define('Ext.ux.mixin.ColumnMixin', {
    extend: 'Ext.Mixin',

    mixinConfig: {
        id: 'columnmixin'
    },
    
    init:function(config, initialConfig, component){
    	
    	var result = {};
    	
    },
	
	config:{
		
		/**
		 * Indicates the summary will based in another dataIndex
		 */
		summaryDataIndex : undefined
	},

	/**
	 * This function renderize in the summary row the dataIndex that indicate directly in the column.
     */
	summaryRenderer: function(value) {
		if (this.getSummaryDataIndex()){
			var pblinkgrid = this.up('pblinkgrid'),
				summaryDataIndex = this.getSummaryDataIndex();
			if (pblinkgrid && summaryDataIndex) {
				if (pblinkgrid.getStore().getRange().length > 1) {
					return pblinkgrid.getStore().getRange()[0].get(summaryDataIndex);
				}
			}
		}else{
			return value;	
		}
	}
    

});