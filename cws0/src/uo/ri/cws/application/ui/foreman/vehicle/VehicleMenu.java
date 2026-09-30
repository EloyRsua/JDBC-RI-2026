package uo.ri.cws.application.ui.foreman.vehicle;

import uo.ri.util.menu.BaseMenu;

public class VehicleMenu extends BaseMenu {

    public VehicleMenu() {
        menuOptions = new Object[][] { { "Foreman > Vehicle management", null },

                { "Register new vehicle", UnsupportedOperationException.class },
                { "Update vehicle", UnsupportedOperationException.class },
                { "Disable vehicle", UnsupportedOperationException.class },
                { "List all vehicles", UnsupportedOperationException.class }, };
    }

}
