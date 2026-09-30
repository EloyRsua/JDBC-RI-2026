package uo.ri.cws.application.ui.foreman;

import uo.ri.cws.application.ui.foreman.client.ClientMenu;
import uo.ri.cws.application.ui.foreman.reception.ReceptionMenu;
import uo.ri.cws.application.ui.foreman.vehicle.VehicleMenu;
import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class MainMenu extends BaseMenu {

    public MainMenu() {
        menuOptions = new Object[][] { { "Foreman", null },
                { "Vehicle reception", ReceptionMenu.class },
                { "Client management", ClientMenu.class },
                { "Vehicle management", VehicleMenu.class },
                { "Review client history", NotYetImplementedAction.class }, };
    }

    public static void main(String[] args) {
        new MainMenu().execute();
    }

}
