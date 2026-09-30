package uo.ri.cws.application.persistence.util.command;

import java.sql.Connection;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.PersistenceException;

public class CommandExecutor {

    public <T> T execute(Command<T> cmd) throws BusinessException {

        try (Connection c = Jdbc.createThreadConnection();) {
            c.setAutoCommit(false);

            try {
                T res = cmd.execute();
                c.commit();
                return res;

            } catch (Exception e) {
                c.rollback();
                throw e;

            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }

    }

}
