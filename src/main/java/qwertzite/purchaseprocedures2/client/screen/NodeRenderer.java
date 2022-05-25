package qwertzite.purchaseprocedures2.client.screen;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import qwertzite.purchaseprocedures2.client.PurchaseProcedure2;
import qwertzite.purchaseprocedures2.client.screen.cmp.CmpDate;
import qwertzite.purchaseprocedures2.procedure.ProcedureNode;
import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;
import qwertzite.purchaseprocedures2.procedure.nodeelements.NodeElement;

public class NodeRenderer extends Composite {
	
	private ProcedureNode procedureNode;
	
	private Label titleLabel;
	private Set<Label> labels = new HashSet<>();
	private Map<NodeElement, Control> widgets = new HashMap<>();
	private Button btnCompleted;
	
	private boolean current;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public NodeRenderer(Composite parent, int style, ProcedureNode node) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		this.procedureNode = node;
		
		titleLabel = new Label(this, SWT.NONE);
		titleLabel.setFont(SWTResourceManager.getFont("Yu Gothic UI", 12, SWT.NORMAL));
		titleLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		titleLabel.setText(node.getNodeTitle());
		new Label(this, SWT.NONE);
		
		for (NodeElement element : node.getElements()) {
			this.addElement(element);
		}
		
		new Label(this, SWT.NONE);
		btnCompleted = new Button(this, SWT.NONE);
		btnCompleted.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnCompleted.setText(node.getCompleteText());
		btnCompleted.addSelectionListener(SelectionListener.widgetSelectedAdapter(this::onCompleted));
		
		this.current = true;
	}
	
	private void addElement(NodeElement element) {
		Label label = new Label(this, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		this.labels.add(label);
		switch (element.getInterfaceType()) {
		case TEXT: {
			label.setText(element.getNodeLabel());
//			Text text = new Text(this, SWT.BORDER | (element.isReadOnly() ? SWT.READ_ONLY : 0) | (element.isMultiLine() ? SWT.MULTI : 0) | SWT.H_SCROLL);
//			Text text = new Text(this, SWT.BORDER | (element.isReadOnly() ? SWT.READ_ONLY : 0) | SWT.MULTI | SWT.H_SCROLL);
			Text text = new Text(this, SWT.BORDER | (element.isReadOnly() ? SWT.READ_ONLY : 0) | (element.isMultiLine() ? SWT.MULTI : 0) | SWT.WRAP);
			text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			text.setText("text");
//			WidgetUtil.makeTextAutoScroll(text);
			if (!element.isReadOnly()) {
				text.addModifyListener(e -> this.onTextModified(text, element));
				text.addFocusListener(FocusListener.focusLostAdapter(e -> this.onTextDeselected(text)));
			}
			if (!element.isMultiLine()) {
				text.addFocusListener(FocusListener.focusGainedAdapter(e -> this.onTextFieldSelected(e, text)));
			}
			text.addKeyListener(KeyListener.keyPressedAdapter(e -> this.keyPressed(e, text)));
			this.widgets.put(element, text);
		} break;
		case BUTTON: {
			Button button = new Button(this, SWT.CHECK);
			button.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
			button.setText(element.getNodeLabel());
			button.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> this.onButtonChecked(button, element)));
			this.widgets.put(element, button);
		} break;
		case DATE: {
			label.setText(element.getNodeLabel());
			CmpDate date = new CmpDate(this, SWT.NONE | (element.isReadOnly() ? SWT.READ_ONLY : 0));
			date.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
			if (!element.isReadOnly()) date.setDateListener(e -> this.onDateChanged(e, element));
			this.widgets.put(element, date);
		} break;
		case LABEL: {
			Label lblElem = new Label(this, SWT.NONE);
			lblElem.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
			this.widgets.put(element, lblElem);
		} break;
		default:
			break;
		}
	}
	
	private void onCompleted(SelectionEvent event) {
		System.out.println("COMPLETED!");
		PurchaseProcedure2.INSTANCE.nextStep();
	}
	
	private void onTextModified(Text text, NodeElement element) {
		PurchaseEntry entry = PurchaseProcedure2.INSTANCE.selected;
		if (entry == null) { return; }
		if (element.setStringValue(entry, text.getText())) {
			text.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		} else {
			text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		this.btnCompleted.setEnabled(this.procedureNode.isCompleted(entry));
	}
	
	private void onTextDeselected(Text text) {
		this.notifyEntryUpdate(PurchaseProcedure2.INSTANCE.selected);
		text.clearSelection();
	}
	
	private void onTextFieldSelected(FocusEvent event, Text text) {
		text.selectAll();
	}
	
	private void keyPressed(KeyEvent event, Text text) {
		if (event.stateMask == SWT.CTRL && event.keyCode == 'a') {
			text.selectAll();
		}
	}
	
	private void onButtonChecked(Button button, NodeElement element) {
		PurchaseEntry entry = PurchaseProcedure2.INSTANCE.selected;
		if (entry == null) { return; }
		element.setBoolValue(entry, button.getSelection());
		this.btnCompleted.setEnabled(this.procedureNode.isCompleted(entry));
		this.notifyEntryUpdate(entry);
	}
	
	private void onDateChanged(LocalDate date, NodeElement element) {
		PurchaseEntry entry = PurchaseProcedure2.INSTANCE.selected;
		if (entry == null) { return; }
		element.setDateValue(entry, date);
		this.btnCompleted.setEnabled(this.procedureNode.isCompleted(entry));
		this.notifyEntryUpdate(entry);
	}
	
	private void notifyEntryUpdate(PurchaseEntry entry) {
		if (this.procedureNode.onEntryModified(entry)) {
			this.reflectEntry(entry, true);
		}
	}
	
	private boolean working = false;
	public void reflectEntry(PurchaseEntry entry, boolean current) {
		if (this.working) { return; }
		this.working = true;
		if (this.current != current) {
			for (Label label : labels) {
				label.setForeground(SWTResourceManager.getColor(current ? SWT.COLOR_BLACK : SWT.COLOR_WIDGET_DISABLED_FOREGROUND));
			}
			for (Map.Entry<NodeElement, Control> e : this.widgets.entrySet()) {
				Control w = e.getValue();
				w.setEnabled(current);
			}
			this.btnCompleted.setEnabled(current);
			this.titleLabel.setForeground(SWTResourceManager.getColor(current ? SWT.COLOR_BLACK : SWT.COLOR_WIDGET_DISABLED_FOREGROUND));
			this.current = current;
		}
		
		for (Map.Entry<NodeElement, Control> e : this.widgets.entrySet()) {
			Control w = e.getValue();
			NodeElement elem = e.getKey();
			switch (elem.getInterfaceType()) { // TODO: label
			case TEXT: {
				Text text = (Text) w;
				text.setText(elem.getStringValue(entry));
			} break;
			case BUTTON: {
				Button button = (Button) w;
				button.setSelection(elem.getBoolValue(entry));
			} break;
			case DATE: {
				CmpDate date = (CmpDate) w;
				date.setDate(elem.getDateValue(entry));
			} break;
			case LABEL: {
				Label label = (Label) w;
				label.setText(elem.getStringValue(entry));
			} break;
			default:
				break;
			}
			w.setEnabled(current);
		}
		this.btnCompleted.setEnabled(this.procedureNode.isCompleted(entry));
		this.working = false;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
