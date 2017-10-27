Ext.define('exabanque.model.AccountOperationReqParam', {
    extend: 'Ext.data.Model',
  
    fields: [
        { name: 'userIdentifier', type: 'string'},
        { name: 'beginDate', type: 'date',format: 'd/m/Y H:i:s' },
        { name: 'endDate', type: 'string', format: 'd/m/Y H:i:s' },
    ]
});




