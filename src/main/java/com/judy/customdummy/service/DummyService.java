package com.judy.customdummy.service;

import com.judy.customdummy.model.ColumnVO;
import com.judy.customdummy.model.DummyRequestVO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DummyService {

    private final String PATH = "";

    public ResponseEntity downloadDummyCSV(DummyRequestVO dummyRequestVO) throws IOException {
        File csv = new File("/Users/ara/customdummy/dummy.csv");

        if (!csv.exists()) {
            csv.createNewFile();
        }

        FileWriter fw = new FileWriter(csv);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(getColumnNames(dummyRequestVO) + "\n");
        if (dummyRequestVO.getDummyCount() > 0) {
            bw.write(makeCsvDataByCount(dummyRequestVO));
        } else {
            bw.write(makeCsvDataByRange(dummyRequestVO));
        }
        System.out.println("끝");
        bw.flush();
        bw.close();

        String query = makeInsertQuery(dummyRequestVO);
        return ResponseEntity.ok(query);
    }

    private String makeInsertQuery(DummyRequestVO dummyRequestVO) {
        String format = "load data local infile '[파일경로]' into table %s \n"
                + "fields terminated by ',' \n"
                + "lines terminated by '\\n' \n"
                + "ignore 1 lines \n"
                + "(%s)";

        String columnNames = getColumnNames(dummyRequestVO);
        return String.format(format, dummyRequestVO.getTableName(), columnNames);
    }

    private static String getColumnNames(DummyRequestVO dummyRequestVO) {
        String columnNames = "";
        for (ColumnVO column : dummyRequestVO.getColumnArray()) {
            columnNames += column.getColumnName() + ",";
        }
        columnNames = columnNames.substring(0, columnNames.length() - 1);
        return columnNames;
    }

    private String makeCsvDataByCount(DummyRequestVO dummyRequestVO) throws IOException {
        int totalCount = dummyRequestVO.getDummyCount();

        List<String> str = new ArrayList<>();

        for (int i = 0; i < totalCount; i++) {
            String row = "";
            for (ColumnVO nowColumn : dummyRequestVO.getColumnArray()) {
                if (ColumnVO.ColumnType.DATE.getCode().equals(nowColumn.getColumnType().getCode())) {
                    List<String> range = nowColumn.getRange();
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                    LocalDate startDate = LocalDate.parse(range.get(0), formatter);
                    LocalDate endDate = LocalDate.parse(range.get(1), formatter);
                    LocalDate randomDate = getRandomDate(startDate, endDate);
                    row += randomDate;
                }
                if (ColumnVO.ColumnType.DATETIME.getCode().equals(nowColumn.getColumnType().getCode())) {
                    List<String> range = nowColumn.getRange();
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                    LocalDateTime startDate = LocalDateTime.parse(range.get(0), formatter);
                    LocalDateTime endDate = LocalDateTime.parse(range.get(1), formatter);
                    LocalDateTime randomDateTime = getRandomDateTime(startDate, endDate);
                    row += randomDateTime;
                }
                if (ColumnVO.ColumnType.INT.getCode().equals(nowColumn.getColumnType().getCode())) {
                    List<String> range = nowColumn.getRange();
                    int idx = 0; // 1씩 순차 증가
                    int start = Integer.parseInt(range.get(0));
                    int end = Integer.parseInt(range.get(1));
                    int randomInt = ThreadLocalRandom.current().nextInt(start, end + 1);
                    row += Integer.toString(randomInt);
                }
                if (ColumnVO.ColumnType.DOUBLE.getCode().equals(nowColumn.getColumnType().getCode())) {
                    List<String> range = nowColumn.getRange();
                    int idx = 0; // 1씩 순차 증가
                    double start = Double.parseDouble(range.get(0));
                    double end = Double.parseDouble(range.get(1));
                    double randomDouble = ThreadLocalRandom.current().nextDouble(start, end);
                    row += Double.toString(randomDouble);
                }
                if (ColumnVO.ColumnType.STRING.getCode().equals(nowColumn.getColumnType().getCode())) {
                    List<String> range = nowColumn.getRange();
                    int randomIdx = ThreadLocalRandom.current().nextInt(0, range.size());
                    row += range.get(randomIdx);
                }
                row += ",";
            }
            str.add(row);
        }
        return makeStrToTotalStr(str);
    }

    private LocalDate getRandomDate(LocalDate startDate, LocalDate endDate) {
        long startEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay + 1);
        return LocalDate.ofEpochDay(randomDay);
    }

    private LocalDateTime getRandomDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.now(), zoneId);
        long startEpochSecond = startDateTime.atZone(zoneId).toEpochSecond();
        long endEpochSecond = endDateTime.atZone(zoneId).toEpochSecond();
        long randomSecond = ThreadLocalRandom.current().nextLong(startEpochSecond, endEpochSecond + 1);
        return LocalDateTime.ofEpochSecond(randomSecond, 0, zonedDateTime.getOffset());
    }

    private String makeCsvDataByRange(DummyRequestVO dummyRequestVO) throws IOException {
        int columnCount = dummyRequestVO.getColumnCount();

        if (columnCount == 0) {
            return "";
        }

        List<String> str = new ArrayList<>();
        int now = 0;
        while (now < columnCount) {
            ColumnVO nowColumn = dummyRequestVO.getColumnArray().get(now);
            if (ColumnVO.ColumnType.DATE.getCode().equals(nowColumn.getColumnType().getCode())) {
                List<String> range = nowColumn.getRange();
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                LocalDate startDate = LocalDate.parse(range.get(0), formatter);
                LocalDate endDate = LocalDate.parse(range.get(1), formatter);

                while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
                    str.add(startDate.format(formatter) + ",");
                    startDate = startDate.plusDays(1);
                }
            }
            if (ColumnVO.ColumnType.DATETIME.getCode().equals(nowColumn.getColumnType().getCode())) {
                List<String> range = nowColumn.getRange();
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                LocalDateTime startDate = LocalDateTime.parse(range.get(0), formatter);
                LocalDateTime endDate = LocalDateTime.parse(range.get(1), formatter);

                while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
                    str.add(startDate.format(formatter) + ",");
                    startDate = startDate.plusMinutes(1);
                }
            }
            if (ColumnVO.ColumnType.INT.getCode().equals(nowColumn.getColumnType().getCode())) {
                List<String> range = nowColumn.getRange();
                // 날짜 개수
                // 범위 개수
                // 날짜 개수 /
                int idx = 0; // 1씩 순차 증가
                int start = Integer.parseInt(range.get(0));
                int end = Integer.parseInt(range.get(1));
                int count = end - start + 1;
                int unit = str.size() / count; // 단위
                for (int i = 0; i < str.size(); i++) {
                    String s = str.get(i);
                    s += start + ",";
//                    bw.write(s);
                    str.set(i, s);
                    System.out.println("썼다" + s);
                    if (i + 1 == unit) {
                        unit += unit;
                        if (start + 1 <= end) {
                            start++;
                        } else {
                            start = Integer.parseInt(range.get(0));
                        }
                    }
                }
            }
            if (ColumnVO.ColumnType.DOUBLE.getCode().equals(nowColumn.getColumnType().getCode())) {
                List<String> range = nowColumn.getRange();
                // 날짜 개수
                // 범위 개수
                // 날짜 개수 /
                int idx = 0; // 1씩 순차 증가
                double start = Double.parseDouble(range.get(0));
                double end = Double.parseDouble(range.get(1));
                int count = (int) (end - start + 1);
                int unit = str.size() / count; // 단위
                for (int i = 0; i < str.size(); i++) {
                    String s = str.get(i);
                    s += start + ",";
//                    bw.write(s);
                    str.set(i, s);
                    System.out.println("썼다" + s);
                    if (i + 1 == unit) {
                        unit += unit;
                        if (start + 1 <= end) {
                            start++;
                        } else {
                            start = Double.parseDouble(range.get(0));
                        }
                    }
                }
            }
            if (ColumnVO.ColumnType.STRING.getCode().equals(nowColumn.getColumnType().getCode())) {
                List<String> range = nowColumn.getRange();
                // 날짜 개수
                // 범위 개수
                // 날짜 개수 /
                int idx = 0; // 1씩 순차 증가
                // 문자 범위는 다수
                int count = range.size();
                int unit = str.size() / count; // 단위
                for (int i = 0; i < str.size(); i++) {
                    String s = str.get(i);
                    s += range.get(idx) + ",";
//                    bw.write(s);
                    str.set(i, s);
                    System.out.println("썼다" + s);
                    if (i + 1 == unit) {
                        unit += unit;
                        if (idx + 1 <= range.size()) {
                            idx++;
                        } else {
                            idx = 0;
                        }
                    }
                }
            }
            now++;
        }

        return makeStrToTotalStr(str);
    }

    private String makeStrToTotalStr(List<String> str) {
        String totalStr = "";
        for (int i = 0; i < str.size(); i++) {
            totalStr += str.get(i) + "\n";
        }
        return totalStr;
    }

    private List<String> getCondition(ColumnVO columnVO) {
        if (columnVO.isUseRange()) {
            return columnVO.getRange();
        } else {
            int count = 50;
            List<String> randomCondition = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                randomCondition.add(getRandomWord());
            }
            return randomCondition;
        }
    }

    private String getRandomWord() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

}
