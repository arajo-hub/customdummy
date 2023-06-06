package com.judy.customdummy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ColumnVO {

    private String columnName;
    private String type;
    private ColumnType columnType;
    private boolean useRange;
    private int rangeSize;
    private List<String> range;

    public void setType(String type) {
        this.type = type;
        this.columnType = ColumnType.parseByCode(type);
    }

    @Getter
    public enum ColumnType {
        DATE("D"),
        DATETIME("DT"),
        STRING("S"),
        INT("I"),
        DOUBLE("DO"),
        ;

        private String code;

        ColumnType(String code) {
            this.code = code;
        }

        public static ColumnType parseByCode(String code) {
            for (ColumnType type : ColumnType.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }

}
