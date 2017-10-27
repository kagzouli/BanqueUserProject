Ext.define('exabanque.controller.AccountOperationReqController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.accountoperation',
    
    stores: ['accountOperationStore'],
    
    requires: [
       	    'exabanque.view.AccountOperationViewModel',
       	],
           
    onSearchClick: function (sender, record) {
        var operationAccountForm = this.getView().getForm();
        var record = operationAccountForm.getValues();
        
                
        var panel = Ext.getCmp('account_Operation_Grid');  
        var accountOperationStore =  panel.getStore();
        
        Ext.Ajax.request({
            url: contextPath+ '/listOperations',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            params : Ext.util.JSON.encode(record),
            success: function(response, opts) {
            	var responseText = response.responseText;
            	try {
	                var result = Ext.util.JSON.decode(responseText);
	                accountOperationStore.loadData(result);
            	}catch(e){
            		if ("exabanque.usernotfound.error" == responseText){
            			Ext.MessageBox.alert('Error', 'The user does not exists');
            		}
            	}
            },
            failure: function(response, opts) {
            	console.log('server-side failure with status code ' + response.status);
            }
            	
        });
        
      
        
   
 
    },

    onResetClick: function (sender, record) {
        this.getView().getForm().reset();
    },
    onClearClick: function (sender, record) {
        this.getView().clearForm();
    },

});