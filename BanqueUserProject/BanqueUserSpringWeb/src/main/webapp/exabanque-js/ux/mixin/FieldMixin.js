 /**
 * Modifies a field introducing a mandatory * and help ? button
 * If the parameter allowBlank is defined as false and
 * the context is not 'CONSULTATION' it will display the mandatory *
 * after the field label
 * 
 * If the label width is too small, the property labelWidth can be set
 * to increase the label dimension
 */
Ext.define('Ext.ux.mixin.FieldMixin', {
    extend: 'Ext.Mixin',

    mixinConfig: {
        id: 'fieldmixin',
		on: {
			beforeRender: 'onBeforeRender',
			afterRender: 'onAfterRender'
			
		}
    },

	config:{

		/**
		 * Value to be displayed on the help button
		 * If the parameter is not defined, no help button is showed.
		 * 
		 * This parameter can be defined using a pTrad directly or by using a convention:
		 * when defining the field the help parameter should be setup has true and a name
		 * parameter should be specified in order to get a convention pTrad
		 * in the following format: 'pagename'+'.'+'name'+'.'+'help'
		 */
		help: undefined,
		
		/**
		 * The parameter defines if the field can be left blank
		 */
		allowBlank: undefined
	},

	labelClsExtra: "pblink-display-label",

	onAfterRender: function (){
		if (this.getHelp()) {
			
			var name = Ext.isDefined(this.getName) ? this.getName() : this.name;

			var pagename = this.getViewModel().get('pagename'),
				html = this.getHelp() === true ? '$' + pagename + '.' + name + '.help' : this.getHelp();

			var tip = Ext.create('Ext.tip.ToolTip', {
				target: this.getEl().dom.querySelector('.helpbutton'),
				html: html
			});
		}
		
		this.updateAllowBlank(this.getAllowBlank());
	},


	onBeforeRender: function () {

		var mandatoryTempl,
		helpTempl,
		bothTempl;

		var context = this.getViewModel().get('context');


		mandatoryTempl = new Ext.XTemplate('<tpl><div class="mandatoryfield"<sup> * </sup></div></tpl>',
			{
				disableFormats: true
			});

		bothTempl = new Ext.XTemplate('<tpl><div class="helpbutton"></div><div class="mandatoryfield"<sup> * </sup></div></tpl>',
			{
				disableFormats: true
			});

		helpTempl = new Ext.XTemplate('<tpl><div class="helpbutton"></div></tpl>',
			{
				disableFormats: true
			});


		if (context == 'CONSULTATION'){
			if (this.getHelp()) {
				this.afterLabelTextTpl = helpTempl;
			}
		}else {

			if (this.getHelp()) {
				this.afterLabelTextTpl = bothTempl;
			}else {
				this.afterLabelTextTpl = mandatoryTempl;
				//this.labelSeparator = '<div class="mandatoryfield"<sup> * </sup>:</div>';
			}
		}

			/*if (this.allowBlank == false && this.getHelp() && context != 'CONSULTATION'){
				this.afterLabelTextTpl = bothTempl;
			}else if(this.getHelp() && context == 'CONSULTATION' || ((this.allowBlank != false || !Ext.isDefined(this.allowBlank)) && this.getHelp())){
				this.afterLabelTextTpl = helpTempl;
			}
			else if(this.allowBlank == false && (!Ext.isDefined(this.getHelp()) || this.getHelp() == '') && context != 'CONSULTATION'){
				this.afterLabelTextTpl = mandatoryTempl;
			}
			else{
				this.afterLabelTextTpl = new Ext.XTemplate('<tpl></tpl>',
				{
					disableFormats: true
				});
			}*/


	},
	
	updateAllowBlank: function (value){
		this.allowBlank = value;
		
		if(this.labelEl && this.rendered){
			var mandatoryField = this.labelEl.dom.querySelector('.mandatoryfield');
			
			if(mandatoryField && value){
				mandatoryField.style.visibility = "hidden";
			}
			else if(mandatoryField && !value){
				mandatoryField.style.visibility = "visible";
			}
		}
		
	}	

});