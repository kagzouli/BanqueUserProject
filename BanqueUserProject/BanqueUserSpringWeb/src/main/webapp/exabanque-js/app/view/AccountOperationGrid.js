Ext.define('exabanque.view.AccountOperationGrid', {
    extend: 'Ext.grid.Panel',
    xtype: 'accountOperationGrid',

    title: 'AccountOperationList',

    selType: 'rowmodel',
    viewModel: { type: 'accountOperationViewModel' },
    selModel:
    {
        mode: 'SINGLE'
    },
    viewConfig:
    {
        stripeRows: true
    },
    bind: {
        store: '{accountOperationStore}'
    },
    constructor : function(config){
    //    this.initConfig(config);
        return this.callParent(arguments);
    },
    initComponent: function () {

        Ext.apply(this, {

            columns: [{
                xtype: 'datecolumn',
            	text: "Operation date",
                flex: 1,
                dataIndex: "operationDate",
                format: 'd/m/Y H:i:s',
               // format: 'c',
                //dateFormat:'c'

            },
            {
                text: "Operation type",
                flex: 1,
                dataIndex: 'operationType',
            },
            {
                text: "Label operation",
                flex: 1,
                dataIndex: 'label',
            },
            {
                text: "Amout",
                flex: 1,
                dataIndex: 'amount',
            }
            
            ]
        });

        this.callParent(arguments);
    }
    });