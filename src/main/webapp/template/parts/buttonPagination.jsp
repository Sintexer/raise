<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pagination-wrap jc-center m-y-3rem">
    <ul class="pagination">
        <c:if test="${currentPage>1}">
            <li><button value="${currentPage-1}" name="pageNumber" class=" link-type pg-button prev" >&#10094;</button></li>
        </c:if>
        <c:if test="${currentPage>2}">
            <li>
                <button class="pg-button btn-round" value="1" name="pageNumber">1</button>
            </li>
        </c:if>
        <c:if test="${currentPage>2}">
            <li>
                <span>...</span>
            </li>
        </c:if>
        <c:if test="${currentPage>1}">
            <li>
                <button class="pg-button btn-round" value="${currentPage-1}" name="pageNumber">${currentPage-1}</button>
            </li>
        </c:if>
        <li>
            <button class="pg-button btn-round active"  value="${currentPage}" name="pageNumber">${currentPage}</button>
        </li>
        <c:if test="${currentPage < maxPage}">
            <li>
                <button class="pg-button btn-round" value="${currentPage+1}" name="pageNumber">${currentPage+1}</button>
            </li>
        </c:if>
        <c:if test="${currentPage < maxPage-2}">
            <li>
                <span>...</span>
            </li>
        </c:if>
        <c:if test="${currentPage < maxPage-1}">
            <li>
                <button class="pg-button btn-round" value="${maxPage}" name="pageNumber">${maxPage}</button>
            </li>
        </c:if>
        <c:if test="${currentPage < maxPage}">
            <li><button value="${currentPage+1}" name="pageNumber" class=" next link-type pg-button">&#10095;</button></li>
        </c:if>

    </ul>
</div>