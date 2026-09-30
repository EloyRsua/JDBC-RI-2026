package uo.ri.cws.application.ui.manager.mechanic.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.menu.Action;

public class UpdateMechanicAction implements Action {

    private static final String TMECHANICS_FINDBYID = 
            "select * from TMechanics where id = ?";


    @Override
    public void execute() throws BusinessException {

        // Get info
        String id = Console.readString("Type mechahic id to update");

        // check mechanic exists
        checkMechanicExists(id);
        // Ask for new data
        // nif is the identity, cannot be changed
        String name = Console.readString("Name");
        String surname = Console.readString("Surname");

        // update
        updateMechanic(id, name, surname);

        // Print result
        Console.println("Mechanic updated");
    }

    private void updateMechanic(String id, String name, String surname) {

       
    }

    private void checkMechanicExists(String id) throws BusinessException {
        try (Connection c = Jdbc.createThreadConnection()) {
            try (PreparedStatement pst = c
                    .prepareStatement(TMECHANICS_FINDBYID)) {
                pst.setString(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    if (!rs.next()) {
                        throw new BusinessException("Mechanic does not exist");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}