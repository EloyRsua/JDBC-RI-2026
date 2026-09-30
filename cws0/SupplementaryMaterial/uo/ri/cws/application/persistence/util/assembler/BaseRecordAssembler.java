package uo.ri.cws.application.persistence.util.assembler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import uo.ri.cws.application.persistence.Gateway.BaseRecord;

public class BaseRecordAssembler<T extends BaseRecord> {

    private Supplier<T> supplier; // Supplier to create new instances of T

    public BaseRecordAssembler(Supplier<T> supplier) {
        this.supplier = Objects.requireNonNull(supplier);
    }

    public List<T> toRecordList(ResultSet rs) throws SQLException {
        List<T> res = new ArrayList<>();
        while (rs.next()) {
            res.add(toRecord(rs));
        }
        return res;
    }

    public T toRecord(ResultSet m) throws SQLException {
        T dto = supplier.get(); // Create a new instance of T using the supplier
        dto.id = m.getString("id");
        dto.version = m.getLong("version");
//        dto.entityState = m.getString("entityState");
//        dto.createdAt = m.getTimestamp("createdat").toLocalDateTime();
//        dto.updatedAt = m.getTimestamp("updatedat").toLocalDateTime();
        return dto;
    }

}
