<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html manifest="" lang="en-US">
<head>

<title>Exabanque</title>


<script type="text/javascript">
	var contextPath = "${pageContext.request.contextPath}";
	var maxElementsOperationAccount = 100;
</script>

 <link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/exabanque-style/ext-theme-classic-all.css" />

	
<script type="text/javascript"
	src="${pageContext.request.contextPath}/webjars/extjs/6.2.0/build/ext-all-debug.js"></script>
	
<script type="text/javascript"
	src="${pageContext.request.contextPath}/exabanque-js/app/app.js"></script>
	
	
</head>

<body>


<br/>
	
	
	<div id="extsearchparam"></div>


	<div id="extbanquecentral"></div>

	<br />

	<div id="buttonCreatePers" style="display: inline"></div>

	<br />

	<div id="zoneFinal">&nbsp;</div>



</body>

</html>



</html>