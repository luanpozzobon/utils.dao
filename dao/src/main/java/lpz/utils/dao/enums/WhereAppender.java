package lpz.utils.dao.enums;

public enum WhereAppender {
    WHERE("WHERE"),
    AND("AND"),
    OR("OR");

    private final String value;

    public String getValue() {
        return value;
    }

    WhereAppender(String value) {
        this.value = value;
    }
}
