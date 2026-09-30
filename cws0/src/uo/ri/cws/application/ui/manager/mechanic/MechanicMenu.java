package uo.ri.cws.application.ui.manager.mechanic;

import uo.ri.cws.application.ui.manager.mechanic.action.*;
import uo.ri.util.menu.BaseMenu;

public class MechanicMenu extends BaseMenu {

    public MechanicMenu() {
        menuOptions = new Object[][] {
                { "Manager > Mechanics management", null },
                { "Add mechanic", AddMechanicAction.class },
                { "Update mechanic", UpdateMechanicAction.class },
                { "Delete mechanic", DeleteMechanicAction.class },
                { "List mechanic", ListMechanicAction.class },
                { "List mechanics", ListAllMechanicsAction.class }, };
    }

}
