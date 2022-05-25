package qwertzite.purchaseprocedures2.client.screen;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import qwertzite.purchaseprocedures2.procedure.EnumProcedureException;
import qwertzite.purchaseprocedures2.procedure.Procedure;
import qwertzite.purchaseprocedures2.procedure.ProcedureNode;
import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;

public class EntryRenderer extends AbstractEntryRenderer {
	
	private Procedure procedure;
	private Map<ProcedureNode, NodeRenderer> nodeToComposite = new HashMap<>();
	private NodeCompleted completedNode;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public EntryRenderer(Composite parent, Procedure procedure) {
		super(parent, SWT.NONE);
		this.setLayout(new GridLayout(1, false));
		
		new NodeEntryHeader(this, SWT.BORDER, procedure).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for (ProcedureNode pNode : procedure.getProcedureNodes()) {
			NodeRenderer node = new NodeRenderer(this, SWT.BORDER, pNode);
			node.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			this.nodeToComposite.put(pNode, node);
		}
		this.completedNode = new NodeCompleted(this, SWT.BORDER);
		this.completedNode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		this.procedure = procedure;
	}
	
	public void reflectEntry(PurchaseEntry entry) {
		boolean normal = entry.getExceptionStatus() == EnumProcedureException.NORMAL;
		ProcedureNode active = procedure.getProcedureNode(entry.getCurrentStep());
		for (Map.Entry<ProcedureNode, NodeRenderer> e : this.nodeToComposite.entrySet()) {
			NodeRenderer renderer = e.getValue();
			renderer.reflectEntry(entry, normal && active == e.getKey());
		}
		this.completedNode.reflectEntry(entry, normal && procedure.getProcedureNodes().size() <= entry.getCurrentStep());
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
