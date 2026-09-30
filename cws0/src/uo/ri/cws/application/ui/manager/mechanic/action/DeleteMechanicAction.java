package uo.ri.cws.application.ui.manager.mechanic.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.menu.Action;

public class DeleteMechanicAction implements Action {

	MechanicCrudService serviceFactory = Factories.service.forMechanicCrudService();

    @Override
    public void execute() throws BusinessException {

        String idMechanic = Console.readString("Type mechanic id ");
        
        Optional<MechanicDto> opMech= serviceFactory.findById(idMechanic);
    
    	serviceFactory.delete(idMechanic);
    	Console.println("Mechanic deleted");
  

       
    }

}