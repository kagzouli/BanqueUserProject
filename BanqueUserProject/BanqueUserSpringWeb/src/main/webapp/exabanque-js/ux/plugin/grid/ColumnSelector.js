Ext.define('Ext.ux.plugin.grid.ColumnSelector', {
	extend : 'Ext.AbstractPlugin',
	alias : 'plugin.columnselector',
	pluginId : "columnselector",
	requires : [ 'PBLinkComponent.view.window.ColumnSelectorWindow' ],
	dock : 'top',
	config : {
		dock : 'bottom',
		insertInto : null,
		insertAt : null,
	},
	init : function(grid) {

		var me = this;

		this.grid = grid;
		this.items = [];

		grid.addListener('afterrender', this.onAfterRender, this);

		this.items.push({
			itemId : 'btnColumnSelector',
			xtype : 'button',
			cls : 'columnselector-trigger',
			height : 18,
			margin : '0 0 0 10',
			handler : function(button) {
				me.onClick();
			}
		});

		if (!this.insertInto) {
			this.tbar = grid.addDocked({
				xtype : 'toolbar',
				dock : this.dock,
				items : this.items
			})[0];
		}

	},
	onAfterRender : function(grid) {
		if (this.insertInto) {
			if (!Ext.isEmpty(this.insertAt))
				grid.down(this.insertInto).insert(this.insertAt, this.items);
			else
				grid.down(this.insertInto).add(this.items);
		}
	},
	onClick : function(button) {
		var grid = this.grid;
		var grouperFeature = grid.getStore().getGrouper();
		var grouperProperty = grouperFeature ? grouperFeature.getProperty() : undefined;

		Ext.create('PBLinkComponent.view.window.ColumnSelectorWindow', {
			storeData: grid.headerCt.query('>gridcolumn[hideable]'),
			grouper: grouperProperty
		}).show();

	}
});
