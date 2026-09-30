package uo.ri.ui.cashier;

import uo.ri.ui.cashier.action.FindNotInvoicedWorkOrdersByClientAction;
import uo.ri.ui.cashier.action.FindWorkOrdersByClientAction;
import uo.ri.ui.cashier.action.InvoiceWorkorderAction;
import uo.ri.ui.cashier.action.SettleInvoiceAction;
import uo.ri.util.menu.BaseMenu;

public class MainMenu extends BaseMenu {{
	menuOptions = new Object[][] {
		{ "Cash", null },
		{ "Find not invoiced work orders by client", 	FindNotInvoicedWorkOrdersByClientAction.class },
		{ "Find work orders by client", 				FindWorkOrdersByClientAction.class },
		{ "Invoice work orders", 						InvoiceWorkorderAction.class },
		{ "Settle invoice", 							SettleInvoiceAction.class },
	};
}}