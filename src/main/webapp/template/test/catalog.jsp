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
    <title><fmt:message key="title.test.catalog"/></title>
    <link type="text/css" rel="stylesheet" href="<c:url value="/css/style.css"/>"/>

</head>

<body>
<div class="page">

    <c:url value="/template/parts/header.jsp" var="headerPath"/>
    <jsp:include page="${headerPath}"/>

    <main class="main">
        <div class="section">
            <div class="content">
                <div class="section block centered padding-0">
                    <h1 class="page-title"><fmt:message key="title.test.catalog"/></h1>
                    <div class="breakline"></div>
                    <form>
                        <div class="land-card p-y-1rem box-100">
                            <div class="centered">
                                <div class="box-100 card-md stack margin-t-2rem items-gap-vertical">
                                    <div>
                                        <span><fmt:message key="test.search.testname"/>: </span>
                                        <input class="form-input" name="testName" type="text" maxlength=256
                                               value="${stashedTestName}"
                                               pattern="^[^\d',.-][^\n_!¡?÷¿\/\\+=@#$%ˆ&*(){}|~<>;:\[\]]{$">
                                    </div>
                                    <div>
                                        <span><fmt:message key="test.search.category"/>: </span>
                                        <select class="form-input" name="category">
                                            <c:choose>
                                                <c:when test="${not empty stashedCategory}">
                                                    <option value=""></option>
                                                    <option value="${stashedCategory.id}"
                                                            selected>${stashedCategory.category}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="" selected></option>
                                                </c:otherwise>
                                            </c:choose>

                                            <c:forEach var="parentCategory" items="${categories.keySet()}">
                                                <option id="optionId${parentCategory.id}" class="bold"
                                                        value="${parentCategory.id}">${parentCategory.category}</option>
                                                <c:forEach var="childCategory"
                                                           items="${categories.get(parentCategory)}">
                                                    <option id="optionId${childCategory.id}"
                                                            value="${childCategory.id}">${childCategory.category}</option>
                                                </c:forEach>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <button class="m-0 btn btn-black w-fit"><fmt:message key="button.search"/></button>
                                </div>
                            </div>
                            <div class="info">
                                <h3 class="title"><fmt:message key="test.catalog.search"/></h3>
                                <span><fmt:message key="test.catalog.search.info"/></span>
                            </div>
                        </div>

                        <div class="breakline"></div>

                        <c:url value="/template/parts/buttonPagination.jsp" var="topPagination"/>
                        <jsp:include page="${topPagination}"/>
                        <c:choose>
                            <c:when test="${not empty tests}">

                                <div class="cards">
                                    <c:forEach var="test" items="${tests}">
                                        <div class="card">
                                            <div class="card-body stack items-gap-vertical">
                                                <a class="font-md" href="<ct:link key="test.preview"/>?testId=${test.id}">${test.testName}</a>
                                                <div class="breakline"></div>
                                                <span class="property"><fmt:message key="test.card.difficulty"/>: </span>
                                                <span class="bold">${test.difficulty}</span>
                                                <span class="property"><fmt:message key="test.card.questions.amount"/>:</span>
                                                <span class="bold">${test.questionsAmount}</span>
                                                <c:if test="${not empty test.characteristics}">
                                                    <div class="breakline"></div>
                                                    <span class="property"><fmt:message key="test.characteristics"/>:</span>
                                                    <c:forEach var="characteristic" items="${test.characteristics}">
                                                        <span class="bold">${characteristic}</span>
                                                    </c:forEach>
                                                </c:if>
                                                <div class="breakline"></div>
                                                <span class="property"><fmt:message
                                                        key="test.category"/>: </span>
                                                    <span class="bold">${test.category.category}</span>
                                                <div class="flex-11a"></div>
                                                <div class="breakline"></div>

                                                <div><a class="btn btn-black"
                                                        href="<ct:link key="test.preview"/>?testId=${test.id}"><fmt:message
                                                        key="test.card.button.view"/></a></div>

                                            </div>
                                            <div class="card-footer">
                                                <span>author:</span>
                                                <a href="<ct:link key="profile"/>?userId=${test.author.id}">
                                                        ${test.author.name} ${test.author.surname}
                                                </a>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>

                            </c:when>
                            <c:otherwise>
                                <div class="bold centered p-t-8rem">
                                    <span><fmt:message key="test.search.empty"/></span>
                                </div>
                            </c:otherwise>
                        </c:choose>
                        <c:url value="/template/parts/buttonPagination.jsp" var="bottomPagination"/>
                        <jsp:include page="${bottomPagination}"/>


                    </form>
                </div>
            </div>
        </div>
    </main>

    <c:url value="/template/parts/footer.jsp" var="footerPath"/>
    <jsp:include page="${footerPath}"/>

</div>
</body>
</html>
