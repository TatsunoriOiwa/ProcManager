package qwertzite.purchaseprocedures2.procedure.nodeelements;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import qwertzite.purchaseprocedures2.procedure.EnumInterfaceType;
import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;

public abstract class NodeElement {
	
	private String nodeLabel;
	
	public NodeElement(String nodeLabel) {
		this.nodeLabel = nodeLabel;
	}
	
	public String getNodeLabel() { return this.nodeLabel; }
	public abstract EnumInterfaceType getInterfaceType();
	/** Interface type TEXT and DATE only */
	public boolean isReadOnly() { return false; }
	/** Interface type TEXT only */
	public boolean isMultiLine() { return false; }
	/** Interface type TEXT only */
	public abstract String getStringValue(PurchaseEntry entry);
	/** Interface type TEXT only */
	public abstract boolean setStringValue(PurchaseEntry entry, String value);
	
	/** Interface type BUTTON only */
	public boolean getBoolValue(PurchaseEntry entry) { return false; }
	/** Interface type BUTTON only */
	public void setBoolValue(PurchaseEntry entry, boolean value) {}
	
	/** Interface type DATE only */
	public LocalDate getDateValue(PurchaseEntry entry) { return null; }
	/** Interface type DATE only */
	public void setDateValue(PurchaseEntry entry, LocalDate date) {}
}
