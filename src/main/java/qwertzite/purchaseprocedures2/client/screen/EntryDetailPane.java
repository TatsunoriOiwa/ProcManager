package qwertzite.purchaseprocedures2.client.screen;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import qwertzite.purchaseprocedures2.client.PurchaseProcedure2;
import qwertzite.purchaseprocedures2.client.screen.cmp.DialogConfirmation;
import qwertzite.purchaseprocedures2.client.screen.cmp.WidgetUtil;
import qwertzite.purchaseprocedures2.procedure.EnumProcedureException;
import qwertzite.purchaseprocedures2.procedure.Procedure;
import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;

public class EntryDetailPane extends Composite {
	
	private ScrolledComposite scrolledComposite;
	private Composite scrollBase;
	
	private StackLayout stacklayout;
	private Map<Procedure, AbstractEntryRenderer> renderer = new HashMap<>();
	private AbstractEntryRenderer missingEntryRenderer;
	
	private AbstractEntryRenderer currentRenderer;
	private Composite composite;
	private Button btnAbort;
	private Button btnDisuse;
	private SashForm sashForm;
	private Text text;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public EntryDetailPane(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		sashForm = new SashForm(this, SWT.SMOOTH | SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		scrolledComposite = new ScrolledComposite(sashForm, SWT.BORDER | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		scrollBase = new Composite(scrolledComposite, SWT.NONE);
		scrollBase.setLayout(this.stacklayout = new StackLayout());
		this.missingEntryRenderer = new MissingEntryRenderer(this.scrollBase);
		
		scrolledComposite.setContent(scrollBase);
		scrolledComposite.setMinSize(scrollBase.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		composite = new Composite(sashForm, SWT.BORDER);
		composite.setLayout(new GridLayout(3, false));
		
		
		text = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		WidgetUtil.makeTextAutoScroll(text);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		text.addFocusListener(FocusListener.focusLostAdapter(this::onNoteFocusLost));
		text.setEnabled(false);
		
		btnAbort = new Button(composite, SWT.NONE);
		btnAbort.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, true, 1, 1));
		btnAbort.setText(" 中止 ");
		btnAbort.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> this.onProcedureException(EnumProcedureException.ABORTED, "Are you sure you want to abort this procedure?")));
		btnAbort.setEnabled(false);
		
		btnDisuse = new Button(composite, SWT.NONE);
		btnDisuse.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1));
		btnDisuse.setText(" 廃番 ");
		btnDisuse.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> this.onProcedureException(EnumProcedureException.DISUSED, "Are you sure you want to disuse this procedure ID?")));
		btnDisuse.setEnabled(false);
		sashForm.setWeights(new int[] {472, 125});
	}
	
	private void onProcedureException(EnumProcedureException exception, String message) {
		if (new DialogConfirmation(this.getShell(), SWT.NONE, message).open()) {
			PurchaseProcedure2.INSTANCE.selected.setExceptionStatus(exception);
			PurchaseProcedure2.INSTANCE.mirrorEntryUpdate();
		}
	}
	
	private void onNoteFocusLost(FocusEvent event) {
		PurchaseProcedure2.INSTANCE.selected.setGeneralNote(this.text.getText());
	}
	
	public void registerProcedure(Procedure procedure) {
		this.renderer.put(procedure, new EntryRenderer(this.scrollBase, procedure));
	}
	
	public void mirrorEntrySelection(PurchaseEntry entry) {
		AbstractEntryRenderer renderer;
		if (entry != null ) {
			if (entry.isMissingEntry()) {
				renderer = this.missingEntryRenderer;
			} else {
				renderer= this.renderer.get(entry.getProcedure());
			}
			this.btnAbort.setEnabled(entry.getExceptionStatus() == EnumProcedureException.NORMAL);
			this.btnDisuse.setEnabled(entry.getExceptionStatus() == EnumProcedureException.NORMAL);
			this.text.setEnabled(entry.getExceptionStatus() == EnumProcedureException.NORMAL);
			this.text.setText(entry.getGeneralNote());
		}
		else {
			renderer = null;
			this.btnAbort.setEnabled(false);
			this.btnDisuse.setEnabled(false);
			this.text.setEnabled(false);
			this.text.setText("");
			if (this.text.isFocusControl()) this.composite.forceFocus();
		}
		this.stacklayout.topControl = renderer;
		this.currentRenderer = renderer;
		if (renderer != null) renderer.reflectEntry(entry);
		this.scrollableRefit();
	}
	
	public void mirrorEntryUpdate(PurchaseEntry entry) {
		this.currentRenderer.reflectEntry(entry);
		this.scrollableRefit();
		
		this.btnAbort.setEnabled(entry.getExceptionStatus() == EnumProcedureException.NORMAL);
		this.btnDisuse.setEnabled(entry.getExceptionStatus() == EnumProcedureException.NORMAL);
		this.text.setEnabled(entry.getExceptionStatus() == EnumProcedureException.NORMAL);
	}
	
	public void scrollableRefit() {
		this.scrolledComposite.layout(true, true);
		scrolledComposite.setMinSize(scrollBase.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
