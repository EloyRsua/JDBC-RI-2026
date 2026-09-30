package uo.ri.cws.application.ui.cashier;

import uo.ri.cws.application.ui.cashier.action.FindNotInvoicedWorkOrdersByClientAction;
import uo.ri.cws.application.ui.cashier.action.InvoiceWorkorderAction;
import uo.ri.cws.application.ui.cashier.action.SettleInvoiceAction;
import uo.ri.util.menu.BaseMenu;

public class MainMenu extends BaseMenu {

    public MainMenu() {

        menuOptions = new Object[][] { { "Cash", null },
                { "Find not invoiced work orders by client",
                        FindNotInvoicedWorkOrdersByClientAction.class },
                { "Find work orders by plate", UnsupportedOperationException.class },
                { "Invoice work orders", InvoiceWorkorderAction.class },
                { "Settle invoice", SettleInvoiceAction.class }, };

    }

    public static void main(String[] args) {
        new MainMenu().execute();
    }

}
