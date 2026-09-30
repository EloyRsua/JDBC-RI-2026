package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.console.Console;
import uo.ri.util.jdbc.Jdbc;

public class ListMechanic {

    private static final String TMECHANICS_FINDBYNIF = 
            "SELECT ID, NAME, SURNAME, nif, VERSION FROM TMECHANICS "
                    + "WHERE NIF = ?";
    private String nif;
    
	public ListMechanic(String nif) {
		this.nif = nif;
	}

	public Optional<MechanicDto> execute() {
        MechanicDto dto = new MechanicDto();
        
        try (Connection c = Jdbc.createThreadConnection()) {
            try (PreparedStatement pst = c
                    .prepareStatement(TMECHANICS_FINDBYNIF)) {
                pst.setString(1, nif);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        Console.printf("\t%s %s %s %s %d\n",
                                rs.getString(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getLong(5));
                        dto.id=rs.getString(1);
                        dto.name = rs.getString(2);
                        dto.surname = rs.getString(3);
                        dto.nif = rs.getString(4);
                        dto.version = rs.getLong(5);
                        return Optional.of(dto);

                    }
                    
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
       return Optional.empty();
        
        
	}

}
