package com.judy.customdummy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class DummyRequestVO {

    private String tableName;
    private int columnCount;
    private List<ColumnVO> columnArray;
    private int dummyCount;

}
