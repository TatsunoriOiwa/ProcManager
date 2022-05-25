package qwertzite.purchaseprocedures2.client.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import qwertzite.purchaseprocedures2.client.ConfClient;
import qwertzite.purchaseprocedures2.procedure.Procedure;
import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;

public class MainScreen extends Composite {
	
	public SashForm sashForm;
	private EntryListPane cmpListPane;
	private EntryDetailPane cmpDetailPane;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MainScreen(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		sashForm = new SashForm(this, SWT.SMOOTH);
		sashForm.setSashWidth(5);
		
		cmpListPane = new EntryListPane(sashForm, SWT.BORDER);
		
		cmpDetailPane = new EntryDetailPane(sashForm, SWT.BORDER);
		sashForm.setWeights(new int[] {ConfClient.sashWeightLeft, ConfClient.sashWeightRight});
	}
	
	public void mirrorSelectionChanged(PurchaseEntry entry) {
		System.out.println("SelectionChanged");
		this.cmpDetailPane.mirrorEntrySelection(entry);
	}
	
	public void mirrorEntryInsertion(PurchaseEntry entry) {
		System.out.println("Insertion!");
		this.cmpListPane.mirrorEntryInsertion(entry);
	}
	
	public void mirrorEntryUpdate(PurchaseEntry entry) {
		this.cmpDetailPane.mirrorEntryUpdate(entry);
		this.cmpListPane.mirrorEntryUpdate(entry);
	}
	
	public void scrollableRefit() {
		this.cmpDetailPane.scrollableRefit();
	}
	
	
	public void registerProcedure(Procedure procedure) {
		this.cmpDetailPane.registerProcedure(procedure);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
