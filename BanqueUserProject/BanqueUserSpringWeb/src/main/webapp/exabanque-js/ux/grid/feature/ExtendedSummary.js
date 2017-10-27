/**
 * The extended summary feature using in the pblinkgrid summary 
 */
Ext.define('Ext.ux.grid.feature.ExtendedSummary', {

    /* Begin Definitions */

    extend: 'Ext.grid.feature.AbstractSummary',

    alias: 'feature.extendedsummary',

    /**
     * @cfg {String} dock
     * Configure `'top'`, `'bottom'` or `both` create a fixed summary row either above, below or both the scrollable table.
     *
     */
    dock: undefined,

    dockedSummaryCls: Ext.baseCSSPrefix + 'docked-summary',

    summaryItemCls: Ext.baseCSSPrefix + 'grid-item-summary',

    panelBodyCls: Ext.baseCSSPrefix + 'summary-',

    scrollPadProperty: 'padding-right',

    staticMode: false,

    // turn off feature events.
    hasFeatureEvent: false,

    init: function(grid) {
        var me = this,
            view = me.view;

        me.callParent(arguments);

        if (me.dock) {
            grid.headerCt.on({
                add: me.onStoreUpdate,
                afterlayout: me.onStoreUpdate,
                scope: me
            });
            grid.on({
                beforerender: function() {
                    var tableCls = [me.summaryTableCls];
                    if (view.columnLines) {
                        tableCls[tableCls.length] = view.ownerCt.colLinesCls;
                    }
                    me.summaryBar = grid.addDocked({
                        childEls: ['innerCt', 'item'],
                        renderTpl: [
                            '<div id="{id}-innerCt" data-ref="innerCt" role="presentation">',
                            '<table id="{id}-item" data-ref="item" cellPadding="0" cellSpacing="0" class="' + tableCls.join(' ') + '">',
                            '<tr class="' + me.summaryRowCls + '"></tr>',
                            '</table>',
                            '</div>'
                        ],
                        scrollable: {
                            x: false,
                            y: false
                        },
                        hidden: !me.showSummaryRow,
                        itemId: 'summaryBar' + me.dock,
                        cls: [ me.dockedSummaryCls, me.dockedSummaryCls + '-' + me.dock ],
                        xtype: 'component',
                        dock: me.dock,
                        weight: 10000000
                    })[0];
                },
                afterrender: function() {
                    grid.body.addCls(me.panelBodyCls + me.dock);
                    view.on('scroll', me.onViewScroll, me);
                    me.onStoreUpdate();
                },
                single: true
            });

            // Stretch the innerCt of the summary bar upon headerCt layout
            grid.headerCt.afterComponentLayout = Ext.Function.createSequence(grid.headerCt.afterComponentLayout, function() {
                var width = this.getTableWidth(),
                    innerCt = me.summaryBar.innerCt;

                me.summaryBar.item.setWidth(width);

                // "this" is the HeaderContainer. Its tooNarrow flag is set by its layout if the columns overflow.
                // Must not measure+set in after layout phase, this is a write phase.
                if (this.tooNarrow) {
                    width += Ext.getScrollbarSize().width;
                }
                innerCt.setWidth(width);
            });
        } else {
            me.view.addFooterFn(me.renderSummaryRow);
        }

        grid.on({
            columnmove: me.onStoreUpdate,
            scope: me
        });

        // On change of data, we have to update the docked summary.
        view.mon(view.store, {
            update: me.onStoreUpdate,
            datachanged: me.onStoreUpdate,
            scope: me
        });
    },

    renderSummaryRow: function(values, out, parent) {
        var view = values.view,
            me = view.findFeature('summary'),
            record;

        if (me.showSummaryRow) {
            record = me.summaryRecord;

            out.push('<table class="' + Ext.baseCSSPrefix + 'table-plain ' + me.summaryItemCls + '">');
            me.outputSummaryRecord((record && record.isModel) ? record : me.createSummaryRecord(view), values, out, parent);
            out.push('</table>');
        }
    },

    toggleSummaryRow: function(visible /* private */, fromLockingPartner) {
        var me = this,
            bar = me.summaryBar;

        me.callParent([visible, fromLockingPartner]);
        if (bar) {
            bar.setVisible(me.showSummaryRow);
            me.onViewScroll();
        }
    },

    getSummaryBar: function() {
        return this.summaryBar;
    },

    vetoEvent: function(record, row, rowIndex, e) {
        return !e.getTarget(this.summaryRowSelector);
    },

    onViewScroll: function() {
        this.summaryBar.setScrollX(this.view.getScrollX());
    },

    createSummaryRecord: function (view) {
        var me = this,
            columns = view.headerCt.getVisibleGridColumns(),
            remoteRoot = me.remoteRoot,
            summaryRecord = me.summaryRecord,
            colCount = columns.length, i, column,
            dataIndex, summaryValue, modelData;

        if (!summaryRecord) {
            modelData = {
                id: view.id + '-summary-record'
            };
            summaryRecord = me.summaryRecord = new Ext.data.Model(modelData);
        }

        // Set the summary field values
        summaryRecord.beginEdit();

        if (remoteRoot && view.store.proxy.reader.rawData) {
            summaryRecord.set(me.generateSummaryData());
        } else if (!remoteRoot) {
            for (i = 0; i < colCount; i++) {
                column = columns[i];

                // In summary records, if there's no dataIndex, then the value in regular rows must come from a renderer.
                // We set the data value in using the column ID.
                dataIndex = column.dataIndex || column.getItemId();

                // We need to capture this value because it could get overwritten when setting on the model if there
                // is a convert() method on the model.
                summaryValue = me.getSummary(view.store, column.summaryType, dataIndex);
                summaryRecord.set(dataIndex, summaryValue);

                // Capture the columnId:value for the summaryRenderer in the summaryData object.
                me.setSummaryData(summaryRecord, column.getItemId(), summaryValue);
            }
        }

        summaryRecord.endEdit(true);
        // It's not dirty
        summaryRecord.commit(true);
        summaryRecord.isSummary = true;

        return summaryRecord;
    },

    setStaticSummaryRecord:function(record){
      this.staticRecord = record;
    },
    
    getStaticSummaryRecord:function(){
        var me = this,
            view = me.view;
        if (!this.staticRecord){
            //var model = view.grid.getViewModel().getStore('GridStore').getModel();
            //this.staticRecord = Ext.create(model);
            this.staticRecord = me.createSummaryRecord(view);
        }
        return this.staticRecord;
    },

    onStoreUpdate: function() {
        var me = this,
            view = me.view;

        if (!view.rendered) {
            return;
        }

        var record =  me.staticMode ? this.getStaticSummaryRecord() : me.createSummaryRecord(view);
        
        me.onUpdateSummaryRow(record);
        
    },

    onUpdateSummaryRow:function(record){
        var me = this,
            view = me.view,
            newRowDom = view.createRowElement(record, -1).firstChild.firstChild,
            oldRowDom,
            p;

        // Summary row is inside the docked summaryBar Component
        if (me.dock) {
            p = me.summaryBar.item.dom.firstChild;
            oldRowDom = p.firstChild;
        }
        // Summary row is a regular row in a THEAD inside the View.
        // Downlinked through the summary record's ID'
        else {
            oldRowDom = me.view.getRow(record);
            p = oldRowDom ? oldRowDom.parentNode : null;
        }

        if (p) {
            p.insertBefore(newRowDom, oldRowDom);
            p.removeChild(oldRowDom);
        }
        // If docked, the updated row will need sizing because it's outside the View
        if (me.dock) {
            me.onColumnHeaderLayout();
        }
    },

    // Synchronize column widths in the docked summary Component
    onColumnHeaderLayout: function() {
        var view = this.view,
            columns = view.headerCt.getVisibleGridColumns(),
            column,
            len = columns.length, i,
            summaryEl = this.summaryBar.el,
            el;

        for (i = 0; i < len; i++) {
            column = columns[i];
            el = summaryEl.down(view.getCellSelector(column));
            if (el) {
                el.setWidth(column.width || (column.lastBox ? column.lastBox.width : 100));
            }
        }
    }
});
