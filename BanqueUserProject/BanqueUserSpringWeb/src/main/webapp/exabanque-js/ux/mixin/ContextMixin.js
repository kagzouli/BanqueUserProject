/**
 * The context mixin allow several options to control in the all framework components.
 */
Ext.define('Ext.ux.mixin.ContextMixin', {
    extend: 'Ext.Mixin',

    mixinConfig: {
        id: 'contextmixin',
		on: {
			beforeRender: 'onBeforeRender'
		}
    },

	config:{

		/**
		 * Indicates the options for hide this field
		 */
		hideOptions: undefined,

		/**
		 * Indicates the options for disabled this field
		 */
		disabledOptions: undefined
	},

	onBeforeRender: function () {
		var context = this.getViewModel().get('context');

		if (context){

			//Hidden
			if (this.getHideOptions()){
				this.setHidden(this._getHidden(this.getHideOptions()))
			}

			//Read-Only
			if (context == 'CONSULTATION'){
				
				if(this.setReadOnly){
					this.setReadOnly(true);
				}

				this.addCls('pblink-readonly-field');
				this.valueField = undefined;
				
			}

			//Disabled
			if (this.getDisabledOptions()){
				this.setDisabled(this._getDisabled(this.getDisabledOptions()));
			}
			

		}

	},

	_getHidden:function(obj){
		var modeLower = mode.toLowerCase();
		if (obj[modeLower]){
			return obj[modeLower];
		}else{
			return false;
		}
	},

	_getDisabled:function(obj){
		var modeLower = mode.toLowerCase();
		if (obj[modeLower]){
			return obj[modeLower];
		}else{
			return false;
		}
	}
});