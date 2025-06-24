package lpz.utils.dao;

public interface IOperation {
    IOperation where(String clause, Object... values);
}
