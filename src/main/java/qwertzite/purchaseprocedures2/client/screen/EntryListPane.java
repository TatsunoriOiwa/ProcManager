package qwertzite.purchaseprocedures2.client.screen;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import qwertzite.purchaseprocedures2.client.PurchaseProcedure2;
import qwertzite.purchaseprocedures2.procedure.EntryManager;
import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;

public class EntryListPane extends Composite {
	
	private ListViewer listViewer;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public EntryListPane(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Button btnNewEntry = new Button(this, SWT.NONE);
		btnNewEntry.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		btnNewEntry.setText("NEW ENTRY");
		btnNewEntry.addSelectionListener(SelectionListener.widgetSelectedAdapter(this::onNewEntrySelected));
		
		listViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL);
		listViewer.setUseHashlookup(true);
		List list = listViewer.getList();
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		listViewer.setContentProvider(new ProcedureContentProvider());
		listViewer.setLabelProvider(new LabelProvider());
		listViewer.setInput(PurchaseProcedure2.INSTANCE.entries);
		listViewer.addSelectionChangedListener(this::onSelectionChanged);

		
	}
	
	private void onSelectionChanged(SelectionChangedEvent event) {
		PurchaseProcedure2.INSTANCE.onSelectionChanged((PurchaseEntry) event.getStructuredSelection().getFirstElement());
	}
	
	private void onNewEntrySelected(SelectionEvent event) {
		PurchaseProcedure2.INSTANCE.addNewEntry();
	}
	
	public void mirrorEntryInsertion(PurchaseEntry entry) {
		this.listViewer.add(entry);
		this.listViewer.setSelection(new StructuredSelection(entry));
	}
	
	public void mirrorEntryUpdate(PurchaseEntry entry) {
		this.listViewer.refresh(entry);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public static class ProcedureContentProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			EntryManager schedules = (EntryManager) inputElement;
			return schedules.getEntries().toArray();
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}
	
	public static class LabelProvider implements ILabelProvider {

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Image getImage(Object element) {
			return null;
		}

		@Override
		public String getText(Object element) {
			PurchaseEntry entry = (PurchaseEntry) element;
			return entry.getSerial().toString() + " " + entry.getStepName() + " : " + entry.getSubject();
		}
	}
}
