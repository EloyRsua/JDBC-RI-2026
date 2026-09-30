package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class AddMechanic {
	 private static final String TMECHANICS_ADD = "insert into TMechanics"
	            + "(id, nif, name, surname, version, "
	            + "createdAt, updatedAt, entityState) "
	            + "values (?, ?, ?, ?, ?, ?, ?, ?)";
	 
	private MechanicDto dto;
    private MechanicCrudService serviceFactory = Factories.service.forMechanicCrudService();

	public AddMechanic (MechanicDto dto) {
		this.dto = dto;
	}
	 
	public MechanicDto execute() throws BusinessException {
		// Get info
        String nif = dto.nif;
        String name = dto.name;
        String surname = dto.surname;
        // Lo asignamos nosotros, el cliente no debería saber esto nunca!
        String id = dto.id = UUID.randomUUID().toString();
        long version = dto.version = 1;

		if(!serviceFactory.findByNif(nif).isEmpty()) {
			throw new BusinessException("Mechanic already exists");
        }
        
        // Process
        try (Connection c = Jdbc.createThreadConnection();) {
            try (PreparedStatement pst = c.prepareStatement(TMECHANICS_ADD)) {
                pst.setString(1, id);
                pst.setString(2, nif);
                pst.setString(3, name);
                pst.setString(4, surname);
                pst.setLong(5, version);
                pst.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                pst.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
                pst.setString(8, "ENABLED");               

                pst.executeUpdate();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dto;
	}
}
