/**
 * JSON writer
 */
Ext.define('Ext.ux.writer.JsonAssociatedWriter', {
    extend: 'Ext.data.writer.Json',
    alias: 'writer.associatedjsonwriter',

    constructor: function(config) {
        this.callParent(arguments);
    },

    getRecordData: function (record, operation) {
        record.data = this.callParent(arguments);
        if (record.getAssociatedData()){
        	for(var propertyName in record.getAssociatedData()) {
        		if(record[propertyName])
        			record[propertyName]().clearFilter();
        	};
        	Ext.apply(record.data, record.getAssociatedData());
        }
        return record.data;
    }
});