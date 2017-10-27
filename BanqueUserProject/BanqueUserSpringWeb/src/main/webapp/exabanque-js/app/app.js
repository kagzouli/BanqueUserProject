/*Ext.Loader.setConfig({
	disableCaching : false,
	paths : {
		'Ext.ux' : '../exabanque-js/ux',
	}
})*/;

Ext.application({
	name : 'exabanque',
	appFolder : contextPath + '/exabanque-js/app',
	autoCreateViewport: false,
	views: ['exabanque.view.AccountOperationGrid', 'exabanque.view.AccountOperationReqForm'],
	requires: ['exabanque.model.AccountOperationModel', 'exabanque.view.AccountOperationViewModel','exabanque.model.UserModel'],
	launch : function() {
		Ext.create('Ext.container.Viewport', {
            layout: 'border',
            id: 'searchViewportOperation',
            items: [
                    {
                        region: 'north',
                        xtype: 'accountOperationForm',
                        id: 'account_operation_formid'
                    },
                {
                    region: 'center',
                    xtype: 'accountOperationGrid',
                    	
                    id: 'account_Operation_Grid',
                }]
        });
    }
});
