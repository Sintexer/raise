<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="h" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ct" uri="/WEB-INF/customlib.tld" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<fmt:setLocale value="${cookie.userLocale.value}" scope="application"/>
<fmt:setBundle basename="/locale/page"/>
<fmt:setBundle basename="/locale/form" var="form"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><fmt:message key="title.user.profile.edit"/></title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/css/style.css"/>"/>
</head>
<body>

<fmt:message key="reg.user.name.tooltip" var="nameTooltip" bundle="${form}"/>
<fmt:message key="reg.user.surname.tooltip" var="surnameTooltip" bundle="${form}"/>
<fmt:message key="reg.user.password.tooltip" var="passwordTooltip" bundle="${form}"/>

<c:set var="nameLength" value="${nameLength}" scope="page"/>
<c:set var="surnameLength" value="${surnameLength}" scope="page"/>
<c:set var="passwordMax" value="${passwordMax}" scope="page"/>
<c:set var="passwordMin" value="${passwordMin}" scope="page"/>

<c:set var="nameTooltipFormatted" value="${fn:replace(nameTooltip, \"{0}\", nameLength)}" scope="page"/>
<c:set var="surnameTooltipFormatted" value="${fn:replace(surnameTooltip, \"{0}\", surnameLength)}" scope="page"/>
<c:set var="passwordTooltipReplaced" value="${fn:replace(passwordTooltip, \"{0}\", passwordMin)}" scope="page"/>
<c:set var="passwordTooltipFormatted" value="${fn:replace(passwordTooltipReplaced, \"{1}\", passwordMax)}"
       scope="page"/>
<div class="page">



    <c:url value="/template/parts/header.jsp" var="headerPath"/>
    <jsp:include page="${headerPath}"/>


    <main class="main">
        <c:if test="${somethingChanged}">
            <div class="text-black alert font-heavy m-y-0 bg-lightcyan stack items-gap-vertical">
                <c:if test="${nameChanged}">
                    <span class="m-y-0"> Name changed </span>
                </c:if>
                <c:if test="${surnameChanged}">
                    <span class="m-y-0"> Surname changed </span>
                </c:if>
                <c:if test="${passwordChanged}">
                    <span class="m-y-0"> Password changed </span>
                </c:if>
            </div>
        </c:if>
        <c:if test="${somethingWrong}">
            <div class="alert m-y-0 stack items-gap-vertical">
                <span><fmt:message key="error.wrong.password"/></span>
            </div>
            <div class="alert m-y-0 stack items-gap-vertical">
                <span><fmt:message key="error.wrong.new.password"/></span>
            </div>
        </c:if>

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
        <div class="section padding-0">
            <div class="user-profile-form bg-none text-black user-profile-actions section centered">
                <form class="stack w-fit" action="<ct:link key="user.profile.change.save"/>" method="post">
                    <span class="margin-b-1rem font-md color-white"><fmt:message key="user.info" bundle="${form}"/></span>
                    <div class="dec-pancake items-gap items-gap-vertical margin-b-1rem jc-left">
                        <div class="tooltip rounded-10">
                            <span class="tooltip-text rounded-10">${nameTooltipFormatted}</span>
                            <input type="name"
                                   maxlength="${nameLength}"
                                   id="name"
                                   name="name"
                                   class="form-input rounded-10 w-fit"
                                   placeholder=
                                   <fmt:message key="placeholder.name" bundle="${form}"/>>
                        </div>

                        <div class="tooltip rounded-10">
                            <span class="tooltip-text rounded-10">${surnameTooltipFormatted}</span>
                            <input type="surname"
                                   maxlength="${surnameLength}"
                                   id="surname"
                                   name="surname"
                                   class="form-input rounded-10 w-fit"
                                   placeholder=
                                   <fmt:message key="placeholder.surname" bundle="${form}"/>>
                        </div>
                    </div>
                    <span class="margin-b-1rem font-md color-white"><fmt:message key="user.password" bundle="${form}"/></span>
                    <div class="dec-pancake items-gap items-gap-vertical margin-b-1rem jc-left">

                        <div class="tooltip rounded-10">
                            <span class="tooltip-text rounded-10">${passwordTooltipFormatted}</span>
                            <input type="password"
                                   minlength="${passwordMin}"
                                   maxlength="${passwordMax}"
                                   id="oldPassword"
                                   name="oldPassword"
                                   class="form-input rounded-10 w-fit"
                                   placeholder=
                                   <fmt:message key="placeholder.password.old" bundle="${form}"/>>
                        </div>
                        <div class="tooltip rounded-10">
                            <span class="tooltip-text rounded-10">${passwordTooltipFormatted}</span>
                            <input type="password"
                                   minlength="${passwordMin}"
                                   maxlength="${passwordMax}"
                                   id="newPassword"
                                   name="newPassword"
                                   class="form-input rounded-10 w-fit"
                                   placeholder=
                            <fmt:message key="placeholder.password.new" bundle="${form}"/>>
                        </div>
                        <div class="tooltip rounded-10">
                            <span class="tooltip-text rounded-10">${passwordTooltipFormatted}</span>
                            <input type="password"
                                   minlength="${passwordMin}"
                                   maxlength="${passwordMax}"
                                   id="newPasswordRepeat"
                                   name="newPasswordRepeat"
                                   class="form-input rounded-10 w-fit"
                                   placeholder=
                            <fmt:message key="placeholder.password.repeat" bundle="${form}"/>>
                        </div>
                    </div>
                    <button class="btn btn-black w-auto m-x-1rem"><fmt:message key="button.save.changes" bundle="${form}"/></button>
                </form>

            </div>
        </div>
    </main>

    <c:url value="/template/parts/footer.jsp" var="footerPath"/>
    <jsp:include page="${footerPath}"/>

</div>
</body>
</html>
