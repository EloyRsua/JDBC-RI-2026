package uo.ri.cws.application.ui.foreman.client;

import uo.ri.util.menu.BaseMenu;

public class ClientMenu extends BaseMenu {

    public ClientMenu() {
        menuOptions = new Object[][] { { "Foreman > Client management", null },

                { "Register a client", UnsupportedOperationException.class },
                { "Update a client", UnsupportedOperationException.class },
                { "Disable a client", UnsupportedOperationException.class },
                { "List all clients", UnsupportedOperationException.class }, };
    }

}
