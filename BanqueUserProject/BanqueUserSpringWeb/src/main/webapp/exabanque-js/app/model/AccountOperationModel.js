Ext.define('exabanque.model.AccountOperationModel', {
    extend: 'Ext.data.Model',
  
    fields: [{  
    	name: 'userIdentifier',  
    	type: 'string'  
    }, {  
    	name: 'label',  
    	type: 'string'  
    }, {  
    	name: 'amount',  
    	type: 'float'  
    },{  
    	name: 'operationType',  
    	type: 'string'  
    },{  
    	name: 'operationDate',  
    	type: 'date',
    	dateFormat: 'c'
    }
    ]  
});




