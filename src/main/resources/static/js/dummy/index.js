var indexView = {

}

$(() => {
    indexView.changeColumnCount();
    indexView.sendTableInfo();
    indexView.makeDummyByRangeBtn();
    indexView.changeColumnRangeType();
});

/**
 * 컬럼 수 설정에 따라 컬럼명 입력칸 늘리기
 */
indexView.changeColumnCount = function() {
    var prevValue = 1;
    var html = '<input type="text" name="columnName" class="form-control" maxlength="50">';
    $('input#columnCount').change((e) => {
        var count = e.currentTarget.value;
        if (count > prevValue) {
            for (let i = 0; i < count - prevValue; i++) {
                $('div#columns').append(html);
            }
        } else {
            if (prevValue != 1) {
                for (let i = 0; i < prevValue - count; i++) {
                    $('div#columns').children().last().remove();
                }
            }
        }
        prevValue = count;
    });
}

/**
 * "테이블 정보 생성" 버튼 클릭
 */
indexView.sendTableInfo = function() {

    $('#makeTableInfoBtn').click((e) => {
        e.preventDefault();
        if (!indexView.checkTableInfoValidation()) {
            return false;
        }
        $('#columnInfo').empty();

        var columnVOList = indexView.getColumnNameList();

        columnVOList.forEach((item, idx) => {
            var html = `<tr><th>${item.columnName}</th>
                                <td>
                                <select id="co${idx}" class="form-select">
                                    <option value="D">날짜</option>
                                    <option value="DT">일시</option>
                                    <option value="S">문자</option>
                                    <option value="I">정수</option>
                                    <option value="DO">실수</option>
                                </select>
                                </td>
                                <td>
                                    <input class="form-check-input" type="checkbox" value="" id="isRange" name="isRange">
                                    <label class="form-check-label" for="isRange">
                                        범위 여부
                                    </label>
                                </td>
                                <td id="rangeTd">
                                </td>
                                <td id="appendBox">
                                </td></tr>`;
            $('#columnInfo').append(html);
        });

        if ($('input[name="dummyType"]:checked').val() == 'BYCOUNT') {
            $('#columnCountArea').css('display', 'block');
        } else {
            $('#columnCountArea').css('display', 'none');
        }

        indexView.appendRange();
        indexView.expandSelectBox();
    });
}

/**
 * 범위 여부 선택했을 때 타입에 맞는 범위 입력칸 append
 */
indexView.appendRange = function() {
    // isRange 체크박스의 상태 변화 감지
    $('input[name="isRange"]').on('change', function(e) {
        if ($(e.currentTarget).is(':checked')) {
            var type = $(e.currentTarget).closest('td').siblings().find('select').val();
            $(e.currentTarget).closest('td').parent().find('td#appendBox').append(getInputBoxByType(type)); // 타입에 맞는 범위 입력칸
            var html = '<input type="text" class="form-control">';
            if (type == 'S') {
                $(e.currentTarget).closest('td').parent().find('td#appendBox').append(html);
                indexView.expandSelectBox();
            }
        } else {
            $(e.currentTarget).closest('td').parent().find('td#appendBox').empty();
        }
    });
}

/**
 * 타입이 문자인 경우, 범위 개수 입력칸 변화에 따라 범위 입력칸 활성화
 */
indexView.expandSelectBox = function() {
    var prevValue = 1;
    $('input[name="rangeBox"]').change((e) => {
        var appendTarget = e.currentTarget.parentElement;
        var count = e.currentTarget.value;
        var type = $(e.currentTarget).parent().siblings().find('select').val();
        var html = '<input type="text" class="form-control">';

        if (count > prevValue) {
            for (let i = 0; i < count - prevValue; i++) {
                $(appendTarget).append(html);
            }
        } else {
            if (prevValue != 1) {
                for (let i = 0; i < prevValue - count; i++) {
                    $(appendTarget).children().last().remove();
                }
            }
        }
        prevValue = count;
    });
}

/**
 * 타입에 따라 맞는 범위 관련 박스 return
 */
