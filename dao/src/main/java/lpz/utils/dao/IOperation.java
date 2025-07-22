package lpz.utils.dao;

import lpz.utils.dao.enums.WhereAppender;

public interface IOperation {
    IOperation where(WhereAppender appender,
                     final String clause,
                     final Object... values);
}
