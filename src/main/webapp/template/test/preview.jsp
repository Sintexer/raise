<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ct" uri="/WEB-INF/customlib.tld" %>

<c:set var="pageLocale" value="${cookie.userLocale.value}" scope="page"/>
<fmt:setLocale value="${pageLocale}" scope="application"/>
<fmt:setBundle basename="/locale/page"/>
<fmt:setBundle basename="/locale/form" var="form"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><fmt:message key="title.test.preview"/></title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/css/style.css"/>"/>
    <link type="text/javascript" href="<c:url value="/script/testing.js"/>"/>
    <script src=<c:url value="/script/testing.js"/>></script>
    <link type="text/javascript" href="<c:url value="/script/adminActions.js"/>"/>
    <script src=<c:url value="/script/adminActions.js"/>></script>
</head>

<body>
<div class="page">

    <c:url value="/template/parts/header.jsp" var="headerPath"/>
    <jsp:include page="${headerPath}"/>

    <main class="main">
        <div class="section stack">
            <div class="content">
                <h1 class="page-title"><fmt:message key="test.preview.title"/></h1>
                <div class="breakline"></div>
                <div class="grid-2 padding-0">
                    <div class="grid-element">
                        <div class="test-preview">
                            <h2 class="margin-y-1rem font-md w-fit m-x-auto">${test.testName}</h2>
                            <div class="card-md box-100 items-gap-vertical margin-b-2rem m-x-auto">
                                <span><fmt:message key="test.card.difficulty"/>: ${test.difficulty}</span>
                                <c:if test="${not empty test.characteristics}">
                                    <span><fmt:message key="test.characteristics"/>:</span>
                                    <c:forEach var="characteristic" items="${test.characteristics}">
                                        <span>${characteristic}</span>
                                    </c:forEach>
                                </c:if>
                                <div class="breakline"></div>
                                <span><fmt:message key="test.questions.amount"/>: ${fn:length(test.questions)}</span>
                                <div class="breakline"></div>
                                <a href="<ct:link key="test.testing"/>?testId=${test.id}" class="btn btn-black">
                                    <fmt:message key="test.button.testing"/>
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="grid-element items-gap-vertical">
                        <h2 class="margin-y-1rem font-md w-fit m-x-auto flex items-gap-sm">
                            <c:if test="${currentPage>1}">
                                <form action="<ct:link key="test.preview"/>">
                                    <input hidden name="testId" value="${test.id}">
                                    <input hidden name="pageNumber" value="${currentPage-1}">
                                        <button class="prev link-type"
                                           title="previous page">&#10094;</button>
                                </form>
                            </c:if>
                            <fmt:message key="title.comments"/>
                            <c:if test="${currentPage < maxPage}">
                                <form action="<ct:link key="test.preview"/>">
                                    <input hidden name="testId" value="${test.id}">
                                    <input hidden name="pageNumber" value="${currentPage+1}">
                                    <button    class="next link-type" title="next page">&#10095;</button>
                                </form>
                            </c:if>
                        </h2>

                        <shiro:authenticated>
                            <form class="items-gap-vertical" action="<ct:link key="test.preview"/>" method="post">
                                <input hidden name="testId" value="${test.id}">
                                <textarea minlength="5"
                                          maxlength="512"
                                          placeholder="<fmt:message key="placeholder.comment" bundle="${form}"/>"
                                          class="form-input unresize w-100"
                                          rows="5"
                                          name="comment"></textarea>
                                <button class="btn btn-black w-100"><fmt:message key="button.save" /></button>
                            </form>
                        </shiro:authenticated>
                        <c:choose>
                            <c:when test="${not empty comments}">
                                <c:forEach var="comment" items="${comments}">
                                    <div class="comment">
                                        <div class="card-md box-100">
                                            <shiro:hasPermission name="comment:delete:*">
                                            <button type="button" class="btn btn-black btn-com-del" onclick="deleteQuestion(${comment.id},
                                                '<ct:link key="rest.delete.comment"/>')">&times;</button>
                                            </shiro:hasPermission>
                                            <span class="font-md">${comment.user.name} ${comment.user.surname}</span>
                                            <span><ct:timestamp locale="${pageLocale}"  timestamp="${comment.timestamp}"/></span>
                                            <span>
<%--                                           --%>
                                            <div class="breakline"></div>
                                            <span class="m-t-1rem">${comment.content}</span>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="bold centered p-t-8rem">
                                    <span><fmt:message key="test.search.empty"/></span>
                                </div>
                            </c:otherwise>
                        </c:choose>


                    </div>
                </div>
            </div>
        </div>
    </main>

    <c:url value="/template/parts/footer.jsp" var="footerPath"/>
    <jsp:include page="${footerPath}"/>

</div>
</body>
</html>