function getInputBoxByType(type) {
    if (type == 'D') {
        return `<input type="date" id="startDate" class="form-control"><input type="date" class="form-control" id="endDate">`;
    } else if (type == 'DT') {
        return `<input type="datetime-local" id="startDateTime" class="form-control"><input type="datetime-local" class="form-control" id="endDateTime">`;
    } else if (type == 'S') {
        return '<input type="number" class="form-control" name="rangeBox" min="1" max="30" value="1">';
    } else if (type == 'I') {
        return `<input type="number" class="form-control" id="startInt"><input type="number" class="form-control" id="endInt">`;
    } else if (type == 'DO') {
        return `<input type="text" class="form-control" id="startDou"><input type="text" class="form-control" id="endDou">`;
    }
}

/**
 * 날짜/일시 범위로 더미데이터 생성
 */
indexView.makeDummyByRangeBtn = function() {

    $('#makeDummyBtn').on('click', function(e) {
        e.preventDefault();
        if (!indexView.checkColumnInfoValidation()) {
            return false;
        }

        var columnNameList = indexView.getColumnNameList();

        var requestParam = {
            tableName: $('input#tableName').val(),
            columnCount: indexView.getColumnNameList().length,
            columnArray: [],
        }

        if ($('input[name="dummyType"]').val() == 'BYCOUNT') {
            requestParam.dummyCount = $("#dummyCount").val();
        }

        var columnArray = [];

        $('tbody#columnInfo > tr').toArray().forEach((item, idx) => {
            var type = $(item).find('select').val();
            var isRange = $(item).find('input').first().is(':checked');
            var rangeTd = $(item).find('td#appendBox').find('input');

            var rangeInputArray = rangeTd.toArray();

            var rangeArray = [];

            var isFirstInput = true;
            rangeInputArray.forEach((rItem, rIdx) => {
                if (type == 'S' && isFirstInput == true) {
                    isFirstInput = false;
                    return;
                }
                rangeArray.push(rItem.value);
            });

            var column = {
                columnName: columnNameList[idx].columnName,
                type: type,
                useRange: isRange.toString(),
                rangeSize: rangeArray.length,
                range: rangeArray
            }
            columnArray.push(column);
        });

        requestParam.columnArray = columnArray;
        console.log("여기" + JSON.stringify(requestParam));

        $.ajax({
            type: "POST",
            url: "/dummy/api/make",
            data: JSON.stringify(requestParam),
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                console.log(data);
            }, error: function () {
                alert("예외 발생!");
            }
        });
    });
}

/**
 * 컬럼명 return
 */
indexView.getColumnNameList = function() {
    var list = new Array();

    $('input[name="columnName"]').each(function(idx, item) {
        var columnName = $(this).attr('name');
        var columnValue = $(this).val();
        var columnObj = {};

        columnObj[columnName] = columnValue;
        list.push(columnObj);
    });

    return list;
}

/**
 * 테이블 정보 유효성 체크
 */
indexView.checkTableInfoValidation = function() {
    if ($('#tableName').val().length < 1 || $('#tableName').val().length > 50) {
        alert("테이블명을 50자까지 입력해주세요.");
        return false;
    }

    var isValid = true;
    $("#columns input").each(function() {
        if ($(this).val() === "") {
            alert("컬럼명을 50자까지 입력해주세요.");
            isValid = false;
            return false;
        }
    });

    return isValid;
}

/**
 * 컬럼 정보 유효성 체크
 */
indexView.checkColumnInfoValidation = function() {
    if (!indexView.checkTableInfoValidation()) {
        return false;
    }

    if ($('#dummyCount').val() == '') {
        alert("더미데이터 수를 입력해주세요.");
        return false;
    }

    var isValid = true;
    $("#columnInfo tr").each(function() {
        var isRangeChecked = $(this).find('input[name="isRange"]').prop('checked');
        var nextInput = $(this).closest('td').next().find('input[type="text"]');
        if (isRangeChecked && nextInput.val() === '') {
            alert("컬럼의 범위를 입력해주세요.");
            isValid = false;
            return false;
        }
    });

    return isValid;
}

/**
 * 셀렉트박스 데이터유형 변경에 따라 범위 타입 변경
 */
indexView.changeColumnRangeType = function() {
    $('#columnInfo').on('change', 'select', function(e) {
        if ($(e.currentTarget).closest('tr').find('input[name="isRange"]').is(':checked')) {
            $(e.currentTarget).closest('tr').find('input[name="isRange"]').click();
        }
    });
}