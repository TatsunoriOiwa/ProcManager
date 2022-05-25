package qwertzite.purchaseprocedures2.client.screen.cmp;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;

public class DialogConfirmation extends Dialog {

	private String message;
	
	protected boolean result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogConfirmation(Shell parent, int style, String message) {
		super(parent, style);
		setText("SWT Dialog");
		this.message = message;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public boolean open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.SHELL_TRIM | SWT.PRIMARY_MODAL);
		shell.setSize(500, 200);
		shell.setText(getText());
		shell.setLayout(new GridLayout(2, false));
		
		Label lblTxt = new Label(shell, SWT.NONE);
		lblTxt.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 2, 1));
		lblTxt.setText(this.message);
		
		Button btnConfirm = new Button(shell, SWT.NONE);
		btnConfirm.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnConfirm.setText(" CONFIRM ");
		btnConfirm.addSelectionListener(SelectionListener.widgetSelectedAdapter(this::onConfirmed));
		
		Button btnCancell = new Button(shell, SWT.NONE);
		btnCancell.setText(" CANCELL ");
		btnCancell.addSelectionListener(SelectionListener.widgetSelectedAdapter(this::onCancelled));
	}
	
	private void onConfirmed(SelectionEvent event) {
		this.result = true;
		this.shell.close();
	}
	
	private void onCancelled(SelectionEvent event) {
		this.shell.close();
	}
}
