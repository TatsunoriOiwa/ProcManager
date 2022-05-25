package qwertzite.purchaseprocedures2.client.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;
import org.eclipse.swt.layout.GridLayout;

public class NodeCompleted extends Composite {

	private Label titleLabel;
	
	private boolean current;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public NodeCompleted(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		titleLabel = new Label(this, SWT.NONE);
		titleLabel.setFont(SWTResourceManager.getFont("Yu Gothic UI", 12, SWT.NORMAL));
		titleLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		titleLabel.setText("購入手続完了済");
		
		this.current = true;
	}
	
	public void reflectEntry(PurchaseEntry entry, boolean current) {
		if (this.current != current) {
			this.titleLabel.setForeground(SWTResourceManager.getColor(current ? SWT.COLOR_BLACK : SWT.COLOR_WIDGET_DISABLED_FOREGROUND));
			this.current = current;
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
