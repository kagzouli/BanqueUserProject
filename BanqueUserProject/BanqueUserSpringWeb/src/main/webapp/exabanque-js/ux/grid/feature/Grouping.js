Ext.define('Ext.ux.grid.feature.Grouping', {
    override: 'Ext.grid.feature.Grouping',
    showMenuBy: function(clickEvent, t, header) {
        var me=this,groupMenuItem = me.getMenu().down('#groupMenuItem');
        if(!groupMenuItem)
        	me.menu=null; // reset menu, call parent and hope for the best :-/
        me.callParent(arguments);
    }
});