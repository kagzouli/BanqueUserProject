/**
 * Messages component
 */
Ext.define('Ext.ux.plugin.messages.MessagesManagement', {
    extend: 'Ext.AbstractPlugin',
    
    alias: 'plugin.messagesManagement',
    
    alternateClassName: 'Ext.ux.MessagesManagement',
    
    uses: 'Ext.ux.plugin.messages.MessageManager',
    
    /**
     * @cfg {Boolean} isDocked
     * States if it is docked.
     */
    isDocked: true,
    
    /**
     * @cfg {Object} itemId
     * The item id.
     */
    itemId: undefined,
    
    /**
     * @cfg {Boolean} showIcon
     * Shows icon
     */
    showIcon: true,
    
    /**
     * @cfg {Boolean} XORIconMessage
     * Shows icon. Exclusive disjunction.
     */
    XORIconMessage: true,
    
    /**
     * @cfg {Boolean} closeHideIcon
     */
    closeHideIcon: true,
    
    /**
     * @cfg {String} msgUI
     * Message User Interface
     */
    msgUI: 'neolink',
    
    actionData : undefined,
    
    constructor: function(config) {
    	var me = this;
        
        config = config || {};
        me.actionData = { result: { globalerrors: [], infos: [], warnings: [] } , success: true };

        me.callParent([config]);
    },
    
    init: function(panel) {
    	var me = this;
    	this.container = panel;
		this.template = Ext.create('Ext.XTemplate', '<ul class="' + Ext.plainListCls + '"><tpl for="."><li><span class="field-name">{.}</span></li></tpl></ul>'),    	
    	
    	this.callParent();
    	
    	if(me.closeHideIcon && !me.XORIconMessage) {
    		me.XORIconMessage = true;
    	}
    	
    	if(me.XORIconMessage && !me.showIcon) {
    		me.showIcon = true;
    	}
    	
		if (this.msgUI=='classic'){
			this._initObjectsClassicUI();
		}else{
			this._initObjectsDefaultUI();
		}
		
    	this.infoLabel =  Ext.create('Ext.form.Label',{
			padding:'1 0 0 5'
    	});
    	this.container =  Ext.create('Ext.container.Container',{
    		//id:'closeContainer',
    		layout:'hbox',
    		padding:'11 0 0 10',
    		hidden:true,
    		border:false,
    		items:[
				this.errorContainer,
				this.warnContainer,
				this.infoContainer,
				this.infoLabel
    		]
    	});
    	
    	this.boxMessages = Ext.create('Ext.container.Container',{
    		padding: me.isDocked ? '10 10 0 10' : 0,
    		listeners:{
            	render: function(comp) {
            		comp.add(me.boxErrorContainer);
            		comp.add(me.boxWarnContainer);
            		comp.add(me.boxInfoContainer);
            	}
            }
    	});
    	
    	panel.on('render', function(panel,eOpts){
    		if (me.isDocked){
    			panel.addDocked(me.boxMessages);
    			panel.addDocked(me.container);
    		}else{
    			panel.add(me.container);
    			if (panel.up('container')){
    				panel.up('container').down('container[reference=messageBox]').insert(0,me.boxMessages);
    			}else{
    				panel.down('container[reference=messageBox]').insert(0,me.boxMessages);
    			}
    		}
    	});
    	
    	
    	this.itemId = panel.getUniqueId(); // key to message processor
    	Ext.ux.plugin.messages.MessageManager.register(this);
    	var beforeHideFn = function() {
    		me.cleanAllMessages();
    	}; 
    	this.container.on("beforedeactivate", beforeHideFn, this);
    	
    	// adds possibility for target component to clean its own messages
    	panel.cleanMessages = function() {
    		me.cleanAllMessages();
    	};
    },
    
    
    _merge: function (arrayA, arrayB) {
    	if (arrayB) {
    		for (var i=0, len=arrayB.length; i<len; i++) {
    			if (arrayA.indexOf(arrayB[i])==-1) {
    				arrayA.push(arrayB[i]);
    			}
    		}
    	}
    },
    
    
    processMessages: function(action, labelMessage){
		var me = this;
		
		Ext.suspendLayouts();
		
		me.errorContainer.setVisible(false);
		me.warnContainer.setVisible(false);
		me.infoContainer.setVisible(false);
		
		me.boxErrorContainer.setVisible(false);
		me.boxWarnContainer.setVisible(false);
		me.boxInfoContainer.setVisible(false);
		
		// merge action to actionData
		me._merge(me.actionData.result.globalerrors, action.result.globalerrors);
		me._merge(me.actionData.result.warnings, action.result.warnings);
		me._merge(me.actionData.result.infos, action.result.infos);
		
		var errors = me.actionData.result.globalerrors;
		var warns  = me.actionData.result.warnings;
		var infos  = me.actionData.result.infos;
		
		if(me.showIcon) {
		
			if ( (errors != undefined && errors != null && errors.length > 0) ||
	 			 (warns != undefined && warns != null && warns.length > 0)	||
	 			 (infos != undefined && infos != null && infos.length > 0) ){
				me.container.setVisible(true);
			}else{
				me.container.setVisible(false);
			}
		}
		
		if (errors != undefined && errors != null && errors.length > 0){
			
			me.boxErrorContainer.down('label').update(me.template.apply(errors));
			me.boxErrorContainer.setVisible(true);
			
			if (!labelMessage){
				this._updateLabelMessage(Ext.ux.plugin.messages.MessageManager.LABEL_TITLE_ERRORS);
			}else{
				this._updateLabelMessage(labelMessage);
			}
			
			if(!me.XORIconMessage) {
				me.errorContainer.setVisible(true);
			}
		}
		
		if (warns != undefined && warns != null && warns.length > 0){
			
			me.boxWarnContainer.down('label').update(me.template.apply(warns));
			
			if ((errors == undefined || errors.length == 0)){

				me.boxWarnContainer.setVisible(true);
				if (!labelMessage){
					this._updateLabelMessage('');
				}else{
					this._updateLabelMessage(labelMessage);
				}
					
				if(!me.XORIconMessage) {
					me.warnContainer.setVisible(true);
				}
			} else {
				me.warnContainer.setVisible(true);
			}
		}
		
		if (infos != undefined && infos != null && infos.length > 0){
			
			me.boxInfoContainer.down('label').update(me.template.apply(infos));
			
			if ((errors == undefined || errors.length == 0) && (warns == undefined || warns.length == 0)){

				me.boxInfoContainer.setVisible(true);
				
				if (!labelMessage){
					this._updateLabelMessage(Ext.ux.plugin.messages.MessageManager.LABEL_TITLE_INFOS);
				}else{
					this._updateLabelMessage(labelMessage);
				}
				
				if(!me.XORIconMessage) {
					me.infoContainer.setVisible(true);
				}
			} else {
				me.infoContainer.setVisible(true);
			}
		}
		
		if(me.errorContainer.isHidden() && me.warnContainer.isHidden() && me.infoContainer.isHidden()) {
			me.container.setVisible(false);
		}
		
		Ext.resumeLayouts(true);
	},
	
	_updateLabelMessage: function(labelMessage){
		
		var me = this;
		//me.infoLabel.update(labelMessage);
	},
	
	cleanAllMessages: function(){
		var me = this;
		
		var result = me.actionData.result;
		result.globalerrors = [];
		result.infos 		= [];
		result.warnings 	= [];
		result.success		= true; 
		
		me.boxErrorContainer.setVisible(false);
		me.boxWarnContainer.setVisible(false);
		me.boxInfoContainer.setVisible(false);
		
		if (me.container.rendered){
//			me.errorContainer.tip.hide();
//			me.warnContainer.tip.hide();
//			me.infoContainer.tip.hide();
//			
//			me.errorContainer.tip.update('');
//			me.warnContainer.tip.update('');
//			me.infoContainer.tip.update('');
//			me.infoLabel.update('')
			
			me.errorContainer.setVisible(false);
			me.warnContainer.setVisible(false);
			me.infoContainer.setVisible(false);
			me.container.setVisible(false);
		}
		
	},
	
	_doIconClick: function(c) {
		
		var me = this;
		
		if(me.errorContainer === c) {
			me.boxErrorContainer.setVisible(true);
		} else if(me.warnContainer === c) {
			me.boxWarnContainer.setVisible(true);
		} else if(me.infoContainer === c) {
			me.boxInfoContainer.setVisible(true);
		}
		
		if(me.XORIconMessage) {
			c.setVisible(false);
		}
		
		if(me.errorContainer.isHidden() && me.warnContainer.isHidden() && me.infoContainer.isHidden()) {
			me.container.setVisible(false);
		}
	},
	
	_doCloseMessage: function(p, e) {
		
		var me = this;
		
		if(me.XORIconMessage && !me.closeHideIcon) {
			
			if(me.boxErrorContainer === p) {
				me.errorContainer.setVisible(true);
			} else if(me.boxWarnContainer === p) {
				me.warnContainer.setVisible(true);
			} else if(me.boxInfoContainer === p) {
				me.infoContainer.setVisible(true);
			}
			
			if(me.container.isHidden()) {
				me.container.setVisible(true);
			}
		}
	},
	
	_initObjectsClassicUI: function(){
		var me = this;
		
    	this.boxErrorContainer =  Ext.create('Ext.panel.Panel',{
	    	xtype:'panel',
	        closable:true,
			layout:'hbox',
			cls: 'errors-tip',
			bodyPadding: '0 10 10 10',
			ui: 'message-comp-classic',
			hidden:true,
			closeAction:'hide',
			items:[
				{
					xtype:'container',
					cls:'icon-exclamation',
					height: 16,
					width:16
				},
				{
					xtype:'label'
				}
			],
			listeners: {
				beforeclose: {fn: me._doCloseMessage, scope: me}
			}
    	});
    	
    	this.boxWarnContainer =  Ext.create('Ext.panel.Panel',{
	    	xtype:'panel',
	        closable:true,
			layout:'hbox',
			cls: 'warn-tip',
			bodyPadding: '0 10 10 10',
			ui: 'message-comp-classic',
			hidden:true,
			closeAction:'hide',
			items:[
				{
					xtype:'container',
					cls:'icon-error',
					height: 16,
					width:16
				},
				{
					xtype:'label'
				}
			],
			listeners: {
				beforeclose: {fn: me._doCloseMessage, scope: me}
			}
    	});
    	
    	this.boxInfoContainer =  Ext.create('Ext.panel.Panel',{
	    	xtype:'panel',
	        closable:true,
			layout:'hbox',
			cls: 'info-tip',
			bodyPadding: '0 10 10 10',
			ui: 'message-comp-classic',
			hidden:true,
			closeAction:'hide',
			items:[
				{
					xtype:'container',
					cls:'icon-accept',
					height: 16,
					width:16
				},
				{
					xtype:'label'
				}
			],
			listeners: {
				beforeclose: {fn: me._doCloseMessage, scope: me}
			}
    	});
    	
    	this.errorContainer =  Ext.create('Ext.container.Container',{
			cls:'icon-exclamation',
			height: 16,
			width:16,
			hidden:true,
			style:{
    			cursor:'pointer'
    		},
			getTip: function() {
                var tip = this.tip;
                if (!tip) {
                    tip = this.tip = Ext.widget('tooltip', {
                        target: this.el,
                        title: 'Error Details:',
                        minWidth: 200,
                        autoHide: false,
                        anchor: 'top',
                        mouseOffset: [-11, -2],
                        closable: true,
                        constrainPosition: false,
                        cls: 'errors-tip',
                       	listeners:{
                           	beforeshow: function() {
                           		if (me.warnContainer.tip != undefined){
                           			me.warnContainer.tip.hide();
                           		}
                           		if (me.infoContainer.tip != undefined){
                           			me.infoContainer.tip.hide();
                           		}
                           	}
                       	}
                    });
                }
                return tip;
            },
            listeners:{
            	afterrender: function(comp) {
            		var element = comp.getEl();
            		var cpointer = comp;
            		element.on('click', function() {
            			me._doIconClick(comp);
            		});
            	}
            }
    	});
    	this.warnContainer =  Ext.create('Ext.container.Container',{
    		cls:'icon-error',
    		style:{
    			cursor:'pointer'
    		},
			height: 16,
			width:16,
			hidden:true,
            listeners:{
            	afterrender: function(comp) {
            		var element = comp.getEl();
            		var cpointer = comp;
            		element.on('click', function() {
            			me._doIconClick(comp);
            		});
            	}
            },
			getTip: function() {
                var tip = this.tip;
                if (!tip) {
                    tip = this.tip = Ext.widget('tooltip', {
                        target: this.el,
                        title: 'Warning Details:',
                        minWidth: 200,
                        autoHide: false,
                        floatable:false,
                        top:0,
                        floating:false,
                        hideDelay: 2000,
                        anchor: 'top',
                        closable: true,
                        constrainPosition: false,
                        cls: 'warn-tip',
                        listeners:{
                           	beforeshow: function() {
                           		if (me.errorContainer.tip != undefined){
                           			me.errorContainer.tip.hide();
                           		}
                           		if (me.infoContainer.tip != undefined){
                           			me.infoContainer.tip.hide();
                           		}
                           	}
                       	}
                    });
                }
                return tip;
            }
    	});
    	this.infoContainer =  Ext.create('Ext.container.Container',{
    		cls:'icon-accept',
			height: 16,
			width:16,
			hidden:true,
			style:{
    			cursor:'pointer'
    		},
			getTip: function() {
                var tip = this.tip;
                if (!tip) {
                    tip = this.tip = Ext.widget('tooltip', {
                        target: this.el,
                        title: 'Info:',
                        minWidth: 200,
                        autoHide: true,
                        hideDelay: 2000,
                        anchor: 'top',
                        mouseOffset: [-11, -2],
                        closable: true,
                        constrainPosition: false,
                        cls: 'info-tip',
                        listeners:{
                           	beforeshow: function() {
                           		if (me.errorContainer.tip != undefined){
                           			me.errorContainer.tip.hide();
                           		}
                           		if (me.warnContainer.tip != undefined){
                           			me.warnContainer.tip.hide();
                           		}
                           	}
                       	}
                    });
                }
                return tip;
            },
            listeners:{
            	afterrender: function(comp) {
            		var element = comp.getEl();
            		var cpointer = comp;
            		element.on('click', function() {
            			me._doIconClick(comp);
            		});
            	}
            }
    	});
    },
    
    _initObjectsDefaultUI: function(){
    	var me = this;
    	this.boxErrorContainer =  Ext.create('Ext.panel.Panel',{
	    	xtype:'panel',
	        closable:true,
			layout:{
				type:'hbox',
				align: 'stretch'
			},
			cls: 'block pblink-frmk-blockTool',
			bodyPadding: '0 10 10 10',
			ui: 'message-comp',
			hidden:true,
			closeAction:'hide',
			items:[
				{
					xtype:'container',
					cls:'icon2 iWarning',
					height: 32,
					width:36
				},
				{
					xtype:'container',
					cls:'innerContainer2',
					flex:1,
					items:[{
						xtype:'container',
						html:'$common-error',
						cls:'titleMsg'
					},{
						xtype:'label'
					}]
				}
			],
			listeners: {
				beforeclose: {fn: me._doCloseMessage, scope: me}
			}
    	});
    	
    	this.boxWarnContainer =  Ext.create('Ext.panel.Panel',{
    		xtype:'panel',
	        closable:true,
			layout:{
				type:'hbox',
				align: 'stretch'
			},
			cls: 'block pblink-frmk-blockTool',
			bodyPadding: '0 10 10 10',
			ui: 'message-comp',
			hidden:true,
			closeAction:'hide',
			items:[
				{
					xtype:'container',
					cls:'icon2 iAlerte',
					height: 32,
					width:36
				},
				{
					xtype:'container',
					cls:'innerContainer2',
					flex:1,
					items:[{
						xtype:'container',
						html:'$common-warning',
						cls:'titleMsg'
					},{
						xtype:'label'
					}]
				}
			],
			listeners: {
				beforeclose: {fn: me._doCloseMessage, scope: me}
			}
    	});
    	
    	this.boxInfoContainer =  Ext.create('Ext.panel.Panel',{
    		xtype:'panel',
	        closable:true,
			layout:{
				type:'hbox',
				align: 'stretch'
			},
			cls: 'block pblink-frmk-blockTool',
			bodyPadding: '0 10 10 10',
			ui: 'message-comp',
			hidden:true,
			closeAction:'hide',
			items:[
				{
					xtype:'container',
					cls:'icon2 iInformation',
					height: 32,
					width:33
				},
				{
					xtype:'container',
					cls:'innerContainer2',
					flex:1,
					items:[{
						xtype:'container',
						html:'$common-info',
						cls:'titleMsg'
					},{
						xtype:'label'
					}]
				}
			],
			listeners: {
				beforeclose: {fn: me._doCloseMessage, scope: me}
			}
    	});
    	
    	this.errorContainer =  Ext.create('Ext.container.Container',{
			cls:'icon2 iErrorSmall iconPadding',
			height: 16,
			width:15,
			hidden:true,
			style:{
    			cursor:'pointer'
    		},
			getTip: function() {
                var tip = this.tip;
                if (!tip) {
                    tip = this.tip = Ext.widget('tooltip', {
                        target: this.el,
                        title: 'Error Details:',
                        minWidth: 200,
                        autoHide: false,
                        anchor: 'top',
                        mouseOffset: [-11, -2],
                        closable: true,
                        constrainPosition: false,
                        cls: 'errors-tip',
                       	listeners:{
                           	beforeshow: function() {
                           		if (me.warnContainer.tip != undefined){
                           			me.warnContainer.tip.hide();
                           		}
                           		if (me.infoContainer.tip != undefined){
                           			me.infoContainer.tip.hide();
                           		}
                           	}
                       	}
                    });
                }
                return tip;
            },
            listeners:{
            	afterrender: function(comp) {
            		var element = comp.getEl();
            		element.on('click', function() {
            			me._doIconClick(comp);
            		});
            	}
            }
    	});
    	this.warnContainer =  Ext.create('Ext.container.Container',{
    		cls:'icon2 iWarnSmall iconPadding',
    		style:{
    			cursor:'pointer'
    		},
			height: 16,
			width:15,
			hidden:true,
            listeners:{
            	afterrender: function(comp) {
            		var element = comp.getEl();
            		element.on('click', function() {
            			me._doIconClick(comp);
            		});
            	}
            },
			getTip: function() {
                var tip = this.tip;
                if (!tip) {
                    tip = this.tip = Ext.widget('tooltip', {
                        target: this.el,
                        title: 'Warning Details:',
                        minWidth: 200,
                        autoHide: false,
                        floatable:false,
                        top:0,
                        floating:false,
                        hideDelay: 2000,
                        anchor: 'top',
                        closable: true,
                        constrainPosition: false,
                        cls: 'warn-tip',
                        listeners:{
                           	beforeshow: function() {
                           		if (me.errorContainer.tip != undefined){
                           			me.errorContainer.tip.hide();
                           		}
                           		if (me.infoContainer.tip != undefined){
                           			me.infoContainer.tip.hide();
                           		}
                           	}
                       	}
                    });
                }
                return tip;
            }
    	});
    	this.infoContainer =  Ext.create('Ext.container.Container',{
    		cls:'icon2 iInfoSmall iconPadding',
			height: 16,
			width:15,
			hidden:true,
			style:{
    			cursor:'pointer'
    		},
			getTip: function() {
                var tip = this.tip;
                if (!tip) {
                    tip = this.tip = Ext.widget('tooltip', {
                        target: this.el,
                        title: 'Info:',
                        minWidth: 200,
                        autoHide: true,
                        hideDelay: 2000,
                        anchor: 'top',
                        mouseOffset: [-11, -2],
                        closable: true,
                        constrainPosition: false,
                        cls: 'info-tip',
                        listeners:{
                           	beforeshow: function() {
                           		if (me.errorContainer.tip != undefined){
                           			me.errorContainer.tip.hide();
                           		}
                           		if (me.warnContainer.tip != undefined){
                           			me.warnContainer.tip.hide();
                           		}
                           	}
                       	}
                    });
                }
                return tip;
            },
            listeners:{
            	afterrender: function(comp) {
            		var element = comp.getEl();
            		element.on('click', function() {
            			me._doIconClick(comp);
            		});
            	}
            }
    	});
    }
    
});