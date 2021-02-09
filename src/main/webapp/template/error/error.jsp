<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="h" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ct" uri="/WEB-INF/customlib.tld" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${cookie.userLocale.value}" scope="application"/>
<fmt:setBundle basename="/locale/page"/>
<!DOCTYPE html>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="<c:url value="/css/style.css"/>"/>
    <meta charset="UTF-8">
    <title>Error <fmt:message key="error.title"/></title>
</head>
<body>
<div class="page">
    <c:url value="/template/parts/header.jsp" var="headerPath"/>
    <jsp:include page="${headerPath}"/>


    <main class="centered main">
        <div class="section">
            <div class="content">
                <div class="section stack">
                    <h1 class="page-title"><fmt:message key="error.title"/></h1>
                    <c:if test="${statusCode}">
                    <h2 class="error-code">
                        ${errorCode}
                    </h2>
                    </c:if>
                    <c:if test="${errorMessage}">
                    <h2 class="font-lg">
                        ${errorMessage}
                    </h2>
                    </c:if>
                    <a class="btn btn-black" href="<ct:link key="root"/>"><fmt:message key="link.back.main"/></a>
                </div>

            </div>
        </div>
    </main>

    <c:url value="/template/parts/footer.jsp" var="footerPath"/>
    <jsp:include page="${footerPath}"/>
</div>
</body>
</html>