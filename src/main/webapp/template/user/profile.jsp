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
    <link rel="favicon" href="<c:url value="/favicon.ico"/>" />
    <title><fmt:message key="title.user.profile"/></title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/css/style.css"/>"/>
</head>
<body>
<div class="page">

    <c:url value="/template/parts/header.jsp" var="headerPath"/>
    <jsp:include page="${headerPath}"/>


    <main class="main">
        <div class="main-head bg-black">
            <div class="section bg-none block">
                <div class="head-info">
                    <h2 class="bold font-lg">${userParameters.user.name} ${userParameters.user.surname}</h2>
                    <p><fmt:message key="user.profile.registered"/>: ${userParameters.user.registrationDate}</p>
                </div>
                <div class="dec-pancake jc-space-around">
                    <div class="user-achievements text-center">
                        <h3 class="font-lg">${userParameters.testsSolved}</h3>
                        <span><fmt:message key="user.profile.tests.solved"/></span>
                    </div>
                    <div class="user-achievements text-center">
                        <h3 class="font-lg">${userParameters.testsCreated}</h3>
                        <span><fmt:message key="user.profile.tests.created"/></span>
                    </div>
                    <div class="user-achievements text-center">
                        <h3 class="font-lg">${userParameters.commentsPosted}</h3>
                        <span><fmt:message key="user.profile.comments.posted"/></span>
                    </div>
                </div>
            </div>
        </div>
        <c:if test="${isOwner}">
            <div>
                <div class="user-profile-actions section bg-primary-dark">
                    <a href="<ct:link key="user.profile.change"/>" class="btn"><fmt:message key="button.profile.edit"/></a>
                </div>
            </div>
        </c:if>
        <div class="section">
            <div class="content">
                <span class="font-mdd bold margin-b-1rem"><fmt:message
                        key="user.profile.characteristics.title"/>:</span>
                <c:forEach var="userCharacteristic" items="${userParameters.userCharacteristics}">
                    <div class="breakline"></div>
                    <div class="padding-2rem">
                        <div class="font-mdd margin-b-1rem">
                            <span>${userCharacteristic.characteristic}: </span><span class="bold">${userCharacteristic.score}</span>
                        </div>
                        <span><fmt:message key="${userCharacteristic.characteristic}" /></span>
                    </div>
                </c:forEach>
            </div>
        </div>
    </main>

    <c:url value="/template/parts/footer.jsp" var="footerPath"/>
    <jsp:include page="${footerPath}"/>

</div>
</body>
</html>
