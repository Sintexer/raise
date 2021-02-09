<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ct" uri="/WEB-INF/customlib.tld" %>

<fmt:setLocale value="${cookie.userLocale.value}" scope="application"/>
<fmt:setBundle basename="/locale/page"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link type="text/css" rel="stylesheet" href="<c:url value="/css/style.css"/>"/>
    <meta charset="UTF-8">
    <title>500</title>
</head>
<body>
<div class="page">
    <c:url value="/template/parts/header.jsp" var="headerPath"/>
    <jsp:include page="${headerPath}"/>
    <main class="centered main">
        <div class="centered error-msg">
            <h2 class="error-code">500</h2>
            <h2><fmt:message key="error.500"/></h2>
            <a class="btn btn-black" href="<ct:link key="root"/>"><fmt:message key="link.back.main"/></a>
        </div>
    </main>
    <c:url value="/template/parts/footer.jsp" var="footerPath"/>
    <jsp:include page="${footerPath}"/>
</div>
</body>
</html>