package uo.ri.cws.application.ui.manager.mechanic.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.menu.Action;

public class ListAllMechanicsAction implements Action {

	MechanicCrudService serviceFactory = Factories.service.forMechanicCrudService();

    @Override
    public void execute() throws BusinessException {

        Console.println("\nList of mechanics \n");
        Console.println(serviceFactory.findAll());
       
    }
}