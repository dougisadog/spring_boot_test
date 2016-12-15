<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<script>
function sub() {
	document.getElementById("form").submit();
}
</script>
<body>
	<c:url value="/resources/text.txt" var="url"/>
	<spring:url value="/resources/text.txt" htmlEscape="true" var="springUrl" />
	Spring URL: ${springUrl} at ${time}
	<br>
	JSTL URL: ${url}
	<br>
	Message: ${message}
	
	<form id ="form" action="form" method="post">
	<input type="" id="a" name = "a">aaa</input>
	<input type="" id="b" name = "b">bbb</input>
	<button type="button" onclick="sub();">submit</button>
	</form>
</body>

</html>
