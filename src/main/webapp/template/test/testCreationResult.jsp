<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ct" uri="/WEB-INF/customlib.tld" %>

<fmt:setLocale value="${cookie.userLocale.value}" scope="application"/>
<fmt:setBundle basename="/locale/page"/>
<%--
  Created by IntelliJ IDEA.
  User: Neonl
  Date: 03.12.2020
  Time: 13:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><fmt:message key="title.test.creator.result"/></title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/css/style.css"/>"/>
    <link type="text/javascript" href="<c:url value="/script/testCreator.js"/>"/>
    <script src=<c:url value="/script/testCreator.js"/>></script>
    <link type="text/javascript" href="<c:url value="/script/modal.js"/>"/>
    <script src=<c:url value="/script/modal.js"/>></script>
</head>

<fmt:message key="test.creator.success" var="result"/>
<c:set var="resultFormatted" value="${fn:replace(result, \"{0}\", testName)}" scope="page"/>

<body>
<div class="page">

    <c:url value="/template/parts/header.jsp" var="headerPath"/>
    <jsp:include page="${headerPath}"/>

    <main class="main">
        <div class="section">
            <div class="content">
                <div class="section block centered padding-0">
                    <c:if test="${not testWasntPosted}">
                        <span class="bold">${resultFormatted}</span>
                    </c:if>
                    <c:if test="${testWasntCreated}">
                        <span class="bold"><fmt:message key="test.creator.save.failure"/></span>
                        <div class="alert margin-y-1rem">
                            <span class="close-btn" onclick="this.parentElement.style.display='none';">&times;</span>
                            <c:if test="${wrongTestFormat}">
                                <fmt:message key="test.creator.error.test.structure"/>
                            </c:if>
                            <c:if test="${invalidTestName}">
                                <fmt:message key="test.creator.error.test.name"/>
                            </c:if>
                            <c:if test="${invalidQuestions}">
                                <fmt:message key="test.creator.error.questions"/>
                            </c:if>
                            <c:if test="${testLimitReached}">
                                <fmt:message key="error.test.limit"/>
                            </c:if>
                        </div>
                        <a class="btn btn-black" href="<ct:link key="test.creator"/>"><fmt:message key="link.back.test.creator"/></a>
                    </c:if>
                    <a class="btn btn-black" href="<ct:link key="test.catalog"/>"><fmt:message key="link.back.test.catalog"/></a>
                </div>
            </div>
        </div>
    </main>

    <c:url value="/template/parts/footer.jsp" var="footerPath"/>
    <jsp:include page="${footerPath}"/>

</div>
</body>
</html>
