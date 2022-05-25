package qwertzite.purchaseprocedures2.procedure.nodeelements;

import qwertzite.purchaseprocedures2.procedure.EnumInterfaceType;
import qwertzite.purchaseprocedures2.procedure.PurchaseEntry;

public class ElementTextString extends NodeElement {
	
	private boolean readonly;
	private String variable;
	
	public ElementTextString(String label, String variable, boolean readonly) {
		super(label);
		this.readonly = readonly;
		this.variable = variable;
	}
	
	@Override
	public EnumInterfaceType getInterfaceType() {
		return EnumInterfaceType.TEXT;
	}
	
	@Override
	public String getStringValue(PurchaseEntry entry) {
		return entry.getVariable(this.variable, "").getStringValue();
	}
	
	@Override
	public boolean setStringValue(PurchaseEntry entry, String value) {
		return entry.getVariable(this.variable, value).setStringValue(value);
	}
}
