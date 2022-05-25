package qwertzite.purchaseprocedures2.client.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridData;

public class MissingEntryRenderer extends AbstractEntryRenderer {

	private Label lblSerial;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MissingEntryRenderer(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		composite.setLayout(new GridLayout(1, false));
		
		Label label = new Label(composite, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Yu Gothic UI", 12, SWT.NORMAL));
		label.setText("不明");
		
		lblSerial = new Label(composite, SWT.NONE);
		lblSerial.setText("Serial");
	}

	@Override
	public void reflectEntry(PurchaseEntry entry) {
		this.lblSerial.setText(entry.getSerial().toString());
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
