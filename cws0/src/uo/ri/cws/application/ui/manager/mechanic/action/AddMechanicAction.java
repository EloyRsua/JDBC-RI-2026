package uo.ri.cws.application.ui.manager.mechanic.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.ServiceFactory;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.UserInteractionChecks;
import uo.ri.util.exception.UserInteractionException;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.menu.Action;
/**
 * Problema inicial, no hay division de capas y por lo tanto está realmente acoplado
 * Vamos a mover a la capa de servicio todo lo que se requiere, de momento lo movemos
 * 	a la capa de servicio pero está mal porque es acceso a datos, la semana que viene 
 * 	lo movemos donde se requiere (Capa de persistencia)
 * 
 * La semanas sucesivas usaremos DTO's para mover los datos entre las capas y usaremos
 * 	las diferentes capas para sus sucesivas tareas.
 * @author UO298184
 *
 */
public class AddMechanicAction implements Action {
	
	MechanicCrudService serviceFactory = Factories.service.forMechanicCrudService();
    
	@Override
    public void execute() throws BusinessException, UserInteractionException {

        // Get info
        String nif = Console.readString("nif");
        String name = Console.readString("Name");
        String surname = Console.readString("Surname");
        String id = UUID.randomUUID().toString();
        long version = 1;
        
        UserInteractionChecks.isNotEmpty(nif, "Invalid NIF");
        UserInteractionChecks.isNotEmpty(name, "Invalid Name");
        UserInteractionChecks.isNotEmpty(surname, "Invalid Surname");

        

        MechanicDto dto = new MechanicDto();
        dto.id=id;
        dto.name=name;
        dto.surname=surname;
        dto.version=version;
        dto.nif=nif;
        
        /**
         * Movemos toda la parte de acceso a datos al comando correspondiente
         */
        serviceFactory.create(dto);
        
        // Print result
        Console.println("Mechanic added");
    }

}
