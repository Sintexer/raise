<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setBundle basename="/locale/header"/>
<footer>
    <div class="page-footer centered">
        <div class="vertical-list items-gap-vertical">
            <h4 class="text-center">
                © 2021 Copyright
                Ilya Buglakov
            </h4>
            <div class="items-gap dec-pancake bold">
                <c:forEach var="locale" items="${locales}">
                    <a class="link-type" onclick="sendLocale('${locale.locale}')">${locale.viewName}</a>
                </c:forEach>
            </div>
        </div>
    </div>
</footer>