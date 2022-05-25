package qwertzite.purchaseprocedures2.client.screen;

import org.eclipse.swt.widgets.Composite;

import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;

public abstract class AbstractEntryRenderer extends Composite {
	public AbstractEntryRenderer(Composite parent, int style) {
		super(parent, style);
	}

	public abstract void reflectEntry(PurchaseEntry entry);
}
