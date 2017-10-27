/**
 * Plugin to create a two step confirmation message box.<br/>
 * The first step consists in displaying a message box, like the plugin GridMessageBox.<br/>
 * If the user confirms the action, the selected rows are tested for one (os more) values. Otherwise the action is canceled.<br/>
 * If a value is present, a second message box is displayed. Otherwise the action is executed.<br/>
 * If the user confirms the second message box, the action is executed. Otherwise the action is canceled.<br/>
 * <br/>
 * Example:
 * <pre><code>
 *	{
 *		"name": "deactivate",
 *		"text": "$USM_PAGE_LISTE_USERS-action.deactivate",
 *		"group": "grid",
 *		"refreshGrid": true,
 *		"url":  "deactivate",
 *		"plugins": [{
 *			"ptype": "grid-checker-message-box",
 *			"messageTitle": "USM_PAGE_LISTE_USERS-alert.deactivateUser",
 *			"confirmation": "USM_PAGE_LISTE_USERS-confirmation.deactivateUser",
 *			"message": "USM_PAGE_LISTE_USERS-confirmation.deactivateUserPlanification",
 *			"column":"userHasPlanOrDest",
 *			"values":"1"
 *		}]
 *	}
 * </code></pre>
 *
 */
Ext.define('Ext.ux.plugin.button.GridCheckerMessageBox', {
    extend : 'Ext.plugin.Abstract',
    alias : 'plugin.grid-checker-message-box',

    /**
     * @cfg {boolean} showConfirm
     * If <code>false</code> ignores the first message box. Defaults to <code>true</code>.
     */
    showConfirm: true,

    /**
     * @cfg {String} messageTitle
     * Title of the message box
     */
    messageTitle: '',

    /**
     * @cfg {String} confirmation
     * Message to display in the first message box
     */
    confirmation: '',

    /**
     * @cfg {String} message
     * Message to display in the second message box
     */
    message: '',

    /**
     * @cfg {String} column
     * The column to check for value presence
     */
	column: '',

	/**
     * @cfg {String} values
     * The values to check. If one of these values is present, the second message box is displayed.
     */
	values: '',

    constructor: function(config) {
    	var me = this;

        config = config || {};

        me.callParent([config]);
    },

    init: function(button) {

    	button.on('click',function(button){

    		if(this.showConfirm) {
	    		var gridMessageBoxTitle = Ext.ux.Bundle.instance.getMsg(this.messageTitle),
	    			gridMessageBoxCore	= Ext.ux.Bundle.instance.getMsg(this.confirmation);

				Ext.Msg.show({
				    title: gridMessageBoxTitle,
				    message: gridMessageBoxCore,
				    buttons: Ext.Msg.YESNO,
				    scope: this,
				    fn: function (btn) {
				        if (btn === 'yes') {
				        	this._firstBoxAccepted(button);
				        }
				    }
				});
    		} else {
    			this._firstBoxAccepted(button);
    		}

    		return false;

    	}, this, {priority:1});
    },

    _firstBoxAccepted:function(button) {
    	if(this._isPropertyValuePresent(button)) {
    		this._displaySecondBox(button);
    	} else {
    		button.fireEvent('resumeclick', button);
    	}
    },

    _isPropertyValuePresent:function(button) {
		var
			values = this.values.split(';'),
			grid = button.up('gridpanel'),
			selection = grid.getView().getSelectionModel().getSelection();

		for(i = 0; i < selection.length; i++) {
			var element = selection[i],
				value=element.get(this.column);

			if(Ext.Array.contains(values, value))
				return true;
		}
    	return false;
    },

    _displaySecondBox:function (button) {
		var gridMessageBoxTitle = Ext.ux.Bundle.instance.getMsg(this.messageTitle),
			gridMessageBoxCheck	= Ext.ux.Bundle.instance.getMsg(this.message);

		Ext.Msg.show({
		    title: gridMessageBoxTitle,
		    message: gridMessageBoxCheck,
		    buttons: Ext.Msg.YESNO,
		    scope: this,
		    fn: function(btn) {
		        if (btn === 'yes') {
		       		button.fireEvent('resumeclick', button);
		        }
		    }
		});
    }

});