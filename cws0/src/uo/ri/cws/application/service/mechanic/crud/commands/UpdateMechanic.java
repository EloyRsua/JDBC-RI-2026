package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class UpdateMechanic {
    
    //Cambios en la consulta, tenia fallos desde el principio
    private static final String TMECHANICS_UPDATE = 
        "UPDATE TMechanics "
        + "SET name = ?, surname = ?, nif = ?, version = version + 1, updatedAt = ? "
        + "WHERE id = ? AND version = ?";
    
    private MechanicDto dto;
    
	public UpdateMechanic(MechanicDto dto) {
		this.dto = dto;
	}


	public void execute() throws BusinessException {
		 // Process
        try (Connection c = Jdbc.createThreadConnection()) {
            try (PreparedStatement pst = c
                    .prepareStatement(TMECHANICS_UPDATE)) {

                    pst.setString(1, dto.name);
                    pst.setString(2, dto.surname);
                    pst.setString(3, dto.nif); 
                    pst.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                    pst.setString(5, dto.id);
                    pst.setLong(6, dto.version);

                    int affectedRows = pst.executeUpdate();
                    if (affectedRows == 0) {
                        throw new BusinessException("Mechanic does not exist or has been modified concurrently");
                    }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
		
	}

}
