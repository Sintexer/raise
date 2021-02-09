<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pagination-wrap jc-center m-y-3rem">
    <ul class="pagination">
        <c:if test="${currentPage>1}">
            <li><a href="?pageNumber=${currentPage-1}" class="prev link-type" title="previous page">&#10094;</a></li>
        </c:if>
        <c:if test="${currentPage>2}">
            <li>
                <a class="btn-round" href="?pageNumber=1">1</a>
            </li>
        </c:if>
        <c:if test="${currentPage>2}">
            <li>
                <span>/</span>
            </li>
        </c:if>
        <c:if test="${currentPage>1}">
            <li>
                <a class="btn-round" href="?pageNumber=${currentPage-1}">${currentPage-1}</a>
            </li>
        </c:if>
        <li>
            <a class="btn-round active"  href="?pageNumber=${currentPage}">${currentPage}</a>
        </li>
        <c:if test="${currentPage < maxPage}">
            <li>
                <a class="btn-round" href="?pageNumber=${currentPage+1}">${currentPage+1}</a>
            </li>
        </c:if>
        <c:if test="${currentPage < maxPage-2}">
            <li>
                <span>/</span>
            </li>
        </c:if>
        <c:if test="${currentPage < maxPage-1}">
            <li>
                <a class="btn-round" href="?pageNumber=${maxPage}">${maxPage}</a>
            </li>
        </c:if>
        <c:if test="${currentPage < maxPage}">
            <li><a href="?pageNumber=${currentPage+1}" class="next link-type" title="next page">&#10095;</a></li>
        </c:if>

    </ul>
</div>