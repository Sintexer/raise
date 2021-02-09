<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ct" uri="/WEB-INF/customlib.tld" %>

<fmt:setLocale value="${cookie.userLocale.value}" scope="application"/>
<fmt:setBundle basename="/locale/page" var="page"/>
<fmt:setBundle basename="/locale/form" var="form"/>
<%--
  Created by IntelliJ IDEA.
  User: Neonl
  Date: 21.12.2020
  Time: 14:17
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <meta charset="utf-8">
    <link type="text/css" rel="stylesheet" href="<c:url value="/css/style.css"/>"/>
    <title><fmt:message key="title.login" bundle="${page}"/></title>
</head>
<body>

<c:set var="emailLength" value="${emailLength}" scope="page"/>
<c:set var="passwordMax" value="${passwordMax}" scope="page"/>

<div class="page">
    <c:url value="/template/parts/header.jsp" var="headerPath"/>
    <jsp:include page="${headerPath}"/>

    <div class="centered">
        <div class="stack">

            <form class="w-auto form-card card-md items-gap-vertical margin-b-2rem" method="post" action="">
                <div class="text-center font-mdd">
                    <h2 class="form-sign-in-heading"><fmt:message key="login" bundle="${page}"/></h2>
                </div>
                <c:if test="${loginFailed}">
                    <div class="alert">
                        <span class="close-btn" onclick="this.parentElement.style.display='none';">&times;</span>
                        <span><fmt:message key="error.emailorpassword" bundle="${form}"/></span>
                    </div>
                </c:if>
                <c:if test="${alreadyLogged}">
                    <div class="alert">
                        <span class="close-btn" onclick="this.parentElement.style.display='none';">&times;</span>
                        <span><fmt:message key="error.logged.already" bundle="${form}"/></span>
                    </div>
                </c:if>

                <div class="tooltip rounded-10">
                    <input type="email"
                           maxlength="${emailLength}"
                           id="username"
                           name="username"
                           class="form-input rounded-10"
                           placeholder=
                           <fmt:message key="placeholder.email" bundle="${form}"/>
                                   required autofocus
                           value="${emailPrevVal}">
                </div>

                <div class="tooltip rounded-10">
                    <input type="password"
                           maxlength="${passwordMax}"
                           id="password"
                           name="password"
                           class="form-input rounded-10"
                           placeholder=
                           <fmt:message key="placeholder.password" bundle="${form}"/>
                                   required>
                </div>

                <button class="btn btn-black mg-top-2rem" type="submit"><fmt:message key="button.login"
                                                                                     bundle="${page}"/></button>
            </form>
            <div class="flex-initial card-md stack jc-center w-100 flex align-items-center items-gap">
                <h2><fmt:message key="tip.new.here" bundle="${form}"/></h2>
                <a href="<ct:link key="registration"/>" class="btn btn-black"><fmt:message key="button.register" bundle="${page}"/></a>
            </div>
        </div>
    </div>

    <c:url value="/template/parts/footer.jsp" var="footerPath"/>
    <jsp:include page="${footerPath}"/>

</div>
</body>
</html>