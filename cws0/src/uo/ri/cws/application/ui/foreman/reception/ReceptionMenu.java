package uo.ri.cws.application.ui.foreman.reception;

import uo.ri.util.menu.BaseMenu;

public class ReceptionMenu extends BaseMenu {

    public ReceptionMenu() {
        menuOptions = new Object[][] { { "Foreman > Vehicle reception", null },

                { "Register work order", UnsupportedOperationException.class },
                { "Update workorder", UnsupportedOperationException.class },
                { "Remove workorder", UnsupportedOperationException.class },
                { "", null },
                { "List work orders", UnsupportedOperationException.class },
                { "View work order detail", UnsupportedOperationException.class },
                { "", null },
                { "List certified mechanics", UnsupportedOperationException.class },
                { "Assign a work order", UnsupportedOperationException.class }, };
    }

}
