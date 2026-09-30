package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.console.Console;
import uo.ri.util.jdbc.Jdbc;

public class ListAllMechanics {
	
    private static final String TMECHANICS_FINDALL = "SELECT ID, NAME, "
            + "SURNAME, NIF, VERSION FROM TMECHANICS";
    
	public List<MechanicDto> execute() {
        MechanicDto dto = new MechanicDto();
        List<MechanicDto> list = new ArrayList<>();
        try (Connection c = Jdbc.createThreadConnection()) {
            try (PreparedStatement pst = c
                    .prepareStatement(TMECHANICS_FINDALL)) {
                try (ResultSet rs = pst.executeQuery();) {
                    while (rs.next()) {
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
                        list.add(dto);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
	}

}
