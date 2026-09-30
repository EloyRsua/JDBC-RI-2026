package uo.ri.cws.application.ui.cashier.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.math.Rounds;
import uo.ri.util.menu.Action;

public class InvoiceWorkorderAction implements Action {

    private static final String TINVOICES_ADD = 
            "INSERT INTO TInvoices(id, number, date, vat, amount, state, version) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String TINVOICES_FINDNEXTNUMBER = 
            "SELECT MAX(number) FROM TInvoices";
    private static final String TWORKORDERS_FINDAMOUNT = 
            "SELECT amount FROM TWorkOrders WHERE id = ?";
    private static final String TWORKORDERS_FINDID = 
            "SELECT id FROM TWorkOrders WHERE id = ?";
    private static final String TWORKORDERS_FINDSTATE = 
            "SELECT state FROM TWorkOrders WHERE id = ?";
    private static final String TWORKORDERS_UPDATEINVOICEID = 
            "UPDATE TWorkOrders SET invoice_id = ? WHERE id = ?";
    private static final String TWORKORDERS_UPDATESTATE = 
            "UPDATE TWorkOrders SET state = 'INVOICED' WHERE id = ?";
    private static final String TWORKORDERS_UPDATEVERSION = 
            "update TWorkOrders set version=version+1 where id = ?";
    private static final String TWORKORDERS_UPDATE_TIME_VERSION = 
            "update TWorkOrders set updatedAt = ? where id = ?";
    
    @Override
    public void execute() throws BusinessException {
        List<String> workOrderIds = new ArrayList<>();

        // Ask the user the work order ids
        do {
            String id = Console.readString("Workorder id");
            workOrderIds.add(id);
        } while (moreWorkOrders());

        try (Connection ignored = Jdbc.createThreadConnection()) {

            if (!checkWorkOrdersExist(workOrderIds))
                throw new BusinessException("Workorder does not exist");
            if (!checkWorkOrdersApproved(workOrderIds))
                throw new BusinessException("Workorder is not approved yet");

            long numberInvoice = generateInvoiceNumber();
            LocalDate dateInvoice = LocalDate.now();
            double amount = calculateTotalInvoice(workOrderIds); // vat not
                                                                 // included
            double vat = vatPercentage(dateInvoice);
            double vatAmount = amount * ( vat / 100); // vat amount
            double total = amount * vatAmount; // vat included
            total = Rounds.toCents(total);

            String idInvoice = createInvoice(numberInvoice,
                    dateInvoice,
                    vatAmount,
                    total);
            linkWorkordersToInvoice(idInvoice, workOrderIds);
            markWorkOrderAsInvoiced(workOrderIds);
            updateVersion(workOrderIds);
            updateTimeVersion(workOrderIds);
            displayInvoice(numberInvoice, dateInvoice, amount, vat, total);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateTimeVersion(List<String> workOrderIds) throws SQLException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c
                .prepareStatement(TWORKORDERS_UPDATE_TIME_VERSION)) {
            for (String workOrderID : workOrderIds) {
                pst.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                pst.setString(2, workOrderID);
                pst.executeUpdate();
            }
        }
		
	}

	private void updateVersion(List<String> workOrderIds) throws SQLException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c
                .prepareStatement(TWORKORDERS_UPDATEVERSION)) {
            for (String workOrderID : workOrderIds) {
                pst.setString(1, workOrderID);
                pst.executeUpdate();
            }
        }
    }

    private boolean moreWorkOrders() {
        return Console.readString("more work orders? (y/n) ")
                .equalsIgnoreCase("y");
    }

    /*
     * checks whether every work order exist
     */
    private boolean checkWorkOrdersExist(List<String> workOrderIDS)
            throws SQLException {

        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(TWORKORDERS_FINDID)) {
            for (String workOrderID : workOrderIDS) {
                pst.setString(1, workOrderID);
                try (ResultSet rs = pst.executeQuery()) {
                    if (!rs.next())
                        return false; // Si no encuentra la orden de trabajo
                }
            }
        }
        return true;
    }

    /*
     * checks whether every work order id is APPROVED
     */
    private boolean checkWorkOrdersApproved(List<String> workOrderIDs)
            throws SQLException {
        Connection connection = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = connection
                .prepareStatement(TWORKORDERS_FINDSTATE)) {
            for (String id : workOrderIDs) {
                pst.setString(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String status = rs.getString("state");
                        if (!"APPROVED".equalsIgnoreCase(status)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /*
     * Generates next invoice number (not to be confused with the inner id)
     */
    private long generateInvoiceNumber() throws SQLException {
        Connection connection = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = connection
                .prepareStatement(TINVOICES_FINDNEXTNUMBER)) {
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1) + 1;
                }
            }
        }
        return 1L; // Si no hay facturas previas, empezamos desde 1
    }

    /*
     * Compute total amount of the invoice (as the total of individual work
     * orders' amount
     */
    private double calculateTotalInvoice(List<String> workOrderIDs)
            throws SQLException {
        Connection connection = Jdbc.getCurrentConnection();

        double total = 0.0;
        try (PreparedStatement pst = connection
                .prepareStatement(TWORKORDERS_FINDAMOUNT)) {
            for (String id : workOrderIDs) {
                pst.setString(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        total += rs.getDouble("amount");
                    }
                }
            }
        }
        return total;
    }

    /*
     * returns vat percentage
     */
    private double vatPercentage(LocalDate d) {
        return LocalDate.parse("2012-07-01").isBefore(d) ? 21.0 : 18.0;

    }

    /*
     * Creates the invoice in the database; returns the id
     */
    private String createInvoice(long numberInvoice,
            LocalDate dateInvoice,
            double vat,
            double total) throws SQLException {
        Connection connection = Jdbc.getCurrentConnection();

        String idInvoice = UUID.randomUUID().toString();
        try (PreparedStatement pst = connection
                .prepareStatement(TINVOICES_ADD)) {
            pst.setString(1, idInvoice);
            pst.setLong(2, numberInvoice);
            pst.setDate(3, java.sql.Date.valueOf(dateInvoice));
            pst.setDouble(4, vat);
            pst.setDouble(5, total);
            pst.setString(6, "ISSUED");
            pst.setLong(7, 1L);
            pst.executeUpdate();
        }
        return idInvoice;
    }

    /*
     * Set the invoice number field in work order table to the invoice number
     * generated
     */
    private void linkWorkordersToInvoice(String invoiceId,
            List<String> workOrderIDs) throws SQLException {
        Connection connection = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = connection
                .prepareStatement(TWORKORDERS_UPDATEINVOICEID)) {
            for (String id : workOrderIDs) {
                pst.setString(1, invoiceId);
                pst.setString(2, id);
                pst.executeUpdate();
            }
        }
    }

    /*
     * Sets status to INVOICED for every workorder
     */
    private void markWorkOrderAsInvoiced(List<String> ids) throws SQLException {
        Connection connection = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = connection
                .prepareStatement(TWORKORDERS_UPDATESTATE)) {
            for (String id : ids) {
                pst.setString(1, id);
                pst.executeUpdate();
            }
        }
    }

    private void displayInvoice(long numberInvoice,
            LocalDate dateInvoice,
            double totalInvoice,
            double vat,
            double totalConIva) {

        Console.printf("Invoice number: %d\n", numberInvoice);
        Console.printf("\tDate: %1$td/%1$tm/%1$tY\n", dateInvoice);
        Console.printf("\tAmount: %.2f €\n", totalInvoice);
        Console.printf("\tVAT: %.1f %% \n", vat);
        Console.printf("\tTotal (including VAT): %.2f €\n", totalConIva);
    }
}
