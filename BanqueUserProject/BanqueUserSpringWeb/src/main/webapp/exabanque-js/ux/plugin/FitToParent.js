/**
 * Plugin that fits component to parent.
 */
Ext.define('Ext.ux.plugin.FitToParent', {
    extend : 'Ext.plugin.Abstract',
    alias : 'plugin.fittoparent',
    
    /**
     * @cfg {HTMLElement/Ext.Element/String} parent The element to fit the
     *      component size to (defaults to the element the component is rendered
     *      to).
     */
    
    /**
     * @cfg {Boolean} fitWidth If the plugin should fit the width of the
     *      component to the parent element (default <tt>true</tt>).
     */
    fitWidth : true,
    
    /**
     * @cfg {Boolean} fitHeight If the plugin should fit the height of the
     *      component to the parent element (default <tt>true</tt>).
     */
    fitHeight : true,
    
    /**
     * @cfg {Array} offsets Decreases the final size with [width, height]
     *      (default <tt>[0, 0]</tt>).
     */
    offsets : [ 0, 0 ],




    /**
     * @constructor
     * @param {HTMLElement/Ext.Element/String/Object}
     *            config The parent element or configuration options.
     * @ptype fittoparent
     */
    constructor : function(config) {
        config = config || {};
        if (config.tagName || config.dom || Ext.isString(config)) {
            config = {
                parent : config
            };
        }
        Ext.apply(this, config);
    },




    init : function(component) {
        component.on('render', this.onRender, this, {
            single : true
        });
    },




    /**
     * private functions.
     */
    privates : {
        onRender : function(component) {
            this.component = component;
            this.parent = Ext.get(this.parent || component.getEl().dom.parentNode);
            this.fitSize();
            Ext.GlobalEvents.on('resize', this.fitSize, this);
        },
    
        fitSize : function() {
            var pos = this.component.getPosition(),
            width = this.fitWidth ? this.parent.getViewSize().width - pos[0] - this.offsets[0] : undefined,
            height = this.fitHeight ? Ext.getBody().getViewSize().height - pos[1] - this.offsets[1] : undefined;
            
            this.component.setSize(width, height);
            return true;
        }
    }
});