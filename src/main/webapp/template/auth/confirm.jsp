<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="h" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ct" uri="/WEB-INF/customlib.tld" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<fmt:setLocale value="${cookie.userLocale.value}" scope="application"/>
<fmt:setBundle basename="/locale/page"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><fmt:message key="title.confirmation"/></title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/css/style.css"/>"/>
</head>
<body>
<div class="page">

    <c:url value="/template/parts/header.jsp" var="headerPath"/>
    <jsp:include page="${headerPath}"/>


    <main class="main">
        <div class="section ">
            <div class="content stack">
                <h2 class="page-title"><fmt:message key="title.confirmation"/></h2>
                <div class="breakline"></div>
                <div class="items-gap-vertical">
                    <c:choose>
                        <c:when test="${failed}">
                            <h3 class="margin-b-2rem"><fmt:message key="confirm.failed"/></h3>
                            <a href="<ct:link key="registration"/>" class="btn btn-black"><fmt:message
                                    key="button.register"/></a>
                        </c:when>
                        <c:otherwise>
                            <h3 class="margin-b-2rem"><fmt:message key="confirm.success"/></h3>
                            <a href="<ct:link key="login"/>" class="btn btn-black"><fmt:message key="button.login"/></a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </main>

    <c:url value="/template/parts/footer.jsp" var="footerPath"/>
    <jsp:include page="${footerPath}"/>

</div>
</body>
</html>
