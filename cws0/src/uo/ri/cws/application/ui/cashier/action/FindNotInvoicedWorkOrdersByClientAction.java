package uo.ri.cws.application.ui.cashier.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.menu.Action;

public class FindNotInvoicedWorkOrdersByClientAction implements Action {

    private static final String TWORKORDERS_FINDNOTINVOICED = 
            "select a.id, a.description, a.date, a.state, a.amount"
            + " from TWorkOrders as a, TVehicles as v, TClients as c"
            + " where a.vehicle_id = v.id" + " and v.client_id = c.id"
            + "state like 'APPROVED' and nif like ?";

    /**
     * Process: 
     * - Ask customer nif 
     * - Display all uncharged workorder (status <> 'INVOICED'). 
     *   For each workorder, display id, vehicle id, date, status, amount 
     *   and description
     */

    @Override
    public void execute() throws BusinessException {
        String nif = Console.readString("Client nif ");

        Console.println("\nClient's not invoiced work orders\n");

        try (Connection c = Jdbc.createThreadConnection()) {
            try (PreparedStatement pst = c
                    .prepareStatement(TWORKORDERS_FINDNOTINVOICED)) {
                pst.setString(1, nif);
                try (ResultSet rs = pst.executeQuery();) {
                    while (rs.next()) {
                        Console.printf(
                                "\t%s \t%-40.40s \t%s \t%-12.12s \t%.2f\n",
                                rs.getString(1),
                                rs.getString(2),
                                rs.getTimestamp(3),
                                rs.getString(4),
                                rs.getDouble(5));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}