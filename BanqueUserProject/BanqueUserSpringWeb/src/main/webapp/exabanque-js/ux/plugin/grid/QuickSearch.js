/**
 *  GridPanel plugin. Allows quick search capabilities on localy loaded data loaded.
 */

Ext.define('Ext.ux.plugin.grid.QuickSearch', {
    extend: 'Ext.AbstractPlugin',
    alias: 'plugin.quicksearch',
    pluginId:"quicksearch",

    /**
     * @cfg {Boolean} matchMultiword
     * Matches multi-word
     */
    matchMultiword: true,
    /**
     * @cfg {Boolean} matchBegin
     * Matches begin of the search string
     */
    matchBegin: true,
    /**
     * @cfg {Boolean} matchEnd
     * Matches end of the search string
     */
    matchEnd: true,
    /**
     * @cfg {Boolean} matchMiddle
     * Matches middle of the search string
     */
    matchMiddle: true,
    /**
     * @cfg {Boolean} ignoreCase
     * Case insensitive
     */
    ignoreCase: true,
    
    /**
     * @cfg {Boolean} showLabel
     * Shows quick-search label
     */
    showLabel: true,

    config: {

        /**
         * LiveSearch delay in milliseconds
         */
        liveSearchDelay: 200,

        /**
         * LiveSearch on/off : search while user is typing (with a configurable delay after typing pause)
         */
        liveSearch: true,

        /**
         * QuickSearch toolbar docking : tom/bottom
         */
        dock: "bottom",

        /**
         * Defines if the QuickSearch toolbar is always visible or appears when user starts typing
         */
        alwaysVisible: false,
        /**
         * @cfg ignoreColumns Grid columns to ignore
         * @cfg {Boolean} ignoreColumns.col1 if true, ignore col1
         * @cfg {Boolean} ignoreColumns.col2 if true, ignore col2

         * ...
         */
        ignoreColumns: {},

        /**
         * @cfg colMatchers Custom match for a given column.
         * The function are called with the following parameters:
         * (this, searchWord, recordData, dataIndex)
         * Must return a position of the matching searchWord for highlighting, or -1 if searchWord not found
         *
         * @cfg {Function} colMatchers.col1 Custom matcher for col1
         * @cfg {Function} colMatchers.col2 Custom matcher for col1
         *
         * ...
         * @cfg {Function} colMatchers.colN Custom matcher for colN
         */
        colMatchers: {},

        /**
         * @cfg {String} insertInto
         *
         * Used in case is the QuickSearch input element must be inserted in an existing toolbar.
         * Contains a css selector to identify the toolbar
         */
        insertInto: null,

        /**
         * @cfg {Number} insertAt
         *
         * Used with insertInto. Defined the position at which the quicksearch element will be inserted.
         * Value <null> means "append"
         */
        insertAt: null
    },

    /**
     * Plugin initialisation
     * @param grid
     */
    init: function (grid) {
    	
        var me = this;
        grid.addListener('beforerender', this.onBeforeRender, this);
        grid.addListener('afterrender', this.onAfterRender, this);

        this.grid = grid;
        
        this.items = [];
        
        this.items.push({
            xtype:"tbfill",
            reorderable:false
        });
        
        if(me.showLabel) {
	        this.items.push({
	    		xtype: 'label', 
	    		//text: Trans.auto('${qs.quick.search.label}',"Quick Search"),
                text: '$common-quickSearch',
	    		margin: '0 10 0 0',
	    		//style: 'font-weight: bold',
	    		cls:'quicksearch-label'
	    	});
        }

        this.items.push({
            itemId: "tfQuickSearch",
            xtype: 'textfield',
			cls: 'quicksearch-text',
			height:13,
			margin:'-3 0 0 1',
            enableKeyEvents: true,
            listeners: {
            	search: function(value){
            		me.search(value);
            	},
                keydown: function (textfield, event) {
                    if (event.getKey() == 13) {
                        me.search(textfield.getValue());
                        event.stopPropagation();
                    } else if (event.getKey() == 27) {
                        me.close();
                        event.stopPropagation();
                    }
                },
                keyup: function () {
                    if (me.liveSearch)
                        me.planSearch();
                }
            }
        });
            
        this.items.push({
            //xtype: 'splitbutton',
        	
            //iconCls: "icon-zoom",
        	
        	
        	xtype: 'button',
            //disabledCls: 'none',
            //frame: false,
            cls: 'quicksearch-trigger',
            arrowVisible:false,
            //baseCls:'x-btn-clear',
        	
        	
            //tooltip: 'Quick Search',
            handler: function () {
                //me.search(me.grid.down("#tfQuickSearch").getValue());
            	
            	//console.log('Click');
            },
            menu: {
                xtype: 'menu',
                items: [
                    {
                        itemId: "matchBegin",
                        //text: Trans.auto('${qs.match.beginning}',"Search in the beginning"),
                        text : '$common-searchBeginning',
                        checked: true,
                        checkHandler: this.onItemCheck,
                        scope: this
                    },
                    {
                        itemId: "matchEnd",
                        //text: Trans.auto('${qs.match.ending}',"Search at the end"),
                        text : '$common-searchEnd',
                        checked: true,
                        checkHandler: this.onItemCheck,
                        scope: this
                    },
                    {
                        itemId: "matchMiddle",
                        //text: Trans.auto('${qs.match.in.middle}',"Search in the middle"),
                        text : '$common-searchMiddle',
                        checked: true,
                        checkHandler: this.onItemCheck,
                        scope: this
                    },
                    {
                        itemId: "matchMultiword",
                        //text: Trans.auto('${qs.smart.multi.word.search}',"Multi-word matching"),
                        text : '$common-searchMultiWord',
                        checked: true,
                        checkHandler: this.onItemCheck,
                        scope: this
                    },
                    {
                        itemId: "ignoreCase",
                        //text: Trans.auto('${qs.ignore.case}',"Ignore case"),
                        text: '$common-searchIgnoreCase',
                        checked: true,
                        checkHandler: this.onItemCheck,
                        scope: this
                    }
                ]
            }
        });

        if (!this.insertInto) {
            this.items.push({
                xtype: 'button',
                iconCls: 'icon-decline',
                //tooltip: Trans.auto('${qs.clear}',"Clear"),
                listeners: {
                    click: function () {
                        me.close();
                    }
                }
            });
        }

        if (!this.insertInto) {
            this.tbar = grid.addDocked({
                xtype: 'toolbar',
                dock: this.dock,
                hidden: !this.alwaysVisible,
                items: this.items
            })[0];
        }
    },

    /**
     * @private
     * Called when the configuration drop-down item is updated by user
     * @param item
     * @param checked
     */
    onItemCheck: function (item, checked) {
        this[item.itemId] = checked;
    },

    /**
     * Attaches cell renderers;
     * @param g
     */
    onBeforeRender: function (g) {

        var columns = g.view.getHeaderCt().getGridColumns();

        for (var i = 0; i < columns.length; i++) {
            var col = columns[i];
            if (this.ignoreColumns[col.dataIndex])
                continue;

            col.oldRenderer = col.renderer;
            columns[i].renderer = this.getRenderer(columns[i]);
        }
    },

    getRenderer:function(column){
        var col = column;
        return function (value, metaData, record, rowIndex, colIndex, store, view) {
            var res = value;
            if (col.oldRenderer)
                res = col.oldRenderer.call(this, value, metaData, record, rowIndex, colIndex, store, view);
            var highlights = record.highlight;
            var dataIndex = col.dataIndex;
            if (highlights && highlights[dataIndex]) {
                var highlight = highlights[dataIndex];
                if (res && res.length >= highlight.to && highlight.from >= 0) {
                    res = res.substring(0, highlight.from) +
                        "<span style=background-color:yellow;>" +
                        res.substring(highlight.from, highlight.to) +
                        "</span>" +
                        res.substring(highlight.to);
                }
            }
            return res;
        };
    },

    planSearch: function () {
        var me = this;
        me.liveSearchWakeUp = new Date().getTime() + this.liveSearchDelay;
        window.setTimeout(function () {
            if (me.liveSearchWakeUp <= new Date().getTime() + me.liveSearchDelay) {
                var searchExpr = me.grid.down("#tfQuickSearch").getValue();
                if (me.lastSearchExpr != searchExpr) {
                    me.lastSearchExpr = searchExpr;
                    me.search(searchExpr);
                }
            }
        }, me.liveSearchDelay);
    },


    /**
     * Attaches toolbar and key listeners
     * @param grid
     */
    onAfterRender: function (grid) {
        this.textfield = grid.down("#tfQuickSearch");

        if (this.insertInto) {
            if (!Ext.isEmpty(this.insertAt))
                grid.down(this.insertInto).insert(this.insertAt, this.items);
            else
                grid.down(this.insertInto).add(this.items);

        }

        var inputField = this.grid.down("#tfQuickSearch");
        inputField.on('keypress', function (el, event) {
            event.stopPropagation();

        }, this);

        grid.getEl().on('keypress', function (event) {

            var key = event.getKey();
            var tb = this.tbar;

            if (key >= 33 && key <= 122 && !event.isSpecialKey()) {
                setTimeout(function () {

                    if (tb) {
                        tb.show();
                    }

                    inputField.focus();
                    inputField.setValue(inputField.getValue() + String.fromCharCode(key));
                }, 10);
            }

        }, this);

    },


    /**
     * Performs the serach in the data of the grid
     * @param searchExpr
     */
    search: function (searchExpr) {

        // in case of datagrid having escaped characters : ",>,<,',& - escape the search phrase as well.
        //PBLINK Change - AntiXSS - Not being imported -  after import - uncomment this line.
        //2015 - 05 - 13
      //  searchExpr = AntiXSS.escapeForHTML(searchExpr);

        if (this.ignoreCase)
            searchExpr = searchExpr.toUpperCase();
        if (this.trim(searchExpr).length === 0) {
            this.grid.store.data.each(
                function (el) {
                    el.highlight = null;
                }
            );
            this.grid.store.clearFilter();
        }
        this.searchArray = this.matchMultiword ? (this.trim(searchExpr).split(/\s+/)) : [this.trim(searchExpr)];

        var s = this.grid.store;
        var view = this.grid.getView();
        var me = this;

        this.grid.store.filter([
            {
                filterFn: function (item) {
                    var highlight = me.matchLine(me.searchArray, item, 0, s, view);
                    item.highlight = highlight;
                    return highlight;
                }
            }
        ]);
    },
    
    _isToIgnoreRendererValue:function(rendererValue) {
    	var rendererValueNoTags = rendererValue.replace(/<[^>]*>?/g, function (a) {
	                    return Array(a.length + 1).join(" ");
	                });  
        return Ext.isEmpty(Ext.String.trim(rendererValueNoTags));
    },

    /**
     * Compares the grid data with the search phrase
     * @param search - search word
     * @param record
     * @param rowIndex
     * @param colIndex
     * @param dataIndex
     * @return start position of the matching search value
     */
    matchValue: function (search, record, rowIndex, colIndex, dataIndex, column, convertFunction, store, view) {
    	var me = this;
        try {
            if (this.colMatchers[dataIndex]) {
                return (this.colMatchers[dataIndex])(this, search, record.data[dataIndex], dataIndex);
            } else {
            	
            	//console.log('[type,dataIndex,text] -> ' + column.type + ', ' + column.dataIndex + ', ' + column.text);
            	var rawValue = (record.data[dataIndex]),
            		v = rawValue,
            		ignoreRendererValue = false;
                
//                console.log('rawValue -> ' + rawValue);
                
                //-- it's already converted..
//                if (convertFunction) {
//                    v = convertFunction(v, record);
//                    console.log('convertFunctionValue -> ' + v);
//                }

                if (column.oldRenderer) {
                    var rendererValue = column.oldRenderer(rawValue, {}, record, rowIndex, colIndex, store, view);
                    //console.log('rendererValue -> ' + rendererValue);
	                
                    ignoreRendererValue = me._isToIgnoreRendererValue(rendererValue);
	                if(!ignoreRendererValue) {
	                	v = rendererValue;
	                }
                }
                
                //console.log('v -> ' + v);
                
                v = ("" + v);
                if (this.ignoreCase)
                    v = v.toUpperCase();

                //strip tags and replace what's inside a tag by spaces
                v = v.replace(/<[^>]*>?/g, function (a) {
                    return Array(a.length + 1).join(" ");
                });

                var pos = v.indexOf(search);

                if (pos == -1)
                    return -1;

                if (!this.matchBegin && pos === 0)
                    pos = v.indexOf(search, 1);

                if (pos == v.length - search.length)
                    return ignoreRendererValue ? -2 : (this.matchEnd ? pos : -1);
                
                return ignoreRendererValue ? -2 : (pos === 0 ? 0 : (this.matchMiddle ? pos : -1));
            }
        } catch (e) {
            return false;
        }
    },

    /**
     * Calls matchValue for all cells if a row
     * @param searchArray
     * @param line
     * @return {Boolean}
     */
    matchLine: function (searchArray, line, rowIndex, store, view) {


        var found = true;
        var highlight = {};

        var fields = this.grid.store.model.getFields();
        var fieldConvert = {};
        for (var i = 0; i < fields.length; i++) {
            var field = fields[i];
            fieldConvert[field.name] = field.convert;
        }
        var cols = this.grid.view.getHeaderCt().getGridColumns();
        for (i = 0; i < searchArray.length; i++) {
            var s = searchArray[i];
            var partFound = false;
            for (var j = 0; j < cols.length; j++) {
                var c = cols[j];
                if (Ext.isEmpty(c.dataIndex) || this.ignoreColumns[c.dataIndex])
                    continue;

                if (!c.hidden) {
                    var pos = this.matchValue(s, line, rowIndex, j, c.dataIndex, c, fieldConvert[c.dataIndex], store, view);
                    if (pos != -1) {
                        partFound = true;
                    	highlight[c.dataIndex] = {from: pos, to: pos + s.length};
                    }
                }
            }
            if (!partFound) {
                found = false;
                break;
            }
        }

        return found ? highlight : null;

    },

    /**
     * Hides the toolbar (if not alwaysVisible=true), clears the search expression, restores original grid data.
     */
    close: function () {
        var tf = this.grid.down("#tfQuickSearch");
        tf.setValue("");

        this.grid.store.data.each(
            function (el) {
                el.highlight = null;
            }
        );
        this.grid.store.clearFilter();

        if (!this.alwaysVisible && this.tbar)
            this.tbar.hide();

        this.grid.getView().focus();
    },

    trim: function (str) {
        //return str.replace(/^\s+|\s+$/g, '');
    	return Ext.String.trim(str);
    }

})
;
