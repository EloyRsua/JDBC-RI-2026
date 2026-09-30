package uo.ri.conf;

import uo.ri.cws.application.persistence.PersistenceFactory;
import uo.ri.cws.application.persistence.PersistenceFactoryImpl;
import uo.ri.cws.application.service.ServiceFactory;
import uo.ri.cws.application.service.ServiceFactoryImpl;

public class Factories {

    public static ServiceFactory service = new ServiceFactoryImpl();
    public static PersistenceFactory persistence = new PersistenceFactoryImpl();

    public static void close() {
    }

}
