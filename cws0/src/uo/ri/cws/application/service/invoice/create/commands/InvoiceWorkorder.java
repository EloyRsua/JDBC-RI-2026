package uo.ri.cws.application.service.invoice.create.commands;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class InvoiceWorkorder {

    private static final String TINVOICES_FINDNEXTNUMBER = 
            "SELECT MAX(number) FROM TInvoices";

    private static final String TINVOICES_ADD = 
            "INSERT INTO TInvoices(id, number, date, subtotal, vatamount, vatrate, total, state, version, createdAt, updatedAt, entityState) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String TWORKORDERS_FIND_BY_ID = 
            "SELECT id, state, total_amount FROM TWorkOrders WHERE id = ?";

    private static final String TWORKORDERS_UPDATE = 
            "UPDATE TWorkOrders SET invoice_id = ?, state = 'INVOICED', version = version + 1, updatedAt = ? WHERE id = ?";

    private List<String> workOrderIds;

    public InvoiceWorkorder(List<String> workOrderIds) {
        this.workOrderIds = workOrderIds;
    }

    public InvoiceDto execute() throws BusinessException {
        checkArguments();

        try (Connection c = Jdbc.createThreadConnection()) {
            BigDecimal subtotal = BigDecimal.ZERO;

            for (String woId : workOrderIds) {
                try (PreparedStatement pst = c.prepareStatement(TWORKORDERS_FIND_BY_ID)) {
                    pst.setString(1, woId);
                    try (ResultSet rs = pst.executeQuery()) {
                        if (!rs.next()) {
                            throw new BusinessException("Workorder does not exist: " + woId);
                        }
                        String state = rs.getString("state");
                        if (!"APPROVED".equalsIgnoreCase(state)) {
                            throw new BusinessException("Workorder is not approved yet: " + woId);
                        }
                        BigDecimal amount = rs.getBigDecimal("total_amount");
                        if (amount != null) {
                            subtotal = subtotal.add(amount);
                        }
                    }
                }
            }

            subtotal = subtotal.setScale(2, RoundingMode.HALF_EVEN);
            LocalDate dateInvoice = LocalDate.now();
            BigDecimal vatRate = getVatPercentage(dateInvoice);
            BigDecimal vatAmount = subtotal.multiply(vatRate)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);
            BigDecimal total = subtotal.add(vatAmount).setScale(2, RoundingMode.HALF_EVEN);

            long numberInvoice = generateInvoiceNumber(c);
            String idInvoice = UUID.randomUUID().toString();
            long version = 1L;
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());

            // Crear factura
            try (PreparedStatement pst = c.prepareStatement(TINVOICES_ADD)) {
                pst.setString(1, idInvoice);
                pst.setLong(2, numberInvoice);
                pst.setDate(3, Date.valueOf(dateInvoice));
                pst.setBigDecimal(4, subtotal);
                pst.setBigDecimal(5, vatAmount);
                pst.setBigDecimal(6, vatRate);
                pst.setBigDecimal(7, total);
                pst.setString(8, "ISSUED");
                pst.setLong(9, version);
                pst.setTimestamp(10, now);
                pst.setTimestamp(11, now);
                pst.setString(12, "ENABLED");
                pst.executeUpdate();
            }

            // Vincular work orders a la factura y actualizar estado/versión
            try (PreparedStatement pst = c.prepareStatement(TWORKORDERS_UPDATE)) {
                for (String woId : workOrderIds) {
                    pst.setString(1, idInvoice);
                    pst.setTimestamp(2, now);
                    pst.setString(3, woId);
                    pst.executeUpdate();
                }
            }

            // Construir resultado DTO
            InvoiceDto dto = new InvoiceDto();
            dto.id = idInvoice;
            dto.version = version;
            dto.number = numberInvoice;
            dto.date = dateInvoice;
            dto.subtotal = subtotal;
            dto.vatAmount = vatAmount;
            dto.vatPercentage = vatRate;
            dto.total = total;
            dto.state = "ISSUED";

            return dto;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkArguments() {
        ArgumentChecks.isNotNull(workOrderIds, "Work order ids cannot be null");
        ArgumentChecks.isFalse(workOrderIds.isEmpty(), "Work order ids cannot be empty");
        for (String id : workOrderIds) {
            ArgumentChecks.isNotNull(id, "Work order id cannot be null");
            ArgumentChecks.isNotBlank(id, "Work order id cannot be empty");
        }
    }

    private long generateInvoiceNumber(Connection c) throws SQLException {
        try (PreparedStatement pst = c.prepareStatement(TINVOICES_FINDNEXTNUMBER);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1) + 1;
            }
        }
        return 1L;
    }

    private BigDecimal getVatPercentage(LocalDate date) {
        return date.isAfter(LocalDate.of(2012, 6, 30)) ? new BigDecimal("21") : new BigDecimal("18");
    }
}
