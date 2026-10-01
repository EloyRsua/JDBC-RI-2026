package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class DeleteMechanic {

    private static final String TMECHANICS_FIND_BY_ID = 
            "SELECT id FROM TMechanics WHERE id = ?";

    private static final String TWORKORDERS_COUNT_BY_MECHANIC = 
            "SELECT COUNT(*) FROM TWorkOrders WHERE mechanic_id = ?";

    private static final String TINTERVENTIONS_COUNT_BY_MECHANIC = 
            "SELECT COUNT(*) FROM TInterventions WHERE mechanic_id = ?";

    private static final String TMECHANICS_DELETE = 
            "DELETE FROM TMechanics WHERE id = ?";

    private String idMechanic;

    public DeleteMechanic(String idMechanic) {
        this.idMechanic = idMechanic;
    }

    public void execute() throws BusinessException {
        ArgumentChecks.isNotNull(idMechanic, "Mechanic id cannot be null");
        ArgumentChecks.isNotBlank(idMechanic, "Mechanic id cannot be empty");

        try (Connection c = Jdbc.createThreadConnection()) {
            checkMechanicExists(c);
            checkNoWorkOrders(c);
            checkNoInterventions(c);

            try (PreparedStatement pst = c.prepareStatement(TMECHANICS_DELETE)) {
                pst.setString(1, idMechanic);
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkMechanicExists(Connection c) throws SQLException, BusinessException {
        try (PreparedStatement pst = c.prepareStatement(TMECHANICS_FIND_BY_ID)) {
            pst.setString(1, idMechanic);
            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
                    throw new BusinessException("Mechanic does not exist");
                }
            }
        }
    }

    private void checkNoWorkOrders(Connection c) throws SQLException, BusinessException {
        try (PreparedStatement pst = c.prepareStatement(TWORKORDERS_COUNT_BY_MECHANIC)) {
            pst.setString(1, idMechanic);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new BusinessException("Mechanic has work orders assigned");
                }
            }
        }
    }

    private void checkNoInterventions(Connection c) throws SQLException, BusinessException {
        try (PreparedStatement pst = c.prepareStatement(TINTERVENTIONS_COUNT_BY_MECHANIC)) {
            pst.setString(1, idMechanic);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new BusinessException("Mechanic has interventions done");
                }
            }
        }
    }
}
