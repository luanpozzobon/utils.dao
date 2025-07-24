package lpz.utils.dao.postgresql;

import lpz.utils.dao.annotations.Field;
import lpz.utils.dao.annotations.Join;
import lpz.utils.dao.annotations.JoinOn;
import lpz.utils.dao.annotations.Table;

import java.util.List;
import java.util.Objects;

@Table(name = "test2")
public class TestEntity2 {
    @Field(primaryKey = true)
    private String id;

    @Field
    private String field1;

    @Join(on = @JoinOn(localField = "id", foreignField = "entity_id"))
    private List<TestEntity> entity;

    public TestEntity2() { }

    public TestEntity2(String id, String field1) {
        this.id = id;
        this.field1 = field1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public List<TestEntity> getEntity() {
        return entity;
    }

    public void setEntity(List<TestEntity> entity) {
        this.entity = entity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TestEntity2 that = (TestEntity2) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
