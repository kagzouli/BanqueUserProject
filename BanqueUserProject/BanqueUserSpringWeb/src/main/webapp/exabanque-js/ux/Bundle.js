/**
 * @author Maximiliano Fierro
 * @class Ext.ux.Bundle
 * @extends Ext.data.Store
 *
 * Bundle is used to load .properties bundle files based in language and expose the bundle's keys thru getMsg method.
 

        Ext.application({
            name: 'AppTest',
            requires: ['Ext.ux.Bundle'],

            bundle: {
                bundle: 'Application',
                lang: 'en-US',
                path: 'resources',
                noCache: true
            },

            launch: function(){
                Ext.create('Ext.panel.Panel',{
                    renderTo: Ext.getBody(),
                    tbar: Ext.create('Ext.toolbar.Toolbar',{
                        items: [{text: 'text'}]
                    }),
                    items:[{
                        height: 300,
                        html: this.bundle.getMsg('panel.html')
                    }],
                });
            }
        });

 */
Ext.define('Ext.ux.Bundle', {
    extend: 'Ext.data.Store',
    requires: [
        'Ext.app.Application',
        'Ext.ux.reader.Property',
        'Ext.ux.reader.Json'
    ],

    //@private
    defaultLanguage: 'en-US',

    autoLoad: false,
    //TODO: review this
    model : Ext.define('Ext.ux._modelBundle', {
                extend: 'Ext.data.Model',

                fields: ['key', 'value'],
                idProperty: 'key'
                
            }),
    /**
     * @cfg bundle {String} bundle name for properties file. Default to message
     */
    bundle: 'message',

    format : 'property',
    /**
     * @cfg path {String} URI to properties files. Default to resources
     */
    path: 'resources',
    
    mode: 'local',
    
    pagename: [],

    /**
     * @cfg lang {String} Language in the form xx-YY where:
     *      xx: Language code (2 characters lowercase)
     *      YY: Country code (2 characters upercase).
     * Optional. Default to browser's language. If it cannot be determined default to en-US.
     */

    /**
     * @cfg noCache {boolean} whether or not to disable Proxy's cache. Optional. Defaults to true.
     */


    constructor: function(config){
        config = config || {};

        var me = this,
            language = me.formatLanguageCode(config.lang || me.guessLanguage()),
            noCache = (config.noCache !== false),
            url, Model;

        me.language = language;
        me.bundle = config.bundle || me.bundle;
        me.path = config.path || me.path;
        me.format = config.format || me.format;
		me.mode = config.mode || me.mode;
		
        url = this.buildURL(language);

        delete config.lang;
        delete config.noCache;

        Ext.applyIf(config, {
            proxy:{
                type: 'ajax',
                url: url,
                noCache: noCache,
                reader: {
                    type: 'i18n.'+ me.format
                },
                //avoid sending limit, start & group params to server
                groupParam: '',
                limitParam: '',
                startParam: ''                
            }
        });

        me.callParent([config]);

        me.on('load', me.onBundleLoad, me);
        me.getProxy().on('exception', this.loadParent, this, {single: true});
    },

    /**
     * @private
     */
    guessLanguage: function(){
        return (navigator.language || navigator.browserLanguage || navigator.userLanguage || this.defaultLanguage);
    },

    /**
     * @method: getMsg
     * Returns the content associated with the bundle key or {bundle key}.undefined if it is not specified.
     * @param: key {String} Bundle key.
     * @param: values {Mixed...} if the bundle key contains any placeholder then you can add any number of values
     * that will be replaced in the placeholder token.
     * @return: {String} The bundle key content.
     */
    getMsg: function(key /*values...*/){
        var values = [].splice.call(arguments, 1),
            rec = this.getById(key),
            decoded = key + '.undefined',
            args;

        if(rec){
            decoded = Ext.util.Format.htmlDecode(rec.get('value'));

            if(values){
                args = [decoded].concat(values);
                decoded = Ext.String.format.apply(null, args);
            }
        }

        return decoded;
    },

    /**
     * @private
     */
    onBundleLoad: function(store, records, success, op) {
        if(success){
            this.fireEvent('loaded');
        }
    },

    /**
     * @private
     */
    onProxyLoad: function(op){
        if(op.getRecords()){
            this.callParent(arguments);
        }
    },

    getResourceExtension: function(){
        return this.format === 'property' ? '.properties' : '.json';
    },

    loadPtradsByPagename:function(pagename, callback){
        var me = this;
        if (!Ext.Array.contains(this.pagename,pagename)) {
            Ext.Ajax.request({
                url: contextPath + '/i18n/' + userLanguage + '/' + pagename + '.json',
                success: function (response, opts) {
                    var data = Ext.JSON.decode(response.responseText, true);
                    Ext.ux.Bundle.instance.loadRawData(data, true);
                    Ext.ux.Bundle.instance.pagename.push(pagename);
                    Ext.callback(callback, me);
                }
            });
        }else{
            Ext.callback(callback, me);
        }
    },

    /**
     * @private
     */
    buildURL: function(language){
        var url = '';
        
        if (this.mode == 'local'){
	        if (this.path) url+= this.path + '/';
	        url+=this.bundle;
	        if (language) url+= '_'+language;
	        url+=this.getResourceExtension();
        }else{
        	url = this.path;
        }
        
        return url;
    },

    /**
     * @private
     */
    loadParent: function(){
        this.getProxy().url = this.buildURL();
        this.load();
    },

    /**
     * @private
     */
    formatLanguageCode: function(lang){
        var langCodes = lang.split('-'),
            primary, second;
        primary = (langCodes[0]) ? langCodes[0].toLowerCase() : '';
        second = (langCodes[1]) ? langCodes[1].toUpperCase() : '';

        return langCodes.length > 1 ? [primary, second].join('-') : primary;
    }


}, function(){

    //hook on Ext.Base
    Ext.override(Ext.Base, {
        initConfig: function(instanceConfig) {
            var me = this,
                cfg = me.getConfigurator(),
                k;

            me.initConfig = Ext.emptyFn; // ignore subsequent calls to initConfig
            me.initialConfig = instanceConfig || {};

            var allConfig = instanceConfig;
            if (this.superclass.$className.indexOf("PBLinkComponent") != -1) {
                Ext.merge(allConfig, instanceConfig, this.defaultConfig);
            }

            for (k in allConfig) {
                if (allConfig.hasOwnProperty(k) && allConfig[k] && typeof allConfig[k] === 'object' && k == 'bind' && (allConfig[k].title || allConfig[k].value)) {
                    if (allConfig[k].title) {
                        var key = allConfig[k].title.match(/\$\{(.*?)\}/);
                        if (key != null && key.length == 2) {
                            allConfig[k].title = allConfig[k].title.replace(key[0], Ext.ux.Bundle.instance.getMsg(key[1]));
                        }
                    } else if (allConfig[k].value) {
                        var key = allConfig[k].value.match(/\$\{(.*?)\}/);
                        if (key != null && key.length == 2) {
                            allConfig[k].value = allConfig[k].value.replace(key[0], Ext.ux.Bundle.instance.getMsg(key[1]));
                        }
                    }
                } else if (allConfig.hasOwnProperty(k) && allConfig[k] && typeof allConfig[k] === 'object' && allConfig[k].type && allConfig[k].type === 'bundle') {
                    allConfig[k] = Ext.ux.Bundle.instance.getMsg(allConfig[k].key);
                } else if (allConfig.hasOwnProperty(k) && allConfig[k] && typeof allConfig[k] === 'string' && Ext.String.startsWith(allConfig[k], "$")) {
                    var key = allConfig[k].replace("$", "");
                    if (Ext.ux.Bundle.instance) {
                        allConfig[k] = Ext.ux.Bundle.instance.getMsg(key);
                    }else{
                        allConfig[k] = key;
                    }
                }
            }

            cfg.configure(me, allConfig);

               
            return me;
        }
    });

    //initialize bundle before app launch
    Ext.override(Ext.app.Application, {
        onBeforeLaunch: function() {
            var me = this,
                overridden = this.onBeforeLaunch.$previous,
                ns;

            if(me.bundle){
                //configure the bundle instance and defer launch until bundle launch
                me.bundle = Ext.create('Ext.ux.Bundle', Ext.apply({
                    autoLoad: true,
                    listeners: {
                        loaded: function(){
                            overridden.apply(me);
                        }
                    }
                }, me.bundle));
                Ext.ux.Bundle.instance = me.bundle;
            }else{
                me.callOverridden();
            }
        }
    });
});
