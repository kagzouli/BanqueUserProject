Ext.define('exabanque.view.UsersListCombo', {
    extend: 'Ext.form.ComboBox',
    
    alias:'widget.userlistcombo',
   store: {
    	model: 'exabanque.model.UserModel',
    	proxy: {  
    		  type: 'rest',  
    		  url:  contextPath+ '/usersList',  
    		  reader: {  
    			  type: 'json',  
    		  	  rootProperty: 'listUsersJson'  
    		  }
    	   }
    },
    displayField: 'entireName',
	valueField: 'identifierCodeUser',
	editable: false,
	columnWidth: 300

});




