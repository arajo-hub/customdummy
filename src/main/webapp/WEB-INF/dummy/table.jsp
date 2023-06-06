<%--
  Created by IntelliJ IDEA.
  User: ara
  Date: 2023/04/13
  Time: 9:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form id="tableForm">
        <div>
            <c:forEach items="${tableInfo.columnVOList}" var="item">
                <!-- item에 대한 작업 수행 -->
                <p>${item.columnName}</p>
            </c:forEach>

        </div>
        <button id="makeTableInfoBtn">테이블 정보 생성</button>
    </form>
</body>
</html>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/js/dummy/index.js"></script>