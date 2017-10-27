Ext.define('exabanque.view.AccountOperationReqForm', {
	extend : 'Ext.form.Panel',

	xtype : 'accountOperationForm',
	title : 'Search account operation',

	requires : [ 'exabanque.controller.AccountOperationReqController',
			'exabanque.view.UsersListCombo' ],
	viewModel : 'accountOperationViewModel',
	controller : 'accountoperation',
	buttonAlign : 'center',
	layout : {
		type : 'hbox'
	},
	items : [ {
		id : 'user_Identifier',
		name : 'userIdentifier',
		fieldLabel : 'User identifier',
		xtype : 'userlistcombo',
		width : 500,
		height : 100,
		fieldStyle: { 'width' : '130px'}		
	}, {
		id : 'begin_date',
		name : 'beginDate',
		fieldLabel : 'Begin date',
		xtype : 'datefield',
		anchor : '100%',
		format : 'd/m/Y',
		submitFormat: 'c',
		width : 430,
		editable : false,
		fieldStyle: { 'width' : '70px'}		


	}, {
		id : 'end_date',
		name : 'endDate',
		fieldLabel : 'End date',
		xtype : 'datefield',
		anchor : '100%',
		format : 'd/m/Y',
		//submitFormat: 'Y-m-d H:i:s',
		submitFormat: 'c',
		width : 430,
		fieldStyle: { 'width' : '70px'}		

	// editable: false
	} ],
	buttons : [ {
		text : 'Search',
		itemId : 'btnSearch',
		// formBind: true,
		handler : 'onSearchClick'
	} ],

	initComponent : function() {
		this.callParent(arguments);

	}

});