package qwertzite.purchaseprocedures2.client.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import qwertzite.purchaseprocedures2.procedure.Procedure;

public class NodeEntryHeader extends Composite {

	private Label titleLabel;

	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public NodeEntryHeader(Composite parent, int style, Procedure procedure) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		titleLabel = new Label(this, SWT.NONE);
		titleLabel.setFont(SWTResourceManager.getFont("Yu Gothic UI", 12, SWT.NORMAL));
		titleLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		titleLabel.setText(procedure.getProcedureName());
		
		Label lblDescription = new Label(this, SWT.WRAP);
		lblDescription.setText(procedure.getProcedureDescription());
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
