/**
 * Grid plugin to allow multi-column sorting with drop event.
 */
Ext.define('Ext.ux.plugin.grid.MultiSortdroppable', {
    extend:'Ext.ux.ToolbarDroppable',
    
    requires:['Ext.ux.BoxReorderer'],

    constructor:function (test) {

    },
    /**
     * Creates the new toolbar item from the drop event
     */
    createItem:function (data) {
        var header = data.header,
            headerCt = header.ownerCt,
            reorderer = headerCt.reorderer;

        // Hide the drop indicators of the standard HeaderDropZone
        // in case user had a pending valid drop in
        if (reorderer) {
            reorderer.dropZone.invalidateDrop();
        }

        return this.sortPlugin.createSorterButtonConfig({
            sortData:{
                text:header.text,
                property:header.dataIndex,
                direction:"ASC"
            }
        });

    },

    /**
     * Custom canDrop implementation which returns true if a column can be added to the toolbar
     * @param {Object} data Arbitrary data from the drag source. For a HeaderContainer, it will
     * contain a header property which is the Header being dragged.
     * @return {Boolean} True if the drop is allowed
     */
    canDrop:function (dragSource, event, data) {
        var sorters = this.sortPlugin.getSorters(),
            header = data.header,
            length = sorters.length,
            entryIndex = this.calculateEntryIndex(event),
            targetItem = this.toolbar.getComponent(entryIndex),
            i;

        // Group columns have no dataIndex and therefore cannot be sorted
        // If target isn't reorderable it could not be replaced
        if (!header.dataIndex) {
            return false;
        }

        for (i = 0; i < length; i++) {
            if (sorters[i] && sorters[i].property == header.dataIndex) {
                return false;
            }
        }
        return true;
    },

    calculateEntryIndex:function (e) {
        var res = this.callParent(arguments);
        if (res > this.toolbar.items.length - 2)
            res = this.toolbar.items.length - 2;
        if (res==0)
            res = 1;
        return res;
    },

    afterLayout:function () {
        this.sortPlugin.toolBarText();
        this.sortPlugin.doSort();
    }

});