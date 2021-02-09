<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="ct" uri="/WEB-INF/customlib.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<fmt:setLocale value="${cookie.userLocale.value}" scope="application"/>
<fmt:setBundle basename="/locale/page"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><fmt:message key="title.admin.pane"/></title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/css/style.css"/>"/>

</head>

<body>
<div class="page">

    <c:url value="/template/parts/header.jsp" var="headerPath"/>
    <jsp:include page="${headerPath}"/>

    <main class="main">
        <div class="section stack items-gap-vertical">
            <h1 class="page-title font-lg"><fmt:message key="test.catalog.new"/></h1>
            <div class="breakline"></div>
            <c:choose>
                <c:when test="${not empty tests}">
                    <c:url value="/template/parts/pagination.jsp" var="topPagination"/>
                    <jsp:include page="${topPagination}"/>
                    <div class="breakline"></div>
                    <h2 class="font-md">Tests:</h2>
                    <div class="test-rows stack">
                        <table class="table-striped table-responsive">
                            <thead>
                            <tr>
                                <th class="w-15">Test Name</th>
                                <th>Questions</th>
                                <th>Question names</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="test" items="${tests}">
                                <tr>
                                    <td>${test.testName}</td>
                                    <td>${test.questionsAmount}</td>
                                    <td>${test.questionNames}</td>
                                    <td class="dec-pancake items-gap-sm jc-fl-end">
                                        <form action="<ct:link key="rest.ban.test"/>" method="post">
                                            <input hidden name="testId" value="${test.id}">
                                            <button class="btn btn-red m-y-0">Ban</button>
                                        </form>
                                        <form action="<ct:link key="rest.confirm.test"/>" method="post">
                                            <input hidden name="testId" value="${test.id}">
                                            <button class="btn btn-black m-y-0">Confirm</button>
                                        </form>
                                        <a href="<ct:link key="test.preview"/>?testId=${test.id}" class="btn btn-black m-y-0">View test</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>

                    </div>
                    <c:url value="/template/parts/pagination.jsp" var="bottomPagination"/>
                    <jsp:include page="${bottomPagination}"/>
                </c:when>
                <c:otherwise>
                    <div class="centered">
                        <h2><fmt:message key="test.search.empty" /></h2>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>
    </main>

    <c:url value="/template/parts/footer.jsp" var="footerPath"/>
    <jsp:include page="${footerPath}"/>

</div>
</body>
</html>
