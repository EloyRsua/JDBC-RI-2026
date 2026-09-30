package uo.ri.cws.application.ui.mechanic;

import uo.ri.util.menu.BaseMenu;

public class MainMenu extends BaseMenu {

    public MainMenu() {
        menuOptions = new Object[][] { { "Mechanic", null },
                { "List assigned work orders", UnsupportedOperationException.class },
                { "Add parts to work order", UnsupportedOperationException.class },
                { "Remove parts from work order",
                	UnsupportedOperationException.class },
                { "Close a work order", UnsupportedOperationException.class }, };
    }

    public static void main(String[] args) {
        new MainMenu().execute();
    }

}
