package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.jdbc.Jdbc;

public class UpdateMechanic {
    private static final String TMECHANICS_UPDATE = 
            "update TMechanics set name = ?, surname = ?, "
    		+ "version = version + 1, updatedat = ?"
            + "where id = ?";
    
    private MechanicDto dto;
    
	public UpdateMechanic(MechanicDto dto) {
		this.dto = dto;
	}


	public void execute() {
		 // Process
        try (Connection c = Jdbc.createThreadConnection()) {
            try (PreparedStatement pst = c
                    .prepareStatement(TMECHANICS_UPDATE)) {
                pst.setString(1, dto.name);
                pst.setString(2, dto.surname);
                pst.setTimestamp(3, new Timestamp(
						System.currentTimeMillis()));
                pst.setString(4, dto.id);

                pst.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
		
	}

}
