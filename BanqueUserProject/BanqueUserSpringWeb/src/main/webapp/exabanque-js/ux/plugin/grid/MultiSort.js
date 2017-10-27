/**
 Grid plugin to allow multi-column sorting (Ext.ux.bnp.grid.MultiSort extension).
 */

Ext.define('Ext.ux.plugin.grid.MultiSort', {
    extend:'Ext.AbstractPlugin',
    alias:'plugin.multisort',
    
    /**
     * The default sorters.
     */
    defaultSorters:[],
    /**
     * Local Storage Provider
     */
    localStorageProvider:null,
    
    /**
     * Persist Order Enabled
     */
    persistOrderEnabled:null,

    /**
     * @cfg {Object} droppable
     * States if the grid can be dropped
     */
    droppable:null,
    /**
     * @cfg {Object} reorderer
     * States if the grid can be reordered
     */
    reorderer:null,
    
    /**
     * @cfg {Boolean} enablePersistance
     * States if the grid has persistance enabled
     */
    enablePersistance: false,

    init:function (grid) {
    	
        this.droppable = Ext.create('Ext.ux.plugin.grid.MultiSortdroppable');
        this.reorderer = Ext.create('Ext.ux.BoxReorderer', {
            listeners:{
                Drop:function (reorderer, c, button) { //update sort direction when button is dropped
                    this.sortPlugin.changeSortDirection(button, false);
                }
            }
        });

        var me = this;
        this.grid = grid;
        
        if(this.enablePersistance) {
        	this.localStorageProvider = new Ext.state.LocalStorageProvider({prefix:grid.id || grid.itemId});
        	//var loadedSortOrder = this.localStorageProvider.get(this.grid.id);
        }

        grid.addListener('reconfigure', this.onAfterReconfigure, this);
        grid.addListener('columnhide', this.onColumnHide, this);
        
        var columns = this.grid.view.getHeaderCt().getGridColumns();

        for (var i = 0; i < columns.length; i++) {
            var col = columns[i];
            col.sortable=false;
        }
                
        var items = [];
        items.push({
            xtype:'tbtext',
            //text:Trans.auto('${multis.tollbar.title}',"Multi-column sorting"),
            text:"$common-multiCollumn",
            reorderable:false
            //,hidden:true
        });
        items.push({
            xtype:"tbfill",
            reorderable:false
        });
		if(this.enablePersistance) {
			items.push({
				xtype:'button',
			    iconCls:'icon-bullet_disk',
			    //tooltip:Trans.auto('${multis.remember.sort.order}',"Remember sort order"),
                tooltip:"Remember sort order",
			    reorderable:false,
			    hidden:true,
			    pressed:me.persistOrderEnabled,
			    enableToggle:true,
			    listeners:{
			        toggle:function (btn, pressed) {
			            this.persistOrderEnabled = pressed;
			            if (!pressed)
			                me.clearSavedOrder();
			            else
			                me.persistOrder();
			        }
			    }
			});
		}
		
		items.push({
		    xtype:'button',
		    iconCls:'icon-bullet_toggle_minus',
		    //tooltip:Trans.auto('${multis.remove.sorting}',"Remove sorting"),
            tooltip:"$common.pblinkgridreconfigwindow-sortNone",
		    reorderable:false,
		    hidden:true,
		    listeners:{
		        click:function (btn) {
		            me.resetSort();
		            me.toolBarText();
		        }
		    }
		});

        this.tbar = grid.addDocked({
            xtype:'toolbar',
            dock:'top',
            plugins:[this.reorderer, this.droppable],
            items: items
        })[0];            
        this.reorderer.sortPlugin =
            this.droppable.sortPlugin = this;     
    },

    /**
     * Saves the sort order to the local storage
     */
    persistOrder:function () {
    	if(this.enablePersistance) {
	        this.persistOrderEnabled = true;
	        this.localStorageProvider.set(this.grid.id, this.getSorters());
    	}
    },

    /**
     * Removes the sort order from the local storage
     */
    clearSavedOrder:function () {
    	if(this.enablePersistance) {
	        this.persistOrderEnabled = false;
	        this.localStorageProvider.clear(this.grid.id);
    	}
    },

    /**
     * Tells the store to sort itself according to our sort data
     */
    doSort:function () {
        var s = this.grid.store;
        s.sort(this.getSorters());

    },


    /**
     * Callback handler used when a sorter button is clicked or reordered
     * @param Ext.Button button The button that was clicked
     * @param {Boolean} changeDirection True to change direction (default). Set to false for reorder
     * operations as we wish to preserve ordering there
     */
    changeSortDirection:function (button, changeDirection) {
        var sortData = button.sortData,
            iconCls = button.iconCls;

        if (sortData) {
            if (changeDirection !== false) {
                button.sortData.direction = Ext.String.toggle(button.sortData.direction, "ASC", "DESC");
                button.setIconCls(Ext.String.toggle(iconCls, "sort-asc", "sort-desc"));
            }

            this.doSort();
        }
    },


    /**
     * Returns an array of sortData from the sorter buttons
     * @return {Array} Ordered sort data from each of the sorter buttons
     */
    getSorters:function () {
        var sorters = [];
        Ext.each(this.tbar.query('button'), function (button) {
            if (button.sortData)
                sorters.push(button.sortData);
        }, this);
        return sorters;
    },

    /**
     * Convenience function for creating Toolbar Buttons that are tied to sorters
     * @param {Object} config Optional config object
     * @return {Object} The new Button configuration
     */
    createSorterButtonConfig:function (config) {
        config = config || {};
        config.text = config.sortData.text;
        Ext.applyIf(config, {
            listeners:{
                scope:this,
                click:function (button, e) {
                    this.changeSortDirection(button, true);
                },
                arrowclick:function (button, e) {
                    var idx = this.droppable.calculateEntryIndex(e);
                    this.tbar.remove(idx-1);
                    this.doSort();
                    this.toolBarText();
                    if (this.persistOrderEnabled)
                        this.persistOrder();

                    this._removeBtn(button);
                },
                afterrender:function (btn) {
                    btn.el.highlight("ffff9c", {duration:200 });
                }
            },
            iconCls:'sort-' + config.sortData.direction.toLowerCase(),
            arrowCls:"splitdelete",
            reorderable:true,
            xtype:'splitbutton'
        });
        return config;
    },

    toolBarText:function () {
		var l = this.tbar.items.length,
			v = l > (this.enablePersistance ? 4 : 3),
			is = this.tbar.items;
		is.get(0).setVisible(v);
		is.get(l - 1).setVisible(v);
		if(this.enablePersistance) {
			is.get(l - 2).setVisible(v);
		}
    },

    resetSort:function () {
        while (this.tbar.items.length > (this.enablePersistance ? 4 : 3))
            this.tbar.remove(1);
        if(this.enablePersistance) {
            if (this.persistOrderEnabled)
                this.persistOrder();        	
        }
        
        // -- remove sort arrow :)
        this._clearOtherSortStates(null);
        //this.doSort();
    },

    addButton:function (property, text, direction) {
        btn = this.createSorterButtonConfig({
            sortData:{
                text:text,
                property:property,
                direction:direction
            }
        });
        
        var len = this.tbar.items.length,
        	pos = len - (this.enablePersistance ? 3 : 2);
    	this.tbar.insert(pos, btn);
    	if(this.enablePersistance) {
			if (this.persistOrderEnabled)
				this.persistOrder();
    	}
    },
    
    onSortChange:function (ct, column, direction, text) {

        if (column.avoidSort){
            return;
        }
        
        var btn, 
        	btnText = text ? text : column.text;
        this.tbar.items.each(function (i) {
            if (i.sortData && i.sortData.property == column.dataIndex)
                btn = i;
        });
        if (btn) {
            this.changeSortDirection(btn, true);
            btn.el.highlight("ffff9c", {duration:200 });
        } else {
            this.addButton(column.dataIndex, btnText, "ASC");
            this.doSort();
            this.toolBarText();

        }
    },


    onAfterReconfigure:function (grid,store,cols) {
        var me = this,
        	columns = grid.getView().getGridColumns();
        
		if (!cols){
			return;
		}
        	
        Ext.each(columns,function(header) {
            if(header.dataIndex !== null && !header.isSubHeader){
            	header.addListener('headerclick',function(ct,column,e,t){
                        me.onSortChange(ct, column);
            	});
            }else if (header.isSubHeader){ // if have the header grouped
        		header.addListener('headerclick',function(ct,column,e,t){
                    me.onSortChange(ct, column, null, header.up().text + '(' + header.text + ')');
        		});
            }
        });

        var headerCt = grid.child("headercontainer");
		if (headerCt.reorderer.dragZone){
        	this.droppable.addDDGroup(headerCt.reorderer.dragZone.ddGroup);
		}
    },
    
    onColumnHide: function( ct, column, eOpts ) {
		var me = this,
			btn = me._findBtn(column);
		me._removeBtn(btn);
	},
	
	_findBtn:function(column) {
		var me = this;
		var btn;
        me.tbar.items.each(function (b) {
            if (b.sortData && b.sortData.property == column.dataIndex)
                btn = b;
        });
		return btn;
	},
	
	_removeBtn: function(btn) {
		if(btn) {
	        this.tbar.remove(btn);
	        this.doSort();
	        this.toolBarText();
	        if(this.enablePersistance) {
		        if (this.persistOrderEnabled)
		            this.persistOrder();
	        }
		}
	},
	
	_clearOtherSortStates: function(activeHeader) {
		var headerCt  = this.grid.headerCt,
        	headers   = headerCt.getGridColumns(),
            headersLn = headers.length,
            i         = 0;

        for (; i < headersLn; i++) {
            if (headers[i] !== activeHeader) {
                // unset the sortstate and dont recurse
                headers[i].setSortState(null, true);
            }
        }
    }
     
});


