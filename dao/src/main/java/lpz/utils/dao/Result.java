package lpz.utils.dao;

import java.util.List;

public record Result<T>(List<T> entities, Integer lines) {
    public static <T> Result<T> of(List<T> entities) {
        return new Result<>(entities, entities.size());
    }

    public static <T> Result<T> of(Integer lines) {
        return new Result<>(null, lines);
    }
}
