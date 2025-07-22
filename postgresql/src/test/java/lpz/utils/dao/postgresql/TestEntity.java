package lpz.utils.dao.postgresql;

import lpz.utils.dao.annotations.Field;
import lpz.utils.dao.annotations.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Table(name = "test")
public class TestEntity {
    @Field(primaryKey = true)
    private UUID id;
    
    @Field
    private String field1;

    @Field
    private Integer field2;

    @Field
    private Long field3;

    @Field
    private Float field4;

    @Field
    private Double field5;

    @Field
    private Boolean field6;

    @Field
    private BigDecimal field7;

    @Field
    private Byte field8;

    @Field
    private LocalDate field9;

    @Field
    private String entityId;

    public TestEntity() {
    }

    public TestEntity(UUID id, String field1, Integer field2, Long field3, Float field4, Double field5, Boolean field6, BigDecimal field7, Byte field8, LocalDate field9) {
        this.id = id;
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.field4 = field4;
        this.field5 = field5;
        this.field6 = field6;
        this.field7 = field7;
        this.field8 = field8;
        this.field9 = field9;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public Integer getField2() {
        return field2;
    }

    public void setField2(Integer field2) {
        this.field2 = field2;
    }

    public Long getField3() {
        return field3;
    }

    public void setField3(Long field3) {
        this.field3 = field3;
    }

    public Float getField4() {
        return field4;
    }

    public void setField4(Float field4) {
        this.field4 = field4;
    }

    public Double getField5() {
        return field5;
    }

    public void setField5(Double field5) {
        this.field5 = field5;
    }

    public Boolean getField6() {
        return field6;
    }

    public void setField6(Boolean field6) {
        this.field6 = field6;
    }

    public BigDecimal getField7() {
        return field7;
    }

    public void setField7(BigDecimal field7) {
        this.field7 = field7;
    }

    public Byte getField8() {
        return field8;
    }

    public void setField8(Byte field8) {
        this.field8 = field8;
    }

    public LocalDate getField9() {
        return field9;
    }

    public void setField9(LocalDate field9) {
        this.field9 = field9;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TestEntity that = (TestEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
