/**
 * Defines the Master Key.
 */
Ext.define('Ext.ux.data.field.PblinkMasterKey', {
    extend: 'Ext.data.field.Field',
    alias: 'data.field.pblinkmasterkey',
	compare:function(a,b) {
		return a==b?0:(a>b?1:-1);
	},
    getType: function() {
        return 'pblinkmasterkey';
    }
});

