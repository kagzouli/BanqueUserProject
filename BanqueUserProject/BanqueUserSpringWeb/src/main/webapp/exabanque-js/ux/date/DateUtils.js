/**
 * Create a utility class for dates
 */
Ext.define('Ext.ux.date.DateUtils', {
	
	/**
     * @cfg {Boolean} singleton
     * @member Ext.Class
     * When set to true, the class will be instantiated as singleton.
     */
	singleton: true,
	
	translateDateTimeFormatToJava: function(format){		
		var result = format.replace("d","dd");
        result = result.replace("m","MM");
        result = result.replace("Y","yyyy");
        
        result = result.replace("h","hh");
        result = result.replace("H","HH");
        result = result.replace("i","mm");
        result = result.replace("s","ss");

        result = result.replace("A","a");
        return result;
	},
	
	translateDateTimeFormatFromJava: function(format){		
		var result = format.replace("dd","d");
        result = result.replace("MM","m");
        result = result.replace("yyyy","Y");
        
        result = result.replace("hh","h");
        result = result.replace("HH","H");
        result = result.replace("mm","i");
        result = result.replace("ss","s");

        result = result.replace("a","A");
        return result;
	},
	
	isCodedDate: function(_date) {
		var patternDateCoded = "^[+-][0-9]{1,5}[JjDdWwMmAaYyPpSsFfEeBbLl]$";
		var regExpDateCoded = new RegExp(patternDateCoded);
		
		return regExpDateCoded.test(_date) ? true : false;
	},


	getDateDecoded : function (value, date) {
	
		if(Ext.isEmpty(value)) {
			return;
		}
	
		var codedDate,
			auxDate  = date ? date : new Date();
		// get sign
		var sTemp = '';
		//if (insertedStringValue.startsWith("-")) {
		if (value.indexOf('-') === 0) {
			sTemp = "-";
		}

		if (value.lastIndexOf('0') === value.length) {
			sTemp = sTemp.concat ('0');
		} else {
			var toConcat = value.substring(1, Ext.String.trim(value).length - 1);
			//sTemp = sTemp.concat(toConcat);
			sTemp = sTemp+toConcat;
			
			
		}
		var pNumber = parseInt(sTemp);
		// get code
		var code = value.charAt(value.length - 1);

		switch (code) {
		case 'J': //�cart en jours calendaires
		case 'j':
		case 'D':
		case 'd':
			//codedDate.add(Calendar.DATE, pNumber);
			codedDate = Ext.Date.add(auxDate, Ext.Date.DAY , pNumber);	
			break;
		case 'W': // �cart en jours ouvr�s (samedi et dimanche exclus)
		case 'w':
			var j = 0,
			step = 0;
			codedDate = auxDate;
			if (pNumber < 0) {
				step = -1;
				pNumber = -pNumber;
			} else {
				step = 1;
			}
			while (j < pNumber) {
				// add step to current date
				//codedDate.add(Calendar.DATE, step);
				codedDate = Ext.Date.add(codedDate, Ext.Date.DAY , step);	
				// skip Weekends
				//0-Sunday and 6-Saturday
				if ((codedDate.getDay() !== 0) & (codedDate.getDay() !== 6)) {
					j++;
				}
//				if ((codedDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) & (codedDate.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)) {
//				j++;
//				/									}
			}
			break;	
		case 'M': //�cart en mois - Month Counts
		case 'm':
			//codedDate.add(Calendar.MONTH, pNumber);
			codedDate = Ext.Date.add(auxDate, Ext.Date.MONTH  , pNumber);
			break;
		case 'A': // �cart en ann�es
		case 'a':
		case 'Y':
		case 'y':
			//	codedDate.add(Calendar.YEAR, pNumber);
			codedDate = Ext.Date.add(auxDate, Ext.Date.YEAR  , pNumber);
			break;
		case 'P': // d�but de mois ouvr� (samedi et dimanche exclus)
		case 'p':
		case 'S':
		case 's':
			//codedDate.add(Calendar.MONTH, pNumber);
			//codedDate.set(Calendar.DATE,codedDate.getMinimum(Calendar.DAY_OF_MONTH));								
			var monthBeginDate = Ext.Date.getFirstDateOfMonth( Ext.Date.add(auxDate, Ext.Date.MONTH  , pNumber) );

//			if (codedDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
//			codedDate.add(Calendar.DATE, 1);
//			}
//			if (codedDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
//			codedDate.add(Calendar.DATE, 2);
//			}
			switch(monthBeginDate.getDay()) {
			case 0 :
				codedDate = Ext.Date.add(monthBeginDate, Ext.Date.DAY , 1);
				break;
			case 6 :
				codedDate = Ext.Date.add(monthBeginDate, Ext.Date.DAY , 2);
				break;
			default:
				codedDate = monthBeginDate;
			break;
			}
			break;	
		case 'F': // fin de mois ouvr�e (samedi et dimanche exclus)
		case 'f':
		case 'E':
		case 'e':
//			codedDate.add(Calendar.MONTH, pNumber + 1);
//			codedDate.set(Calendar.DATE,codedDate.getMinimum(Calendar.DAY_OF_MONTH));
//			codedDate.add(Calendar.DATE, -1);

			var monthEndDate = Ext.Date.getLastDateOfMonth( Ext.Date.add(auxDate, Ext.Date.MONTH  , pNumber) );
//			if (codedDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
//			codedDate.add(Calendar.DATE, -2);
//			}
//			if (codedDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
//			codedDate.add(Calendar.DATE, -1);
//			}
			switch(monthEndDate.getDay()) {
			case 0 :
				codedDate = Ext.Date.add(monthEndDate, Ext.Date.DAY , -2);
				break;
			case 6 :
				codedDate = Ext.Date.add(monthEndDate, Ext.Date.DAY , -1);
				break;
			default:
				codedDate = monthEndDate;
			break;
			}								
			break;
		case 'B': // debut de mois (samedi et dimanche inclus)
		case 'b':
//			codedDate.add(Calendar.MONTH, pNumber);
//			codedDate.set(Calendar.DATE,codedDate.getMinimum(Calendar.DAY_OF_MONTH));
			codedDate = Ext.Date.getFirstDateOfMonth( Ext.Date.add(auxDate, Ext.Date.MONTH  , pNumber) );
			break;	
		case 'L': // fin de mois (samedi et dimanche inclus)
		case 'l':
//			codedDate.add(Calendar.MONTH, pNumber + 1);
//			codedDate.set(Calendar.DATE,codedDate.getMinimum(Calendar.DAY_OF_MONTH));
//			codedDate.add(Calendar.DATE, -1);
			codedDate = Ext.Date.getLastDateOfMonth( Ext.Date.add(auxDate, Ext.Date.MONTH  , pNumber) );
			break;
		default:								
		}	
		return codedDate;
	},


	testCaseScenarios : function () {
		var testInputUpperCase = [
		                          '+1J',
		                          '-1J',
		                          '+2D',
		                          '-2D',
		                          '-1W',
		                          '+1W',
		                          '-1M',
		                          '+1M',
		                          '+1A',
		                          '-1A',
		                          '+2Y',
		                          '-2Y',
		                          '+1P',
		                          '-1P',
		                          '+2S',
		                          '-2S',
		                          '+1F',
		                          '-1F',
		                          '+2E',
		                          '-2E',
		                          '+1B',
		                          '-1B',
		                          '+1L',
		                          '-1L'
		                          ];
		var testInputLowerCase = [
		                          '+1j',
		                          '-1j',
		                          '+2d',
		                          '-2d',
		                          '-1w',
		                          '+1w',
		                          '-1m',
		                          '+1m',
		                          '+1a',
		                          '-1a',
		                          '+2y',
		                          '-2y',
		                          '+1p',
		                          '-1p',
		                          '+2s',
		                          '-2s',
		                          '+1f',
		                          '-1f',
		                          '+2e',
		                          '-2e',
		                          '+1b',
		                          '-1b',
		                          '+1l',
		                          '-1l'
		                          ];	
		
		
		var expectedOutPut  = [
		                          '7/10/2014',
		                          '5/10/2014',
		                          '8/10/2014',
		                          '4/10/2014',
		                          '3/10/2014',
		                          '7/10/2014',
		                          '6/09/2014',
		                          '6/11/2014',
		                          '6/10/2015',
		                          '6/10/2013',
		                          '6/10/2016',
		                          '6/10/2012',
		                          '3/11/2014',
		                          '1/09/2014',
		                          '1/12/2014',
		                          '1/08/2014',
		                          '28/11/2014',
		                          '30/09/2014',
		                          '31/12/2014',
		                          '29/08/2014',
		                          '1/11/2014',
		                          '1/09/2014',
		                          '30/11/2014',
		                          '30/09/2014'
		                          ];
		console.log ('------------------------');	
		console.log ('Upper Case Validation');							
		for (var index = 0; index <testInputUpperCase.length; index++ ) {
			var inputTestCase = testInputUpperCase [index];
			var outputFromTestCase = PageFrmk.util.DateUtils.getDateDecoded(inputTestCase);
			console.log ('Expeted Output = [' +expectedOutPut[index]+ ']    |    Input = ['+inputTestCase+'] = Calulated Output = ['+outputFromTestCase+'] = Formated Date = ' + Ext.Date.format(outputFromTestCase, 'd/m/Y'));

		}
		console.log ('------------------------');
		console.log ('------------------------');
		console.log ('Lower Case Validation');						
		for (var index = 0; index <testInputLowerCase.length; index++ ) {
			var inputTestCase = testInputLowerCase [index];
			var outputFromTestCase = PageFrmk.util.DateUtils.getDateDecoded(inputTestCase);
			console.log ('Expeted Output = [' +expectedOutPut[index]+ ']    |    Input = ['+inputTestCase+'] = Calulated Output = ['+outputFromTestCase+'] = Formated Date = ' + Ext.Date.format(outputFromTestCase, 'd/m/Y'));

		}
		console.log ('------------------------');		
	},
	
	floorDate: function(date){
   	
    	if(Ext.isDate(date)){
    		var d = new Date(date);
    		
    		d = new Date(d.setMilliseconds(0));
    		d = new Date(d.setSeconds(0));
    		d = new Date(d.setMinutes(0));
    		d = new Date(d.setHours(0));
        	
        	return d;
    	}
    	
    	return null;
    },

	addDaysCodedDate:function(codedDate, days){
    	var length = codedDate.length,
			letter = codedDate.substring(length-1,length),
			value = parseInt(codedDate.substring(0,length-1)),
			sign = codedDate.substring(0,1),
			sum = value + days;

		if (sum >= 0){
			return '+' + sum.toString();
		}else {
			return sum.toString();
		}
	},

	subtractDaysCodedDate:function(codedDate, days){
		var length = codedDate.length,
			letter = codedDate.substring(length-1,length),
			value = parseInt(codedDate.substring(0,length-1)),
			sign = codedDate.substring(0,1),
			sum = value - days;

		if (sum >= 0){
		 	return '+' + sum.toString();
		}else {
			return sum.toString();
		}
	}


});