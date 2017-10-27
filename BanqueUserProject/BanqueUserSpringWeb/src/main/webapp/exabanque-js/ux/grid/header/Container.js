/**
 * Sort header menu columns according to some criteria.
 * Adapted from:
 * https://www.sencha.com/forum/showthread.php?28517&p=1109252&viewfull=1#post1109252
 */
Ext.define('Ext.ux.grid.header.Container', {
    override: 'Ext.grid.header.Container',

    getColumnMenu:function(headerCt) {
        var me = this,
    	item, i,
        // Retrieve all menu items and search for the column item
    	menuItems = me.callParent([headerCt]);

        if(!menuItems) return;

    	// Save column order, just in case
    	for(i = 0; i < menuItems.length; i++)
    		menuItems[i]['ord']=i+1;
        //Start of configurability (only change)
        if (me.grid.headerFn && me[me.grid.headerFn]) {
            Ext.Array.sort(menuItems,me[me.grid.headerFn]);
        }
        return menuItems;
    },/* */

    /* ***********************************************************************
			   Sorting Functions available to getMenuItems
    *********************************************************************** */

    /**
     * Sort hidden column names alphabetically but keep visible columns current order.
     */
    alphabetical: function(a,b) {
		// if selected column, use current order
		if(a.checked && b.checked) return (a.ord-b.ord);
		// selected columns takes precedence
		if(a.checked) return -1;
		if(b.checked) return 1;

		// otherwise sort alphabetically (trim spaces and ignore case)
		return a.text.trim().localeCompare(b.text.trim());
    }

});

