package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class DeleteMechanic {
    private static final String TMECHANICS_DELETE = "DELETE FROM TMECHANICS "
            + "WHERE ID = ?";

    private MechanicCrudService serviceFactory = Factories.service.forMechanicCrudService();

    
    private String idMechanic;
	public DeleteMechanic(String idMechanic) {
		this.idMechanic=idMechanic;
	}

	public void execute() throws BusinessException {
        // Process
		
		if(serviceFactory.findById(idMechanic).isEmpty()) {
			throw new BusinessException("Mechanic does not exist");
		}
		
		
        try (Connection c = Jdbc.createThreadConnection();) {
            try (PreparedStatement pst = c
                    .prepareStatement(TMECHANICS_DELETE)) {
                pst.setString(1, idMechanic);
                pst.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

		
	}

}
