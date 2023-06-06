<%--
  Created by IntelliJ IDEA.
  User: ara
  Date: 2023/04/13
  Time: 9:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <h1>테이블 정보 입력</h1>
    <div>더미데이터를 만들 테이블 정보를 입력해주세요. 날짜/일시를 기준으로 만들고 싶다면 "날짜/일시 범위로 더미데이터 생성"을, 원하는 개수로 만들고 싶다면 "원하는 개수로 더미데이터 생성"을 클릭해주세요.</div>
    <form id="tableForm">
        <div class="mb-3 row">
            <label for="tableName" class="col-sm-2 col-form-label">테이블명</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="tableName" id="tableName" maxlength="50" placeholder="테이블명을 50자까지 입력해주세요.">
            </div>
        </div>
        <div class="mb-3 row">
            <label for="columnCount" class="col-sm-2 col-form-label">컬럼 수</label>
            <div class="col-sm-10">
                <input type="number" class="form-control" name="columnCount" id="columnCount" value="1" min="1" max="30">
            </div>
        </div>
        <div class="mb-3 row">
            <label for="columnCount" class="col-sm-2 col-form-label">더미데이터 타입</label>
            <div class="col-sm-10">
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="dummyType" id="byCount" value="BYCOUNT" checked>
                    <label class="form-check-label" for="byCount">
                        원하는 개수로 더미데이터 생성
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="dummyType" id="byRange" value="BYRANGE">
                    <label class="form-check-label" for="byRange">
                        날짜/일시 범위로 더미데이터 생성
                    </label>
                </div>
            </div>
        </div>

        <div class="mb-3 row">
            <label for="columnCount" class="col-sm-2 col-form-label">컬럼명</label>
            <div class="col-sm-10">
                <div id="columns">
                    <input type="text" name="columnName" class="form-control" maxlength="50">
                </div>
            </div>
        </div>
        <button type="button" class="btn btn-success" id="makeTableInfoBtn">테이블 정보 생성</button>
    </form>

    <h1>컬럼 정보 입력</h1>
    <form id="columnForm">
        <div class="mb-3 row" id="columnCountArea" style="display:none;">
            <label for="columnCount" class="col-sm-2 col-form-label">더미데이터 수</label>
            <div class="col-sm-10">
                <input type="number" class="form-control" name="dummyCount" id="dummyCount" value="1000" min="1" max="50000000">
            </div>
        </div>
        <div>
            <table class="table">
                <tbody id="columnInfo"></tbody>
            </table>
        </div>
        <button type="button" class="btn btn-success" id="makeDummyBtn">더미데이터 생성</button>
    </form>
</div>
</body>
</html>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/js/dummy/index.js"></script>
