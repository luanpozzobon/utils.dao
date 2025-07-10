package lpz.utils.dao.postgresql.helper;

public abstract class ParamAdder {
    protected static void addString(StringBuilder sql) {
        sql.append("?::varchar");
    }

    protected static void addUUID(StringBuilder sql) {
        sql.append("?::uuid");
    }

    protected static void addInteger(StringBuilder sql) {
        sql.append("?::integer");
    }

    protected static void addLong(StringBuilder sql) {
        sql.append("?::bigint");
    }

    protected static void addFloat(StringBuilder sql) {
        sql.append("?::float");
    }

    protected static void addDouble(StringBuilder sql) {
        sql.append("?::double precision");
    }

    protected static void addBoolean(StringBuilder sql) {
        sql.append("?::boolean");
    }

    protected static void addBigDecimal(StringBuilder sql) {
        sql.append("?::numeric");
    }

    protected static void addByte(StringBuilder sql) {
        sql.append("?::smallint");
    }

    protected static void addLocalDate(StringBuilder sql) {
        sql.append("?::date");
    }

    protected static void addDefault(StringBuilder sql) {
        sql.append("?");
    }
}