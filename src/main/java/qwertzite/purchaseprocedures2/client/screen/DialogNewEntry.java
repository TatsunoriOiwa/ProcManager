package qwertzite.purchaseprocedures2.client.screen;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import qwertzite.purchaseprocedures2.client.PurchaseProcedure2;
import qwertzite.purchaseprocedures2.procedure.Procedure;
import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;
import org.eclipse.swt.widgets.Text;

public class DialogNewEntry extends Dialog {

	protected PurchaseEntry result;
	protected Shell shell;

	private ComboViewer comboViewer;
	private Button btnConfirm;
	private Button btnCancel;
	private Text txtSubject;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogNewEntry(Shell parent, int style) {
		super(parent, style);
		setText("New Entry");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public PurchaseEntry open() {
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
		shell.setSize(500, 300);
		shell.setText(getText());
		shell.setLayout(new GridLayout(2, false));
		
		Label lblSelectProcedureType = new Label(shell, SWT.NONE);
		lblSelectProcedureType.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		lblSelectProcedureType.setText("Select a procedure type for the new entry.");
		
		comboViewer = new ComboViewer(shell, SWT.NONE);
		Combo combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		comboViewer.addSelectionChangedListener(this::onComboSelectionChanged);
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new ComboLabelProvider());
		comboViewer.add(Procedure.getProcedureList().toArray());
		
		txtSubject = new Text(shell, SWT.BORDER);
		txtSubject.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		txtSubject.addModifyListener(this::onSubjectModified);
		
		btnConfirm = new Button(shell, SWT.NONE);
		btnConfirm.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, true, 1, 1));
		btnConfirm.setText("CONFIRM");
		btnConfirm.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> this.onConfirmed(e, true)));
		
		btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1));
		btnCancel.setText("CANCEL");
		btnCancel.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> this.onConfirmed(e, false)));
		
		this.btnConfirm.setEnabled(false);
	}
	
	private void onConfirmed(SelectionEvent event, boolean flag) {
		this.btnConfirm.setEnabled(false);
		this.btnCancel.setEnabled(false);
		Procedure procedure = flag ? (Procedure) this.comboViewer.getStructuredSelection().getFirstElement() : null;
		if (flag && procedure != null) {
			PurchaseEntry entry = PurchaseProcedure2.INSTANCE.entries.addNewEntry(procedure, this.txtSubject.getText());
			this.result = entry;
		} else {
			this.result = null;
		}
		this.shell.close();
	}
	
	private void onComboSelectionChanged(SelectionChangedEvent e) {
		this.updateConfirmButton();
	}
	
	private void onSubjectModified(ModifyEvent event) {
		this.updateConfirmButton();
	}
	
	private void updateConfirmButton() {
		this.btnConfirm.setEnabled(this.comboViewer.getStructuredSelection().getFirstElement() != null && !this.txtSubject.getText().isEmpty());
	}
	
	private static class ComboLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			Procedure procedure = (Procedure) element;
			return procedure.getProcedureName();
		}
	}
}
