Ext.define('exabanque.view.AccountOperationViewModel', {
    extend: 'Ext.app.ViewModel',
    alias:'viewmodel.accountOperationViewModel',
    requires: [
          	    'exabanque.model.AccountOperationModel',
    ],
    stores: {
    	accountOperationStore: {
            model: 'exabanque.model.AccountOperationModel',
            pageSize: maxElementsOperationAccount,
            sorters: [{
                property: 'operationDate',
                direction:'DESC'
            }]
        }
    }

});